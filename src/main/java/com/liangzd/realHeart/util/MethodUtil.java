package com.liangzd.realHeart.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.util.ResourceUtils;

import com.alibaba.druid.util.StringUtils;
import com.liangzd.realHeart.VO.EmailVo;
import com.liangzd.verifyCode.mail.MailConfiguration;
import com.liangzd.verifyCode.mail.MailVerifyCode;
import com.liangzd.verifyCode.phone.PhoneConfiguration;
import com.liangzd.verifyCode.phone.PhoneVerifyCode;

public class MethodUtil {
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static String encrypt(String encryptType, String encryptString, String salt, int times) {
		return new SimpleHash(encryptType, encryptString, salt, times).toString();
	}
	
	public static String hideEmail(String email) {
		String result = "";
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
		return result+email.substring(email.indexOf("@"));
	}
	
	public static String getEmailLoginURL(String email) {
		String emailType = email.substring(email.indexOf("@"));
		String result = StringUtils.isEmpty(ConstantParams.EMAIL_LOGIN_URL.get(emailType)) ? "" : ConstantParams.EMAIL_LOGIN_URL.get(emailType);
		return result;
	}
	
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
		String result = readFileToString("classpath:EmailResetPasswordTemplate.html");
		System.out.println(result);
	}
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
}
