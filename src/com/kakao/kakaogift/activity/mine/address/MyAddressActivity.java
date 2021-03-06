package com.kakao.kakaogift.activity.mine.address;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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

import com.flyco.dialog.widget.NormalDialog;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.FromVo;
import com.kakao.kakaogift.entity.HAddress;
import com.kakao.kakaogift.entity.Result;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.AlertDialogUtils;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.ToastUtils;

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
	private int fromm = FromVo.AboutMyFragment;
	private NormalDialog dialog;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_address_layout);
		ActionBarUtil.setActionBarStyle(this, "地址管理");
		fromm = getIntent().getIntExtra("from", FromVo.AboutMyFragment);
		if (fromm == FromVo.GoodsBalanceActivity){
			selectedId = getIntent().getLongExtra("selectedId", 0);
		}
		findView();
		loadData();

	}
	//VOLLEY框架加载网络数据
	private void loadData() {
		//加载动画
		getLoading().show();
		VolleyHttp.doGetRequestTask( getHeaders(), UrlUtil.ADDRESS_LIST_URL, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				//请求成功
				getLoading().dismiss();
				List<HAddress> list = DataParser.parserAddressList(result);
				data.clear();
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
		mListView.setEmptyView(findViewById(R.id.no_data));
		addAddress = (TextView) findViewById(R.id.add);
		addAddress.setOnClickListener(this);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				showDelDialog(arg2);
				return true;
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (fromm == FromVo.GoodsBalanceActivity) {
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
		if(fromm == FromVo.GoodsBalanceActivity)
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
				holder.isDefault =  convertView
						.findViewById(R.id.isDefault);
				holder.isSelected = (ImageView) convertView
						.findViewById(R.id.isSelected);
				holder.btn_xiugai = convertView.findViewById(R.id.btn_xiugai);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText("姓名：" + adress.getName());
			holder.phone.setText("联系电话：" + CommonUtils.phoneNumPaser(adress.getPhone()));
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
			if (fromm == FromVo.GoodsBalanceActivity) {
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
			private View isDefault;
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
		VolleyHttp.doPostRequestTask2( getHeaders(), UrlUtil.ADDRESS_DEL_URL, new VolleyJsonCallback() {
			
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
