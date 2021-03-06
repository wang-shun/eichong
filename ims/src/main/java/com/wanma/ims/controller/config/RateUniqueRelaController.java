package com.wanma.ims.controller.config;

import com.wanma.ims.common.domain.RateUniqueRelaDO;
import com.wanma.ims.common.domain.UserDO;
import com.wanma.ims.common.domain.base.Pager;
import com.wanma.ims.common.dto.BaseResultDTO;
import com.wanma.ims.constants.ResultCodeConstants;
import com.wanma.ims.controller.BaseController;
import com.wanma.ims.controller.result.JsonException;
import com.wanma.ims.controller.result.JsonResult;
import com.wanma.ims.controller.vo.CompanyChargeRelaVO;
import com.wanma.ims.service.RateUniqueRelaService;
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
import java.util.List;

/**
 * 唯一费率管理 
 */
@RequestMapping("/manage/config")
@Controller
public class RateUniqueRelaController extends BaseController{

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RateUniqueRelaService rateUniqueRelaService;
	
	/**
	* <p>Description: 获取分组后的唯一费率列表</p>
	* @param
	* @author bingo
	* @date 2017-7-25
	 */
	@RequestMapping(value = "/getRateUniqueRelaGroup")
	@ResponseBody
	public void getRateUniqueRelaGroup(@ModelAttribute("pager") Pager pager,
			@ModelAttribute RateUniqueRelaDO rateUniqueRela, HttpServletRequest request){
		JsonResult batchResult = new JsonResult();
		List<RateUniqueRelaDO> rateUniqueRelaList = rateUniqueRelaService.getRateUniqueRelaGroup(rateUniqueRela);
		if (rateUniqueRelaList == null) {
			rateUniqueRelaList = new ArrayList<RateUniqueRelaDO>();
		}
		batchResult.setDataObject(rateUniqueRelaList);
		
		responseJson(batchResult);
	}
	
	
	/**
	* <p>Description: 获取唯一费率列表</p>
	* @param
	* @author bingo
	* @date 2017-7-25
	 */
	@RequestMapping(value = "/getRateUniqueRelaList")
	@ResponseBody
	public void getRateUniqueRelaList(@ModelAttribute("pager") Pager pager,
			@ModelAttribute RateUniqueRelaDO rateUniqueRela, HttpServletRequest request){
		JsonResult batchResult = new JsonResult();
		Long total = rateUniqueRelaService.getRateUniqueRelaCount(rateUniqueRela);
		if (total <= pager.getOffset()) {
			pager.setPageNo(1L);
		}
		pager.setTotal(total);
		rateUniqueRela.setPager(pager);
		
		List<RateUniqueRelaDO> rateUniqueRelaList = rateUniqueRelaService.getRateUniqueRelaList(rateUniqueRela);
		if (rateUniqueRelaList == null) {
			rateUniqueRelaList = new ArrayList<RateUniqueRelaDO>();
		}
		batchResult.setDataObject(rateUniqueRelaList);
		batchResult.setPager(pager);
		
		responseJson(batchResult);
	}	
	
	/**
	* <p>Description: 新增唯一费率信息</p>
	* @param
	* @author bingo
	* @date 2017-7-25
	 */
	@RequestMapping(value = "/addRateUniqueRela")
	@ResponseBody
	public void addRateUniqueRela(@ModelAttribute RateUniqueRelaDO rateUniqueRela, Model model, HttpServletRequest request) {
		//取当前登录用户
		UserDO user = getCurrentLoginUser();
		BaseResultDTO baseResultDTO = new BaseResultDTO();
		try {
			baseResultDTO = rateUniqueRelaService.addRateUniqueRela(rateUniqueRela, user);
		} catch (Exception e) {
			log.error(this.getClass() + ".addRateInfo() error : ", e);
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, e.toString()));
		}
		
		if(!baseResultDTO.isSuccess()){
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, baseResultDTO.getErrorDetail()));
		}else {			
			responseJson(new JsonResult());
		}
	}
	
	 /**
     * 获取充电点和电桩
     */
    @RequestMapping(value = "/getStationAndPile")
    @ResponseBody
    public void getStationAndPile(@ModelAttribute RateUniqueRelaDO rateUniqueRela) {
        JsonResult result = new JsonResult();
        UserDO loginUser = getCurrentLoginUser();

        try {
            result.setDataObject(CompanyChargeRelaVO.valueOf(rateUniqueRelaService.getStationAndPile(rateUniqueRela, loginUser)));

            responseJson(result);
        } catch (Exception e) {
            log.error(this.getClass() + "-getStationAndPile is error|cpyId={}|loginUser={}", rateUniqueRela.getCpyId(), loginUser, e);
            responseJson(new JsonException("获取充电点和电桩失败，请刷新页面后重试！"));
        }
    }
    
    /**
	* <p>Description: 修改唯一费率</p>
	* @param
	* @author bingo
	* @date 2017-6-16
	 */
	@RequestMapping(value = "/modifyRateUniqueRela")
	@ResponseBody
	public void modifyRateUniqueRela(@ModelAttribute RateUniqueRelaDO rateUniqueRela, Model model, HttpServletRequest request) {
		BaseResultDTO baseResultDTO = new BaseResultDTO();
		//取当前登录用户
		UserDO user = getCurrentLoginUser();
		try {
			baseResultDTO = rateUniqueRelaService.modifyRateUniqueRela(rateUniqueRela, user);
		} catch (Exception e) {
			log.error(this.getClass() + ".modifyRateUniqueRela() error : ", e);
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, e.toString()));
		}
		
		if(!baseResultDTO.isSuccess()){
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, baseResultDTO.getErrorDetail()));
		}else {			
			responseJson(new JsonResult());
		}
	}
	
	/**
	* <p>Description: 修改唯一费率的费率值</p>
	* @param
	* @author bingo
	* @date 2017-9-1
	 */
	@RequestMapping(value = "/modifyRateUniqueRelaRateinfoId")
	@ResponseBody
	public void modifyRateUniqueRelaRateinfoId(@ModelAttribute RateUniqueRelaDO rateUniqueRela, Model model, HttpServletRequest request) {
		BaseResultDTO baseResultDTO = new BaseResultDTO();
		//取当前登录用户
		UserDO user = getCurrentLoginUser();
		try {
			baseResultDTO = rateUniqueRelaService.modifyRateUniqueRelaRateinfoId(rateUniqueRela, user);
		} catch (Exception e) {
			log.error(this.getClass() + ".modifyRateUniqueRela() error : ", e);
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, e.toString()));
		}
		
		if(!baseResultDTO.isSuccess()){
			responseJson(new JsonResult(false, ResultCodeConstants.FAILED, baseResultDTO.getErrorDetail()));
		}else {			
			responseJson(new JsonResult());
		}
	}
}
