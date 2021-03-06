package com.wanma.ims.controller.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wanma.ims.common.domain.UserDO;
import com.wanma.ims.common.domain.base.Pager;
import com.wanma.ims.common.domain.bo.SimCardBO;
import com.wanma.ims.common.dto.BaseResultDTO;
import com.wanma.ims.constants.ResultCodeConstants;
import com.wanma.ims.controller.BaseController;
import com.wanma.ims.controller.result.JsonResult;
import com.wanma.ims.service.SimCardService;
/**
 * SIM卡
 */
@Controller
@RequestMapping("/manage/config")
public class SimCardController extends BaseController{

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SimCardService simCardService;
	
	/**
	 * 获取sim卡管理列表
	 * @param pager
	 * @param simCard
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/getSimCardList")
	@ResponseBody
	public void getSimCardList(@ModelAttribute("pager") Pager pager,
			@ModelAttribute SimCardBO simCard, Model model,HttpServletRequest request){
		JsonResult batchResult = new JsonResult();
		Long total = 0l;
		try {
			// ------------|02：查询电桩业务处理|-----------
			// 获取登陆用户
			UserDO user = getCurrentLoginUser();
			// 个体/纯商家只能查询所属电桩
			simCard.setElpiUserid(user.getUserId().toString());
			simCard.setUserLevel(user.getUserLevel().toString());
			// 电桩总数
			total = simCardService.getSimCardListCount(simCard);
			if (total <= pager.getOffset()) {
				pager.setPageNo(1L);
			}
			pager.setTotal(total);
			// 设置查询参数
			simCard.setPager(pager);
			List<SimCardBO> simCardList = simCardService.getSimCardList(simCard);
			for (SimCardBO simCardBO : simCardList) {
				simCardBO.setCommStatusName(simCardBO.getCommStatus() == 0 ? "已断开" : "连接中");
			}
			
			batchResult.setDataObject(simCardList);
			batchResult.setPager(pager);
			
			responseJson(batchResult);
		} catch (Exception e) {
			log.error("获取电桩状态失败", e);
			responseJson(e);
		}
	}
	
	@RequestMapping(value = "/modifySimCard")
	@ResponseBody
	public void modifySimCard(@ModelAttribute SimCardBO simCard,HttpServletRequest request) throws Exception{
		UserDO loginUser = getCurrentLoginUser();
		BaseResultDTO baseResultDTO = new BaseResultDTO();
		try {
			baseResultDTO = simCardService.modifySimCard(simCard, loginUser);
		} catch (Exception e) {
			log.error(this.getClass() + ".modifySimCard() error : ", e);
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, e.toString()));
		}
		
		if(!baseResultDTO.isSuccess()){
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, baseResultDTO.getErrorDetail()));
		}else {
			responseJson(new JsonResult());
		}
	}
}
