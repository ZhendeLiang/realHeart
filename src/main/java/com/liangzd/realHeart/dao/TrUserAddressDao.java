package com.liangzd.realHeart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TrUserAddress;

/**
 * 
 * @Description: 用户与详细地址的关系对象的持久化操作类,tr_user_address
 * @author liangzd
 * @date 2018年6月16日 下午8:41:12
 */
@Repository
public interface TrUserAddressDao extends JpaRepository<TrUserAddress, Integer>{
	/**
	 * 
	 * @Description: 根据用户的uid查找所有的 用户与详细地址的关系对象
	 * @param 
	 * @return List<TrUserAddress>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:44:55
	 */
	public List<TrUserAddress> findByUserId(Integer userId);
}
