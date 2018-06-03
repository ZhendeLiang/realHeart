package com.liangzd.realHeart.util;

import java.util.HashMap;
import java.util.Map;

public class ConstantParams {
	public static final String TEAM_NAME ="Love In China's Team";
	public static final String CONTACT_US ="181 3733 6103";
	
	public static final String PHONE_APP_KEY = "2ccbcfe7da5cae79690b870d8e7cfb42";
	public static final String PHONE_APP_SECRET = "b3054efaaa42";
	public static final String PHONE_CODE_LEN = "6";
	public static final String PHONE_NONCE = "123456";
	public static final String PHONE_SERVER_URL = "https://api.netease.im/sms/sendcode.action";
	public static final String PHONE_TEMPLATE_ID = "4073043";
	
	public static final String EMAIL_USERMAIL = "517610366@qq.com";
	public static final String EMAIL_PASSWORD = "bsjbwflhblazbifh";
	
	public static Map<String, String> EMAIL_LOGIN_URL = null;
	static {
		EMAIL_LOGIN_URL = new HashMap<String, String>();
		EMAIL_LOGIN_URL.put("@163.com", "https://mail.163.com/");
		EMAIL_LOGIN_URL.put("@163.net", "http://mail.tom.com/");
		EMAIL_LOGIN_URL.put("@126.com", "https://www.126.com/");
		EMAIL_LOGIN_URL.put("@126.net", "https://www.126.com/");
		EMAIL_LOGIN_URL.put("@tom.com", "http://mail.tom.com/");
		EMAIL_LOGIN_URL.put("@sina.com", "https://mail.sina.com.cn/");
		EMAIL_LOGIN_URL.put("@sina.com.cn", "https://mail.sina.com.cn/");
		EMAIL_LOGIN_URL.put("@yahoo.com", "https://login.yahoo.com/m");
		EMAIL_LOGIN_URL.put("@yahoo.com.cn", "https://login.yahoo.com/m");
		EMAIL_LOGIN_URL.put("@hotmail.com", "https://outlook.live.com/owa/");
		EMAIL_LOGIN_URL.put("@gmail.com", "www.gmail.com/");
		EMAIL_LOGIN_URL.put("@21cn.com", "http://mail.21cn.com/w2/");
		EMAIL_LOGIN_URL.put("@sohu.com", "https://mail.sohu.com/fe/#/login");
		EMAIL_LOGIN_URL.put("@263.com", "http://mail.263.net/");
		EMAIL_LOGIN_URL.put("@eyou.com", "http://www.eyou.com/");
		EMAIL_LOGIN_URL.put("@qq.com", "https://mail.qq.com/cgi-bin/loginpage");
		EMAIL_LOGIN_URL.put("@188.com", "http://www.188.com/");
	}

}
