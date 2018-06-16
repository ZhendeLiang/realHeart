package com.liangzd.realHeart.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 
 * @Description: tb_address表的持久化对象,详细地址表
 * @author liangzd
 * @date 2018年6月16日 下午9:12:26
 */
@Entity(name="tb_address")
public class TbAddress implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;//主键
	@Column(length=20)
	private String province;//省,长度20
	@Column(length=20)
	private String city;//市,长度20
	@Column(length=20)
	private String town;//区,长度20
	@Column(length=20)
	private String details;//详细地址,长度20
	@Transient
	private Integer uid;//非持久化对象,用户uid
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
}
