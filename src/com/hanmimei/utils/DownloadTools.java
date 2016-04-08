package com.hanmimei.utils;

import java.io.File;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.hanmimei.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class DownloadTools {
	@SuppressLint("SdCardPath")
	public static final String DOWNLOAD_PATH = "/mnt/sdcard/hanmimei/downloads/";
	private Context mContext;
	private Notification notification;
	private RemoteViews contentView;
	private NotificationManager notificationManager;
	private String filePath;

	public DownloadTools(Context mContext) {
		this.mContext = mContext;
	}

	public void download(String url) {
		Uri uri = Uri.parse(url);
		filePath = DOWNLOAD_PATH+ uri.getLastPathSegment();
		com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils();
		http.configRequestThreadPoolSize(3);
		http.download(url,filePath, false, false, new ManagerCallBack());
	}

	/**
	 * 安装apk
	 * 
	 * @param context
	 *            上下文
	 * @param file
	 *            APK文件
	 */
	public void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	public class ManagerCallBack extends RequestCallBack<File>{

		@Override
		public void onStart() {
			super.onStart();
			createNotification();
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			Log.i("HttpException", arg0.toString());
			ToastUtils.Toast(mContext, "下载失败，请检查网络");
		}

		@Override
		public void onLoading(long total,long current,boolean isUploading){
			super.onLoading(total, current, isUploading);
			notifyNotification(current, total);
		}

		@Override
		public void onSuccess(ResponseInfo<File> arg0) {
			installApk(mContext, new File(filePath));
		}
		
	}
	
	public void createNotification() {
        notification = new Notification();
        notification.tickerText = "安装包正在下载...";
        notification.icon = R.drawable.ic_launcher;
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        
        /*** 自定义  Notification 的显示****/
        contentView = new RemoteViews(mContext.getPackageName(), R.layout.notification_for_update);
        contentView.setProgressBar(R.id.progress, 100, 0, false);
        contentView.setTextViewText(R.id.tv_progress, "0%");
        contentView.setTextViewText(R.id.title, "安装包正在下载...");
        contentView.setViewVisibility(R.id.tv_progress, View.VISIBLE);
		contentView.setViewVisibility(R.id.progress, View.VISIBLE);
        notification.contentView = contentView;
        
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        
		notificationManager.notify(R.layout.notification_for_update, notification);
    }
	
	private void notifyNotification(long percent,long length){
		if(percent < length){
			contentView.setTextViewText(R.id.tv_progress, (percent*100/length)+"%");
	        contentView.setProgressBar(R.id.progress, (int)length,(int)percent, false);
	        notification.contentView = contentView;
	        notificationManager.notify(R.layout.notification_for_update, notification);
		}else{
			contentView.setTextViewText(R.id.title, "下载已完成");
			contentView.setViewVisibility(R.id.tv_progress, View.GONE);
			contentView.setViewVisibility(R.id.progress, View.GONE);
	        notification.contentView = contentView;
	        notificationManager.notify(R.layout.notification_for_update, notification);
		}
	}

}
