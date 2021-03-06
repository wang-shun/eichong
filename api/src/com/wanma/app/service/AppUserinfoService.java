package com.wanma.app.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wanma.model.MyProductcomment;
import com.wanma.model.MyWallet;
import com.wanma.model.TblEarnings;
import com.wanma.model.TblFootprint;
import com.wanma.model.TblProductcomment;
import com.wanma.model.TblPurchasehistory;
import com.wanma.model.TblUserinfo;
import com.wanma.model.Userinfo;
import com.wanma.model.UserLoginRecord;

/**
 * 
 * @Description: 用户信息业务处理接口
 * @author songjf
 * @createTime：2015-3-13 上午10:31:56
 * @updator：
 * @updateTime：
 * @version：V1.0
 */
public interface AppUserinfoService {
	
	
	/*
	 * 登录记录 
	 */
	
	
	public void insertLoginRecord(UserLoginRecord record) throws Exception;

	/**
	 * @Title: checkLoginUser
	 * @Description: 登录检查
	 * @param params
	 *            用户名 密码
	 * @return String 用户登录检查结果 001:成功 002:用户不存在 003:密码错误
	 */
	public String checkLoginUser(Map<String, Object> params);

	/**
	 * 
	 * @Description: 充电卡初始化，登录检查
	 * @param params
	 *            account 用户名，pwd 密码
	 * @return String 用户登录检查结果 001:成功 002:用户不存在 003:密码错误
	 */
	public String checkCLoginUser(Map<String, Object> params);

	/**
	 * @Title: getUserById
	 * @Description: 根据id获取用户信息
	 * @param pkUserInfo
	 * @return
	 */
	public TblUserinfo getUserById(int pkUserInfo);
	
	/**
	 * @Title: updateUserInfo
	 * @Description: 修改用户资料
	 * @param pkUserInfo
	 * @return
	 */
	public void updateUserInfo(TblUserinfo pkUserInfo);
	
	

	/**
	 * @Title: userIsExist
	 * @Description: 判断用户是否存在
	 * @param usinPhone
	 * @return
	 */
	public int userIsExist(String usinPhone);

	/**
	 * @Title: checkAuthCode
	 * @Description: 检查验证码是否正确
	 * @param authCode
	 *            验证码
	 * @param usinPhone
	 *            手机号
	 * @return 0正确  1验证码错误  2超时  3无效
	 */
	public int checkAuthCode(String authCode, String usinPhone);

	/**
	 * @Title: insertUserinfo
	 * @Description: 保存用户注册信息
	 * @param params
	 * @return
	 */
	public void insertUserinfo(TblUserinfo userinfo) throws Exception;

	/**
	 * @Title: resetPasswrod
	 * @Description: 重置密码
	 * @param params
	 *            新密码 手机号
	 * @return
	 */
	public int resetPasswrod(Map<String, Object> params);

	/**
	 * 获取用户信息
	 * 
	 * @param userinfo
	 * @return
	 */
	public Userinfo getUserInfo(Userinfo userinfo);

	/**
	 * 一键升级为商家
	 * 
	 * @param userinfo
	 * @return
	 */
	public void upgradeoUser(TblUserinfo tblUserinfo);

	/**
	 * @Title: findProComments
	 * @Description: 获取产品评论
	 * @param params
	 * @return
	 */
	public List<MyProductcomment> findProComments(
			TblProductcomment fProductcomment);

	public List<HashMap<String, Object>> findProCommentsN(int userId);
	/**
	 * @Title: findProComments
	 * @Description: 删除产品评论
	 * @param params
	 * @return
	 */
	public void removeMyComment(String productId);
	/**
	 * 根据评论id删除对应的电桩或电站评论
	 * @param cId
	 * @param type
	 */
	public void removeMyCommentN(String cId,String type);
	/**
	 * @Title: findProComments
	 * @Description: 获取我的足迹
	 * @param params
	 * @return
	 */
	public List<TblFootprint> getMyFootPrint(TblFootprint tblFootprint);

	/**
	 * @Title: findProComments
	 * @Description: 获取我的钱包
	 * @param params
	 * @return
	 */
	public MyWallet getMyWallet(TblPurchasehistory tblPurchasehistory,
			TblEarnings tblEarnings);
	
	/**
	 * 根据用户id获取用户账单（出账、入账）
	 * @param params
	 * 			uId 用户id，pageNum 页码，pageNumber 每页数量
	 * @return
	 */
	public MyWallet myBills(Map<String, Object> params);
	
	/**
	 * 获取发票对应的消费列表
	 * @param iId 发票id
	 * @return
	 */
	public MyWallet invoicePurList(long iId);
	
	/**
	 * 用户充值
	 * @param userinfo
	 * @return
	 */
	public void userRecharge(TblUserinfo tblUserinfo);
	
	/**
	 * @Title: getByPhone
	 * @Description: 根据用户手机号获取用户信息
	 * @param usinPhone
	 * @return
	 */
	public TblUserinfo getByPhone(String usinPhone);
	/**
	 * 更新用户账户余额
	 * @param userMap
	 * 			userId 用户id blance 账户余额
	 */
	public void updateUserBlance(Map<String, Object> userMap);
	
	/**
	 * 更新登陆时的设备key
	 * @param deviceKey
	 */
	public void updateUserDeviceId(Map<String, Object> params);
	
	/**
	 * 设置支付密码
	 * @param map
	 */
	public void setPayPwd(Map<String, Object> map);
	
	/**
	 * 获取用户账户余额
	 * @param userId
	 * @return
	 */
	public BigDecimal getUserABById(int userId);
	
	/**
	 * 根据用户id修改密码
	 * @param params
	 */
	public void updatePwByUid(Map<String, String> params);
	
	/**
	 * 申请充电卡
	 * @param params
	 */
	public void applyCard(Map<String, String> params);
	
	/**
	 * 更新用户表中卡申请状态
	 * @param params
	 * 			uId 用户id，status 状态1申请中2已发卡
	 */
	public void updateUserApplyCardStatus(Map<String, String> params);
	
	/**
	 * 卡申请列表
	 * @param uId
	 * @return
	 */
	public List<Map<String, String>> applyListByUid(long uId);
	
	/**
	 * 从user表获取用户的状态，正常还是冻结
	 * @param uId
	 * @return
	 */
	public int getStatusFromUserTable(long uId);

	
	/**
	 * 获取月账单
	 * @param uId
	 * @return
	 */
	public List<Map<String,Object>> getMtBills(Map<String,Object> params);

	/**
	 * 获取消费记录
	 * @param uId
	 * @return
	 */
	public List<Map<String, Object>> getPhDetail(Map<String, Object> params);
}
