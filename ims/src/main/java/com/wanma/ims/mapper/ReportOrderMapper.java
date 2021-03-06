package com.wanma.ims.mapper;

import com.wanma.ims.common.domain.ReportOrderDO;

import java.util.List;


public interface ReportOrderMapper {
	
	public Long countReportCpyOrder(ReportOrderDO orderReportDO);
	
	public List<ReportOrderDO> selectReportCpyOrder(ReportOrderDO orderReportDO);

    Long countOrderByPowerStation(ReportOrderDO searchModel);

	List<ReportOrderDO> selectOrderByPowerStation(ReportOrderDO searchModel);

	Long countOrderByCpyAndPowerStation(ReportOrderDO searchModel);

	List<ReportOrderDO> selectOrderByCpyAndPowerStation(ReportOrderDO searchModel);
}
