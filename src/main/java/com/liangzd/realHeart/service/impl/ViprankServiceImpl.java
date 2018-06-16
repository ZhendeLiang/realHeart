package com.liangzd.realHeart.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liangzd.realHeart.dao.TbAddressDao;
import com.liangzd.realHeart.dao.TbViprankDao;
import com.liangzd.realHeart.entity.TbViprank;
import com.liangzd.realHeart.service.ViprankService;

@Service
public class ViprankServiceImpl implements ViprankService {
	@Autowired
	private TbViprankDao tbViprankDao;
	
	public TbViprankDao getTbViprankDao() {
		return tbViprankDao;
	}

	public void setTbViprankDao(TbViprankDao tbViprankDao) {
		this.tbViprankDao = tbViprankDao;
	}

	@Override
	public TbViprank findById(Integer id) {
		return tbViprankDao.findById(id).get();
	}
}
