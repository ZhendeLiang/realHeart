package com.liangzd.realHeart.VO;

public class UserVo {
	//正常登陆
	private String userinp;//手机号/用户名/UID/邮箱
	private String password;//密码
	private String veri;//二维码 验证码
	
	//手机登陆
	private String num2;//手机登陆  手机号
	private String veriCode;//手机登录 短信验证码 需要转
	
	//用户注册  
	private String tel;
	//验证码与手机登陆验证码相同veri-code
	//密码1 passport 懒得改了 password
	private String passport;
	//密码2
	private String passport2;
	
	//找回密码
	private String pcTel; //手机号/邮箱
	//验证码与手机登陆验证码相同veri-code
	//二维码验证码与正常登陆验证码相同veri

	//管理员登录
	private String adminUsername;
	private String adminPassword;
	
	private Integer verifyTimes;
	
	public String getVeri() {
		return veri;
	}

	public String getUserinp() {
		return userinp;
	}

	public void setUserinp(String userinp) {
		this.userinp = userinp;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVeri(String veri) {
		this.veri = veri;
	}

	public String getNum2() {
		return num2;
	}

	public void setNum2(String num2) {
		this.num2 = num2;
	}

	public String getVeriCode() {
		return veriCode;
	}

	public void setVeriCode(String veriCode) {
		this.veriCode = veriCode;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getPassport2() {
		return passport2;
	}

	public void setPassport2(String passport2) {
		this.passport2 = passport2;
	}

	public String getPcTel() {
		return pcTel;
	}

	public void setPcTel(String pcTel) {
		this.pcTel = pcTel;
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

	public Integer getVerifyTimes() {
		return verifyTimes;
	}

	public void setVerifyTimes(Integer verifyTimes) {
		this.verifyTimes = verifyTimes;
	}
}
