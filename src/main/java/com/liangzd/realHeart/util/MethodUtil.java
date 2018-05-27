package com.liangzd.realHeart.util;

import java.util.regex.Pattern;

import org.apache.shiro.crypto.hash.SimpleHash;

public class MethodUtil {
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static String encrypt(String encryptType, String encryptString, String salt, int times) {
		return new SimpleHash(encryptType, encryptString, salt, times).toString();
	}
}
