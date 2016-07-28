package com.kakao.kakaogift.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dd.processbutton.OnStateListener;
import com.dd.processbutton.ProcessButton;
import com.dd.processbutton.iml.ActionProcessButton;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.ToastUtils;

public class LinkDialog extends Dialog{

	private TextView nameView,phoneView;
	private Context context;
	private CallBack mCallBack;
	private ActionProcessButton btn_submit;
	
	private boolean isComplete = false;
	
	public LinkDialog(Context context) {
		super(context,R.style.CustomDialog);
		this.context = context;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_toast_massage);
		nameView = (TextView) findViewById(R.id.username);
		phoneView = (TextView) findViewById(R.id.phone);
		btn_submit = (ActionProcessButton) findViewById(R.id.btn_submit);
		
		btn_submit.setMode(ActionProcessButton.Mode.ENDLESS);
		btn_submit.setOnClickListener(new SubmitClick());
		setCanceledOnTouchOutside(false);
		
		btn_submit.setOnStateListener(new OnStateListener() {
			
			@Override
			public void onProgress() {
				nameView.setEnabled(false);
				phoneView.setEnabled(false);
			}
			
			@Override
			public void onError() {
				nameView.setEnabled(true);
				phoneView.setEnabled(true);
			}
			
			@Override
			public void onComplete() {
				isComplete = true;
			}
		});
	}
	
	public Boolean getState(){
		return isComplete;
	}
	
	private class SubmitClick implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			if(isComplete){
				mCallBack.onCompleteClick();
				return;
			}
			if(TextUtils.isEmpty(nameView.getText())){
				ToastUtils.Toast(context, "请填写联系人姓名");
				return;
			}
			if(TextUtils.isEmpty(phoneView.getText())){
				ToastUtils.Toast(context, "请填写联系方式");
				return;
			}
			if(!CommonUtils.isPhoneNum(phoneView.getText().toString())){
				ToastUtils.Toast(context, "请填写正确的联系方式");
				return;
			}
			btn_submit.setProgress(50);
			if(mCallBack !=null)
				mCallBack.onClick(btn_submit,nameView.getText().toString(),phoneView.getText().toString());
		}
		
	}
	
	public void setCallBack(CallBack mCallBack){
		this.mCallBack = mCallBack;
	}
	public interface CallBack{
		public void onClick(ProcessButton button,String name,String phone);
		public void onCompleteClick();
	}

}
