package com.liangzd.realHeart.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="tb_user_relation")
public class TbUserRelation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;
	private Integer firstUid;
	private Integer secondUid;
	@Column(length=2)
	private String firstUserRelation;
	@Column(length=2)
	private String secondUserRelation;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFirstUid() {
		return firstUid;
	}
	public void setFirstUid(Integer firstUid) {
		this.firstUid = firstUid;
	}
	public Integer getSecondUid() {
		return secondUid;
	}
	public void setSecondUid(Integer secondUid) {
		this.secondUid = secondUid;
	}
	public String getFirstUserRelation() {
		return firstUserRelation;
	}
	public void setFirstUserRelation(String firstUserRelation) {
		this.firstUserRelation = firstUserRelation;
	}
	public String getSecondUserRelation() {
		return secondUserRelation;
	}
	public void setSecondUserRelation(String secondUserRelation) {
		this.secondUserRelation = secondUserRelation;
	}
}
