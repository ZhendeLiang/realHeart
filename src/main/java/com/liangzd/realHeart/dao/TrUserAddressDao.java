package com.liangzd.realHeart.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TrUserAddress;
import com.liangzd.realHeart.entity.TrUserViprank;

@Repository
public interface TrUserAddressDao extends JpaRepository<TrUserAddress, Integer>{
	public List<TrUserAddress> findByUserId(Integer userId);
}
