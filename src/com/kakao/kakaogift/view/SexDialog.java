package com.kakao.kakaogift.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.kakao.kakaogift.R
;

public class SexDialog extends AlertDialog {

	
	private SexSelectListener l;
	
	
	public SexDialog(Context context,SexSelectListener l) {
		super(context,R.style.CustomDialog);
		this.l = l;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_sex_pop_layout);
		

		findViewById(R.id.btn_nv).setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (l != null)
					l.onClick("F");
				dismiss();
			}
		});
		findViewById(R.id.btn_nan).setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (l != null)
					l.onClick("M");
				dismiss();
			}
		});
	}

	public interface SexSelectListener{
		public void onClick(String sex);
	}
	
}
