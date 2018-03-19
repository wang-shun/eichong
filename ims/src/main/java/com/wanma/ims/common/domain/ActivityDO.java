package com.wanma.ims.common.domain;

import java.util.Date;
import java.util.List;

import com.wanma.ims.common.domain.base.BasicModel;

public class ActivityDO extends BasicModel {
	private static final long serialVersionUID = 932552489483747317L;

	private Integer pkActivity;

	private String actActivityname;//活动名称

	private Integer actType;//活动类型：1-用户活动，2-大客户活动

	private Integer actChanneltype;//渠道主键(投资公司+爱充)

	private Integer actStatus;//状态（1-终止）

	private Integer actActivityrule;//活动规则（1-注册送现金券活动，2-首次体验享现金券，3-邀请注册返现金券活动，4-邀请首次消费返现金券活动,5-充值送，6-消费送，7-指定送 , 8.新手充值送）

	private String actCreateuserid;//

	private String actUpdateuserid;

	private String actRemark;//备注（奖品发放规则）

	private Date actBegindate;//活动开始时间

	private Date actEnddate;//活动结束时间
	
	private Date actCouponEndDate;//活动的优惠券到期时间

	private Date actCreatedate;//创建时间

	private Date actUpdatedate;//修改时间

	private List<ActivityDO> headList;

	private Integer num;

	private String actsType;
	private Integer pkCouponVariety;

	private String prizeName;

	private String actBegindates;

	private String actEnddates;

	private String actCouponEndDates;
	
	private Date currentDate;
	
	private Integer pkCompanyId;//下拉列表公司标识id
	
	private String cpyCompanyName;//下拉列表公司名称
	
	private String singelMoney;//单笔充值/消费/指定地点满就送金额限制
	
	private  String actTopMoney;//单人最高充值金额限制
	
	private Integer actScope;//0:全国通用 1:城市通用 2:站点通用 
	private String provinceCode; // 现金券品种适用范围：省
	private String cityCode; // 现金券品种适用范围：市
	private String areaCode; // 现金券品种适用范围：区
	private Integer pkPowerstation;//充电站主键
	private String actCityScope; // 范围 列表页面用
	private Integer couponValue;//优惠券面值
	private Integer cpCouponcondition;// 优惠券使用条件

	public String getActTopMoney() {
		return actTopMoney;
	}

	public void setActTopMoney(String actTopMoney) {
		this.actTopMoney = actTopMoney;
	}

	public String getSingelMoney() {
		return singelMoney;
	}

	public void setSingelMoney(String singelMoney) {
		this.singelMoney = singelMoney;
	}

	public Integer getPkCompanyId() {
		return pkCompanyId;
	}

	public void setPkCompanyId(Integer pkCompanyId) {
		this.pkCompanyId = pkCompanyId;
	}

	public String getCpyCompanyName() {
		return cpyCompanyName;
	}

	public void setCpyCompanyName(String cpyCompanyName) {
		this.cpyCompanyName = cpyCompanyName;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	
	public Date getActBegindate() {
		return actBegindate;
	}

	public void setActBegindate(Date actBegindate) {
		this.actBegindate = actBegindate;
	}

	public String getActBegindates() {
		return actBegindates;
	}

	public void setActBegindates(String actBegindates) {
		this.actBegindates = actBegindates;
	}

	public String getActEnddates() {
		return actEnddates;
	}

	public void setActEnddates(String actEnddates) {
		this.actEnddates = actEnddates;
	}

	public String getPrizeName() {
		
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getActsType() {
		return actsType;
	}

	public void setActsType(String actsType) {
		this.actsType = actsType;
	}

	public Integer getPkCouponVariety() {
		return pkCouponVariety;
	}

	public void setPkCouponVariety(Integer pkCouponVariety) {
		this.pkCouponVariety = pkCouponVariety;
	}

	public List<ActivityDO> getHeadList() {
		return headList;
	}

	public void setHeadList(List<ActivityDO> headList) {
		this.headList = headList;
	}

	public Integer getPkActivity() {
		return pkActivity;
	}

	public void setPkActivity(Integer pkActivity) {
		this.pkActivity = pkActivity;
	}

	public String getActActivityname() {
		return actActivityname;
	}

	public void setActActivityname(String actActivityname) {
		this.actActivityname = actActivityname == null ? null : actActivityname
				.trim();
	}

	public Integer getActType() {
		return actType;
	}

	public void setActType(Integer actType) {
		this.actType = actType;
	}

	public Integer getActChanneltype() {
		return actChanneltype;
	}

	public void setActChanneltype(Integer actChanneltype) {
		this.actChanneltype = actChanneltype;
	}

	public Integer getActStatus() {
		return actStatus;
	}

	public void setActStatus(Integer actStatus) {
		this.actStatus = actStatus;
	}

	public Integer getActActivityrule() {
		return actActivityrule;
	}

	public void setActActivityrule(Integer actActivityrule) {
		this.actActivityrule = actActivityrule;
	}

	public String getActCreateuserid() {
		return actCreateuserid;
	}

	public void setActCreateuserid(String actCreateuserid) {
		this.actCreateuserid = actCreateuserid;
	}

	public String getActUpdateuserid() {
		return actUpdateuserid;
	}

	public void setActUpdateuserid(String actUpdateuserid) {
		this.actUpdateuserid = actUpdateuserid; 
	}

	public String getActRemark() {
		return actRemark;
	}

	public void setActRemark(String actRemark) {
		this.actRemark = actRemark == null ? null : actRemark.trim();
	}

	
	public Date getActEnddate() {
		return actEnddate;
	}

	public void setActEnddate(Date actEnddate) {
		this.actEnddate = actEnddate;
	}

	public Date getActCreatedate() {
		return actCreatedate;
	}

	public void setActCreatedate(Date actCreatedate) {
		this.actCreatedate = actCreatedate;
	}

	public Date getActUpdatedate() {
		return actUpdatedate;
	}

	public void setActUpdatedate(Date actUpdatedate) {
		this.actUpdatedate = actUpdatedate;
	}

	public Date getActCouponEndDate() {
		return actCouponEndDate;
	}

	public void setActCouponEndDate(Date actCouponEndDate) {
		this.actCouponEndDate = actCouponEndDate;
	}

	public String getActCouponEndDates() {
		return actCouponEndDates;
	}

	public void setActCouponEndDates(String actCouponEndDates) {
		this.actCouponEndDates = actCouponEndDates;
	}
	
	public Integer getActScope() {
		return actScope;
	}

	public void setActScope(Integer actScope) {
		this.actScope = actScope;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Integer getPkPowerstation() {
		return pkPowerstation;
	}

	public void setPkPowerstation(Integer pkPowerstation) {
		this.pkPowerstation = pkPowerstation;
	}

	public String getActCityScope() {
		return actCityScope;
	}

	public void setActCityScope(String actCityScope) {
		this.actCityScope = actCityScope;
	}
	
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Override
	public String toString() {
		return "ActivityDO [pkActivity=" + pkActivity + ", actActivityname="
				+ actActivityname + ", actType=" + actType
				+ ", actChanneltype=" + actChanneltype + ", actStatus="
				+ actStatus + ", actActivityrule=" + actActivityrule
				+ ", actCreateuserid=" + actCreateuserid + ", actUpdateuserid="
				+ actUpdateuserid + ", actRemark=" + actRemark
				+ ", actBegindate=" + actBegindate + ", actEnddate="
				+ actEnddate + ", actCouponEndDate=" + actCouponEndDate
				+ ", actCreatedate=" + actCreatedate + ", actUpdatedate="
				+ actUpdatedate + ", headList=" + headList + ", num=" + num
				+ ", actsType=" + actsType + ", pkCouponVariety="
				+ pkCouponVariety + ", prizeName=" + prizeName
				+ ", actBegindates=" + actBegindates + ", actEnddates="
				+ actEnddates + ", actCouponEndDates=" + actCouponEndDates
				+ ", currentDate=" + currentDate + ", pkCompanyId="
				+ pkCompanyId + ", cpyCompanyName=" + cpyCompanyName
				+ ", singelMoney=" + singelMoney + ", actTopMoney="
				+ actTopMoney + ", actScope=" + actScope + ", provinceCode="
				+ provinceCode + ", cityCode=" + cityCode + ", areaCode="
				+ areaCode + ", pkPowerstation=" + pkPowerstation
				+ ", actCityScope=" + actCityScope + "]";
	}

	public Integer getCouponValue() {
		return couponValue;
	}

	public void setCouponValue(Integer couponValue) {
		this.couponValue = couponValue;
	}

	public Integer getCpCouponcondition() {
		return cpCouponcondition;
	}

	public void setCpCouponcondition(Integer cpCouponcondition) {
		this.cpCouponcondition = cpCouponcondition;
	}
}