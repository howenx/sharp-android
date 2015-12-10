package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMMAddress;
import com.hanmimei.entity.Result;
import com.hanmimei.utils.HttpUtils;

public class GoodsBalanceAddressActivity extends BaseActivity implements
		OnClickListener {

	private ListView mListView;
	private List<HMMAddress> data;
	private AddressAdapter adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_balance_address_layout);
		
		data = new ArrayList<HMMAddress>();
		adapter = new AddressAdapter(data);
		 mListView = (ListView) findViewById(R.id.mListView);
		 mListView.setAdapter(adapter);
		 loadAddressData();
		 
	}
	
	
	private void loadAddressData(){
		submitTask(new Runnable() {
			
			@Override
			public void run() {
				String result = HttpUtils.getToken(UrlUtil.ADDRESS_LIST_URL, "id-token", getUser().getToken());
				List<HMMAddress> list = DataParser.parserAddressList(result);
				Message msg = mHandler.obtainMessage(1,list);
				mHandler.sendMessage(msg);
				
			}
		});
		
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				List<HMMAddress> list = (List<HMMAddress>) msg.obj;
				if(list != null && list.size() > 0 ){
					findViewById(R.id.no_address).setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					data.clear();
					data.addAll(list);
					adapter.notifyDataSetChanged();
				}else{
					findViewById(R.id.no_address).setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}
		}
		
	};
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	private class AddressAdapter extends BaseAdapter {

		private List<HMMAddress> adresses;

		public AddressAdapter(List<HMMAddress> data) {
			this.adresses = data;
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
				convertView = getLayoutInflater().inflate(
						R.layout.adress_list_item, null);
				holder = new ViewHolder(convertView);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText("收货人：" + adress.getName());
			holder.phone.setText("联系电话：" + adress.getPhone());
			holder.id_card.setText("身份证号：" + adress.getIdCard());
			holder.adress.setText("收货地址：" + adress.getCity() + "  "
					+ adress.getAdress());
			if (adress.isDefault()) {
				holder.isDefault.setVisibility(View.VISIBLE);
			} else {
				holder.isDefault.setVisibility(View.GONE);
			}
			return convertView;
		}

		private class ViewHolder {
			private TextView name;
			private TextView phone;
			private TextView adress;
			private TextView id_card;
			private ImageView isDefault;

			public ViewHolder(View view) {
				this.name = (TextView) view.findViewById(R.id.name);
				this.phone = (TextView) view.findViewById(R.id.phone);
				this.adress = (TextView) view.findViewById(R.id.adress);
				this.id_card = (TextView) view.findViewById(R.id.idCard);
				this.isDefault = (ImageView) view.findViewById(R.id.isDefault);
			}

		}

	}

}
