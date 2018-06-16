package com.liangzd.realHeart.VO;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class UserAndroid implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer uid;
	private String username;
	private String nickname;
	private String password;
	private String gender;
	private String phoneNumber;
	private String idCard;
	private String email;
	private String selfIntroduction;//自我介绍
	private byte state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
	private Date createTime;//创建日期
	private String viprankName;
	private String addressId;
	private String headImgPath;
	
	private String userHeadImg;
	List<String> backgroundImgs;
	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSelfIntroduction() {
		return selfIntroduction;
	}
	public void setSelfIntroduction(String selfIntroduction) {
		this.selfIntroduction = selfIntroduction;
	}
	public byte getState() {
		return state;
	}
	public void setState(byte state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getViprankName() {
		return viprankName;
	}
	public void setViprankName(String viprankName) {
		this.viprankName = viprankName;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getHeadImgPath() {
		return headImgPath;
	}
	public void setHeadImgPath(String headImgPath) {
		this.headImgPath = headImgPath;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", nickname="
				+ nickname + ", password=" + password + ", gender=" + gender
				+ ", phoneNumber=" + phoneNumber + ", idCard=" + idCard
				+ ", email=" + email + ", selfIntroduction=" + selfIntroduction
				+ ", state=" + state + ", createTime=" + createTime
				+ ", viprankName=" + viprankName + ", addressId=" + addressId
				+ ", headImgPath=" + headImgPath + "]";
	}
}
