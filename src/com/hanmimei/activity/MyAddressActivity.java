package com.hanmimei.activity;

import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.From;
import com.hanmimei.entity.HAddress;
import com.hanmimei.entity.Result;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;

/**
 * @author eric
 *
 */
@SuppressLint("NewApi")
public class MyAddressActivity extends BaseActivity implements OnClickListener {

	private ListView mListView;
	private TextView addAddress;
	private List<HAddress> data;
	private AdressAdapter adapter;
	private JSONObject object;
	private long selectedId = 0;
	private int fromm = From.AboutMyFragment;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_address_layout);
		ActionBarUtil.setActionBarStyle(this, "地址管理");
		fromm = getIntent().getIntExtra("from", From.AboutMyFragment);
		if (fromm == From.GoodsBalanceActivity){
			selectedId = getIntent().getLongExtra("selectedId", 0);
		}
		findView();
		loadData();

	}
	//VOLLEY框架加载网络数据
	private void loadData() {
		//加载动画
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.ADDRESS_LIST_URL, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				//请求成功
				getLoading().dismiss();
				List<HAddress> list = DataParser.parserAddressList(result);
				data.clear();
				//自我排序
//				if (list != null && list.size() > 0) {
//					data.addAll(sequenceData(list));
//				}
				data.addAll(list);
				adapter.notifyDataSetChanged();
			}
			@Override
			public void onError() {
				getLoading().dismiss();
			}
		});
	}

	private void findView() {
		data = new ArrayList<HAddress>();
		adapter = new AdressAdapter(this, data);
		mListView = (ListView) findViewById(R.id.list);
		mListView.setAdapter(adapter);
		addAddress = (TextView) findViewById(R.id.add);
		addAddress.setOnClickListener(this);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
//				toObject(arg2);
				showDelDialog(arg2);
				return true;
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
			}
		});
	}
	
	/**
	 * @param arg2
	 */
	private void showDelDialog(final int arg2) {
		if(fromm == From.GoodsBalanceActivity)
			return;
		dialog = AlertDialogUtils.showDialog(this, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				toObject(arg2);
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		}
	}

//	private List<HMMAddress> sequenceData(List<HMMAddress> list){
//		List<HMMAddress> addresses = new ArrayList<HMMAddress>();
//		for(int i = 0; i < list.size(); i ++){
//			if(list.get(i).isDefault() == true){
//				addresses.add(0, list.get(i));
//			}else{
//				addresses.add(list.get(i));
//			}
//		}
//		return addresses;
//		
//	}

	private class AdressAdapter extends BaseAdapter {

		private List<HAddress> adresses;

		public AdressAdapter(Context mContext, List<HAddress> data) {
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
			final HAddress adress = adresses.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.adress_list_item, null);
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
				holder.btn_xiugai = convertView.findViewById(R.id.btn_xiugai);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText("姓名：" + adress.getName());
			holder.phone.setText("联系电话：" + adress.getPhone().substring(0, 3) + "****" + adress.getPhone().substring(7, adress.getPhone().length()));
			holder.id_card.setText("身份证号：" + adress.getIdCard().substring(0, 5) + "********" + adress.getIdCard().substring(14, adress.getIdCard().length()));
			holder.adress.setText("收货地址：" + adress.getCity() + "  "
					+ adress.getAdress());
			holder.btn_xiugai.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(MyAddressActivity.this,EditAdressActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("address", adress);
					intent.putExtras(bundle);
					intent.putExtra("isWhat", 1);
					MyAddressActivity.this.startActivityForResult(intent,
							AppConstant.ADR_UP_SU);
				}
			});
			if (adress.isDefault()) {
				holder.isDefault.setVisibility(View.VISIBLE);
			} else {
				holder.isDefault.setVisibility(View.GONE);
			}
			if (fromm == From.GoodsBalanceActivity) {
				if (adress.getAdress_id() == selectedId) {
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
			private View btn_xiugai;
		}

	}

	private void toObject(int position) {
		HAddress adress = data.get(position);
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
		delAddress(position);
	}
	
	private void delAddress(final int position) {
		dialog.dismiss();
		getLoading().show();
		Http2Utils.doPostRequestTask2(this, getHeaders(), UrlUtil.ADDRESS_DEL_URL, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				Result mResult = DataParser.parserResult(result);
				if (mResult.getCode() == 200) {
					data.remove(position);
					adapter.notifyDataSetChanged();
				}else{
					ToastUtils.Toast(MyAddressActivity.this, "删除失败");
				}
				getLoading().dismiss();
			}
			
			@Override
			public void onError() {
				getLoading().dismiss();
				ToastUtils.Toast(MyAddressActivity.this, "删除失败");
			}
		},object.toString());
	}

}
