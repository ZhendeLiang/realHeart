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

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.JoinColumn;

@Entity(name="tb_user_img")
public class TbUserImg implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;
	@Column(length = 11)
	private Integer uid;
	@Column(unique = true,length = 1000)
	private String imgUUID;
	@Column(length = 2)
	private String imgType;
	@Column
	private Date createTime;
	@Column
	private Integer increasement;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getImgUUID() {
		return imgUUID;
	}
	public void setImgUUID(String imgUUID) {
		this.imgUUID = imgUUID;
	}
	public String getImgType() {
		return imgType;
	}
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getIncreasement() {
		return increasement;
	}
	public void setIncreasement(Integer increasement) {
		this.increasement = increasement;
	}
}
