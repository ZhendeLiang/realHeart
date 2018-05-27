package com.liangzd.realHeart.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TrUserViprank;

@Repository
public interface TrUserViprankDao extends JpaRepository<TrUserViprank, Integer>{
	public Optional<TrUserViprank> findByUserId(int userId);
}
