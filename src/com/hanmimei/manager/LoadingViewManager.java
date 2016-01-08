package com.hanmimei.manager;

import android.content.Context;

import com.hanmimei.view.AddGoodsSuccessDialog;

public class LoadingViewManager {

	private AddGoodsSuccessDialog dialog;

	private static class PopupWindowManagerHolder {
		public static final LoadingViewManager instance = new LoadingViewManager();
	}

	public static LoadingViewManager getInstance() {
		return PopupWindowManagerHolder.instance;
	}
		

	public void showDialog(Context context) {
		if(dialog == null){
			dialog = new AddGoodsSuccessDialog(context);
		}
		if(dialog.isShowing())
			dialog.cancel();
		dialog.show();
	}

	
	public void clearDialog(){
		if(dialog !=null){
			dialog.cancel();
			dialog =null;
		}
	}
	
}
