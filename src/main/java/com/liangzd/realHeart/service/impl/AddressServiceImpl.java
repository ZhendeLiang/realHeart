package com.liangzd.realHeart.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liangzd.realHeart.dao.TbAddressDao;
import com.liangzd.realHeart.dao.TbNationalProvinceCityTownDao;
import com.liangzd.realHeart.entity.TbAddress;
import com.liangzd.realHeart.entity.TbNationalProvinceCityTown;
import com.liangzd.realHeart.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	private TbAddressDao tbAddressDao;
	@Autowired
	private TbNationalProvinceCityTownDao tbNationalProvinceCityTownDao;
	
	public TbAddressDao getTbAddressDao() {
		return tbAddressDao;
	}

	public void setTbAddressDao(TbAddressDao tbAddressDao) {
		this.tbAddressDao = tbAddressDao;
	}

	public TbNationalProvinceCityTownDao getTbNationalProvinceCityTownDao() {
		return tbNationalProvinceCityTownDao;
	}

	public void setTbNationalProvinceCityTownDao(TbNationalProvinceCityTownDao tbNationalProvinceCityTownDao) {
		this.tbNationalProvinceCityTownDao = tbNationalProvinceCityTownDao;
	}
	
	@Override
	public List<TbNationalProvinceCityTown> findAllByParentCode(String parentCode) {
		return tbNationalProvinceCityTownDao.findByParentCode(parentCode);
	}

	@Override
	public Optional<TbAddress> findByUid(Integer uid) {
		return tbAddressDao.findByUid(uid);
	}

}
