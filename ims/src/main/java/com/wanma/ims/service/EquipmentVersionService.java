package com.wanma.ims.service;

import com.wanma.ims.common.domain.EquipmentVersionDO;

/**
 * 设备版本信息
 */
public interface EquipmentVersionService {

	EquipmentVersionDO getBomById(String pkBomListId);

	void deleteByProductID(String evProductID);

	void insertEpVersion(EquipmentVersionDO epVersion);
	

}
