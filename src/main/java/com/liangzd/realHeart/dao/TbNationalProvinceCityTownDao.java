package com.liangzd.realHeart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TbNationalProvinceCityTown;

@Repository
public interface TbNationalProvinceCityTownDao extends JpaRepository<TbNationalProvinceCityTown, Integer>{
	public List<TbNationalProvinceCityTown> findByParentCode(String parentCode);
}
