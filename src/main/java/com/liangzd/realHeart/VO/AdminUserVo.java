package com.liangzd.realHeart.VO;

/**
 * 
 * @Description: 管理员信息的前后台数据交互实体类
 * @author liangzd
 * @date 2018年6月20日 下午3:22:58
 */
public class AdminUserVo {
	private String adminUsername;//管理员名称
	private String adminPassword;//管理员密码
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
	@Override
	public String toString() {
		return "AdminUserVo [adminUsername=" + adminUsername + ", adminPassword=" + adminPassword + "]";
	}
}
