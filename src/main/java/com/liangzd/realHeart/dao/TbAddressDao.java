package com.liangzd.realHeart.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TbAddress;

/**
 * 
 * @Description: 当前用户详细地址对象的持久化操作类,tb_address
 * @author liangzd
 * @date 2018年6月16日 下午8:36:27
 */
@Repository
public interface TbAddressDao extends JpaRepository<TbAddress, Integer>{
	/**
	 * 
	 * @Description: 根据用户的Uid查找当前用户的所在地址
	 * @param 
	 * @return Optional<TbAddress>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:27:37
	 */
	@Query(nativeQuery=true,value="SELECT * FROM tr_user_address tua left JOIN tb_address ta "
			+ "on ta.id = tua.address_id WHERE tua.user_id = :uid")
	public Optional<TbAddress> findByUid(@Param("uid")Integer uid);
}
