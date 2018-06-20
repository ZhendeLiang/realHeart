package com.liangzd.realHeart.util;

/**
 * 
 * @Description: 用户登陆的类型,用户区分普通用户与管理员用户,进行不同的ShiroRealm进行身份验证
 * @author liangzd
 * @date 2018年6月20日 下午3:17:13
 */
public enum LoginType {
	//user用户,admin用户
    USER("User"), ADMIN("Admin");

    private String type;

    private LoginType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}