package com.liangzd.realHeart.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.JoinColumn;

@Entity(name="tr_user_viprank")
public class TrUserViprank implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;
	@Column(unique = true,length = 11)
	private Integer userId;
	@Column(length = 11)
	private Integer viprankId;
	@Column(columnDefinition="timestamp")
	@JSONField(format="yyyy-MM-dd")
	private Date openTime;//创建日期
	@Column(length = 5)
	private Integer surplusDay;
	
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
	public Integer getViprankId() {
		return viprankId;
	}
	public void setViprankId(Integer viprankId) {
		this.viprankId = viprankId;
	}
	public Date getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	public Integer getSurplusDay() {
		return surplusDay;
	}
	public void setSurplusDay(Integer surplusDay) {
		this.surplusDay = surplusDay;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
