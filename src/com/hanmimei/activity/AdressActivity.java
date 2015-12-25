package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.hanmimei.R;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.From;
import com.hanmimei.entity.HMMAddress;
import com.hanmimei.entity.Result;
import com.hanmimei.entity.User;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.HttpUtils;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class AdressActivity extends BaseActivity implements OnClickListener {

	private LayoutInflater inflater;
	private TextView header;
	private ImageView back;
	private SwipeMenuListView mListView;
	private TextView addAddress;
	private List<HMMAddress> data;
	private AdressAdapter adapter;
	private int index;
	private JSONObject object;
	private User user;
	private Integer selectedId = 0;

	private int fromm = From.AboutMyFragment;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_address_layout);
		ActionBarUtil.setActionBarStyle(this, "管理地址", 0, true, null);
		user = getUser();
		fromm = getIntent().getIntExtra("from", From.AboutMyFragment);
		if (fromm == From.GoodsBalanceActivity)
			selectedId = getIntent().getIntExtra("selectedId", 0);
		findView();
		loadData();

	}

	private SwipeMenuCreator creator = new SwipeMenuCreator() {

		@Override
		public void create(SwipeMenu menu) {

			SwipeMenuItem deleteItem = new SwipeMenuItem(
					getApplicationContext());
			// 设置背景颜色
			deleteItem.setBackground(new ColorDrawable(Color
					.parseColor("#F9616A")));
			// 设置删除的宽度
			deleteItem.setWidth(CommonUtil.dp2px(AdressActivity.this, 90));
			// 设置图标
			deleteItem.setIcon(R.drawable.icon_delete);
			// 增加到menu中
			menu.addMenuItem(deleteItem);
		}
	};

	private void loadData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.getToken(UrlUtil.ADDRESS_LIST_URL,
						"id-token", user.getToken());
				List<HMMAddress> list = DataParser.parserAddressList(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = list;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void findView() {
		inflater = LayoutInflater.from(this);
//		adress = new HMMAddress();
		data = new ArrayList<HMMAddress>();
		adapter = new AdressAdapter(this, data);
		header = (TextView) findViewById(R.id.header);
		header.setText("地址列表");
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		mListView = (SwipeMenuListView) findViewById(R.id.list);
		mListView.setAdapter(adapter);
		mListView.setMenuCreator(creator);
		addAddress = (TextView) findViewById(R.id.add);
		addAddress.setOnClickListener(this);
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				index = position;
				toObject(data.get(position));
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (fromm == From.GoodsBalanceActivity) {
					selectedId = data.get(position).getAdress_id();
					adapter.notifyDataSetChanged();
					Intent intent = new Intent();
					intent.putExtra("address", data.get(position));
					setResult(1, intent);
					finish();
					return;
				}
				Intent intent = new Intent(AdressActivity.this,
						EditAdressActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("address", data.get(position));
				intent.putExtras(bundle);
				intent.putExtra("isWhat", 1);
				AdressActivity.this.startActivityForResult(intent,
						AppConstant.ADR_UP_SU);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.add:
			Intent intent = new Intent(this, EditAdressActivity.class);
			intent.putExtra("isWhat", 0);
			startActivityForResult(intent, AppConstant.ADR_ADD_SU);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == AppConstant.ADR_ADD_SU) {
			loadData();
		} else if (resultCode == AppConstant.ADR_UP_SU) {
			loadData();
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				List<HMMAddress> list = (List<HMMAddress>) msg.obj;
				if (list != null && list.size() > 0) {
					data.clear();
					adapter.notifyDataSetChanged();
					data.addAll(sequenceData(list));
					adapter.notifyDataSetChanged();
				}
				break;
			case 2:
				Result result = (Result) msg.obj;
				if (result.getCode() == 200) {
					data.remove(index);
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		}

	};
	private List<HMMAddress> sequenceData(List<HMMAddress> list){
		List<HMMAddress> addresses = new ArrayList<HMMAddress>();
		for(int i = 0; i < list.size(); i ++){
			if(list.get(i).isDefault() == true){
				addresses.add(0, list.get(i));
			}else{
				addresses.add(list.get(i));
			}
		}
		return addresses;
		
	}

	private class AdressAdapter extends BaseAdapter {

		private List<HMMAddress> adresses;

		public AdressAdapter(Context mContext, List<HMMAddress> data) {
			adresses = data;
		}

		@Override
		public int getCount() {
			return adresses.size();
		}

		@Override
		public Object getItem(int arg0) {
			return adresses.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			final HMMAddress adress = adresses.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.adress_list_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				holder.adress = (TextView) convertView
						.findViewById(R.id.adress);
				holder.id_card = (TextView) convertView
						.findViewById(R.id.idCard);
				holder.isDefault = (TextView) convertView
						.findViewById(R.id.isDefault);
				holder.isSelected = (ImageView) convertView
						.findViewById(R.id.isSelected);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText("收货人：" + adress.getName());
			holder.phone.setText("联系电话：" + adress.getPhone());
			holder.id_card.setText("身份证号：" + adress.getIdCard().substring(0, 5) + "********" + adress.getIdCard().substring(14, adress.getIdCard().length()));
			holder.adress.setText("收货地址：" + adress.getCity() + "  "
					+ adress.getAdress());
			if (adress.isDefault()) {
				holder.isDefault.setVisibility(View.VISIBLE);
			} else {
				holder.isDefault.setVisibility(View.GONE);
			}
			if (fromm == From.GoodsBalanceActivity) {
				if (adress.getAdress_id().equals(selectedId)) {
					holder.isSelected.setVisibility(View.VISIBLE);
				} else {
					holder.isSelected.setVisibility(View.GONE);
				}
			}

			return convertView;
		}

		private class ViewHolder {
			private TextView name;
			private TextView phone;
			private TextView adress;
			private TextView id_card;
			private TextView isDefault;
			private ImageView isSelected;
		}

	}

	private void toObject(HMMAddress adress) {
		object = new JSONObject();
		try {
			object.put("addId", adress.getAdress_id());
			if(adress.isDefault()){
				object.put("orDefault", 1);
			}else{
				object.put("orDefault", 0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		delAddress();
	}

	private void delAddress() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.post(UrlUtil.ADDRESS_DEL_URL, object,
						"id-token", user.getToken());
				Result mResult = DataParser.parserResult(result);
				Message msg = mHandler.obtainMessage(2);
				msg.obj = mResult;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("AdressActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("AdressActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
	    MobclickAgent.onPause(this);
	}

}
