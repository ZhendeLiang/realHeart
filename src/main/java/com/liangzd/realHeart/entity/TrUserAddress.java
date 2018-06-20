package com.liangzd.realHeart.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 
 * @Description: tr_user_address表的持久化类,用户与详细地址的关系表
 * @author liangzd
 * @date 2018年6月16日 下午9:28:59
 */
@Entity(name="tr_user_address")
public class TrUserAddress implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;//主键
	@Column(unique = true,length = 11)
	private Integer userId;//用户uid,长度11
	@Column(length = 11)
	private Integer addressId;//详细地址的id,长度11
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getAddressId() {
		return addressId;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	@Override
	public String toString() {
		return "TrUserAddress [id=" + id + ", userId=" + userId + ", addressId=" + addressId + "]";
	}
}
