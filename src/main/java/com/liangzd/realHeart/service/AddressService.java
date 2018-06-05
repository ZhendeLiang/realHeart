package com.liangzd.realHeart.service;

import java.util.List;
import java.util.Optional;

import com.liangzd.realHeart.entity.TbAddress;
import com.liangzd.realHeart.entity.TbNationalProvinceCityTown;

public interface AddressService {
	public List<TbNationalProvinceCityTown> findAllByParentCode(String parentCode);
	public Optional<TbAddress> findByUid(Integer uid);
}
