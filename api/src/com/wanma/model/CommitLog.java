package com.wanma.model;

import java.io.Serializable;
import java.util.Date;

import com.bluemobi.product.model.common.BasicListAndMutiFile;

/**
 *  日志管理
  * @Description:
  * @author bruce cheng(http://blog.csdn.net/brucehome)
  * @createTime：2015-4-7 下午05:17:39 
  * @updator： 
  * @updateTime：   
  * @version：V1.0
 */
public class CommitLog extends BasicListAndMutiFile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String commitLog;//主键id

	private String logName;//用户名称
	
	private String ipAddress;//ip地址
	
	private String logContent;//操作内容
	
	private String status;//0 显示 -1：已删除
	
	private Date Createdate; //创建时间
	
	private Date Updatedate;  //修改时间按

	
	private String coLoUserId;  //日志操作人
	
	// 以下字段不予数据库对应
	private String start_create_date;
			
	public String getStart_create_date() {
		return start_create_date;
	}

	public void setStart_create_date(String start_create_date) {
		this.start_create_date = start_create_date;
	}

	public String getEnd_create_date() {
		return end_create_date;
	}

	public void setEnd_create_date(String end_create_date) {
		this.end_create_date = end_create_date;
	}

	private String end_create_date;
	
	public String getCommitLog() {
		return commitLog;
	}

	public void setCommitLog(String commitLog) {
		this.commitLog = commitLog;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedate() {
		return Createdate;
	}

	public void setCreatedate(Date createdate) {
		Createdate = createdate;
	}

	public Date getUpdatedate() {
		return Updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		Updatedate = updatedate;
	}

	public String getCoLoUserId() {
		return coLoUserId;
	}

	public void setCoLoUserId(String coLoUserId) {
		this.coLoUserId = coLoUserId;
	}

	

}
