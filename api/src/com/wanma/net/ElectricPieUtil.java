/**
 * FileName: ElectricPieUtil.java
 * Author: Administrator
 * Create: 2015年3月18日
 * Last Modified: 2015年3月18日
 * Version: V1.0 
 */
package com.wanma.net;

import io.netty.channel.ChannelFuture;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.appCore.netty.buffer.DynamicByteBuffer;
import com.appCore.util.ByteUtil;
import com.bluemobi.product.utils.JPushUtil;
import com.bluemobi.product.utils.SpringContextHolder;
import com.wanma.app.service.AppJpushService;
import com.wanma.app.service.AppUserinfoService;
import com.wanma.app.util.DateUtil;
import com.wanma.common.WanmaConstants;
import com.wanma.model.TblJpush;
import com.wanma.model.TblUserinfo;

/**
 * 电桩工具类
 * 
 * @version V1.0
 * @author yangweiran
 * @date 2015年3月18日
 */
public class ElectricPieUtil {

	/** 日志文件生成器 */
	private static Logger log = Logger.getLogger(ElectricPieUtil.class);

	/** 指令 :预约充电 */
	public static final short ORDER_TYPE_10301 = 10301;

	/** 指令 :取消预约 */
	public static final short ORDER_TYPE_10302 = 10302;

	/** 指令 :开始充电 */
	public static final short ORDER_TYPE_10303 = 10303;

	/** 指令 :停止充电 */
	public static final short ORDER_TYPE_10304 = 10304;

	/** 指令 :私有电桩运营时间控制 */
	public static final short ORDER_TYPE_10305 = 10305;
	
	/** 指令：下发费率 */
	public static final short ORDER_TYPE_10306 = 10306;
	
	/** 指令：下发二维码 */
	public static final short ORDER_TYPE_10307 = 10307;
	
	/** 指令：降地锁*/
	public static final short ORDER_TYPE_10309 = 10309;
	
	/** 指令：LED灯开关*/
	public static final short ORDER_TYPE_10310 = 10310;
	
	/** 指令：2.0版之后的开始充电*/
	public static final short ORDER_TYPE_10311 = 10311;
	/** 指令：桩自动升级*/
	public static final short ORDER_TYPE_10312 = 10312;
	/** 指令：下发集中器id*/
	public static final short ORDER_TYPE_10313 = 10313;
	/** 指令：下发最大离线充电次数*/
	public static final short ORDER_TYPE_10314 = 10314;
	/** 指令：下发定时充电*/
	public static final short ORDER_TYPE_10315 = 10315;
	
	/** 服务器返回指令 :预约充电 */
	public static final short BACK_TYPE_101 = 101;

	/** 服务器返回指令 :取消预约 */
	public static final short BACK_TYPE_102 = 102;

	/** 服务器返回指令 :开始充电 */
	public static final short BACK_TYPE_103 = 103;

	/** 服务器返回指令 :结束充电 */
	public static final short BACK_TYPE_104 = 104;

	/** 服务器返回指令 :充电开始事件 */
	public static final short BACK_TYPE_105 = 105;

	/** 服务器返回指令 :充电结束事件 */
	public static final short BACK_TYPE_106 = 106;

	/** 服务器返回指令 :消费记录数据 */
	public static final short BACK_TYPE_107 = 107;

	/** 服务器返回指令 :充电余额告警 */
	public static final short BACK_TYPE_108 = 108;
	//取消预约返回事件
	public static final short BACK_TYPE_109 = 109;
	//电桩gate改变时修改内存
	public static final short BACK_TYPE_110 = 110;
	//取消预约返回事件
	public static final short BACK_TYPE_111 = 111;
	
	/**
	 * 预约
	 * 
	 * @param bussinessId
	 *            预约订单主键ID
	 * @param electricPieNumber
	 *            电桩编号
	 * @param buyTime
	 *            预约买断时间
	 * @param bookStartTime
	 *            预约开始时间
	 * @param renewalFlag
	 *            续约标识（0：第一次预约，1：续约）
	 * @param accountId
	 *            用户账号
	 * @param remainAmount
	 *            用户余额
	 * @param ephId
	 *            电桩枪口主键
	 * @param electricpileHeadId
	 *            电桩枪口编号
	 * @param bespFrozenamt
	 *            预约冻结金额
	 * @param orgNum
	 *            合作商编码
	 * @param payMod
	 *            支付模式 1先付费 2后付费            
	 * @return
	 */
	public static void bookElectricPie( String bussinessCode,int epId,
			String electricPieNumber, String buyTime, Date bookStartTime,
			int renewalFlag, int userid,String accountId, int ephId,
			int electricpileHeadId,int orgNum,int payMod ) {
		log.debug("开始预约...——bookElectricPie");
		DynamicByteBuffer byteBuffer = DynamicByteBuffer.allocate();

		//预约主键
		//byteBuffer.putLong(bespId);
		// 预约号
		byteBuffer.putString(bussinessCode);
		//电桩id
		byteBuffer.putInt(epId);
		// 电桩编号
		byteBuffer.putString(electricPieNumber);
		// 预约买断时间
		byteBuffer.putShort(Short.parseShort(buyTime));
		// 预约开始时间
		byteBuffer.putLong(DateUtil.toLong(bookStartTime));
		// 续约标识
		byteBuffer.putInt(renewalFlag);
		//用户id
		byteBuffer.putInt(userid);
		// 用户账户
		byteBuffer.putString(accountId);
		// 用户余额
		//byteBuffer.putDouble(remainAmount.doubleValue());
		// 电桩枪口编号
		byteBuffer.putInt(electricpileHeadId);
		// 枪口主键
		byteBuffer.putLong(ephId);
		// 合作商标识 爱充是1000
		byteBuffer.putInt(orgNum);
		// 1先付费 2后付费
		byteBuffer.putInt(payMod);
		byte[] bb = byteBuffer.getBytes();
		ChannelFuture cf = NetConnetionManager.sendMessage(electricPieNumber,ORDER_TYPE_10301, bb);
		if(null == cf){
			WanmaConstants.bespkeState.put(bussinessCode, 6002);
		}
		log.debug("充电预约结束——bookElectricPie。");
	}

	/**
	 * 预约充电返回处理
	 * 
	 * @param byteBuffer
	 *            返回信息
	 * @return
	 */
	public static void bookElectricPieBack(ByteBuffer byteBuffer) {
		log.debug("充电预约返回开始...——bookElectricPieBack");
		
		//预约编号
		String bespCode = ByteUtil.getString(byteBuffer);
		//是否成功标识 0失败1成功
		int bespStatus = byteBuffer.getShort();
		//错误原因编码 0成功6000以上失败
		int errCode = byteBuffer.getInt();
		//用户id
		int userId = byteBuffer.getInt();
		//续约标志 0预约1续约
		int bespType = byteBuffer.getShort();
		
		if (bespStatus == 1) {
			if(bespType == 0){
				WanmaConstants.bespkeState.replace(bespCode, WanmaConstants.BESPOKE_ING);
			}else{
				WanmaConstants.bespkeState.replace(bespCode, WanmaConstants.BESPOKE_CONTRACT);
			}
			
			//完毕后推送
			AppJpushService jpushService = (AppJpushService) SpringContextHolder
					.getSpringContext().getBean("jpushService");
			TblJpush jpush = jpushService.getByuserInfo(userId);
			String msg = "尊敬的用户：您的预约已经生效，请尽快前往充电！";
			JPushUtil.point2point("预约成功",msg,jpush.getJpushRegistrationid(),
							jpush.getJpushDevicetype(), "5");
			
		}else{
			if(errCode >= 6000 || errCode == 1002){
				WanmaConstants.bespkeState.replace(bespCode, errCode);
			}else{
				WanmaConstants.bespkeState.replace(bespCode, WanmaConstants.BESPOKE_AFFIRM_FAIL);
			}
		}
		
		
		log.debug("充电预约返回结束——bookElectricPieBack。");
	}

	/**
	 * 取消预约
	 * 
	 * @param bussinessId
	 *            业务ID
	 * @param electricPieNumber
	 *            电桩编号
	 * @param tipId
	 *            电桩枪口
	 * @param electricpileHeadId
	 *            电桩枪口编号
	 * @return
	 */
	public static void cancelBookElectricPie(long bespId,String bespCode,
			String electricPieNumber,int headNum) {
		log.debug("取消预约...——cancelBookElectricPie");
		DynamicByteBuffer byteBuffer = DynamicByteBuffer.allocate();
		//预约id
		byteBuffer.putLong(bespId);
		// 预约号
		byteBuffer.putString(bespCode);
		// 电桩编号
		byteBuffer.putString(electricPieNumber);
		//枪口号
		byteBuffer.putInt(headNum);
		
		byte[] bb = byteBuffer.getBytes();
		// 以下注释有用，因测试需要暂时注释
		ChannelFuture cf = NetConnetionManager.sendMessage(electricPieNumber,ORDER_TYPE_10302, bb);
		if(null == cf){
			WanmaConstants.bespkeState.replace(bespCode, 6002);
		}
		log.debug("取消预约结束——cancelBookElectricPie。");
	}

	/**
	 * 取消预约充电返回处理
	 * 
	 * @param byteBuffer
	 *            返回信息
	 * @return
	 */
	public static void cancelBookElectricPieBack(ByteBuffer byteBuffer) {
		log.debug("取消预约返回开始...——cancelBookElectricPieBack");
		//预约编号
		String bespCode = ByteUtil.getString(byteBuffer); 
		//成功状态 1成功0失败
		short status = byteBuffer.getShort();
		//错误码 0成功 
		int errCode = byteBuffer.getInt();
		
		if (status == 1) {
			WanmaConstants.bespkeState.replace(bespCode, WanmaConstants.BESPOKE_END);
		}else{
			WanmaConstants.bespkeState.replace(bespCode, errCode);
		}
		log.debug("取消预约返回结束——cancelBookElectricPieBack。");
	}

	/**
	 * 取消预约返回事件
	 * @param byteBuffer
	 */
	public static void cancelBookElectricPieEvent(ByteBuffer byteBuffer){
		//实际消费
		double consume = byteBuffer.getDouble();
		//账户余额
		double blance = byteBuffer.getDouble();
		//用户账户
		String userPhone = ByteUtil.getString(byteBuffer);
		
		// 用户业务处理对象
		AppUserinfoService userinfoService = (AppUserinfoService) SpringContextHolder
				.getSpringContext().getBean("userinfoService");
		TblUserinfo userInfo = userinfoService.getByPhone(userPhone);
		// 极光推送业务处理对象
		AppJpushService jpushService = (AppJpushService) SpringContextHolder
				.getSpringContext().getBean("jpushService");
		TblJpush jpush = jpushService.getByuserInfo(userInfo.getPkUserinfo());
		String msg = "尊敬的用户：您的预约已经取消！实际消费：" + consume + "元，账户剩余：" + blance + "元。";
		log.debug("取消预约事件推送，推送类型：6，推送消息：" + msg);
		JPushUtil.point2point("取消预约",msg,jpush.getJpushRegistrationid(),
						jpush.getJpushDevicetype(), "6");
	}

	/**
	 * 开始充电
	 * 
	 * @param bussinessId
	 *            电桩ID
	 * @param electricPieNumber
	 *            电桩编号
	 * @param tipId
	 *            电桩枪口编号
	 * @param userPhone
	 *            用户账户
	 * @param org
	 *            合作商编号 爱充为1000
	 * @param payMod
	 *            1先付费 2后付费
	 * @return
	 */
	public static void startCharge(String bussinessId,String electricPieNumber, 
			int tipId, int userId, String userPhone,String bespokeCode,BigDecimal userBlance,
			String scanType,double preMoney,int org,int payMod) {
		log.debug("开始充电...——startCharge");
		DynamicByteBuffer byteBuffer = DynamicByteBuffer.allocate();

		// 电桩编号
		byteBuffer.putString(electricPieNumber);
		// 电桩枪口编号
		byteBuffer.putInt(tipId);
		if(StringUtils.isEmpty(bespokeCode)){
			bespokeCode = "";
		}
		//预约编号
		byteBuffer.putString(bespokeCode);
		//用户id
		byteBuffer.putInt(userId);
		// 用户账户
		byteBuffer.putString(userPhone);
		//充电方式标示 1二维码2验证码
		byteBuffer.putShort(Short.parseShort(scanType));
		//预冲金额
		byteBuffer.putDouble(preMoney);
		// 第三方合作商编号 爱充为1000
		byteBuffer.putInt(org);
		// 1先付费 2后付费
		byteBuffer.putInt(payMod);
		
		byte[] bb = byteBuffer.getBytes();
		
		// 以下注释有用，因测试需要暂时注释
		ChannelFuture cf =  NetConnetionManager.sendMessage(electricPieNumber,ORDER_TYPE_10311, bb);
		
		if(null == cf){
			WanmaConstants.changeState.replace(electricPieNumber+"|"+tipId, 6002);
		}
		
		log.debug("开始充电结束——startCharge。");
	}

	/**
	 * 开始充电返回处理
	 * 
	 * @param byteBuffer
	 *            返回信息
	 * @return
	 */
	// 此注释有用，因测试需要暂时注释
	public static void startChargeBack(ByteBuffer byteBuffer) {

		log.debug("开始充电返回处理开始...——startChargeBack");
		// 实现开始完成业务逻辑
		// 状态 0：失败，1：成功
		short status = byteBuffer.getShort();
		// 错误码
		int errCode = byteBuffer.getInt();
		// 电桩编号
		String electricPieNumber = ByteUtil.getString(byteBuffer);
		// 电桩枪口编号
		int tipId = byteBuffer.getInt();

		if (status == 1) {
			WanmaConstants.changeState.replace(electricPieNumber+"|"+tipId, WanmaConstants.ELECTRICPILEHEAD_BATTERY);
		}else{
			
			WanmaConstants.changeState.replace(electricPieNumber+"|"+tipId, errCode);
			log.debug("充电失败——startChargeBack。electricPieNumber：" + electricPieNumber 
					+ "-tipId:" + tipId);
		}

		log.debug("开始充电返回处理结束——startChargeBack。");
	}
	
	/**
	 * 开始充电事件
	 * 
	 * @param byteBuffer
	 *            返回信息
	 * @return
	 */
	public static void beginChargeEvent(ByteBuffer byteBuffer) {
		log.debug("开始充电事件开始...——beginChargeEvent");
		//用户手机
		String userPhone = ByteUtil.getString(byteBuffer);
		//用户id
		int userId = byteBuffer.getInt();
		// 极光推送业务处理对象
		AppJpushService jpushService = (AppJpushService) SpringContextHolder
				.getSpringContext().getBean("jpushService");
		TblJpush jpush = jpushService.getByuserInfo(userId);
		String msg = "尊敬的用户：本次充电已经开始，感谢您的使用！";
		JPushUtil.point2point("开始充电",msg,
						jpush.getJpushRegistrationid(),
						jpush.getJpushDevicetype(), "3");
		
		log.debug("开始充电事件结束...——beginChargeEvent");
	}

	/**
	 * 停止充电
	 * 
	 * @param bussinessId
	 *            业务ID
	 * @param electricPieNumber
	 *            电桩编号
	 * @param tipId
	 *            电桩枪口编号
	 * @return
	 */
	public static void stopCharge(String bussinessId, String electricPieNumber,
			int tipId, int userId) {
		log.debug("停止充电开始...——stopCharge");
		DynamicByteBuffer byteBuffer = DynamicByteBuffer.allocate();

		// 电桩编号
		byteBuffer.putString(electricPieNumber);
		// 电桩枪口编号
		byteBuffer.putInt(tipId);
		//用户id
		byteBuffer.putInt(userId);
		
		byte[] bb = byteBuffer.getBytes();
		// 以下注释有用，因测试需要暂时注释
		ChannelFuture cf = NetConnetionManager.sendMessage(electricPieNumber,ORDER_TYPE_10304, bb);
		if(null == cf){
			WanmaConstants.changeState.put(electricPieNumber+"|"+tipId, 6002);
		}
		log.debug("停止充电结束——stopCharge。");
	}

	/**
	 * 停止充电(第三方)
	 * 
	 * @param bussinessId
	 *            业务ID
	 * @param electricPieNumber
	 *            电桩编号
	 * @param tipId
	 *            电桩枪口编号
	 * @return
	 */
	public static void stopChargeTfi(String bussinessId, String electricPieNumber,
			int tipId, int userId,String userLog, int org) {
		log.debug("停止充电开始...——stopCharge");
		DynamicByteBuffer byteBuffer = DynamicByteBuffer.allocate();

		// 电桩编号
		byteBuffer.putString(electricPieNumber);
		// 电桩枪口编号
		byteBuffer.putInt(tipId);
		//用户id
		byteBuffer.putInt(userId);
		//用户标识
		byteBuffer.putString(userLog);
		//组织编号
		byteBuffer.putInt(org);
		
		byte[] bb = byteBuffer.getBytes();
		// 以下注释有用，因测试需要暂时注释
		ChannelFuture cf = NetConnetionManager.sendMessage(electricPieNumber,ORDER_TYPE_10304, bb);
		if(null == cf){
			WanmaConstants.changeState.put(electricPieNumber+"|"+tipId, 6002);
		}
		log.debug("停止充电结束——stopCharge。");
	}

	/**
	 * 停止充电充电返回处理
	 * 
	 * @param byteBuffer
	 *            返回信息
	 * @return
	 */
	public static void stopChargeBack(ByteBuffer byteBuffer) {
		log.debug("停止充电返回开始...——stopChargeBack");
		// 状态 0：失败，1：成功
		short status = byteBuffer.getShort();
		// 失败码，成功时为0，失败时为具体码
		int errCode = byteBuffer.getInt();
		// 电桩编号
		String electricPieNumber = ByteUtil.getString(byteBuffer);
		// 电桩枪口编号
		int tipId = byteBuffer.getInt();
		if (status == 1) {
			WanmaConstants.changeState.put(electricPieNumber+"|"+tipId, WanmaConstants.ELECTRICPILEHEAD_FREE);
		}else{
			WanmaConstants.changeState.put(electricPieNumber+"|"+tipId, errCode);
		}

		/****** 此处代码可能没用 ******/
		log.debug("停止充电充电返回结束——stopChargeBack。");
	}

	

	/**
	 * 结束充电事件
	 * 
	 * @param byteBuffer
	 *            返回信息
	 * @return
	 */
	public static void endChargeEvent(ByteBuffer byteBuffer) {
		log.debug("结束充电事件开始...——endChargeEvent");
		// 充电时长，单位分钟
		int time = byteBuffer.getInt();
		// 状态 0：异常停止，1：正常停止
		short status = byteBuffer.getShort();
		//异常停止状态码,如果是正常停止，则为0，异常停止为具体的异常码
		int stateCode = byteBuffer.getInt();
		//用户主键
		int userId = byteBuffer.getInt();
		
		if (stateCode != 0) {
			log.error("充电异常结束，异常码为：" + stateCode);
		}
		
		String timeStr = DateUtil.min2TimeString(time);
		// 极光推送业务处理对象
		AppJpushService jpushService = (AppJpushService) SpringContextHolder
				.getSpringContext().getBean("jpushService");
		TblJpush jpush = jpushService.getByuserInfo(userId);
		String msg = "";
		if(status == 1){
			msg = "尊敬的用户：本次充电已经结束，总充电时长为：" + timeStr + "，感谢您的使用";
		}else{
			msg = "尊敬的用户：由于设备原因，本次充电异常结束，总充电时长为：" + timeStr +"，感谢您的使用";
		}
		
		JPushUtil.point2point("充电结束",msg,
						jpush.getJpushRegistrationid(),
						jpush.getJpushDevicetype(), "1");
		
		log.debug("结束充电事件结束...——endChargeEvent");
	}

	/**
	 * 下发费率
	 * @param paramStrs 电桩编号,电桩编号:费率id 
	 */
	public static void sendRateInfo(String paramStrs){
		log.error("下发费率开始...");
		log.error("下发内容：" + paramStrs);
		
		DynamicByteBuffer byteBuffer = DynamicByteBuffer.allocate();

		String[] strs = paramStrs.split(":");
		// 电桩编号字符串
		byteBuffer.putString(strs[0]);
		// 费率id
		byteBuffer.putInt(Integer.parseInt(strs[1]));
		
		String eNo = "";
		if(paramStrs.indexOf(",") <= 0){
			eNo = strs[0];
		}else{
			eNo = paramStrs.split(",")[0];
		}
		NetConnetionManager.sendMessage(eNo,ORDER_TYPE_10306, byteBuffer.getBytes());
		log.error("下发费率结束...");
	}
	
	/**
	 * 下发二维码
	 * @param epCode 桩体编号
	 * @param ephNum 枪口序号
	 * @param urlContent 二维码url内容
	 */
	public static void sendTwoDiCode(String epCode,String ephNum,String urlContent){
		log.debug("下发二维码开始...");
		DynamicByteBuffer byteBuffer = DynamicByteBuffer.allocate();
		byteBuffer.putString(epCode);
		byteBuffer.putString(ephNum);
		byteBuffer.putString(urlContent);
		
		NetConnetionManager.sendMessage(epCode,ORDER_TYPE_10307, byteBuffer.getBytes());
		
		log.debug("下发二维码结束...");
	}
	
	/**
	 * 开关led灯
	 * @param epCode 桩体编码
	 * @param type 1开2关
	 * @param remainTime 持续闪烁时间，分钟为单位
	 */
	public static void ledControl(String epCode,int type,int remainTime,int uid,String lat,String lng){
		log.debug("led灯控制，开关类型（1开2关）：" + type);
		DynamicByteBuffer bb = DynamicByteBuffer.allocate();
		bb.putString(epCode);
		bb.putShort((short)type);
		bb.putInt(remainTime);
		bb.putInt(uid);
		bb.putFloat(Float.parseFloat(lng));
		bb.putFloat(Float.parseFloat(lat));
		
		NetConnetionManager.sendMessage(epCode, ORDER_TYPE_10310, bb.getBytes());
		log.debug("led灯控制结束...");
	}
	
	/**
	 * 降地锁
	 * @param epCode 桩体编号
	 * @param headNum 枪口号，不是枪口id
	 * @param parkNum 枪口对应的车位号
	 */
	public static void downParkLock(String epCode,int headNum,String parkNum, int uid,String lat,String lng){
		log.debug("开始降地锁...");
		DynamicByteBuffer bb = DynamicByteBuffer.allocate();
		bb.putString(epCode);
		bb.putShort((short)headNum);
		bb.putString(parkNum);
		bb.putInt(uid);
		bb.putFloat(Float.parseFloat(lng));
		bb.putFloat(Float.parseFloat(lat));
		
		NetConnetionManager.sendMessage(epCode, ORDER_TYPE_10309, bb.getBytes());
		log.debug("降地锁命令发送完成...");
	}
	
	/**
	 * 更新电桩绑定的gate
	 * @param byteBuffer
	 */
	public static void updateEpGate(ByteBuffer byteBuffer){
		log.debug("更新电桩gate...");
		String epCode = ByteUtil.getString(byteBuffer);
		int gateId = byteBuffer.getInt();
		log.debug("epCode:" + epCode + ",oldGateId:" + WanmaConstants.ELECTRICPILE_TO_GATE_INFO.get(epCode) + "gateId:" + gateId);
		WanmaConstants.ELECTRICPILE_TO_GATE_INFO.put(epCode, gateId);
		
		log.debug("更新gate完成...");
	}
	
	/**
	 * 桩程序升级
	 * @param pId 产品id
	 * @param num bom个数
	 * @param versionList bom列表
	 * 			格式：硬件编号;硬件版本号;固件编号;固件版本号;强制更新标志
	 */
	public static void updateEpVision(int pId,int num,String[] versionList){
		log.debug("升级电桩程序，对应的产品id为：" + pId);
		
		DynamicByteBuffer bb = DynamicByteBuffer.allocate();
		bb.putInt(pId);
		bb.putInt(num);
		for(String str : versionList){
			String[] vers = str.split(";");
			for(String ver : vers){
				bb.putString(ver);
			}
		}
		NetConnetionManager.sendMessage(ORDER_TYPE_10312, bb.getBytes());
		
		log.debug("升级电桩程序完成");
		
	}
	
	/**
	 * 下发集中器id
	 * @param cId
	 */
	public static void sendConcentrator(int cId){
		log.debug("下发集中器id：" + cId);
		DynamicByteBuffer bb = DynamicByteBuffer.allocate();
		bb.putInt(cId);
		NetConnetionManager.sendMessage(ORDER_TYPE_10313, bb.getBytes());
		
		log.debug("下发集中器完成");
	}
	
	/**
	 * 消费记录数据
	 * 
	 * @param byteBuffer
	 *            返回信息
	 * @return
	 */
	public static void consumeRecord(ByteBuffer byteBuffer) {
		log.debug("消费记录数据开始...——consumeRecord");
		// 用户账户
		String chOrUserphone = ByteUtil.getString(byteBuffer);
		// 用户id
		int userId = byteBuffer.getInt();
		// 总费用 电费+服务费
		BigDecimal totalFee = (new BigDecimal(byteBuffer.getDouble())).setScale(2,BigDecimal.ROUND_HALF_UP);
		// 充电时间（分钟计）
		int time = byteBuffer.getInt();
		// 状态 0：异常停止，1：正常停止
		short status = byteBuffer.getShort();
		//异常停止状态码,如果是正常停止，则为0，异常停止为具体的异常码
		int stateCode = byteBuffer.getInt();
		//代金券抵扣金额
		double coupon = byteBuffer.getDouble();
		
		String timeStr = DateUtil.min2TimeString(time);

		// 极光推送业务处理对象
		AppJpushService jpushService = (AppJpushService) SpringContextHolder.getSpringContext().getBean("jpushService");
		if (stateCode != 0) {
			log.error("充电异常结束，异常码为：" + stateCode);
		}
		
		// 获取用户推送信息
		TblJpush jpush = jpushService.getByuserInfo(userId);
		String msg = "尊敬的用户：";
		if(status == 0){
			msg += "由于设备原因";
		}
		msg += "本次充电已经结束，总充电时长为：" + timeStr + "，共计消费" + totalFee + "元，";
		if(coupon > 0){
			msg += "其中代金券抵扣" + coupon + "元，";
		}
		msg += "请尽快进行费用结算，感谢您的使用，谢谢";
		
		// 推送消息给用户，查看消费总额,最后一个参数： 消息类型 1 充电结束推送 2 余额不足推送
		JPushUtil.point2point("充电结束", msg,
				 jpush.getJpushRegistrationid(),jpush.getJpushDevicetype(), "4");
		 
		log.debug("消费记录数据结束...——consumeRecord");
	}

	/**
	 * 充电余额告警处理
	 * 
	 * @param byteBuffer
	 *            返回信息
	 * @return
	 */
	public static void WarningBack(ByteBuffer byteBuffer) {
		log.debug("充电余额告警处理开始——WarningBack...");
		// 用户账户
		String chOrUserphone = ByteUtil.getString(byteBuffer);
		// 电桩编号
		String elpiElectricpilecode = ByteUtil.getString(byteBuffer);
		// 用户业务处理对象
		AppUserinfoService userinfoService = (AppUserinfoService) SpringContextHolder
				.getSpringContext().getBean("userinfoService");

		// 极光推送业务处理对象
		AppJpushService jpushService = (AppJpushService) SpringContextHolder
				.getSpringContext().getBean("jpushService");

		// 根据用户手机号获取用户信息
		TblUserinfo userInfo = userinfoService.getByPhone(chOrUserphone);

		// 获取用户推送信息
		TblJpush jpush = jpushService.getByuserInfo(userInfo.getPkUserinfo());
		// 推送消息给用户，查看消费总额 ，最后一个参数： 消息类型 1 充电结束推送 2 余额不足推送
		String msg = "尊敬的用户：由于您账户余额不足，可能会影响您的有效充电时间，请尽快充值。";//，当前余额：" + rateinformation.getRainWarnmoney() + "元。";
		JPushUtil.point2point("余额不足",msg,jpush.getJpushRegistrationid(),jpush.getJpushDevicetype(), "2");

		log.debug("充电余额告警处理结束——WarningBack。");
	}

	/**
	 * 私有电桩运营时间控制
	 * 
	 * @param bussinessId
	 *            业务ID
	 * @param electricPieNumber
	 *            电桩编号
	 * @param buyTime
	 *            预约买断时间
	 * @param bookStartTime
	 *            预约开始时间
	 * @param renewalFlag
	 *            续约标识（0：第一次预约，1：续约）
	 * @param accountId
	 *            用户账号
	 * @param remainAmount
	 *            用户余额
	 * @param tipId
	 *            电桩枪口
	 * @return
	 */
	public static void managePersonalPie(String bussinessId,
			String electricPieNumber, Date buyTime, Date bookStartTime,
			int renewalFlag, String accountId, int remainAmount, int tipId) {
		log.debug("私有电桩运营时间控制开始...");
		DynamicByteBuffer byteBuffer = DynamicByteBuffer.allocate();

		// 业务ID
		byteBuffer.putString(bussinessId);
		// 电桩编号
		byteBuffer.putString(electricPieNumber);
		// 预约买断时间
		byteBuffer.putString(DateUtil.toString(buyTime,
				DateUtil.DATE_FORMAT_FULL));
		// 预约开始时间
		byteBuffer.putString(DateUtil.toString(bookStartTime,
				DateUtil.DATE_FORMAT_FULL));
		// 续约标识
		byteBuffer.putInt(renewalFlag);
		// 电桩编号
		byteBuffer.putString(accountId);
		// 用户余额
		byteBuffer.putInt(remainAmount);
		// 电桩枪口
		byteBuffer.putInt(tipId);

		byte[] bb = byteBuffer.getBytes();

		NetConnetionManager.sendMessage(electricPieNumber,ORDER_TYPE_10305, bb);

		log.debug("私有电桩运营时间控制结束。");
	}

	/**
	 * 私有电桩运营时间控制返回处理
	 * 
	 * @param byteBuffer
	 *            返回信息
	 * @return
	 */
	public static void managePersonalPieBack(ByteBuffer byteBuffer) {
		log.debug("停止私有电桩运营时间控制开始...");
		BackDataObject backDataObject = ElectricPieUtil
				.hanleBackMsg(byteBuffer);
		// TODO 实现私有电桩运营时间控制完成业务逻辑

		log.debug("停止私有电桩运营时间控制结束。");
	}

	/**
	 * 处理服务器返回数据
	 * 
	 * @param byteBuffer
	 *            返回信息
	 * @return BackDataObject 返回信息对象
	 */
	public static BackDataObject hanleBackMsg(ByteBuffer byteBuffer) {
		BackDataObject backDataObject = new BackDataObject();
		//预约id
		String pBussinessId = ByteUtil.getString(byteBuffer);
		String electricPieNumber = ByteUtil.getString(byteBuffer);
		short status = byteBuffer.getShort();
		String msg = ByteUtil.getString(byteBuffer);
		String userPhone = ByteUtil.getString(byteBuffer);
		int userId = byteBuffer.getInt();
		//预约续约标识 1续约 0预约
		int bespType = byteBuffer.getShort();
		//预约续约结束时间
		long endtime = byteBuffer.getLong();
		//当次预约/续约需要扣的钱
		String spendMoney = ByteUtil.getString(byteBuffer);
		
		backDataObject.setBussinessId(pBussinessId);
		backDataObject.setElectricPieNumber(electricPieNumber);
		backDataObject.setStatus(status);
		backDataObject.setMsg(msg);
		backDataObject.setUserPhone(userPhone);
		backDataObject.setUserId(userId);
		backDataObject.setBespType(bespType);
		backDataObject.setBespokeEndTime(new Date(endtime*1000));
		backDataObject.setSpendMoney(spendMoney);
		return backDataObject;

	}

	/**
	 * 处理服务器返回数据
	 * 
	 * @param byteBuffer
	 *            返回信息
	 * @return BackDataObject 返回信息对象
	 */
	public static BackDataObject hanleCancelBackMsg(ByteBuffer byteBuffer) {
		BackDataObject backDataObject = new BackDataObject();
		//
		String pBussinessId = ByteUtil.getString(byteBuffer);
		//String electricPieNumber = ByteUtil.getString(byteBuffer);
		short status = byteBuffer.getShort();
		//String msg = ByteUtil.getString(byteBuffer);
		//long bespokeEndTime = byteBuffer.getLong();
		String userPhone = ByteUtil.getString(byteBuffer);
		
		backDataObject.setBussinessId(pBussinessId);
		//backDataObject.setElectricPieNumber(electricPieNumber);
		backDataObject.setStatus(status);
		//backDataObject.setMsg(msg);
		//backDataObject.setBespokeEndTime(DateUtil.toDate(bespokeEndTime));
		backDataObject.setUserPhone(userPhone);
		
		return backDataObject;

	}

	public static void main(String[] args) {
		/*
		 * ElectricPieUtil.bookElectricPie("9999999999", "22222", new Date(),
		 * new Date(), 0, "333333", 1000, 1,1);
		 */

	}
	/**
	 * 离线充电次数下发
	 * @param paramStr 公司标示:最大充电次数
	 * @return
	 */
	public static void sendCompanyNum(String paramStrs) {
		log.debug("下发离线充电次数开始...");
		log.debug("下发内容：" + paramStrs);
		
		DynamicByteBuffer byteBuffer = DynamicByteBuffer.allocate();

		String[] strs = paramStrs.split(":");
		// 公司标示
		byteBuffer.putInt(Integer.parseInt(strs[0]));
		// 最大充电次数
		byteBuffer.putInt(Integer.parseInt(strs[1]));
		
	
		NetConnetionManager.sendMessage(ORDER_TYPE_10314, byteBuffer.getBytes());
		log.debug("下发离线充电次数结束...");
		
	}

	public static void sendTimeCharge(String paramStrs) {
		log.debug("下发定时充电次数开始...");
		log.debug("下发内容：" + paramStrs);
		
		DynamicByteBuffer byteBuffer = DynamicByteBuffer.allocate();

		String[] strs = paramStrs.split(":");
		// 公司标示
		byteBuffer.putString(strs[0]);
		// 最大充电次数
		byteBuffer.putString(strs[1]+strs[2]);
		byteBuffer.putInt(Integer.parseInt(strs[3]));
	
		NetConnetionManager.sendMessage(ORDER_TYPE_10315, byteBuffer.getBytes());
		log.debug("下发定时充电次数结束...");
		
	}
	public static void sendChargeSet(String paramStrs) {
		log.info("下发充电设置开始...");
		log.info("下发充电设置内容：" + paramStrs);
		DynamicByteBuffer byteBuffer = DynamicByteBuffer.allocate();
		String[] strs = paramStrs.split(":");
		// 桩体编码
		byteBuffer.putString(strs[0]);
		//通讯码
		short ORDER_TYPE= Short.parseShort(strs[1]);
		NetConnetionManager.sendMessage(ORDER_TYPE, byteBuffer.getBytes());
		log.info("下发定充电设置结束...");
		
	}


}
