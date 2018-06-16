package com.liangzd.realHeart.realm;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 
 * @Description: 自定义密码身份验证Token
 * @author liangzd
 * @date 2018年6月16日 下午7:32:04
 */
public class CustomizedToken extends UsernamePasswordToken{
	private static final long serialVersionUID = 1L;
	//登录类型，判断是普通用户登录，教师登录还是管理员登录
    private String loginType;

    public CustomizedToken(final String username, final String password,String loginType) {
        super(username,password);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
