package com.hanmimei.activity.mine.message;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.PushMessageType;
import com.hanmimei.entity.PushMessageTypeInfo;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.manager.MessageMenager;
import com.hanmimei.override.OnGetMessageListener;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.DateUtils;
import com.hanmimei.utils.ToastUtils;

/**
 * @author eric
 *
 */
public class MessageTypeActivity extends BaseActivity implements OnClickListener {

	private TextView tishi1;
	private TextView tishi2;
	private TextView tishi3;
	private TextView tishi4;
	private TextView tishi5;
	private TextView time1;
	private TextView time2;
	private TextView time3;
	private TextView time4;
	private TextView time5;
	private RelativeLayout sys_msg;
	private RelativeLayout goods_msg;
	private RelativeLayout youhui_msg;
	private RelativeLayout wuliu_msg;
	private RelativeLayout zichan_msg;
	private View no_data;
	private ScrollView mScrollView;
	
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_message_layout);
		ActionBarUtil.setActionBarStyle(this, "消息盒子",this);
		registerReceivers();
		findView();
		loadData();
	}

	private void findView() {
		tishi1 = (TextView) findViewById(R.id.tishi1);
		tishi2 = (TextView) findViewById(R.id.tishi2);
		tishi3 = (TextView) findViewById(R.id.tishi3);
		tishi4 = (TextView) findViewById(R.id.tishi4);
		tishi5 = (TextView) findViewById(R.id.tishi5);
		time1 = (TextView) findViewById(R.id.time1);
		time2 = (TextView) findViewById(R.id.time2);
		time3 = (TextView) findViewById(R.id.time3);
		time4 = (TextView) findViewById(R.id.time4);
		time5 = (TextView) findViewById(R.id.time5);
		sys_msg = (RelativeLayout) findViewById(R.id.sys_msg);
		goods_msg = (RelativeLayout) findViewById(R.id.good_msg);
		youhui_msg = (RelativeLayout) findViewById(R.id.ac_msg);
		wuliu_msg = (RelativeLayout) findViewById(R.id.wuliu_msg);
		zichan_msg = (RelativeLayout) findViewById(R.id.zichan_msg);
		no_data =  findViewById(R.id.no_data);
		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		findViewById(R.id.sys_msg).setOnClickListener(this);
		findViewById(R.id.good_msg).setOnClickListener(this);
		findViewById(R.id.wuliu_msg).setOnClickListener(this);
		findViewById(R.id.zichan_msg).setOnClickListener(this);
		findViewById(R.id.ac_msg).setOnClickListener(this);
	}

	private void loadData() {
		getLoading().show();
		VolleyHttp.doGetRequestTask( getHeaders(), UrlUtil.GET_MSG_TYPE, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				getLoading().dismiss();
				PushMessageTypeInfo typeInfo = DataParser.parseMsgType(result);
				if(typeInfo.getMessage().getCode() == 200){
					initView(typeInfo.getList());
				}else{
					ToastUtils.Toast(MessageTypeActivity.this, typeInfo.getMessage().getMessage());
				}
			}
			
			@Override
			public void onError() {
				getLoading().dismiss();
				ToastUtils.Toast(MessageTypeActivity.this, "请检查您的网络");
			}
		});
	}

	private void initView(List<PushMessageType> type) {
		if(type != null && type.size() > 0){
			for(int i = 0; i < type.size(); i++){
				if(type.get(i).getType().equals("system")){
					sys_msg.setVisibility(View.VISIBLE);
					tishi1.setText(type.get(i).getContent());
					time1.setText(DateUtils.getTimeDiffDesc(DateUtils.getDate(type.get(i).getTime())));
					if(type.get(i).getNum() > 0){
						findViewById(R.id.hasNew1).setVisibility(View.VISIBLE);
					}else{
						findViewById(R.id.hasNew1).setVisibility(View.GONE);
					}
				}else if(type.get(i).getType().equals("discount")){
					youhui_msg.setVisibility(View.VISIBLE);
					tishi5.setText(type.get(i).getContent());
					time5.setText(DateUtils.getTimeDiffDesc(DateUtils.getDate(type.get(i).getTime())));
					if(type.get(i).getNum() > 0){
						findViewById(R.id.hasNew5).setVisibility(View.VISIBLE);
					}else{
						findViewById(R.id.hasNew5).setVisibility(View.GONE);
					}
				}else if(type.get(i).getType().equals("coupon")){
					zichan_msg.setVisibility(View.VISIBLE);
					tishi4.setText(type.get(i).getContent());
					time4.setText(DateUtils.getTimeDiffDesc(DateUtils.getDate(type.get(i).getTime())));
					if(type.get(i).getNum() > 0){
						findViewById(R.id.hasNew4).setVisibility(View.VISIBLE);
					}else{
						findViewById(R.id.hasNew4).setVisibility(View.GONE);
					}
				}else if(type.get(i).getType().equals("logistics")){
					wuliu_msg.setVisibility(View.VISIBLE);
					tishi3.setText(type.get(i).getContent());
					time3.setText(DateUtils.getTimeDiffDesc(DateUtils.getDate(type.get(i).getTime())));
					if(type.get(i).getNum() > 0){
						findViewById(R.id.hasNew3).setVisibility(View.VISIBLE);
					}else{
						findViewById(R.id.hasNew3).setVisibility(View.GONE);
					}
				}else if(type.get(i).getType().equals("goods")){
					goods_msg.setVisibility(View.VISIBLE);
					tishi2.setText(type.get(i).getContent());
					time2.setText(DateUtils.getTimeDiffDesc(DateUtils.getDate(type.get(i).getTime())));
					if(type.get(i).getNum() > 0){
						findViewById(R.id.hasNew2).setVisibility(View.VISIBLE);
					}else{
						findViewById(R.id.hasNew2).setVisibility(View.GONE);
					}
				}
			}
		}else{
			mScrollView.setVisibility(View.GONE);
			no_data.setVisibility(View.VISIBLE);
		}
		
	}
	private void clearAllMsg(){
		sys_msg.setVisibility(View.GONE);
		youhui_msg.setVisibility(View.GONE);
		zichan_msg.setVisibility(View.GONE);
		wuliu_msg.setVisibility(View.GONE);
		goods_msg.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sys_msg:
			doJump("system");
			findViewById(R.id.hasNew1).setVisibility(View.GONE);
			break;
		case R.id.good_msg:
			doJump("goods");
			findViewById(R.id.hasNew2).setVisibility(View.GONE);
			break;
		case R.id.wuliu_msg:
			doJump("logistics");
			findViewById(R.id.hasNew3).setVisibility(View.GONE);
			break;
		case R.id.zichan_msg:
			doJump("coupon");
			findViewById(R.id.hasNew4).setVisibility(View.GONE);
			break;
		case R.id.ac_msg:
			doJump("discount");
			findViewById(R.id.hasNew5).setVisibility(View.GONE);
			break;
		case R.id.back:
			exitClick();
			break;
		default:
			break;
		}
		
	}
	private void doJump(String type){
		Intent intent = new Intent(this,MessageActivity.class);
		intent.putExtra("type", type);
		startActivity(intent);
	}
	private void exitClick() {
		if(findViewById(R.id.hasNew1).getVisibility() == 8 && findViewById(R.id.hasNew2).getVisibility() == 8 && findViewById(R.id.hasNew3).getVisibility()
				== 8 && findViewById(R.id.hasNew4).getVisibility() == 8 && findViewById(R.id.hasNew5).getVisibility() == 8){
			MessageMenager.getInstance().getListener().onGetMessage(R.drawable.hmm_icon_message_n);
		}
		finish();
	}
	private MyBroadCastReceiver myReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		myReceiver = new MyBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_UPDATE_MSG);
		getActivity().registerReceiver(myReceiver, intentFilter);
	}
	private class MyBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_UPDATE_MSG)) {
				clearAllMsg();
					loadData();
			}
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myReceiver);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitClick();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
