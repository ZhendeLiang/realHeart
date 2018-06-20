package com.liangzd.realHeart.service;

import java.util.List;
import java.util.Optional;

import com.liangzd.realHeart.entity.TbAddress;
import com.liangzd.realHeart.entity.TbNationalProvinceCityTown;

/**
 * 
 * @Description: 处理关于用户地址的业务处理接口
 * @author liangzd
 * @date 2018年6月20日 下午2:21:54
 */
public interface AddressService {
	/**
	 * 
	 * @Description:  根据父级的code值查找对应的省市区父级持久化地址
	 * @param 
	 * @return List<TbNationalProvinceCityTown>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:22:15
	 */
	public List<TbNationalProvinceCityTown> findAllByParentCode(String parentCode);
	
	/**
	 * 
	 * @Description: 根据用户uid查找用户的详细地址
	 * @param 
	 * @return Optional<TbAddress>
	 * @author liangzd
	 * @date 2018年6月20日 下午2:22:59
	 */
	public Optional<TbAddress> findByUid(Integer uid);
}
