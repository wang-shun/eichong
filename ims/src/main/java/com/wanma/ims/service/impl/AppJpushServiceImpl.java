package com.wanma.ims.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanma.ims.common.domain.JpushDO;
import com.wanma.ims.mapper.AppJpushMapper;
import com.wanma.ims.service.AppJpushService;

@Service("appJpushService")
public class AppJpushServiceImpl implements AppJpushService{
	@Autowired AppJpushMapper appJpushMapper;
	
	
	public JpushDO getByuserInfo(Long jpushUserinfo) {
		return appJpushMapper.getByuserInfo(jpushUserinfo);
	}

}