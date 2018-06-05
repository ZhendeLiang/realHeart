package com.liangzd.realHeart.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.liangzd.realHeart.VO.EmailVo;
import com.liangzd.verifyCode.mail.MailConfiguration;
import com.liangzd.verifyCode.mail.MailVerifyCode;
import com.liangzd.verifyCode.phone.PhoneConfiguration;
import com.liangzd.verifyCode.phone.PhoneVerifyCode;

public class MethodUtil {
	/**
	 * 
	 * @Description: 判断是否是数字
	 * @param 
	 * @return boolean
	 * @author liangzd
	 * @date 2018年6月5日 下午1:54:15
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 
	 * @Description: 加密字符串
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月5日 下午1:54:30
	 */
	public static String encrypt(String encryptType, String encryptString, String salt, int times) {
		return new SimpleHash(encryptType, encryptString, salt, times).toString();
	}
	
	/**
	 * 
	 * @Description: 隐藏邮箱详细信息
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月5日 下午1:57:43
	 */
	public static String hideEmail(String email) {
		String result = "";
		if(!StringUtils.isEmpty(email)) {
			if(email.indexOf("@") == 3 || email.indexOf("@") == 4) {
				result = email.substring(0,1);
				for(int i = 0; i < email.indexOf("@")-2; i++) {
					result += "*";
				}
				result += email.substring(email.indexOf("@")-1,email.indexOf("@"));
			}
			if(email.indexOf("@") > 4) {
				result = email.substring(0,2);
				for(int i = 0; i < email.indexOf("@")-4; i++) {
					result += "*";
				}
				result += email.substring(email.indexOf("@")-2,email.indexOf("@"));
			}
		}
		return result+email.substring(email.indexOf("@"));
	}
	
	/**
	 * 
	 * @Description: 隐藏用户名详细信息
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月5日 下午1:57:43
	 */
	public static String hideName(String username, String gender) {
		String result = "";
		if(!StringUtils.isEmpty(username)) {
			result = username.substring(0, 1);
			if(!StringUtils.isEmpty(gender)) {
				result += ("0".equals(gender) ? ConstantParams.MALE : ConstantParams.FEMALE);
			}else {
				result += "神秘人";
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 隐藏手机号详细信息
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月5日 下午1:57:43
	 */
	public static String hidePhoneNumber(String phoneNumber) {
		String result = "";
		if(!StringUtils.isEmpty(phoneNumber)) {
			result = phoneNumber.substring(0, 3) + " **** " + phoneNumber.substring(7,phoneNumber.length());
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 隐藏手机号详细信息
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月5日 下午1:57:43
	 */
	public static String hideIdCard(String idCard) {
		String result = "";
		if(!StringUtils.isEmpty(idCard)) {
			result = "**************" + idCard.substring(14, idCard.length());
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 通过邮箱地址获取对应的邮箱登陆地址
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月5日 下午1:54:42
	 */
	public static String getEmailLoginURL(String email) {
		String emailType = email.substring(email.indexOf("@"));
		String result = StringUtils.isEmpty(ConstantParams.EMAIL_LOGIN_URL.get(emailType)) ? "" : ConstantParams.EMAIL_LOGIN_URL.get(emailType);
		return result;
	}
	
	/**
	 * 
	 * @Description: 发送手机验证码
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月5日 下午1:55:07
	 */
	public static String sendPhoneVerifyCode(String phoneNumber) throws IOException {
		PhoneConfiguration config = new PhoneConfiguration();
		config.setAppKey(ConstantParams.PHONE_APP_KEY);
		config.setAppSecret(ConstantParams.PHONE_APP_SECRET);
		config.setCodelen(ConstantParams.PHONE_CODE_LEN);
		config.setNonce(ConstantParams.PHONE_NONCE);
		config.setServerUrl(ConstantParams.PHONE_SERVER_URL);
		config.setTemplateid(ConstantParams.PHONE_TEMPLATE_ID);
		PhoneVerifyCode phoneVerifyCode = new PhoneVerifyCode(config);
		return phoneVerifyCode.sendMsg(phoneNumber);
	}
	
	/**
	 * 
	 * @Description: 发送邮件
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年6月5日 下午1:55:23
	 */
	public static void sendEmailVerifyURL(EmailVo emailVo) throws IOException, MessagingException {
		MailConfiguration mailConfiguration = new MailConfiguration();
		mailConfiguration.setUserMail(ConstantParams.EMAIL_USERMAIL);
		mailConfiguration.setPassword(ConstantParams.EMAIL_PASSWORD);
		MailVerifyCode mailVerifyCode = new MailVerifyCode(mailConfiguration);
		String emailSubject = "邮件主题";
		String emailContent = "邮件内容";
		if(!StringUtils.isEmpty(emailVo.getType()) && "resetPassword".equals(emailVo.getType())) {
			emailSubject = "Love In China用户密码找回(此邮件由系统产生不可回复)";
			emailContent = readFileToString("classpath:EmailResetPasswordTemplate.html");
			emailContent = emailContent.replace("${username}", emailVo.getUsername());
			emailContent = emailContent.replace("${URL}", emailVo.getContextPath()+emailVo.getURL());
			emailContent = emailContent.replace("${contextPath}", emailVo.getContextPath());
			emailContent = emailContent.replace("${contactUs}", ConstantParams.CONTACT_US);
			emailContent = emailContent.replace("${teamName}", ConstantParams.TEAM_NAME);
		}
		mailVerifyCode.setSubject(emailSubject);
		mailVerifyCode.setContent(emailContent);
		mailVerifyCode.setTo(new String[] {emailVo.getToEmail()});
		mailVerifyCode.sendMessage();
	}
	
	
	public static void main(String[] args) throws IOException {
	}
	
	/**
	 * 
	 * @Description: 读取ClassPath路径下的文件,转换成字符串
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月5日 下午1:56:08
	 */
	public static String readFileToString(String fileName) throws IOException {
		File file = ResourceUtils.getFile(fileName);
		FileInputStream fileInputStream = new FileInputStream(file);
		StringBuffer buffer = new StringBuffer();
		byte[] bytes = new byte[4096];
		int len = 0;
		while((len = fileInputStream.read(bytes)) != -1) {
			buffer.append(new String(bytes,0,len));
		}
		fileInputStream.close();
		return buffer.toString();
	}

	/**
	 * 
	 * @Description: 保存上传文件信息
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月5日 下午1:56:49
	 */
	public static String saveFile(String filePath, MultipartFile file) {
//		String fileName = file.getOriginalFilename();
//		int size = (int) file.getSize();
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String fileName = UUID.randomUUID().toString()+suffix;
		if (file.isEmpty()) {
			return "false";
		} else {
			File path = new File(filePath + "/" + fileName);
			if (!path.getParentFile().exists()) { // 判断文件父目录是否存在
				path.getParentFile().mkdirs();
			}
			try {
				file.transferTo(path);
			} catch (Exception e) {
				e.printStackTrace();
				return "false";
			}
		}
		return fileName;
	}
}
