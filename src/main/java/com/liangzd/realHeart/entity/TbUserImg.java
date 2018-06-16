package com.liangzd.realHeart.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 
 * @Description: tb_user_img表的持久化对象,用户头像与背景图片表
 * @author liangzd
 * @date 2018年6月16日 下午9:17:08
 */
@Entity(name="tb_user_img")
public class TbUserImg implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;//主键
	@Column(length = 11)
	private Integer uid;//用户uid
	@Column(unique = true,length = 1000)
	private String imgUUID;//对象的名称才去UUID的生成方式包含后缀名,如0a8eba9c-8b9a-47e9-afff-03f278a7a7e4.jpg
	@Column(length = 2)
	private String imgType;//图片类型,0为头像,1为背景
	@Column
	private Date createTime;//上传时间
	@Column
	private Integer increasement;//上传版本
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
