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

/**
 * 
 * @Description: 处理关于用户地址的业务处理接口实现类
 * @author liangzd
 * @date 2018年6月20日 下午2:21:54
 */
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
	
	/**
	 * 
	 * @Description:  根据父级的code值查找对应的省市区父级持久化地址
	 * @param 
	 * @return List<TbNationalProvinceCityTown>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:22:15
	 */
	@Override
	public List<TbNationalProvinceCityTown> findAllByParentCode(String parentCode) {
		return tbNationalProvinceCityTownDao.findByParentCode(parentCode);
	}

	/**
	 * 
	 * @Description: 根据用户uid查找用户的详细地址
	 * @param 
	 * @return Optional<TbAddress>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:22:59
	 */
	@Override
	public Optional<TbAddress> findByUid(Integer uid) {
		return tbAddressDao.findByUid(uid);
	}

}
