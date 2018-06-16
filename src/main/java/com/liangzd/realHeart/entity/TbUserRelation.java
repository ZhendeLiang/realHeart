package com.liangzd.realHeart.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 
 * @Description: tb_user_relation表的持久化对象,用户与用户间关系表
 * @author liangzd
 * @date 2018年6月16日 下午9:19:25
 */
@Entity(name="tb_user_relation")
public class TbUserRelation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;//主键
	private Integer firstUid;//用户1的uid
	private Integer secondUid;//用户2的uid
	@Column(length=2)
	private String firstUserRelation;//用户1对用户2的关系 0为喜欢,1为不喜欢,2为未处理
	@Column(length=2)
	private String secondUserRelation;//用户2对用户1的关系
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
