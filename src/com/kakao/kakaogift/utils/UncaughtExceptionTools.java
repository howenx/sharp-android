/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-11 下午4:29:37 
 **/
package com.kakao.kakaogift.utils;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;

import com.kakao.kakaogift.activity.FirstShowActivity;
import com.kakao.kakaogift.activity.HMainActivity;

/**
 * @author vince
 * 
 */
public class UncaughtExceptionTools {

	public static void handler(final Context context) {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				// TODO Auto-generated method stub
				restartApp(context);// 发生崩溃异常时,重启应用
			}
		}); // 程序崩溃时触发线程 以下用来捕获程序崩溃异常
	}

	private static void restartApp(Context context) {
		Intent intent = new Intent(context, FirstShowActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid()); // 结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
	}
}
