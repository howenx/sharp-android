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
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;
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
//	private int index;
	private JSONObject object;
	private User user;
	private long selectedId = 0;

	private int fromm = From.AboutMyFragment;


	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_address_layout);
		ActionBarUtil.setActionBarStyle(this, "管理地址", 0, true, null);
		user = getUser();
		fromm = getIntent().getIntExtra("from", From.AboutMyFragment);
		if (fromm == From.GoodsBalanceActivity){
			selectedId = getIntent().getLongExtra("selectedId", 0);
		}
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
					.parseColor("#e56254")));
			// 设置删除的宽度
			deleteItem.setWidth(CommonUtil.dp2px(AdressActivity.this, 90));
			// 设置图标
			deleteItem.setIcon(R.drawable.icon_delete);
			// 增加到menu中
			menu.addMenuItem(deleteItem);
		}
	};
	//VOLLEY框架加载网络数据
	private void loadData() {
		//加载动画
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.ADDRESS_LIST_URL, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				//请求成功
				getLoading().dismiss();
				List<HMMAddress> list = DataParser.parserAddressList(result);
				data.clear();
				if (list != null && list.size() > 0) {
					data.addAll(sequenceData(list));
				}
				adapter.notifyDataSetChanged();
			}
			@Override
			public void onError() {
				//请求失败
				getLoading().dismiss();
			}
		});
	}

	private void findView() {
		inflater = LayoutInflater.from(this);
		data = new ArrayList<HMMAddress>();
		adapter = new AdressAdapter(this, data);
		header = (TextView) findViewById(R.id.header);
		header.setText("地址列表");
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		mListView = (SwipeMenuListView) findViewById(R.id.list);
		mListView.setAdapter(adapter);
		if(fromm != From.GoodsBalanceActivity)
			mListView.setMenuCreator(creator);
		addAddress = (TextView) findViewById(R.id.add);
		addAddress.setOnClickListener(this);
		//侧滑事件
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
				holder.icon = (ImageView) convertView
						.findViewById(R.id.icon);
				holder.btn_xiugai = convertView.findViewById(R.id.btn_xiugai);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText("姓名：" + adress.getName());
			holder.phone.setText("联系电话：" + adress.getPhone());
			holder.id_card.setText("身份证号：" + adress.getIdCard().substring(0, 5) + "********" + adress.getIdCard().substring(14, adress.getIdCard().length()));
			holder.adress.setText("收货地址：" + adress.getCity() + "  "
					+ adress.getAdress());
			holder.btn_xiugai.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(AdressActivity.this,
							EditAdressActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("address", adress);
					intent.putExtras(bundle);
					intent.putExtra("isWhat", 1);
					AdressActivity.this.startActivityForResult(intent,
							AppConstant.ADR_UP_SU);
				}
			});
			if (adress.isDefault()) {
				holder.isDefault.setVisibility(View.VISIBLE);
			} else {
				holder.isDefault.setVisibility(View.GONE);
			}
			if (fromm == From.GoodsBalanceActivity) {
				holder.icon.setVisibility(View.INVISIBLE);
				if (adress.getAdress_id() == selectedId) {
					holder.isSelected.setVisibility(View.VISIBLE);
				} else {
					holder.isSelected.setVisibility(View.GONE);
				}
			}else{
				holder.icon.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

		private class ViewHolder {
			private TextView name;
			private TextView phone;
			private TextView adress;
			private TextView id_card;
			private TextView isDefault;
			private ImageView isSelected,icon;
			private View btn_xiugai;
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
		Http2Utils.doPostRequestTask2(this, getHeaders(), UrlUtil.ADDRESS_DEL_URL, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				Result mResult = DataParser.parserResult(result);
				if (mResult.getCode() == 200) {
					loadData();
				}else{
					ToastUtils.Toast(AdressActivity.this, "删除失败");
				}
			}
			
			@Override
			public void onError() {
				ToastUtils.Toast(AdressActivity.this, "删除失败");
			}
		},object.toString());
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
