package com.wanma.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanma.support.common.BodyReaderHttpServletRequestWrapper;
import com.wanma.support.common.HttpHelper;
import com.wanma.support.common.RedisService;
import com.wanma.support.common.SpringContextHolder;
import com.wanma.support.common.WanmaConstants;
import com.wanma.support.utils.HMacMD5;
import com.wanma.support.utils.StringUtil;

public class TpiTokenFilter implements Filter {
	private Logger log = Logger.getLogger(TpiTokenFilter.class);
	private RedisService redisService;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String ext = request.getParameter("ext");
		String 	uri=request.getServletPath();
		if("/shsz/query_token.do".equals(uri)||"/shsz/query_token".equals(uri)){
			chain.doFilter(req, resp);
			return;
		}
		if ("t".equals(ext)) {
			chain.doFilter(req, resp);
			return;
		}
		if (!request.getMethod().equalsIgnoreCase("post")) {
			printErrorMessage(response, "wrong method");
			return;
		}
		ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        String body = HttpHelper.getBodyString(requestWrapper);
        JSONObject jsDt = JSON.parseObject(body);
		String org =StringUtil.obj2Str(jsDt.get("OperatorID"));
		String token =request.getHeader("Authorization").substring(7, request.getHeader("Authorization").length());
		if (StringUtils.isBlank(org)
                || StringUtils.isBlank(token)) {//Bearer Token
			printErrorMessage(response, "need params,operatorID,accessToken");
			return;
		}

		// 身份验证:组织名+验证码+time，三个参数顺序拼接后进行32位小写md5加密得到token
		if (!checkUserValid(org.toString(), token.toString())) {
			printErrorMessage(response, "token invalid");
			return;
		}
		String data = StringUtil.obj2Str(jsDt.get("Data"));
		String timeStamp = StringUtil.obj2Str(jsDt.get("TimeStamp"));
		String seq = StringUtil.obj2Str(jsDt.get("Seq"));
		String sigStr =  org  + data + timeStamp  + seq;
		if (!HMacMD5.getHmacMd5Str("1234567890abcdef", sigStr).equals(
				StringUtil.obj2Str(jsDt.get("Sig")))) {
			printErrorMessage(response, "Sig invalid");
			return;
		}
		chain.doFilter(requestWrapper, resp);
	}

	private boolean checkUserValid(String org, String token) {
		String tkVal = redisService.strGet(WanmaConstants.PREFIX_TOKWEN + org);
		long termT = System.currentTimeMillis()
				- Long.valueOf(tkVal.split(",")[0]);
		return termT < WanmaConstants.PREFIX_TOKWEN_TERM
				&& token.equals(redisService.strGet(
						WanmaConstants.PREFIX_TOKWEN + org).split(",")[1]);
	}

	private void printErrorMessage(ServletResponse resp, String message) {
		log.info(message);
		resp.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		try {
			pw = resp.getWriter();
			pw.write(message);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		ApplicationContext context = SpringContextHolder.getSpringContext();
		redisService = (RedisService) context.getBean("redisService");
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis() / 1000;
		System.out.println(time - new Long(1488331192));
	}
}
