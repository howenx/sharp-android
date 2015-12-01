package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.hanmimei.R;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.entity.Adress;
import com.hanmimei.entity.Result;
import com.hanmimei.entity.User;
import com.hanmimei.utils.HttpUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi") public class AdressActivity extends BaseActivity implements OnClickListener{

	private LayoutInflater inflater;
	private TextView header;
	private ImageView back;
	private ListView mListView;
	private TextView addAddress;
	private List<Adress> data;
	private AdressAdapter adapter;
	private Adress adress;
	private int index;
	private JSONObject object;
	private User user;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_address_layout);
		getActionBar().hide();
		user = getUser();
		findView();
		loadData();
		
	}
	
	private void loadData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.getToken("http://172.28.3.18:9004/api/address/list", "id-token", user.getToken());
				List<Adress> list = DataParser.parserAddressList(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = list;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	private void findView() {
		inflater = LayoutInflater.from(this);
		adress = new Adress();
		data = new ArrayList<Adress>();
		adapter = new AdressAdapter(this,data);
		header = (TextView) findViewById(R.id.header);
		header.setText("地址列表");
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.list);
		mListView.setAdapter(adapter);
		addAddress = (TextView) findViewById(R.id.add);
		addAddress.setOnClickListener(this);
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
			adress = (Adress) bundle.getSerializable("address");
			addNewAddress();
		}else if(resultCode == AppConstant.ADR_UP_SU){
			Bundle bundle = intent.getExtras();
			adress = (Adress) bundle.getSerializable("address");
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
				List<Adress> list = (List<Adress>) msg.obj;
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
					data.remove(index);
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		}
		
	};
	private class AdressAdapter extends BaseAdapter{

		private List<Adress> adresses;
		public AdressAdapter(Context mContext,List<Adress> data){
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
			final Adress adress = adresses.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.adress_list_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				holder.adress = (TextView) convertView.findViewById(R.id.adress);
				holder.del = (TextView) convertView.findViewById(R.id.del);
				holder.update = (TextView) convertView.findViewById(R.id.update);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText(adress.getName());
			holder.phone.setText(adress.getPhone());
			holder.adress.setText(adress.getCity() + "  " + adress.getAdress());
			holder.del.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View arg0) {
					toObject(adress);
				}
			});
			holder.update.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					index = position;
					Intent intent = new Intent(AdressActivity.this,EditAdressActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("address", adress);
					intent.putExtras(bundle);
					intent.putExtra("isWhat", 1);
					AdressActivity.this.startActivityForResult(intent, AppConstant.ADR_UP_SU);
				}
			});
			return convertView;
		}
		private class ViewHolder{
			private TextView name;
			private TextView phone;
			private TextView city;
			private TextView adress;
			private TextView del;
			private TextView update;
		}
		
	}


	private void toObject(Adress adress) {
		object = new JSONObject();
		try {
			object.put("addId", adress.getAdress_id());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		delAddress();
	}

	private void delAddress() {
		new Thread(new Runnable() {	
			@Override
			public void run() {
				String result = HttpUtils.post("http://172.28.3.18:9004/api/address/del", object, "id-token",user.getToken());
				Result mResult = DataParser.parserResult(result);
				Message msg = mHandler.obtainMessage(2);
				msg.obj = mResult;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

}
