package com.liangzd.realHeart.VO;

/**
 * 
 * @Description: 邮箱基本配置信息的数据交互实体类
 * @author liangzd
 * @date 2018年6月20日 下午3:27:09
 */
public class EmailVo {
	private String username;//邮箱中需要替换的用户名
	private String URL;//邮箱中需要替换的URL
	private String toEmail;//接受者邮箱
	private String type;//类型,"resetPassword"为找回密码邮件
	private String contextPath;//邮件的内容
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	@Override
	public String toString() {
		return "EmailVo [username=" + username + ", URL=" + URL + ", toEmail=" + toEmail + ", type=" + type
				+ ", contextPath=" + contextPath + "]";
	}
}
