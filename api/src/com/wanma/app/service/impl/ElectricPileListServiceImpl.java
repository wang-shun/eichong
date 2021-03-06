/**
 * FileName:AppFeedbackMapper.java
 * Author: Administrator
 * Create: 2014年6月26日
 * Last Modified: 2014年6月26日
 * Version: V1.0 
 */
package com.wanma.app.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bluemobi.product.common.CommonConsts;
import com.bluemobi.product.common.MessageManager;
import com.bluemobi.product.common.dao.MultipartFileDao;
import com.bluemobi.product.model.UserModel;
import com.bluemobi.product.utils.MultipartFileUtil;
import com.bluemobi.product.utils.ObjectUtil;
import com.wanma.app.dao.TblElectricpileMapper;
import com.wanma.app.dao.TblPowerstationMapper;
import com.wanma.app.service.ElectricPileListService;
import com.wanma.common.ApplicationConsts;
import com.wanma.common.JudgeNullUtils;
import com.wanma.common.WanmaConstants;
import com.wanma.model.ElectricPileList;
import com.wanma.model.TblElectricpile;
import com.wanma.model.TblPowerstation;

/***
 *
 *   电桩查找(列表模式) 
  * @Description:
  * @author bruce cheng(http://blog.csdn.net/brucehome)
  * @createTime：2015-3-13 下午04:51:34 
  * @updator： 
  * @updateTime：   
  * @version：V1.0
 */
@Service
public class ElectricPileListServiceImpl implements ElectricPileListService {

	@Autowired
	TblElectricpileMapper tblElectricpileMapper;
	@Autowired
	TblPowerstationMapper tblPowerstationMapper;
	
	 
	@Override
	public List<TblElectricpile> findRelevancePowerStation(TblElectricpile tblElectricpile) {
	
		return tblElectricpileMapper.findRelevancePowerStation(tblElectricpile);
	}
	
	/**
	 * 后台电桩列表获取数据
	 */
	@Override
	public List<?> getElectricpileByCondition(TblElectricpile tblElectricpile) {
	
		return tblElectricpileMapper.getElectricpileByCondition(tblElectricpile);
	}
	/**
	 * 后台电桩列表获取总数据
	 */
	@Override
	public long getElectricpileByConditionCount(TblElectricpile tblElectricpile) {
	
		return tblElectricpileMapper.getElectricpileByConditionCount(tblElectricpile);
	}
	/**
	 * 获取地图模式电桩列表
	 * @param electricTypeId 汽车类型ID
	 * @param distance 距离 m
	 * @param price 价格
	 * @param evaluate 好评
	 */
	@Override
	public List<ElectricPileList> getElectricPileList(TblPowerstation tblPowerstation,TblElectricpile tblElectricpile) {
		
		List<ElectricPileList> electricPileList= new ArrayList<ElectricPileList>();
		
		//01:获取电站列表
		List<?> powersList=tblPowerstationMapper.getPowerstation(tblPowerstation);
		for (int i = 0; i < powersList.size(); i++) {
			ElectricPileList electricPileLists=new ElectricPileList();
		    Map<String,Object> powersLMap=(Map<String,Object>)powersList.get(i);
			electricPileLists.setElectricId(JudgeNullUtils.nvlStr(powersLMap.get("pk_PowerStation")));
			electricPileLists.setElectricType(ApplicationConsts.ElectricPileManager.POWERSTATION);
			electricPileLists.setElectricName(JudgeNullUtils.nvlStr(powersLMap.get("poSt_Name")));
			electricPileLists.setElectricPileSum(JudgeNullUtils.nvlStr(powersLMap.get("electricPileCount")));
			electricPileLists.setElectricImage(JudgeNullUtils.nvlStr(powersLMap.get("poSt_Pic")));
			electricPileLists.setElectricAddress(JudgeNullUtils.nvlStr(powersLMap.get("poSt_Address")));
			electricPileLists.setElectricDistance(JudgeNullUtils.nvlStr(powersLMap.get("distance")));
			electricPileLists.setElectricLatitude(JudgeNullUtils.nvlStr(powersLMap.get("poSt_Latitude")));
			electricPileLists.setElectricLongitude(JudgeNullUtils.nvlStr(powersLMap.get("poSt_Longitude")));
			electricPileList.add(electricPileLists);
		}
		//01:获取电桩列表
		tblElectricpile.setElpiBinding(0);
		List<?> electricpileList=tblElectricpileMapper.getElectricpile(tblElectricpile);
		for (int i = 0; i < electricpileList.size(); i++) {
			ElectricPileList electricPileLists=new ElectricPileList();
			Map<String,Object> electricPileMap=(Map<String,Object>)electricpileList.get(i);
			electricPileLists.setElectricId(JudgeNullUtils.nvlStr(electricPileMap.get("pk_ElectricPile")));
			electricPileLists.setElectricType(ApplicationConsts.ElectricPileManager.ELECTRICPILE);
			electricPileLists.setElectricName(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_ElectricPileName")));
			electricPileLists.setElectricImage(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_Image")));
			
			electricPileLists.setElectricUse(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_PowerUser")));//
			electricPileLists.setElectriChargingMode(JudgeNullUtils.nvlStr(electricPileMap.get("chargingModeName")));//
			electricPileLists.setElectricPowerInterface(JudgeNullUtils.nvlStr(electricPileMap.get("powerName")));//
			electricPileLists.setElectricPowerSize(JudgeNullUtils.nvlStr(electricPileMap.get("powerSizeName")));
			electricPileLists.setElectricMaxElectricity(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_MaxElectricity")));
			electricPileLists.setElectricAddress(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_ElectricPileAddress")));
			electricPileLists.setElectricDistance(JudgeNullUtils.nvlStr(electricPileMap.get("distance")));
			electricPileLists.setElectricLatitude(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_Latitude")));
			electricPileLists.setElectricLongitude(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_Longitude")));
			electricPileList.add(electricPileLists);
		    
		}
		return electricPileList;
	}
  
	/**
	 * 新的快速检索
	 * @param params
	 * 			keyword 关键字
	 * 			elecType 类型（1电站，2电桩，3自行车充电电）
	 * 			orderType 排序（1距离，2价格，3好评）
	 * 			Longitude 经度
	 * 			Latitude 维度
	 * */
	public List<Map<String, String>> getElecSearchList(Map<String, String> params){
		//List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String elecType = params.get("elecType");
		if("1".equals(elecType)){
			return tblPowerstationMapper.getSearchPowerStationList(params);
		}else if("2".equals(elecType) || "3".equals(elecType)){
			return tblElectricpileMapper.getSearchElecPileList(params);
		}
		return new ArrayList<Map<String,String>>();
	}
	
	/**
	 * 获取列表模式电桩列表
	 * @param distance 距离 m
	 * @param price 价格
	 * @param evaluate 好评
	 */
	@Override
	public List<ElectricPileList> getElectricPileListN(TblPowerstation tblPowerstation,TblElectricpile tblElectricpile) {
		
		List<ElectricPileList> electricPileList= new ArrayList<ElectricPileList>();
		
		//4是搜索电动自行车充电点
		//if(!"4".equals(tblElectricpile.getScreenType())){
			//01:获取电站列表
			List<?> powersList=tblPowerstationMapper.getPowerstationN(tblPowerstation);
			for (int i = 0; i < powersList.size(); i++) {
				ElectricPileList electricPileLists=new ElectricPileList();
			    Map<String,Object> powersLMap=(Map<String,Object>)powersList.get(i);
				electricPileLists.setElectricId(JudgeNullUtils.nvlStr(powersLMap.get("pk_PowerStation")));
				electricPileLists.setElectricType(ApplicationConsts.ElectricPileManager.POWERSTATION);
				electricPileLists.setElectricName(JudgeNullUtils.nvlStr(powersLMap.get("poSt_Name")));
				electricPileLists.setElectricPileSum(JudgeNullUtils.nvlStr(powersLMap.get("electricPileCount")));
				electricPileLists.setElectricAddress(JudgeNullUtils.nvlStr(powersLMap.get("poSt_Address")));
				electricPileLists.setElectricDistance(JudgeNullUtils.nvlStr(powersLMap.get("distance")));
				electricPileLists.setServiceCharge(JudgeNullUtils.nvlStr(powersLMap.get("avgServiceCharge"))); 
				electricPileLists.setCommentStart(JudgeNullUtils.nvlStr(powersLMap.get("avgCommentStart")));
				electricPileLists.setElectricLatitude(JudgeNullUtils.nvlStr(powersLMap.get("poSt_Latitude")));
				electricPileLists.setElectricLongitude(JudgeNullUtils.nvlStr(powersLMap.get("poSt_Longitude")));
				electricPileLists.setJlHeadNum(JudgeNullUtils.nvlStr(powersLMap.get("jlHeadNum")));
				electricPileLists.setJlFreeHeadNum(JudgeNullUtils.nvlStr(powersLMap.get("jlFreeHeadNum")));
				electricPileLists.setZlHeadNum(JudgeNullUtils.nvlStr(powersLMap.get("zlHeadNum")));
				electricPileLists.setZlFreeHeadNum(JudgeNullUtils.nvlStr(powersLMap.get("zlFreeHeadNum")));
				electricPileLists.setIsAppoint(JudgeNullUtils.nvlStr(powersLMap.get("poSt_IsAppoint")));
				//电费格式化
				String mark = PowerStationDetailServiceImpl.getCurrentPowerRateMark(JudgeNullUtils.nvlStr(powersLMap.get("raIn_QuantumDate")));
				switch(mark){
					case "1":
						electricPileLists.setCurrentRate(new BigDecimal(JudgeNullUtils.nvlStr(powersLMap.get("raIn_TipTimeTariffPrice"))));
						break;
					case "2":
						electricPileLists.setCurrentRate(new BigDecimal(JudgeNullUtils.nvlStr(powersLMap.get("raIn_PeakElectricityPrice"))));
						break;
					case "3":
						electricPileLists.setCurrentRate(new BigDecimal(JudgeNullUtils.nvlStr(powersLMap.get("raIn_UsualPrice"))));
						break;
					case "4":
						electricPileLists.setCurrentRate(new BigDecimal(JudgeNullUtils.nvlStr(powersLMap.get("raIn_ValleyTimePrice"))));
						break;
					default:
						electricPileLists.setCurrentRate(new BigDecimal(0));
				}
				electricPileList.add(electricPileLists);
			}
		//}
		//01:获取电桩列表
		List<?> electricpileList=tblElectricpileMapper.getElectricpileN(tblElectricpile);
		for (int i = 0; i < electricpileList.size(); i++) {
			ElectricPileList electricPileLists=new ElectricPileList();
			Map<String,Object> electricPileMap=(Map<String,Object>)electricpileList.get(i);
			electricPileLists.setElectricId(JudgeNullUtils.nvlStr(electricPileMap.get("pk_ElectricPile")));
			electricPileLists.setElectricType(ApplicationConsts.ElectricPileManager.ELECTRICPILE);
			electricPileLists.setElectricName(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_ElectricPileName")));
			electricPileLists.setElectriChargingMode(JudgeNullUtils.nvlStr(electricPileMap.get("chargingModeName")));//
			electricPileLists.setElectricPowerInterface(JudgeNullUtils.nvlStr(electricPileMap.get("powerName")));//
			electricPileLists.setElectricPowerSize(JudgeNullUtils.nvlStr(electricPileMap.get("powerSizeName")));
			electricPileLists.setElectricMaxElectricity(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_MaxElectricity")));
			electricPileLists.setElectricAddress(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_ElectricPileAddress")));
			electricPileLists.setElectricDistance(JudgeNullUtils.nvlStr(electricPileMap.get("distance")));
			electricPileLists.setServiceCharge(JudgeNullUtils.nvlStr(electricPileMap.get("raIn_ServiceCharge"))); 
			electricPileLists.setCommentStart(JudgeNullUtils.nvlStr(electricPileMap.get("prCo_CommentStart"))); 
			electricPileLists.setElectricLatitude(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_Latitude")));
			electricPileLists.setElectricLongitude(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_Longitude")));
			electricPileLists.setJlHeadNum(JudgeNullUtils.nvlStr(electricPileMap.get("jlHeadNum")));
			electricPileLists.setJlFreeHeadNum(JudgeNullUtils.nvlStr(electricPileMap.get("jlFreeHeadNum")));
			electricPileLists.setZlHeadNum(JudgeNullUtils.nvlStr(electricPileMap.get("zlHeadNum")));
			electricPileLists.setZlFreeHeadNum(JudgeNullUtils.nvlStr(electricPileMap.get("zlFreeHeadNum")));
			electricPileLists.setIsAppoint(JudgeNullUtils.nvlStr(electricPileMap.get("elPi_IsAppoint")));
			//电费格式化
			String mark = PowerStationDetailServiceImpl.getCurrentPowerRateMark(JudgeNullUtils.nvlStr(electricPileMap.get("raIn_QuantumDate")));
			switch(mark){
				case "1":
					electricPileLists.setCurrentRate(new BigDecimal(JudgeNullUtils.nvlStr(electricPileMap.get("raIn_TipTimeTariffPrice"))));
					break;
				case "2":
					electricPileLists.setCurrentRate(new BigDecimal(JudgeNullUtils.nvlStr(electricPileMap.get("raIn_PeakElectricityPrice"))));
					break;
				case "3":
					electricPileLists.setCurrentRate(new BigDecimal(JudgeNullUtils.nvlStr(electricPileMap.get("raIn_UsualPrice"))));
					break;
				case "4":
					electricPileLists.setCurrentRate(new BigDecimal(JudgeNullUtils.nvlStr(electricPileMap.get("raIn_ValleyTimePrice"))));
					break;
				default:
					electricPileLists.setCurrentRate(new BigDecimal(0));
			}
			electricPileList.add(electricPileLists);
		    
		}
		return electricPileList;
	}
  
	/**
	 * 添加电桩数据
	 */
	/*@Override
	public void addElectricpile(TblElectricpile tblElectricpile,MultipartFile[]  listImage,MultipartFile[] detailImage,UserModel loginUser) {
		tblElectricpile.setElpiState(0);//提交审核中
		tblElectricpile.setElpiCreatedate(new Date());
		//tblElectricpile.setElpiUpdatedate(new Date());
		tblElectricpile.setElpiBinding(0);
		tblElectricpile.setElpiIsappoint(1);
		tblElectricpile.setElpiUserid(loginUser.getUserId());
		tblElectricpile.setElpiUsertype(JudgeNullUtils.nvlInteget(loginUser.getUserLevel()));

        if(loginUser.getUserLevel().equals(WanmaConstants.USER_LEVER_merchant2)){//纯商家,桩体归属企业名称
        	tblElectricpile.setElPiUserName(loginUser.getUserName());
        	
        }else if(loginUser.getUserLevel().equals(WanmaConstants.USER_LEVER_SUPER0) ||
        		loginUser.getUserLevel().equals(WanmaConstants.USER_LEVER_ADMIN1)){//管理员，桩体归属爱充网公司
        	MessageManager manager = MessageManager.getMessageManager();
    		String acwName = manager.getSystemProperties("company.acw");
        	tblElectricpile.setElPiUserName(acwName);
        	
        }else{//其他，管理员确定
        	tblElectricpile.setElPiUserName(loginUser.getUserId());
        }
		if(StringUtils.isBlank(tblElectricpile.getElPi_Tell())){
			tblElectricpile.setElPi_Tell("4000850006");
		}
		
		 tblElectricpileMapper.insert(tblElectricpile);
		 
	    if (ObjectUtil.isNotEmpty(listImage)) {//添加列表图片
	    	tblElectricpile.setAllMultiFile(listImage);
				MultipartFileUtil.saveAllMulti(tblElectricpile,
						WanmaConstants.MULTI_TYPE_ELECTRICT_LIST_IMAGE,
						tblElectricpile.getPkElectricpile() + "");
			}
	    if (ObjectUtil.isNotEmpty(detailImage)) {//添加详情图片
	    	tblElectricpile.setAllMultiFile(detailImage);
			MultipartFileUtil.saveAllMulti(tblElectricpile,
					WanmaConstants.MULTI_TYPE_ELECTRICT_DETAIL_IMAGE,
					tblElectricpile.getPkElectricpile() + "");
		}
	}
	*//**
	 * 修改电桩数据
	 *//*
	@Override
	public void changeElectricpile(TblElectricpile tblElectricpile,MultipartFile[] listImage,MultipartFile[] detailImage,UserModel loginUser) {
		
		//tblElectricpile.setElpiCreatedate(new Date());
		//tblElectricpile.setElpiUpdatedate(new Date());
		//tblElectricpile.setElpiBinding(0);
		//tblElectricpile.setElpiIsappoint(1);
		tblElectricpile.setElpiUserid(loginUser.getUserId());
		tblElectricpile.setElpiUsertype(JudgeNullUtils.nvlInteget(loginUser.getUserLevel()));
		if(StringUtils.isBlank(tblElectricpile.getElPi_Tell())){
			tblElectricpile.setElPi_Tell("4000850006");
		}
		 tblElectricpileMapper.updateByElecId(tblElectricpile);
		 
	    if (ObjectUtil.isNotEmpty(listImage)) {//添加列表图片
	    	tblElectricpile.setAllMultiFile(listImage);
	    	if(MultipartFileUtil.verifyImageIsNotNull(tblElectricpile)){
	    		MultipartFileDao multipartFileDao = new MultipartFileDao();
				List<String> allMultiFileName = multipartFileDao.getAllMultiFileName(WanmaConstants.MULTI_TYPE_ELECTRICT_LIST_IMAGE, tblElectricpile.getPkElectricpile() + "");
		    	MultipartFileUtil.delelteMulti(allMultiFileName, WanmaConstants.MULTI_TYPE_ELECTRICT_LIST_IMAGE, tblElectricpile.getPkElectricpile() + "");
		    	
		    	MultipartFileUtil.saveAllMulti(tblElectricpile,
							WanmaConstants.MULTI_TYPE_ELECTRICT_LIST_IMAGE,
							tblElectricpile.getPkElectricpile() + "");
				}
	    	}
	    if (ObjectUtil.isNotEmpty(detailImage)) {//添加详情图片
	    	tblElectricpile.setAllMultiFile(detailImage);
	    	if(MultipartFileUtil.verifyImageIsNotNull(tblElectricpile)){
	    		
	    		MultipartFileDao multipartFileDao = new MultipartFileDao();
				List<String> allMultiFileName = multipartFileDao.getAllMultiFileName(WanmaConstants.MULTI_TYPE_ELECTRICT_DETAIL_IMAGE, tblElectricpile.getPkElectricpile() + "");
		    	MultipartFileUtil.delelteMulti(allMultiFileName, WanmaConstants.MULTI_TYPE_ELECTRICT_DETAIL_IMAGE, tblElectricpile.getPkElectricpile() + "");
		    	
		    	MultipartFileUtil.saveAllMulti(tblElectricpile,
						WanmaConstants.MULTI_TYPE_ELECTRICT_DETAIL_IMAGE,
						tblElectricpile.getPkElectricpile() + "");
	    	}
		}
	}*/
	/**
	 * 通过ID获取电桩数据
	 */
	@Override
	public TblElectricpile getElectricpileById(TblElectricpile tblElectricpile) {
		
		tblElectricpile=tblElectricpileMapper.get(tblElectricpile);
		//获取电桩列表图片
		List<String> listImage=MultipartFileUtil.getAllMultiUrl(WanmaConstants.MULTI_TYPE_ELECTRICT_LIST_IMAGE,
				tblElectricpile.getPkElectricpile() + "");
		//获取电桩详情图片
		List<String> detailImage=MultipartFileUtil.getAllMultiUrl(WanmaConstants.MULTI_TYPE_ELECTRICT_DETAIL_IMAGE,
				tblElectricpile.getPkElectricpile() + "");
		if(listImage.size()>0 && !listImage.isEmpty()){
			tblElectricpile.setElpiImage(JudgeNullUtils.nvlStr(listImage.get(0)));
		}
		if(detailImage.size()>0 && !detailImage.isEmpty()){
			tblElectricpile.setElpiDetailimage(JudgeNullUtils.nvlStr(detailImage.get(0)));
		}
		  return tblElectricpile;
	}
	
	@Override
	public String checkElectricUnique(String elpiElectricpilecode) {
		// TODO Auto-generated method stub
		// 处理结果
		String processResult = CommonConsts.CHECK_RESULT_OK;
		TblElectricpile tblElectricpile=new TblElectricpile();
		tblElectricpile.setElpiElectricpilecode(elpiElectricpilecode);
		long elecCount=tblElectricpileMapper.searchElectricCountByElecCode(tblElectricpile);
		// 如果取得的用户数大于0个，返回错误标识
		if (elecCount > 0) {
			return CommonConsts.CHECK_RESULT_NG;
		}

		// 返回处理结果标识
		return processResult;
	}
	/**
	 *  修改电桩状态 
	 * @param feedback
	 */
	@Override
	public void updateElectricPileSate(String elpiElectricpilecode,int elpiState) {
	
		TblElectricpile tblElectricpile=new TblElectricpile();
		tblElectricpile.setElpiElectricpilecode(elpiElectricpilecode);
		tblElectricpile.setElpiState(elpiState);
		tblElectricpile.setElpiRejectionreason(null);
	   tblElectricpileMapper.updateElectricPileSate(tblElectricpile);
	}
	/**
	 * 驳回电桩
	 */
	@Override
	public long changeElectricPileExamineReason(TblElectricpile tblElectricpile) {
	
		return tblElectricpileMapper.updateElectricPileExamineReason(tblElectricpile);
	}
	
	/**
	 * 后台电桩列表获取数据
	 */
	@Override
	public List<?> getElectricpileByCondition1(TblElectricpile tblElectricpile) {
		List<Map<String,Object>> electricList=(List<Map<String,Object>>)tblElectricpileMapper.getElectricpileByCondition1(tblElectricpile);
		for (int i = 0; i < electricList.size(); i++) {
			//{pkElectricpile:'1', elpiElectricpilename:'技术部'}
			Map<String,Object> electricpileMap=(Map<String,Object>)electricList.get(i);
			electricpileMap.put("electricpileValue", "{pkElectricpile:'"+JudgeNullUtils.nvlLang(electricpileMap.get("pk_ElectricPile"))+"', " +
					"elpiElectricpilename:'"+JudgeNullUtils.nvlStr(electricpileMap.get("elPi_ElectricPileName"))+"'}");
		}
		return electricList;
	}
	/**
	 * 后台电桩列表获取总数据
	 */
	@Override
	public long getElectricpileByConditionCount1(TblElectricpile tblElectricpile) {
	
		return tblElectricpileMapper.getElectricpileByConditionCount1(tblElectricpile);
	}
	/**
	 * 电桩绑定电站
	 */
	@Override
	public void electricPileBindPower(TblElectricpile tblElectricpile) {
	
		 tblElectricpileMapper.electricPileBindPower(tblElectricpile);
	}
	/**
	 * 删除电桩
	 */
	@Override
	public void removeElectricPile(TblElectricpile tblElectricpile) {
	
		 tblElectricpileMapper.delete(tblElectricpile);
	}
	
	/**
	 * 在app启动时初始化电桩数据到本地
	 * @param epId 暂时无用，后期可能会根据id来获取单条记录
	 * @return
	 */
	public List<Map<String, Object>> initEpFromDB(int epId){
		return tblElectricpileMapper.initEpFromDB(epId);
	}
	
	@Override
	public List<TblElectricpile> getElectricPileGateList() {
		// TODO Auto-generated method stub
		return tblElectricpileMapper.getElectricPileGateList();
	}
}
