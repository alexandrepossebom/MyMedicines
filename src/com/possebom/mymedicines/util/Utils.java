package com.possebom.mymedicines.util;

public class Utils {

	public static Integer parseInt(String text,int value) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return value;
		}
	}

}
