package com.enet.cn.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtil {
	private static final String FILE_NAME = "enet";
	private static SharedPreferences sp;
	/**
	 * 存boolean
	 * 
	 * @param context
	 * @param key
	 * contributePage
	 * @param value
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		if (sp == null) {
			sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}
	/**
	 * 取boolean
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}
	
	public static void putString(Context context, String key, String value) {
		if (sp == null) {
			sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	public static String getString(Context context, String key,
			String defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}
}
