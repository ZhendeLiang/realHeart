package com.liangzd.realHeart.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TbAddress;

@Repository
public interface TbAddressDao extends JpaRepository<TbAddress, Integer>{
	@Query(nativeQuery=true,value="SELECT * FROM tr_user_address tua left JOIN tb_address ta "
			+ "on ta.id = tua.address_id WHERE tua.user_id = :uid")
	public Optional<TbAddress> findByUid(@Param("uid")Integer uid);
}
