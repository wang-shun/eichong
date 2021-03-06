package com.wanma.filter;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.echong.constant.CommonConsts;
import com.echong.utils.SigTool;
import com.google.common.base.Strings;
import com.wanma.support.common.MessageManager;
import com.wanma.support.common.RedisService;
import com.wanma.support.common.SpringContextHolder;
import com.wanma.support.common.WanmaConstants;
import com.wanma.support.utils.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebFilter implements Filter {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Logger log = Logger.getLogger(WebFilter.class);
    private RedisService redisService;
    private int ACCESS_MAX_COUNT;
    private long ACCESS_TIMEOUT;
    private int accessCount;
    private String accessDate;

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        //e充网单独过滤，是e充网则不走安全验证
        if (checkEChongWang(request)) {
            chain.doFilter(req, resp);
            return;
        }
        String ext = request.getParameter("ext");
        if ("t".equals(ext)) {
            chain.doFilter(req, resp);
            return;
        }
        if (!request.getMethod().equalsIgnoreCase("post")) {
            printErrorMessage(response, "wrong method");
            return;
        }
        String time = request.getParameter("time");
        String org = request.getParameter("org");
        String token = request.getParameter("token");
        if (StringUtils.isBlank(time) || StringUtils.isBlank(org)
                || StringUtils.isBlank(token)) {
            printErrorMessage(response, "need params time,org,token");
            return;
        }
        // 验证是否超时
        long timeL = StringUtils.isBlank(time) ? 0 : new Long(time);
        long interval = System.currentTimeMillis() / 1000 - timeL;
        if (Math.abs(interval) > ACCESS_TIMEOUT) {
            printErrorMessage(response, "access timeout");
            return;
        }
        // 身份验证:组织名+验证码+time参数的后半段（5位），三个参数顺序拼接后进行32位小写md5加密得到token
        boolean flag = checkUserValid(org, time, token);
        if (!flag) {
            log.info("user invalid" + org + "~~" + time + "~~" + token);
            printErrorMessage(response, "user invalid");
            return;
        }
        // 验证登录次数
        boolean accessFlag = checkAccessCount();
        if (!accessFlag) {
            printErrorMessage(response, "access limit");
            return;
        }
        chain.doFilter(req, resp);
    }

    private boolean checkUserValid(String org, String time, String token) {
        String authCode = redisService.strGet(WanmaConstants.PREFIX_ORG + org)
                .split(":")[0];
        String t = MD5Util.Md5(org + authCode + time.substring(5));
        return t.equals(token);
    }

    private void printErrorMessage(ServletResponse resp, String message) {
        log.info(message);
        resp.setContentType("text/plain;charset=UTF-8");
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

    private boolean checkAccessCount() {
        String d = sdf.format(new Date());
        if (d.equals(accessDate)) {
            if (accessCount < ACCESS_MAX_COUNT) {
                accessCount++;
            } else {// 当天超过最大访问次数时限制访问
                return false;
            }
        } else {
            accessDate = d;
            accessCount = 0;
        }
        return true;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        ApplicationContext context = SpringContextHolder.getSpringContext();
        redisService = (RedisService) context.getBean("redisService");
        MessageManager manager = MessageManager.getMessageManager();
        ACCESS_MAX_COUNT = new Integer(
                manager.getInterfaceProperties("access.max"));
        ACCESS_TIMEOUT = new Long(
                manager.getInterfaceProperties("access.timeout"));
        accessCount = 0;
        accessDate = sdf.format(new Date());
    }

    //检查是否为e充网单
    private boolean checkEChongWang(HttpServletRequest request) {
        String app_id = request.getParameter("app_id");
        String info = request.getParameter("info");
        if (null == app_id || null == info) {
            return false;
        }
        info = URLDecoder(info);
        String sig = request.getParameter("sig");
        if (!Strings.isNullOrEmpty(app_id) && !Strings.isNullOrEmpty(info)) {
            String ensureSig = SigTool.getSignString(app_id, info, CommonConsts.E_CHONG_APP_KEY);
            return ensureSig.equals(sig);
        }
        return false;
    }

    private String URLDecoder(String info) {
        info = info.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        String urlStr = null;
        try {
            urlStr = URLDecoder.decode(info, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("URLDecoder is fail,info:" + info);
        }
        return urlStr;
    }

    public static void main(String[] args) {
        String org="45B1B38C88CF475DB08E9507CC121E82";
        String time="1500608225";
        String t = MD5Util.Md5(org + org + time.substring(5));
        System.out.println(t);
        System.out.println(t.equals("fecbdca4cb8189ada3126ee15c1f1cdc"));
    }

}
