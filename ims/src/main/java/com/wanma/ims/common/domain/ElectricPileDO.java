package com.wanma.ims.common.domain;

import java.math.BigDecimal;
import java.util.List;

import com.wanma.ims.common.domain.base.BasicModel;

/**
 * 电桩实体类
 * 
 * @author xyc
 */
public class ElectricPileDO extends BasicModel {

	private static final long serialVersionUID = -6046471508720550938L;

	/**
	 * 主键 ref表字段: pk_ElectricPile
	 */
	private Long id;

	/**
	 * 电桩名称 ref表字段: elPi_ElectricPileName
	 */
	private String name;

	/**
	 * 电桩编码 ref表字段: elPi_ElectricPileCode
	 */
	private String code;

	/**
	 * 电桩编号 ref表字段:ep_num
	 */
	private Integer num;

	/**
	 * 电桩额定功率，配置参数内容的ID（6-3.5kw，15-7kw，172-30kw，171-37.5kw，173-45kw，169-60.5kw，170-60kw，43-120kw） ref表字段:elPi_PowerSize
	 */
	private Integer power;

	/**
	 * 电桩充电方式，配置参数内容的ID （5-直流充电桩，14-交流充电桩） ref表字段:elPi_ChargingMode
	 */
	private Integer chargingMethod;

	/**
	 * 电桩状态 0.离散 1.上线 2.待审核 ref表字段:elPi_State
	 */
	private Integer status;

	/**
	 * 电桩类型，配置参数内容的ID （1-落地式，2-壁挂式，11-一体式，12-分体式） ref表字段:elpi_TypeSpanId
	 */
	private Integer typeId;

	/**
	 * 产品归属，电桩所有权（公司名称） ref表字段: owner_ship
	 */
	private String ownerShip;

	/**
	 * 电桩制造商Id,对应表tbl_pilemaker，配置参数内容的ID （万马新能源，南京循道，北京三优，上海普天） ref表字段: elPi_Maker
	 */
	private Long pileMakerId;

	/**
	 * 产品型号Id,ref表：tbl_typespan ref表字段: elpi_TypeSpanId
	 */
	private Long productModelId;

	/**
	 * 公司Id ref表字段: cpy_id
	 */
	private Long companyId;

	/**
	 * 公司标识 ref表字段: company_number
	 */
	private Long companyNumber;

	/**
	 * 费率表id ref表字段: elPi_RateInformationId
	 */
	private Long rateInformationId;

	/**
	 * 电桩所属省份代码 ref表字段:elPi_OwnProvinceCode
	 */
	private String provinceCode;

	/**
	 * 电桩所属城市代码 ref表字段:elPi_OwnCityCode
	 */
	private String cityCode;

	/**
	 * 电桩所属区县代码 ref表字段:elPi_OwnCountyCode
	 */
	private String areaCode;

	/**
	 * 电桩地址 ref表字段:elPi_ElectricPileAddress
	 */
	private String address;

	/**
	 * 电桩地址经度 ref表字段:elPi_Longitude
	 */
	private BigDecimal longitude;

	/**
	 * 电桩地址维度 ref表字段:elPi_Latitude
	 */
	private BigDecimal latitude;

	/**
	 * 电桩枪口数量 ref表字段:elPi_PowerNumber
	 */
	private Integer muzzleNumber;

	/**
	 * 接口标准 ref表字段:elPi_PowerInterfaceName
	 */
	private String interfaceStandard;

	/**
	 * sim运营商名字 ref表字段:sim_name
	 */
	private String simName;

	/**
	 * sim卡卡号 ref表字段:sim_mac
	 */
	private String simMac;

	/**
	 * 最大电压 单位：V ref表字段:elPi_OutputVoltage
	 */
	private BigDecimal maxVoltage;

	/**
	 * 最大电流 单位：A ref表字段:elPi_OutputCurrent
	 */
	private BigDecimal maxElectricity;

	/**
	 * 备注 ref表字段:elPi_Remark
	 */
	private String remark;

	/**
	 * 集中器Id ref表字段:pk_concentratorID
	 */
	private Long concentratorId;

	/**
	 * 充电站Id ref表字段: elPi_RelevancePowerStation
	 */
	private Long powerStationId;

	/**
	 * 充电点id集合
	 */
	private List<Long> powerStationIdList;

	/**
	 * 0断开；1连接中
	 */
	private Integer commStatus;

	/**
	 * 以下为非持久化字段
	 */
	private List<ElectricPileHeadDO> headList;

	/**
	 * 电桩额定功率 中文
	 */
	private String chPower;

	/**
	 * 电桩充电方式 中文
	 */
	private String chChargingMethod;

	/**
	 * 电桩状态 中文
	 */
	private String chStatus;

	/**
	 * 电桩类型 中文
	 */
	private String type;

	/**
	 * 电站名称 中文
	 */
	private String powerStationName;

	/**
	 * 电站省
	 */
	private String psProvince;

	/**
	 * 电站省
	 */
	private String psProvinceCode;

	/**
	 * 电站城市
	 */
	private String psCity;

	private String psCityCode;

	/**
	 * 电站地区
	 */
	private String psArea;

	private String psAreaCode;

	/**
	 * 电站地址
	 */
	private String psAddress;

	/**
	 * 电桩制造商 中文
	 */
	private String pileMaker;

	/**
	 * 电桩制造商标识
	 */
	private String pileMakerRemark;

	/**
	 * 产品型号 中文
	 */
	private String productModel;

	/**
	 * 渠道 中文
	 */
	private String company;

	/**
	 * 电桩所属省份 中文
	 */
	private String province;

	/**
	 * 电桩所属城市 中文
	 */
	private String city;

	/**
	 * 电桩所属区县 中文
	 */
	private String area;

	/**
	 * 电桩id范围
	 */
	private List<Long> ids;

	/**
	 * 是否是绑定充电点查询 0 否，1是
	 */
	private Integer isBindPowerStationSearch;

	/**
	 * 是否是绑定集中器查询 0否，1是
	 */
	private Integer isBindConcentratorSearch;

	/**
	 * 以下为在获取充电范围树时使用的字段 是否被勾选
	 */
	private Boolean isSelected;

	/**
	 * 资金账户
	 */
	private Long accountId;

	/**
	 *  明细1
	 */
	private String flag;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public Integer getChargingMethod() {
		return chargingMethod;
	}

	public void setChargingMethod(Integer chargingMethod) {
		this.chargingMethod = chargingMethod;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getOwnerShip() {
		return ownerShip;
	}

	public void setOwnerShip(String ownerShip) {
		this.ownerShip = ownerShip;
	}

	public Long getPileMakerId() {
		return pileMakerId;
	}

	public void setPileMakerId(Long pileMakerId) {
		this.pileMakerId = pileMakerId;
	}

	public Long getProductModelId() {
		return productModelId;
	}

	public void setProductModelId(Long productModelId) {
		this.productModelId = productModelId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(Long companyNumber) {
		this.companyNumber = companyNumber;
	}

	public Long getRateInformationId() {
		return rateInformationId;
	}

	public void setRateInformationId(Long rateInformationId) {
		this.rateInformationId = rateInformationId;
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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public Integer getMuzzleNumber() {
		return muzzleNumber;
	}

	public void setMuzzleNumber(Integer muzzleNumber) {
		this.muzzleNumber = muzzleNumber;
	}

	public String getInterfaceStandard() {
		return interfaceStandard;
	}

	public void setInterfaceStandard(String interfaceStandard) {
		this.interfaceStandard = interfaceStandard;
	}

	public String getSimName() {
		return simName;
	}

	public void setSimName(String simName) {
		this.simName = simName;
	}

	public String getSimMac() {
		return simMac;
	}

	public void setSimMac(String simMac) {
		this.simMac = simMac;
	}

	public BigDecimal getMaxVoltage() {
		return maxVoltage;
	}

	public void setMaxVoltage(BigDecimal maxVoltage) {
		this.maxVoltage = maxVoltage;
	}

	public BigDecimal getMaxElectricity() {
		return maxElectricity;
	}

	public void setMaxElectricity(BigDecimal maxElectricity) {
		this.maxElectricity = maxElectricity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getConcentratorId() {
		return concentratorId;
	}

	public void setConcentratorId(Long concentratorId) {
		this.concentratorId = concentratorId;
	}

	public Long getPowerStationId() {
		return powerStationId;
	}

	public void setPowerStationId(Long powerStationId) {
		this.powerStationId = powerStationId;
	}

	public List<Long> getPowerStationIdList() {
		return powerStationIdList;
	}

	public void setPowerStationIdList(List<Long> powerStationIdList) {
		this.powerStationIdList = powerStationIdList;
	}

	public List<ElectricPileHeadDO> getHeadList() {
		return headList;
	}

	public void setHeadList(List<ElectricPileHeadDO> headList) {
		this.headList = headList;
	}

	public String getChPower() {
		return chPower;
	}

	public void setChPower(String chPower) {
		this.chPower = chPower;
	}

	public String getChChargingMethod() {
		return chChargingMethod;
	}

	public void setChChargingMethod(String chChargingMethod) {
		this.chChargingMethod = chChargingMethod;
	}

	public String getChStatus() {
		return chStatus;
	}

	public void setChStatus(String chStatus) {
		this.chStatus = chStatus;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPileMaker() {
		return pileMaker;
	}

	public void setPileMaker(String pileMaker) {
		this.pileMaker = pileMaker;
	}

	public String getPileMakerRemark() {
		return pileMakerRemark;
	}

	public void setPileMakerRemark(String pileMakerRemark) {
		this.pileMakerRemark = pileMakerRemark;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public Integer getIsBindPowerStationSearch() {
		return isBindPowerStationSearch;
	}

	public void setIsBindPowerStationSearch(Integer isBindPowerStationSearch) {
		this.isBindPowerStationSearch = isBindPowerStationSearch;
	}

	public Integer getIsBindConcentratorSearch() {
		return isBindConcentratorSearch;
	}

	public void setIsBindConcentratorSearch(Integer isBindConcentratorSearch) {
		this.isBindConcentratorSearch = isBindConcentratorSearch;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Integer getCommStatus() {
		return commStatus;
	}

	public void setCommStatus(Integer commStatus) {
		this.commStatus = commStatus;
	}

	public String getPowerStationName() {
		return powerStationName;
	}

	public void setPowerStationName(String powerStationName) {
		this.powerStationName = powerStationName;
	}

	public String getPsProvince() {
		return psProvince;
	}

	public void setPsProvince(String psProvince) {
		this.psProvince = psProvince;
	}

	public String getPsCity() {
		return psCity;
	}

	public void setPsCity(String psCity) {
		this.psCity = psCity;
	}

	public String getPsArea() {
		return psArea;
	}

	public void setPsArea(String psArea) {
		this.psArea = psArea;
	}

	public String getPsAddress() {
		return psAddress;
	}

	public void setPsAddress(String psAddress) {
		this.psAddress = psAddress;
	}

	public String getPsProvinceCode() {
		return psProvinceCode;
	}

	public void setPsProvinceCode(String psProvinceCode) {
		this.psProvinceCode = psProvinceCode;
	}

	public String getPsCityCode() {
		return psCityCode;
	}

	public void setPsCityCode(String psCityCode) {
		this.psCityCode = psCityCode;
	}

	public String getPsAreaCode() {
		return psAreaCode;
	}

	public void setPsAreaCode(String psAreaCode) {
		this.psAreaCode = psAreaCode;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

}
