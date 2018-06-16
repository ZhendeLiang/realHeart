package com.liangzd.realHeart.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 
 * @Description: tb_admin_user表的持久化类,管理员用户表
 * @author liangzd
 * @date 2018年6月16日 下午9:08:25
 */
@Entity(name="tb_admin_user")
public class AdminUser implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;//主键
	@Column(unique = true)
	private String adminUsername;//唯一值,管理员的用户名
	private String adminPassword;//管理员的密码
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAdminUsername() {
		return adminUsername;
	}
	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
}
