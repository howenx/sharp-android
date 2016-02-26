package com.hanmimei.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * 常用工具
 * 
 * @author 刘奇
 * 
 */
public class CommonUtil {

	private CommonUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private final static int MAX_NUM_PIXELS = 320 * 490;
	private final static int MIN_SIDE_LENGTH = 350;

	/**
	 * 判断是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @Description 生成图片的压缩图
	 * @param filePath
	 * @return
	 */
	public static Bitmap createImageThumbnail(String filePath) {
		if (null == filePath || !new File(filePath).exists())
			return null;
		Bitmap bitmap = null;
		int degree = readPictureDegree(filePath);
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, MAX_NUM_PIXELS);
			opts.inJustDecodeBounds = false;
			// if (opts.inSampleSize == 1) {
			// bitmap = BitmapFactory.decodeFile(filePath, opts);
			//
			// } else {
			bitmap = BitmapFactory.decodeFile(filePath, opts);
			// }
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		Bitmap newBitmap = rotaingImageView(degree, bitmap);
		return newBitmap;
	}

	/**
	 * 判断一个activity是不是在栈顶
	 * 
	 * @param context
	 * @param activityName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean isTopActivity(Context context, String activityName) {
		if (null == activityName)
			return false;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// List<AppTask> appTasks= am.getAppTasks();
		List<RunningTaskInfo> runningTaskInfos = am.getRunningTasks(1);
		String compName = null;
		if (null != runningTaskInfos) {
			compName = runningTaskInfos.get(0).topActivity.getClassName();
		}
		if (null == compName)
			return false;

		return activityName.equals(compName);
	}

	/**
	 * 获取面板高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getDefaultPannelHeight(Context context) {
		if (context != null) {
			int size = (int) (getElementSzie(context) * 5.5);
			return size;
		} else {
			return 300;
		}
	}

	public static int getAudioBkSize(int sec, Context context) {
		int size = getElementSzie(context) * 3;
		if (sec <= 0) {
			return -1;
		} else if (sec <= 2) {
			return size;
		} else if (sec <= 8) {
			return (int) (size + ((float) ((sec - 2) / 6.0)) * size);
		} else if (sec <= 60) {
			return (int) (2 * size + ((float) ((sec - 8) / 52.0)) * size);
		} else {
			return -1;
		}
	}

	/*
	 * 根据不同的屏幕生成对应的面板高度
	 */
	public static int getElementSzie(Context context) {
		if (context != null) {
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			int screenHeight = px2dip(dm.heightPixels, context);
			int screenWidth = px2dip(dm.widthPixels, context);
			int size = screenWidth / 6;
			if (screenWidth >= 800) {
				size = 60;
			} else if (screenWidth >= 650) {
				size = 55;
			} else if (screenWidth >= 600) {
				size = 50;
			} else if (screenHeight <= 400) {
				size = 20;
			} else if (screenHeight <= 480) {
				size = 25;
			} else if (screenHeight <= 520) {
				size = 30;
			} else if (screenHeight <= 570) {
				size = 35;
			} else if (screenHeight <= 640) {
				if (dm.heightPixels <= 960) {
					size = 35;
				} else if (dm.heightPixels <= 1000) {
					size = 45;
				}
			}
			return size;
		}
		return 40;
	}

	/**
	 * px准换成dp
	 * 
	 * @param pxValue
	 * @param context
	 * @return dp
	 */
	public static int px2dip(float pxValue, Context context) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * dip转换成dx
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(float dipValue) {
		final float scale = Resources.getSystem().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 获取图形距离左边的宽度
	 * 
	 * @param dp
	 *            距离右边的宽度
	 * @return
	 */

	public static int apartRightWidth(float dp) {
		int px = dip2px(dp);
		return (int) ((int) Resources.getSystem().getDisplayMetrics().widthPixels - px);
	}

	/**
	 * 根据时间获得编号码
	 * 
	 * @param string
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTimeCode(String sub) {
		SimpleDateFormat format = new SimpleDateFormat("yyMMddhhssmm");
		String code = format.format(new Date());
		return sub + code;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			Log.i("Sysout", e.getMessage());

		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		if (null == bitmap) {
			return null;
		}
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? MIN_SIDE_LENGTH : (int) Math
				.min(Math.floor(w / minSideLength),
						Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {

		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {

		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int screenHeight = dm.heightPixels;
		return screenHeight;
	}

	/**
	 * 获取状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 隐藏键盘
	 * 
	 * @param mcontext
	 */
	public static void closeBoard(Context mcontext) {
		InputMethodManager imm = (InputMethodManager) mcontext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		if (imm.isActive()) // 一直是true
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
					InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 隐藏键盘
	 * 
	 * @param mcontext
	 */
	public static void closeBoardIfShow(Activity activity) {
		View view = activity.getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * 用户匿名显示
	 */
	public static String nimingShow(String name) {
		String result = name.toCharArray()[0] + "***"
				+ name.toCharArray()[name.length() - 1];
		return result;

	}

	/**
	 * 判断是否安装app
	 */

	public static boolean isAvilible(Context context, String packageName) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packageName, 0);

		} catch (NameNotFoundException e) {
			packageInfo = null;
		}

		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}

	/**
	 * activity跳转
	 * 
	 * @param mcContext
	 * @param clazz
	 */

	@SuppressWarnings("rawtypes")
	public static void doJump(Context mcContext, Class clazz) {
		Intent intent = new Intent(mcContext, clazz);
		mcContext.startActivity(intent);
	}
	
	/**
	 * 获取当前系统版本名
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context context) throws Exception {
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),
				0);
		return packInfo.versionName;
	}

	/**
	 * 判断手机格式 正则
	 * 
	 * @param nums
	 * @return
	 */
	public static boolean isPhoneNum(String nums) {
		/*
		 * 判断前两位 13/15/17/18
		 */
		String telRegex = "[1][345678]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(nums))
			return false;
		else
			return nums.matches(telRegex);
	}

	/**
	 * 等待窗
	 * 
	 * @param mContext
	 * @param msg
	 * @return
	 */
	public static ProgressDialog dialog(Context mContext, String msg) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setMessage(msg);
		return dialog;
	}

	/**
	 * MD5加密
	 * 
	 * @param string
	 * @return
	 */
	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	public static String DecimalFormat(Double format) {
		return new DecimalFormat("##0.00").format(format);
	}

	// 身份证校验
	@SuppressWarnings("rawtypes")
	@SuppressLint("SimpleDateFormat") 
	public static String IDCardValidate(String IDStr) {
		String errorInfo = "";// 记录错误信息
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInfo = "身份证号码长度应该为15位或18位。";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			errorInfo = "身份证15位号码都应为数字 ; v";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 天
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = "身份证生日无效。";
			return errorInfo;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
							strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				errorInfo = "身份证生日不在有效范围。";
				return errorInfo;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = "身份证月份无效";
			return errorInfo;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = "身份证日期无效";
			return errorInfo;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = "身份证地区编码错误。";
			return errorInfo;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				errorInfo = "身份证无效，不是合法的身份证号码";
				return errorInfo;
			}
		} else {
			return errorInfo;
		}
		// =====================(end)=====================
		return errorInfo;
	}

	/**
	 * 功能：判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isPassWord(String str) {
		Pattern pattern = Pattern
				.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9]{6,12}");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isJiaoYan(String str) {
		Pattern pattern = Pattern.compile("(?![^0-9a-zA-Z]+$)[a-zA-Z0-9]{4}");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：判断字符串是否为日期格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Hashtable GetAreaCode() {
		Hashtable hashtable = new Hashtable();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}
	//是将转换
	public static long[] getTimer(int time){
		int minute = 0;
		int second = 0;
		int hour = 0;
		minute = time / 60;
		if (minute < 60) {
			second = time % 60;
		} else {
			hour = minute / 60;
			minute = minute % 60;
			second = time - hour * 3600 - minute * 60;
		}
		long[] timer = { hour, minute, second };
		return timer;
	}

	//校验用户名
	public static String inputIsName(String name, int min, int max){
		String result = "";
		if (name.equals("")) {
			result = "不能为空";
		} else if (name.length() < min) {
			result = "不能少于" + min + "个字符";
		} else if (name.length() > max) {
			result = "不能多余" + max + "个字符";
		} else if (!strIsHeFa(name)) {
			result = "只能是中文／数字／字母";
		}
		return result;
	}

	private static boolean strIsHeFa(String str) {
		String pattern = "[a-zA-Z0-9\u4e00-\u9fa5]+";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}

	}
	//生成随机数
	public static int getRandom(){
		Random random = new Random();
		return random.nextInt();
	}

	public static String doubleTrans(double num) {
		if (num % 1.0 == 0) {
			return String.valueOf((long) num);
		}
		return String.valueOf(num);
	}

	//设置提示内容显示内容以及方式
	public static void setAttention(final TextView attention,String str){
		attention.setText(str);
		attention.setVisibility(View.VISIBLE);
		final Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					attention.setVisibility(View.INVISIBLE);
					break;
				default:
					break;
				}
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					Message msg = mHandler.obtainMessage(1);
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 将文件转成流 base64
	 * 
	 * @param file
	 * @return
	 */
	public static String Base64(File file) {
		byte b[] = null;
		try {
			InputStream in = new FileInputStream(file);

			b = new byte[(int) file.length()];
			try {
				in.read(b);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new String(Base64.encode(b, Base64.NO_WRAP));
	}

	public static  int measureViewWidth(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return view.getMeasuredWidth();
	}

}
