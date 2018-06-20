package com.liangzd.realHeart.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Description: 存储系统常用变量与Android端的数据校验,@TODO一些数据应该定期删除,二期开发改用Redis存储
 * @author liangzd
 * @date 2018年6月20日 下午3:05:35
 */
public class ConstantParams {
	// 系统信息
	public static final String TEAM_NAME = "Love In China's Team";//项目名称
	public static final String CONTACT_US = "187 1880 6083";//联系我

	// 发送短信的账户信息
	public static final String PHONE_APP_KEY = "2ccbcfe7da5cae79690b870d8e7cfb42";//网易云短信平台应用key
	public static final String PHONE_APP_SECRET = "b3054efaaa42";//密码
	public static final String PHONE_CODE_LEN = "6";//验证码长度
	public static final String PHONE_NONCE = "123456";//Nonce是一个只被使用一次的任意或非重复的随机数值,此处暂时不修改
	public static final String PHONE_SERVER_URL = "https://api.netease.im/sms/sendcode.action";//接口地址
	public static final String PHONE_TEMPLATE_ID1 = "4073043";//验证码模板1
	public static final String PHONE_TEMPLATE_ID2 = "3983052";//验证码模板2

	// 发送邮件的用户名和密钥
	public static final String EMAIL_USERMAIL = "517610366@qq.com";//发送邮件的主账号
	public static final String EMAIL_PASSWORD = "bsjbwflhblazbifh";//发送邮件的主账号对应的密钥

	// 上传图片的配置信息
	public static final String LOCAL_HEADIMG_UPLOAD_PATH = "D:/loveInChina/uploadFile/img/headImg";//头像-windows服务文件系统路径
	// public static final String LOCAL_HEADIMG_UPLOAD_PATH = "/loveInChina/uploadFile/img/headImg";//头像-Linux服务器文件系统路径

	public static final String LOCAL_BACKGROUNDIMGS_UPLOAD_PATH = "D:/loveInChina/uploadFile/img/backgroudImgs";//背景图片-windows服务文件系统路径
	// public static final String LOCAL_BACKGROUNDIMGS_UPLOAD_PATH = "/loveInChina/uploadFile/img/backgroudImgs";//背景图片-Linux服务器文件系统路径
	public static final String SERVER_HEADIMG_UPLOAD_PATH = "/headImg/";//头像地址的项目相对路径
	public static final String SERVER_BACKGROUNDIMGS_UPLOAD_PATH = "/backgroudImgs/";//背景图片的项目相对路径
	public static final String HEADIMG = "0";//头像类型
	public static final String BACKGROUNDIMGS = "1";//背景图片类型

	// 数据库中对应0,1字段的属性
	public static final String TB_USER_GENDER_MALE = "1";//TB_USER表gender字段对应性别0女1男
	public static final String TB_USER_GENDER_FEMALE = "0";
	public static final Byte TB_USER_STATE_LIMIT = 1;//TB_USER表state字段对应性别0正常1限制
	public static final Byte TB_USER_STATE_NORMAL = 0;

	// 定义男女统称
	public static final String MALE = "男士";
	public static final String FEMALE = "女士";

	// 用户关系
	public static final String USER_RELATION_LIKE = "0";//TB_USER_RELATION表first_user_relation和second_user_relation字段对应性别0喜欢 1不喜欢 2未知
	public static final String USER_RELATION_DISLIKE = "1";
	public static final String USER_RELATION_UNKNOW = "2";
	
	//网页聊天页面的后天异步请求时间,单位/毫秒
	public static final Integer CHATFLUSHTIME = 2000 ;

	// 映射邮箱对应登陆地址
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

	//存储安卓请求对象数据
	public static Map<String, Object> ANDROID_PARAMS = null;
	static {
		ANDROID_PARAMS = new HashMap<String, Object>();
	}
}
