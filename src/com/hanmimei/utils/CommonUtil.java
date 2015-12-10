package com.hanmimei.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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

	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
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

	public static int getScreenWidth(Context context) {
		
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}
	
public static int getScreenHeight(Context context) {
		
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
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

	@SuppressLint("SimpleDateFormat")
	public static String getCurTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}

	/*
	 * activity跳转
	 */
	public static void doJump(Context mcContext, Class clazz) {
		Intent intent = new Intent(mcContext, clazz);
		mcContext.startActivity(intent);
	}

	// 判断手机格式 正则
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

	// 等待窗
	public static ProgressDialog dialog(Context mContext, String msg) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setMessage(msg);
		return dialog;
	}

	// MD5加密
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

	/**
	 * 重新设置viewPager高度
	 * 
	 * @param position
	 */
	public static void resetViewPagerHeight(ViewPager pager, int position) {
		View child = pager.getChildAt(position);
		if (child != null) {
			child.measure(0, 0);
			int h = child.getMeasuredHeight();
			LinearLayout.LayoutParams params = (LayoutParams) pager
					.getLayoutParams();
			params.height = h;
			pager.setLayoutParams(params);
		}
	}
	


	public static void loadImg(Activity activity, ImageView img, String url,
			int w, int h) {
		ImageLoader imageLoader = InitImageLoader.initLoader(activity);
		DisplayImageOptions imageOptions = InitImageLoader.initOptions();
		// 图片的比例适配
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		android.view.ViewGroup.LayoutParams params;
		params = img.getLayoutParams();
		params.height = screenWidth * h / w;
		params.width = screenWidth;
		img.setLayoutParams(params);
		imageLoader.displayImage(url, img, imageOptions);
	}
	public static int dp2px(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
	
	public static String DecimalFormat(Double format){
		return  new DecimalFormat("##0.00").format(format);
	}
	
}
