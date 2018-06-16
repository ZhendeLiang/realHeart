package com.liangzd.realHeart.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @Description: tr_user_viprank表的持久化类,用户-会员详细信息关系表
 * @author liangzd
 * @date 2018年6月16日 下午9:30:16
 */
@Entity(name="tr_user_viprank")
public class TrUserViprank implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;//主键
	@Column(unique = true,length = 11)
	private Integer userId;//用户uid,长度11
	@Column(length = 11)
	private Integer viprankId;//会员详细信息id,长度11
	@Column(columnDefinition="timestamp")
	@JSONField(format="yyyy-MM-dd")
	private Date openTime;//创建日期,格式化JSON数据日期类型
	@Column(length = 5)
	private Integer surplusDay;//剩余天数,长度5
	
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
