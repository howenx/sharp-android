package com.hanmimei.utils;

import com.hanmimei.R;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class AlertDialogUtils {
	
	public static void MyDialog(Context mContext, String title, String left, String right){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		View view = inflater.inflate(R.layout.dialog_layout, null);
		dialog.setView(view);
		dialog.show();
	}

}
