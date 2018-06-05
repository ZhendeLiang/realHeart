package com.liangzd.realHeart.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;

@Entity(name="tb_national_province_city_town")
public class TbNationalProvinceCityTown implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(unique = true,length=6)
	private String code;
	@Column(length=15)
	private String name;
	@Column(length=6)
	private String parentCode;
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
