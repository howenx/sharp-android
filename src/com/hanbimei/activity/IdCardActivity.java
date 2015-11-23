package com.hanbimei.activity;

import com.hanbimei.R;
import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") 
public class IdCardActivity extends BaseActivity implements OnClickListener{

	private TextView header;
	private ImageView back;
	private EditText name_edit;
	private EditText nums_edit;
	private TextView add;
	private ImageView left_img;
	private ImageView right_img;
	//
	private PopupWindow popWindow;
	private View parenView;
	
	private String name;
	private String idcard_nums;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.bind_idcard_layout);
		getActionBar().hide();
		findView();
		initSelectPop();
	}
	private void findView() {
		header = (TextView) findViewById(R.id.header);
		header.setText("上传身份证信息");
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		name_edit = (EditText) findViewById(R.id.name);
		nums_edit = (EditText) findViewById(R.id.nums);
		add = (TextView) findViewById(R.id.add);
		left_img = (ImageView) findViewById(R.id.left);
		right_img = (ImageView) findViewById(R.id.right);
		left_img.setOnClickListener(this);
		right_img.setOnClickListener(this);
		parenView = LayoutInflater.from(this).inflate(R.layout.bind_idcard_layout, null);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.add:
			checkInput();
			break;
		case R.id.left:
			popWindow.showAtLocation(parenView, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.right:
			popWindow.showAtLocation(parenView, Gravity.BOTTOM, 0, 0);
			break;
		default:
			break;
		}
	}
	private void checkInput() {
		name = name_edit.getText().toString();
		idcard_nums = nums_edit.getText().toString();
		if(name.equals("")){
			Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
			return;
		}else if(idcard_nums.equals("")){
			Toast.makeText(this, "请输入身份证", Toast.LENGTH_SHORT).show();
			return;
		}
	}
	private void initSelectPop(){
		popWindow = new PopupWindow(this);
		View view = LayoutInflater.from(this).inflate(R.layout.select_img_pop_layout, null);
		popWindow.setWidth(LayoutParams.MATCH_PARENT);
		popWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setContentView(view);
		view.findViewById(R.id.play_photo).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		view.findViewById(R.id.my_photo).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		view.findViewById(R.id.cancle).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popWindow.dismiss();
			}
		});
	}

}
