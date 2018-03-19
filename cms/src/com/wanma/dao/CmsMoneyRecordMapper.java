package com.wanma.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CmsMoneyRecordMapper {
/**
 * 查出充值总额
 * @return
 */
public HashMap<String,Object> getTotalRecharge();
/**
 * 查出消费总额
 * @return
 */
public HashMap<String,Object> getTotalPurchase();

/**
 * 查出总余额
 * @return
 */
public HashMap<String,Object> getTotalAccount();

/**
 * 查出用户消费充值情况
 * @param parems
 * @return
 */
public List<HashMap<String,Object>> getUserMoneyRecordList(Map<String,Object> params);

public long getUserMoneyRecordCount(Map<String,Object> params);


/**
 * 查出单个用户充值总额
 * @return
 */
public HashMap<String,Object> getUserTotalRecharge(Map<String,Object> params);
/**
 * 查出单个用户消费总额
 * @return
 */
public HashMap<String,Object> getUserTotalPurchase(Map<String,Object> params);

/**
 * 查出单个用户消费充值详情
 * @param parems
 * @return
 */

public List<HashMap<String,Object>> getMoneyRecordListByUserId(Map<String,Object> params);

public long getMoneyRecordCountByUserId(Map<String,Object> params);


}