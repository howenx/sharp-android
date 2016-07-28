package com.kakao.kakaogift.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap.Config;

public class PreferenceUtil {
	public static final String USER_CONFIG = "USER_INFO";

	private PreferenceUtil() {
		throw new AssertionError();
	}

	/**
	 * 首次使用设置
	 * 
	 * @author
	 *
	 */
	public static class IntroConfig {
		public static final String INTRO_CONFIG = "intro_config";
		public static final String INTRO_CONFIG_MSG = "intro_config_msg";
		public static final String INTRO_CONFIG_VALUE_IS = "intro_config_value_is";
		public static final String INTRO_CONFIG_VALUE_NO = "intro_config_value_no";

		public static void putIntroCfg(Context context, String content) {
			putString(context, INTRO_CONFIG, content);
		}

		public static String getIntroCfg(Context context) {
			return getString(context, INTRO_CONFIG, INTRO_CONFIG_VALUE_IS);
		}
		
		public static void putIntroMsgCfg(Context context, String content) {
			putString(context, INTRO_CONFIG_MSG, content);
		}
		
		public static String getIntroMsgCfg(Context context) {
			return getString(context, INTRO_CONFIG_MSG, INTRO_CONFIG_VALUE_IS);
		}
	}

	/**
	 * 用户登录设置
	 * 
	 * @author
	 *
	 */
	public static class LoginConfig {

		public static final String KEEP_AUTO_KEY = "auto_login";
		public static final String LOGIN_NAME = "loginName";
		public static final String LOGIN_PWD = "loginPwd";
		public static final String LOGIN_ID = "loginid";
		public static final String KEEP_LOGIN = "is_login";

		/**
		 * 是否自动登录
		 * 
		 * @param context
		 * @return
		 */
		public static boolean isAutoLogin(Context context) {
			return getBoolean(context, KEEP_AUTO_KEY, false);
		}

		public static void setAutoLogin(Context context, boolean isAtuo) {
			putBoolean(context, KEEP_AUTO_KEY, isAtuo);
		}

		/**
		 * 是否已经登录
		 * 
		 * @param context
		 * @return
		 */

		public static boolean isLogin(Context context) {
			return getBoolean(context, KEEP_LOGIN, false);
		}

		public static void setKeepLogin(Context context, boolean isAtuo) {
			putBoolean(context, KEEP_LOGIN, isAtuo);
		}
	}

	/**
	 * 用户信息接受通知设定
	 * 
	 * @author
	 */

	public static class NotificationConfig {

		public static final String NOTIFICATION = "keep_notification";

		public static final String NOTIFICATION_SOUND = "keep_sound";
		public static final String NOTIFICATION_VIBRATE = "keep_vibrate";

		/**
		 * 是否新消息提醒
		 * 
		 * @param context
		 * @return
		 */
		public static boolean isKeepNotification(Context context) {
			return getBoolean(context, NOTIFICATION, true);
		}

		/**
		 * 是否响铃 提示
		 * 
		 * @param context
		 * @return
		 */
		public static boolean isKeepSound(Context context) {
			return getBoolean(context, NOTIFICATION_SOUND, true);
		}

		/**
		 * 是否震动 提示
		 * 
		 * @param context
		 * @return
		 */
		public static boolean isKeepVibrate(Context context) {
			return getBoolean(context, NOTIFICATION_VIBRATE, true);
		}

		/**
		 * 设置通知
		 * 
		 * @param context
		 * @param isKeep
		 */
		public static void setKeepNotification(Context context, boolean isKeep) {
			putBoolean(context, NOTIFICATION, isKeep);
		}

		/**
		 * 设置震动
		 * 
		 * @param context
		 * @param isKeep
		 */
		public static void setKeepVibrate(Context context, boolean isKeep) {
			putBoolean(context, NOTIFICATION_VIBRATE, isKeep);
		}

		/**
		 * 设置响铃
		 * 
		 * @param context
		 * @param isKeep
		 */
		public static void setKeepSound(Context context, boolean isKeep) {
			putBoolean(context, NOTIFICATION_SOUND, isKeep);
		}

	}

	public static class PictureQualityConfig {

		public static final String QUALITY = "picturequality";
		public static final String CONFIG = "pictureconfig";

		public static final String CONFIG_HIGH = "high";
		public static final String CONFIG_NORMAL = "normal";
		public static final String CONFIG_ZHINENG = "zhineng";

		public static final Config QUALITY_HIGH = Config.ARGB_8888;
		public static final Config QUALITY_NORMAL = Config.RGB_565;
		public static final Config QUALITY_ZHINENG = Config.ALPHA_8;

		public static void setPictureQuality(Context context, int id) {
			putString(context, QUALITY, id + "");
		}

		public static Config getPictureQualityConfig(Context context) {
			Config config = null;
			switch (getString(context, CONFIG, CONFIG_ZHINENG)) {
			case CONFIG_HIGH:
				config = QUALITY_HIGH;
				break;
			case CONFIG_NORMAL:
				config = QUALITY_NORMAL;
				break;
			case CONFIG_ZHINENG:
			default:
				config = QUALITY_ZHINENG;
				break;
			}
			return config;
		}

		public static void setPictureQualityConfig(Context context,
				String config) {
			putString(context, CONFIG, config);
		}

		public static int getPictureQuality(Context context) {
			String q = getString(context, QUALITY, "0");
			return Integer.valueOf(q);
		}
	}

	/**
	 * put boolean preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putBoolean(Context context, String key, boolean value) {
		SharedPreferences settings = context.getSharedPreferences(USER_CONFIG,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	/**
	 * get boolean preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a boolean
	 */
	public static boolean getBoolean(Context context, String key,
			boolean defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(USER_CONFIG,
				Context.MODE_PRIVATE);
		return settings.getBoolean(key, defaultValue);

	}

	/**
	 * put string preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putString(Context context, String key, String value) {
		SharedPreferences settings = context.getSharedPreferences(USER_CONFIG,
				Context.MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * get string preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a string
	 */
	public static String getString(Context context, String key,
			String defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(USER_CONFIG,
				Context.MODE_MULTI_PROCESS);
		return settings.getString(key, defaultValue);
	}
}
