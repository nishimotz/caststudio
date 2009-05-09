/*
 * $Id: Messages.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	//	private static String 	bundleName = "com.nishimotz.mmm.messages";
	private static ResourceBundle resourceBundle;
	//	= ResourceBundle.getBundle(bundleName);

	public static void setup(String bundleName) {
		resourceBundle = ResourceBundle.getBundle(bundleName);
	}

	public static String getString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static String getString(String key, String defaultRSS) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return defaultRSS;
		}
	}
	
	public static boolean getAsBoolean(String key) {
		return Boolean.valueOf(getString(key));
	}

	public static int getAsInteger(String key, int defaultValue) {
		int ret = defaultValue;
		try {
			ret = Integer.valueOf(getString(key));
		} catch (Exception e) {}
		return ret;
	}

	public static float getAsFloat(String key) {
		return Float.valueOf(getString(key));
	}
}
