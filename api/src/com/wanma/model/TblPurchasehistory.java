package com.wanma.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * tbl_PurchaseHistory表
 * @author mew
 *
 */
public class TblPurchasehistory implements Serializable {
	private java.lang.Integer pkPurchasehistory; // 主键
	private java.lang.Integer puhiType; // 消费类型 1-充电消费 2-预约消费 3-购物消费 4-充值
	private java.util.Date puhiPurchasehistorytime; // 消费时间
	private java.math.BigDecimal puhiMonetary; // 消费金额
	private java.lang.String puhiConsumerremark; // 消费金额
	private java.util.Date puhiCreatedate; // 消费金额
	private java.util.Date puhiUpdatedate; // 修改时间
	private java.lang.String puhiPurchasecontent; // 收益内容
	private java.lang.Integer puhiUserid; // 用户ID
	private int chargeType; //充值类型，1支付宝充值，2微信充值，3银联充值，4充电卡现金充值
	private String starttime; //搜索条件 开始时间
	private String endtime; //搜索条件 结束时间
	//private int type; //搜索条件 消费类型
	private java.lang.String puhiPaymentNumber; // 消费流水号
	
	private Integer puhiChargeType;//充值类型(当puhiType==4时有值):1支付宝充值，2微信充值，3银联充值.
	private java.util.Date act_beginTime; // 活动开始时间
	
	
	public java.util.Date getAct_beginTime() {
		return act_beginTime;
	}

	public void setAct_beginTime(java.util.Date act_beginTime) {
		this.act_beginTime = act_beginTime;
	}

	/**
     * 获取主键属性
     *
     * @return pkPurchasehistory
     */
	public java.lang.Integer getPkPurchasehistory() {
		return pkPurchasehistory;
	}

	public int getChargeType() {
		return chargeType;
	}

	public void setChargeType(int chargeType) {
		this.chargeType = chargeType;
	}

	/**
	 * 设置主键属性
	 *
	 * @param pkPurchasehistory
	 */
	public void setPkPurchasehistory(java.lang.Integer pkPurchasehistory) {
		this.pkPurchasehistory = pkPurchasehistory;
	}
	
	
	
	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	/**
     * 获取消费类型 1-充电消费 2-预约消费 3-购物消费 4-充值属性
     *
     * @return puhiType
     */
	public java.lang.Integer getPuhiType() {
		return puhiType;
	}
	
	/**
	 * 设置消费类型 1-充电消费 2-预约消费 3-购物消费 4-充值属性
	 *
	 * @param puhiType
	 */
	public void setPuhiType(java.lang.Integer puhiType) {
		this.puhiType = puhiType;
	}
	
	/**
     * 获取消费时间属性
     *
     * @return puhiPurchasehistorytime
     */
	public java.util.Date getPuhiPurchasehistorytime() {
		return puhiPurchasehistorytime;
	}
	
	/**
	 * 设置消费时间属性
	 *
	 * @param puhiPurchasehistorytime
	 */
	public void setPuhiPurchasehistorytime(java.util.Date puhiPurchasehistorytime) {
		this.puhiPurchasehistorytime = puhiPurchasehistorytime;
	}
	
	/**
     * 获取消费金额属性
     *
     * @return puhiMonetary
     */
	public java.math.BigDecimal getPuhiMonetary() {
		return puhiMonetary;
	}
	
	/**
	 * 设置消费金额属性
	 *
	 * @param puhiMonetary
	 */
	public void setPuhiMonetary(java.math.BigDecimal puhiMonetary) {
		this.puhiMonetary = puhiMonetary;
	}
	
	/**
     * 获取消费金额属性
     *
     * @return puhiConsumerremark
     */
	public java.lang.String getPuhiConsumerremark() {
		return puhiConsumerremark;
	}
	
	/**
	 * 设置消费金额属性
	 *
	 * @param puhiConsumerremark
	 */
	public void setPuhiConsumerremark(java.lang.String puhiConsumerremark) {
		this.puhiConsumerremark = puhiConsumerremark;
	}
	
	/**
     * 获取消费金额属性
     *
     * @return puhiCreatedate
     */
	public java.util.Date getPuhiCreatedate() {
		return puhiCreatedate;
	}
	
	/**
	 * 设置消费金额属性
	 *
	 * @param puhiCreatedate
	 */
	public void setPuhiCreatedate(java.util.Date puhiCreatedate) {
		this.puhiCreatedate = puhiCreatedate;
	}
	
	/**
     * 获取修改时间属性
     *
     * @return puhiUpdatedate
     */
	public java.util.Date getPuhiUpdatedate() {
		return puhiUpdatedate;
	}
	
	/**
	 * 设置修改时间属性
	 *
	 * @param puhiUpdatedate
	 */
	public void setPuhiUpdatedate(java.util.Date puhiUpdatedate) {
		this.puhiUpdatedate = puhiUpdatedate;
	}
	
	/**
     * 获取收益内容属性
     *
     * @return puhiPurchasecontent
     */
	public java.lang.String getPuhiPurchasecontent() {
		return puhiPurchasecontent;
	}
	
	/**
	 * 设置收益内容属性
	 *
	 * @param puhiPurchasecontent
	 */
	public void setPuhiPurchasecontent(java.lang.String puhiPurchasecontent) {
		this.puhiPurchasecontent = puhiPurchasecontent;
	}
	
	/**
     * 获取用户ID属性
     *
     * @return puhiUserid
     */
	public java.lang.Integer getPuhiUserid() {
		return puhiUserid;
	}
	
	/**
	 * 设置用户ID属性
	 *
	 * @param puhiUserid
	 */
	public void setPuhiUserid(java.lang.Integer puhiUserid) {
		this.puhiUserid = puhiUserid;
	}
	

	public java.lang.String getPuhiPaymentNumber() {
		return puhiPaymentNumber;
	}

	public void setPuhiPaymentNumber(java.lang.String puhiPaymentNumber) {
		this.puhiPaymentNumber = puhiPaymentNumber;
	}

	public Integer getPuhiChargeType() {
		return puhiChargeType;
	}

	public void setPuhiChargeType(Integer puhiChargeType) {
		this.puhiChargeType = puhiChargeType;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TblPurchasehistory");
        sb.append("{pkPurchasehistory=").append(pkPurchasehistory);
        sb.append(", puhiType=").append(puhiType);
        sb.append(", puhiPurchasehistorytime=").append(puhiPurchasehistorytime);
        sb.append(", puhiMonetary=").append(puhiMonetary);
        sb.append(", puhiConsumerremark=").append(puhiConsumerremark);
        sb.append(", puhiCreatedate=").append(puhiCreatedate);
        sb.append(", puhiUpdatedate=").append(puhiUpdatedate);
        sb.append(", puhiPurchasecontent=").append(puhiPurchasecontent);
        sb.append(", puhiUserid=").append(puhiUserid);
		sb.append('}');
        return sb.toString();
    }
}