package com.wanma.ims.controller.integral;

import com.wanma.ims.common.domain.IntegralActivityDO;
import com.wanma.ims.common.domain.UserDO;
import com.wanma.ims.common.domain.base.Pager;
import com.wanma.ims.common.domain.bo.IntegralActivityAndRulesBO;
import com.wanma.ims.common.domain.bo.IntgeralAreaRelaBO;
import com.wanma.ims.common.dto.BaseResultDTO;
import com.wanma.ims.constants.ResultCodeConstants;
import com.wanma.ims.controller.BaseController;
import com.wanma.ims.controller.result.JsonException;
import com.wanma.ims.controller.result.JsonResult;
import com.wanma.ims.service.IntegralActivityService;
import com.wanma.ims.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 积分活动
 */
@RequestMapping("/manage/integral")
@Controller
public class IntegralActivityController extends BaseController{
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IntegralActivityService integralActivityService;
	
	/**
	* <p>Description: 获取积分活动列表</p>
	* @param
	* @author bingo
	* @date 2017-8-15
	 */
	@RequestMapping(value = "/getIntegralActivityList")
	@ResponseBody
	public void getIntegralActivityList(@ModelAttribute("pager") Pager pager,
			@ModelAttribute IntegralActivityDO integralActivity, Model model,HttpServletRequest request){
		JsonResult batchResult = new JsonResult();
		Long total = integralActivityService.getIntegralActivityCount(integralActivity);
		if (total <= pager.getOffset()) {
			pager.setPageNo(1L);
		}
		pager.setTotal(total);
		integralActivity.setPager(pager);
		
		List<IntegralActivityAndRulesBO> integralActivityList = integralActivityService.getIntegralActivityList(integralActivity);
		if (integralActivityList == null) {
			integralActivityList = new ArrayList<IntegralActivityAndRulesBO>();
		}
		for (IntegralActivityAndRulesBO integralActivityAndRulesBO : integralActivityList) {
			integralActivityAndRulesBO.setDirectionName(integralActivityAndRulesBO.getDirection().equals(0) ? "获取" : "消耗");
		}
		batchResult.setDataObject(integralActivityList);
		batchResult.setPager(pager);
		
		responseJson(batchResult);
	}	
	
	/**
	* <p>Description: 修改积分活动</p>
	* @param
	* @author bingo
	* @date 2017-8-15
	 */
	@RequestMapping(value = "/modifyIntegralActivity")
	@ResponseBody
	public void modifyIntegralActivity(@ModelAttribute IntegralActivityAndRulesBO integralActivityAndRulesBO, Model model, HttpServletRequest request) {
		//取当前登录用户
		UserDO user = getCurrentLoginUser();
		if (user == null) {
			log.error(this.getClass() + ".modifyIntegralActivity() error : " + ResultCodeConstants.ERROR_MSG_EMPTY_SESSION_USER);
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, ResultCodeConstants.ERROR_MSG_EMPTY_SESSION_USER));
			return;
		}

		if(integralActivityAndRulesBO == null || integralActivityAndRulesBO.getPkId() == null){
			log.error(this.getClass() + ".modifyIntegralActivity() error : 修改的积分活动不能为空");
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, "修改的积分活动不能为空"));
			return;
		}

		if(integralActivityAndRulesBO.getStrStartDate() != null
				&& DateUtil.compareNow(DateUtil.parse(integralActivityAndRulesBO.getStrStartDate(), DateUtil.TYPE_COM_YMD)) <= 0){
			log.error(this.getClass() + ".modifyIntegralActivity() error : 积分活动的开始时间不能小于等于系统当前时间");
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, "积分活动的开始时间不能小于等于系统当前时间"));
			return;
		}
		
		BaseResultDTO baseResultDTO = new BaseResultDTO();
		try {
			integralActivityAndRulesBO.setModifier(user.getUserId().toString());
			integralActivityAndRulesBO.setGmtModified(new Date());
			baseResultDTO = integralActivityService.modifyIntegralActivity(integralActivityAndRulesBO);
		} catch (Exception e) {
			log.error(this.getClass() + ".modifyIntegralActivity() error : ", e);
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, e.toString()));
		}
		
		if(!baseResultDTO.isSuccess()){
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, baseResultDTO.getErrorDetail()));
		}else {
			responseJson(new JsonResult());
		}
	}
	
	/**
	* <p>Description: 增加积分活动</p>
	* @param
	* @author bingo
	* @date 2017-8-15
	 */
	@RequestMapping(value = "/addIntegralActivity")
	@ResponseBody
	public void addIntegralActivity(@ModelAttribute IntegralActivityAndRulesBO integralActivityAndRulesBO, Model model, HttpServletRequest request) {
		//取当前登录用户
		UserDO user = getCurrentLoginUser();
		if (user == null) {
			log.error(this.getClass() + ".addIntegralActivity() error : " + ResultCodeConstants.ERROR_MSG_EMPTY_SESSION_USER);
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, ResultCodeConstants.ERROR_MSG_EMPTY_SESSION_USER));
			return;
		}
		
		BaseResultDTO baseResultDTO = new BaseResultDTO();
		try {
			integralActivityAndRulesBO.setCreator(user.getUserId().toString());
			integralActivityAndRulesBO.setModifier(user.getUserId().toString());
			integralActivityAndRulesBO.setGmtCreate(new Date());
			integralActivityAndRulesBO.setGmtModified(new Date());
			baseResultDTO = integralActivityService.addIntegralActivity(integralActivityAndRulesBO);
		} catch (Exception e) {
			log.error(this.getClass() + ".addIntegralActivity() error : ", e);
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, e.toString()));
		}
		
		if(!baseResultDTO.isSuccess()){
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, baseResultDTO.getErrorDetail()));
		}else {
			responseJson(new JsonResult());
		}
	}

	/**
	 * 获取省、市、电站、桩
	 */
	@RequestMapping(value = "/getStationAndPile")
	@ResponseBody
	public void getStationAndPile(@ModelAttribute IntegralActivityAndRulesBO integralActivityAndRulesBO) {
		JsonResult result = new JsonResult();
		UserDO loginUser = getCurrentLoginUser();

		try {
			result.setDataObject(IntgeralAreaRelaBO.valueOf(integralActivityService.getStationAndPile(integralActivityAndRulesBO, loginUser)));

			responseJson(result);
		} catch (Exception e) {
			log.error(this.getClass() + "-getStationAndPile is error：", e);
			responseJson(new JsonException("获取充电点和电桩失败，请刷新页面后重试！"));
		}
	}

	/**
	 * <p>Description: 获取积分活动、规则、扩展属性列表</p>
	 * @param
	 * @author bingo
	 * @date 2017-8-15
	 */
	@RequestMapping(value = "/getIntegralActivityAndRulesList")
	@ResponseBody
	public void getIntegralActivityAndRulesList(@ModelAttribute("pager") Pager pager,
										@ModelAttribute IntegralActivityDO integralActivity, Model model,HttpServletRequest request){
		JsonResult batchResult = new JsonResult();
		Long total = 0L;
		if (total <= pager.getOffset()) {
			pager.setPageNo(1L);
		}
		pager.setTotal(total);
		integralActivity.setPager(pager);

		List<IntegralActivityAndRulesBO> integralActivityList = integralActivityService.getIntegralActivityAndRulesList(integralActivity);
		if (integralActivityList == null) {
			integralActivityList = new ArrayList<IntegralActivityAndRulesBO>();
		}
		for (IntegralActivityAndRulesBO integralActivityAndRulesBO : integralActivityList) {
			integralActivityAndRulesBO.setDirectionName(integralActivityAndRulesBO.getDirection().equals(0) ? "获取" : "消耗");
		}
		batchResult.setDataObject(integralActivityList);
		batchResult.setPager(pager);

		responseJson(batchResult);
	}
}
