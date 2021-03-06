package com.wanma.ims.service;

import java.util.List;
import java.util.Map;

import com.wanma.ims.common.domain.CompanyDO;
import com.wanma.ims.common.domain.UserDO;
import com.wanma.ims.common.dto.BaseResultDTO;
import com.wanma.ims.common.dto.BatchResultDTO;
import com.wanma.ims.common.dto.ResultDTO;

/**
 * <p>公司管理<p>
 * @author zhangchunyan
 * @date 2017-6-13
 */
public interface CompanyService {
   
	/**
	 * <p>公司-统计<p>
	 * @param companyDO
	 * @author zhangchunyan
	 * @date 2017-6-13
	 */
	public Long countCompanyList(CompanyDO companyDO);
	
	/**
	 * <p>公司-查询<p>
	 * @param companyDO
	 * @author zhangchunyan
	 * @date 2017-6-13
	 */
	public List<CompanyDO> getCompanyList(CompanyDO companyDO);
	
	/**
	 * <p>公司-新增<p>
	 * @param companyDO
	 * @author zhangchunyan
	 * @date 2017-6-13
	 */
	public BaseResultDTO addCompany(CompanyDO companyDO,UserDO loginUser) throws Exception;
	
	/**
	 * <p>公司-编辑<p>
	 * @param companyDO
	 * @author zhangchunyan
	 * @date 2017-6-13
	 */
	public boolean modifyCompany(CompanyDO companyDO);
	
	/**
	 * <p>公司-禁用<p>
	 * @param companyDO
	 * @author zhangchunyan
	 * @date 2017-6-13
	 */
	public boolean disableCompany(CompanyDO companyDO);
	
	/**
	 * <p>根据id查询渠道(批量)<p>
	 * @param ids 主键ID,不能为空
	 * @author zhangchunyan
	 * @date 2017-6-13
	 */
	public List<CompanyDO> getCompanyListByIds(List<Long> ids);
	
	/**
	 * <p>根据id查询渠道<p>
	 * @param ids 主键ID,不能为空
	 * @author zhangchunyan
	 * @date 2017-6-13
	 */
	public CompanyDO getCompanyListById(Long cpyId);
	
	/**
	 * <p>根据管理员类型、公司ID查看自己权限范围内的公司列表<p>
	 * @param logUser 
	 * @param provinceCode 省编码
	 * @param cityCode 市编码
	 */
	public BatchResultDTO<CompanyDO> getCpyListByUserLevel(UserDO logUser,String provinceCode,String cityCode);

	/**
	 *<p>公司列表</p> 
	 * @param 
	 * @author zhangchunyan
	 * @date 2017-7-3
	 */
    public Map<Long,CompanyDO> getCompanyMap(CompanyDO companyDO);
    
    /**
     *<p>合作公司列表</p> 
     */
    public List<CompanyDO> getOperateCompanyList();
    
    /**
	 *<p>判断公司标识或者名称是否重复</p> 
	 * @param cpyNumber
	 * @author zhangchunyan
	 * @date 2017-7-3
	 */
    public boolean checkCompanyUnique(Map<String,String> param);
    
    /**
     * 投资公司管理的合作公司
     * @param cpyId
     */
    public List<CompanyDO> getCompanyByMaster(Long cpyId);
    
    /**
	 * <p>根据公司信息查公司信息<p>
	 * @param cpyNumber
	 * @author mb
	 * @date 2017-8-11
	 */
	public CompanyDO getCompanyByCpyInfo(CompanyDO companyDO);
	
	 /**
		 * <p>公司-公司付费<p>
		 * @param cpyNumber
		 * @author zhangchunyan
		 * @date 2017-8-11
		 */
	public ResultDTO<Map<String,Object>> getCpyPayRule(Integer cpyNumber);

	/**
	 * 根据公司名称获取公司列表
	 * @param companyDO
	 * @return
	 */
	public List<CompanyDO> getCompanyListByCpyName(CompanyDO companyDO);
}
