package com.wanma.app.controller;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluemobi.product.utils.AccessErrorResult;
import com.bluemobi.product.utils.AccessSuccessResult;
import com.wanma.app.service.impl.AppPowerStationStarServiceImpl;

@Controller
@RequestMapping("/app/psStar")
public class AppPowerStationStarController {
	private static Log log = LogFactory.getLog(AppPowerStationStarController.class);

	@Autowired
	private AppPowerStationStarServiceImpl pscService;
	
	/**
	 * @Description: 新增电站星评
	 * @param params 
	 * 			psId 电站id，uId 用户id，psStar 星评等级
	 * @return
	 */
	@RequestMapping("/insertPsStar")
	@ResponseBody
	public String insertPsStar(@RequestParam Map<String, Object> param) {

		try {
			int num = pscService.getStarNumByUserIdPsId(param);
			if(num > 0){
				return new AccessErrorResult(1001, "您已对此站进行评分，不可再评，谢谢").toString();
			}
			
			pscService.insertPsStar(param);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			log.error("新增电站星评错误", e);
			// 返回登录信息错误信息
			return new AccessErrorResult(2002,"error.msg.invalid.parameter")
					.toString();
		}
		// 返回处理成功信息
		return new AccessSuccessResult().toString();
	}

	/**
	 * @Description: 获取产品信息
	 * @param params
	 *            分页参数 产品id
	 * @return
	 */
	/*@RequestMapping("/findPsStar")
	@ResponseBody
	public String findProComments(@RequestParam Map<String, Object> params,
			AppPager pager) {
		// 分页参数
		params.put("pageNum", pager.getPageNum());
		params.put("pageNumber", pager.getPageNumber());

		List<Map<String, Object>> proCommentList = null;

		try {
			//proCommentList = productCommentService.findProComments(params);
			proCommentList = pscService.getPsCommentsPageList(params);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			log.error("获取产品信息错误", e);
			return new AccessErrorResult("error.msg.invalid.parameter")
					.toString();
		}

		// 返回处理成功信息
		return new AccessSuccessResult(proCommentList).toString();
	}*/

}