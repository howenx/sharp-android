package com.hanmimei.utils;

import com.hanmimei.R;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertDialogUtils {
	
	public static void MyDialog(Context mContext, String title, String left, String right){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		View view = inflater.inflate(R.layout.dialog_layout, null);
		dialog.setView(view);
		dialog.show();
	}
	public static void KouDialog(Context mContext, String ti, String pri, String imgurl){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final AlertDialog dialog = new AlertDialog.Builder(mContext,R.style.CustomDialog).create();
		View view = inflater.inflate(R.layout.hanmimei_password_layout, null);
		ImageView img = (ImageView) view.findViewById(R.id.img);
		ImageLoaderUtils.loadImage(mContext, imgurl, img);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView price = (TextView) view.findViewById(R.id.price);
		title.setText(ti);
		price.setText(pri);
		view.findViewById(R.id.cancle).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.now).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		dialog.setView(view);
		dialog.show();
		
	}

}
