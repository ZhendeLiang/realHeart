package com.liangzd.realHeart.util;

import java.util.regex.Pattern;

public class MethodUtil {
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
}
