package com.liangzd.realHeart.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 
 * @Description: tb_national_province_city_town表的持久化对象,省市区表
 * @author liangzd
 * @date 2018年6月16日 下午9:13:49
 */
@Entity(name="tb_national_province_city_town")
public class TbNationalProvinceCityTown implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(unique = true,length=6)
	private String code;//主键,唯一值,长度6
	@Column(length=15)
	private String name;//省/市/区的名字
	@Column(length=6)
	private String parentCode;//省/市/区的父级主键,省级的父级为0
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
}
