package com.wanma.ims.mapper;

import java.util.List;

import com.wanma.ims.common.domain.LevelDO;

/**
 * 等级Mapper
 * 
 * @version V1.0
 * @author zcy
 * @date 2017年5月26日
 */
public interface LevelMapper {
	
	/**
	 * 根据ID查询等级
	 * @param cpyId
	 */
	public List<LevelDO> selectLevelListByIds(List<Long> list);
	
	/**
	 * 根据渠道ID查询等级
	 * @param cpyId
	 */
	public List<LevelDO> selectLevelListByCpyId(Long cpyId);
	/**
	 * 获取等级数量
	 * @param level
	 * @return
	 */
	public long getLevelCount(LevelDO level);
	/**
	 * 获取等级列表
	 * @param level
	 * @return
	 */
	public List<LevelDO> getLevelList(LevelDO level);
	/**
	 * 删除等级
	 * @param level
	 * @return
	 */
	public int removeLevel(LevelDO level);
	/**
	 * 设为默认
	 * @param level
	 * @return
	 */
	public int setLevelDefault(LevelDO level);
	/**
	 * 判断序列是否存在
	 * @author mb
	 * @param 
	 * @return
	 */
	public int checkLevelCode(LevelDO level);
	/**
	 * 判断等级名称是否存在
	 * @author mb
	 * @param 
	 * @return
	 */
	public int checkLevelName(LevelDO level);
	/**
	 * 新增等级
	 * @param level
	 * @return
	 */
	public int addLevel(LevelDO level);
	/**
	 * 根据公司id 查询等级
	 * @author mb
	 * @param 
	 * @return
	 */
	public List<LevelDO> getLevelByCpyId(Long cpyId);
	/**
	 * 如果是设为默认等级 则先修改之前的默认等级为普通
	 * @author mb
	 * @param 
	 * @return
	 */
	public void setLevelNormal(LevelDO level);
	/**
	 * 根据公司id 查询等级
	 * @author zcy
	 * @param 
	 * @return
	 */
	public LevelDO selectIsDefaultLevel(Long cpyId);
	

}
