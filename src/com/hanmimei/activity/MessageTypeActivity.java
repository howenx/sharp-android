package com.hanmimei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.MessageType;
import com.hanmimei.manager.MessageMenager;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;

public class MessageTypeActivity extends BaseActivity implements OnClickListener {

	private TextView tishi1;
	private TextView tishi2;
	private TextView tishi3;
	private TextView tishi4;
	private TextView tishi5;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_message_layout);
		ActionBarUtil.setActionBarStyle(this, "消息盒子");
		MessageMenager.getInstance().setMsgDrawble(getResources().getDrawable(R.drawable.icon_xiaoxi));
		tishi1 = (TextView) findViewById(R.id.tishi1);
		tishi2 = (TextView) findViewById(R.id.tishi2);
		tishi3 = (TextView) findViewById(R.id.tishi3);
		tishi4 = (TextView) findViewById(R.id.tishi4);
		tishi5 = (TextView) findViewById(R.id.tishi5);
		findViewById(R.id.sys_msg).setOnClickListener(this);
		findViewById(R.id.good_msg).setOnClickListener(this);
		findViewById(R.id.wuliu_msg).setOnClickListener(this);
		findViewById(R.id.zichan_msg).setOnClickListener(this);
		findViewById(R.id.ac_msg).setOnClickListener(this);
		loadData();
	}

	private void loadData() {
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.GET_MSG_TYPE, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				MessageType type = DataParser.parseMsgType(result);
				if(type.getCode() == 200){
					initView(type);
				}else{
					ToastUtils.Toast(MessageTypeActivity.this, type.getMessage());
				}
			}
			
			@Override
			public void onError() {
				ToastUtils.Toast(MessageTypeActivity.this, "请检查您的网络");
			}
		});
	}

	private void initView(MessageType type) {
		if(type.getSysNum() != 0){
			tishi1.setText("您有" + type.getSysNum() + "条新的消息");
		}
		if(type.getWuliuNum() != 0){
			tishi3.setText("您有" + type.getWuliuNum() + "条新的消息");
		}
		if(type.getGoodNum() != 0){
			tishi2.setText("您有" + type.getGoodNum()+ "条新的消息");
		}
		if(type.getHuodongNum() != 0){
			tishi5.setText("您有" + type.getHuodongNum()+ "条新的消息");
		}
		if(type.getZichanNum() != 0){
			tishi4.setText("您有" + type.getZichanNum()+ "条新的消息");
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this,MessageActivity.class);
		switch (v.getId()) {
		case R.id.sys_msg:
			intent.putExtra("type", "system");
			tishi1.setVisibility(View.GONE);
			break;
		case R.id.good_msg:
			intent.putExtra("type", "goods");
			tishi2.setVisibility(View.GONE);
			break;
		case R.id.wuliu_msg:
			intent.putExtra("type", "logistics");
			tishi3.setVisibility(View.GONE);
			break;
		case R.id.zichan_msg:
			intent.putExtra("type", "coupon");
			tishi4.setVisibility(View.GONE);
			break;
		case R.id.ac_msg:
			intent.putExtra("type", "discount");
			tishi5.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		startActivity(intent);
	}
	

}
