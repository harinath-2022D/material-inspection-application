package com.zettamine.mi.utils;

public class StringUtil {

	private static String EXTRA_SPACE = "\\s+";
	private static String SINGLE_SPACE = " ";
	private static String EMPTY = "";

	public static String removeExtraSpaces(String str) {

		if (str != null && str.length() != 0) {

			String response = str.replaceAll(EXTRA_SPACE, SINGLE_SPACE).trim();

			return response;
		}

		return str;
	}

	public static String removeAllSpaces(String str) {

		if (str != null && str.length() != 0) {

			String response = str.replaceAll(EXTRA_SPACE, EMPTY).trim();

			return response;
		}
		return str;
	}

}
