package com.echong.service;

import com.echong.model.ElectricLabel;
import com.echong.model.Result;

/**
 * Created by zangyaoyi on 2017/1/5.
 */
public interface OperateLabelService {
    Result startOperate(ElectricLabel electricLabel);

    Result stopOperate(ElectricLabel electricLabel);

    void queryOffLinePile(Integer companyId,Integer stationID);
}
