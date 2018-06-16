package com.liangzd.realHeart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.liangzd.realHeart.entity.TbNationalProvinceCityTown;

/**
 * 
 * @Description: 省市区对象的持久化操作类,tb_national_province_city_town
 * @author liangzd
 * @date 2018年6月16日 下午8:38:34
 */
@Repository
public interface TbNationalProvinceCityTownDao extends JpaRepository<TbNationalProvinceCityTown, Integer>{
	/**
	 * 
	 * @Description: 根据parentCode查找对相应的国下的省,省下的市,市下的区
	 * @param 
	 * @return List<TbNationalProvinceCityTown>
	 * @author liangzd
	 * @date 2018年6月16日 下午8:28:21
	 */
	public List<TbNationalProvinceCityTown> findByParentCode(String parentCode);
}
