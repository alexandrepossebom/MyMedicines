package com.possebom.mymedicines.util;


public abstract class Utils {

	private static final boolean	DEBUG	= true;
	private static final String	TAG	= "MEDICINE";

	public static Integer parseInt(final String text,final int value) {
		int number = 0;
		try {
			number = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			number = value;
		}
		return number;
	}
	
	public static void log(final String message) {
		if (DEBUG) {
			android.util.Log.d(TAG, message);
		}
	}

}
