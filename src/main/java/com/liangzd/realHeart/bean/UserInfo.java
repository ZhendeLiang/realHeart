package com.liangzd.realHeart.bean;

public class UserInfo {
	private String username;
	private String gender;
	private String password;
	private String email;
	private String phoneNumber;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public UserInfo() {
		super();
	}
	public UserInfo(String username, String gender, String password, String email, String phoneNumber) {
		super();
		this.username = username;
		this.gender = gender;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
	@Override
	public String toString() {
		return "UserInfo [username=" + username + ", gender=" + gender + ", password=" + password + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + "]";
	}
}
