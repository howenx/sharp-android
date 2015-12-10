package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.hanmimei.R;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMMAddress;
import com.hanmimei.entity.Result;
import com.hanmimei.entity.User;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.HttpUtils;

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
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi") public class AdressActivity extends BaseActivity implements OnClickListener{

	private LayoutInflater inflater;
	private TextView header;
	private ImageView back;
	private SwipeMenuListView mListView;
	private TextView addAddress;
	private List<HMMAddress> data;
	private AdressAdapter adapter;
	private HMMAddress adress;
	private int index_;
	private int index;
	private JSONObject object;
	private User user;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_address_layout);
		ActionBarUtil.setActionBarStyle(this, "管理地址", 0, true, null);
		user = getUser();
		findView();
		loadData();
		
	}
	private SwipeMenuCreator creator = new SwipeMenuCreator() {

		@Override
		public void create(SwipeMenu menu) {
			
			SwipeMenuItem deleteItem = new SwipeMenuItem(
					getApplicationContext());
			// 设置背景颜色
			deleteItem.setBackground(new ColorDrawable(Color.parseColor("#F9616A")));
			//设置删除的宽度
			deleteItem.setWidth(CommonUtil.dp2px(AdressActivity.this, 90));
			//设置图标
			deleteItem.setIcon(R.drawable.icon_delete);
			//增加到menu中
			menu.addMenuItem(deleteItem);
		}
	};
	private void loadData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.getToken(UrlUtil.ADDRESS_LIST_URL, "id-token", user.getToken());
				List<HMMAddress> list = DataParser.parserAddressList(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = list;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
	private void findView() {
		inflater = LayoutInflater.from(this);
		adress = new HMMAddress();
		data = new ArrayList<HMMAddress>();
		adapter = new AdressAdapter(this,data);
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
				index_ = position;
				toObject(data.get(position));
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				index = position;
				Intent intent = new Intent(AdressActivity.this,EditAdressActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("address", data.get(position));
				intent.putExtras(bundle);
				intent.putExtra("isWhat", 1);
				AdressActivity.this.startActivityForResult(intent, AppConstant.ADR_UP_SU);
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
			Intent intent = new Intent(this,EditAdressActivity.class);
			intent.putExtra("isWhat", 0);
			startActivityForResult(intent, AppConstant.ADR_ADD_SU);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(resultCode == AppConstant.ADR_ADD_SU){
			Bundle bundle = intent.getExtras();
			adress = (HMMAddress) bundle.getSerializable("address");
			addNewAddress();
		}else if(resultCode == AppConstant.ADR_UP_SU){
			Bundle bundle = intent.getExtras();
			adress = (HMMAddress) bundle.getSerializable("address");
			updateAddress();
		}
	}
	private void updateAddress() {
		int id = data.get(index).getAdress_id();
		adress.setAdress_id(id);
		data.remove(index);
		data.add(index, adress);
		adapter.notifyDataSetChanged();
//		loadData();
	}

	private void addNewAddress() {
		data.add(adress);
		adapter.notifyDataSetChanged();
	}

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				List<HMMAddress> list = (List<HMMAddress>) msg.obj;
				if(list != null && list.size() > 0 ){
					data.clear();
					adapter.notifyDataSetChanged();
					data.addAll(list);
					adapter.notifyDataSetChanged();
				}
				break;
			case 2:
				Result result = (Result) msg.obj;
				if(result.getCode() == 200){
					data.remove(index_);
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		}
		
	};
	private class AdressAdapter extends BaseAdapter{

		private List<HMMAddress> adresses;
		public AdressAdapter(Context mContext,List<HMMAddress> data){
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
			if(convertView == null){
				convertView = inflater.inflate(R.layout.adress_list_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				holder.adress = (TextView) convertView.findViewById(R.id.adress);
				holder.id_card = (TextView) convertView.findViewById(R.id.idCard);
				holder.isDefault = (ImageView) convertView.findViewById(R.id.isDefault);
//				holder.del = (TextView) convertView.findViewById(R.id.del);
//				holder.update = (TextView) convertView.findViewById(R.id.update);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText("收货人：" + adress.getName());
			holder.phone.setText("联系电话：" + adress.getPhone());
			holder.id_card.setText("身份证号：" + adress.getIdCard());
			holder.adress.setText("收货地址：" + adress.getCity() + "  " + adress.getAdress());
			if(adress.isDefault()){
				holder.isDefault.setVisibility(View.VISIBLE);
			}else{
				holder.isDefault.setVisibility(View.GONE);
			}
			return convertView;
		}
		private class ViewHolder{
			private TextView name;
			private TextView phone;
			private TextView adress;
			private TextView id_card;
			private ImageView isDefault;
//			private TextView del;
//			private TextView update;
		}
		
	}


	private void toObject(HMMAddress adress) {
		object = new JSONObject();
		try {
			object.put("addId", adress.getAdress_id());
			object.put("orDefault", adress.isDefault());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		delAddress();
	}

	private void delAddress() {
		new Thread(new Runnable() {	
			@Override
			public void run() {
				String result = HttpUtils.post(UrlUtil.ADDRESS_DEL_URL, object, "id-token",user.getToken());
				Result mResult = DataParser.parserResult(result);
				Message msg = mHandler.obtainMessage(2);
				msg.obj = mResult;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

}
