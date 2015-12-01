package com.hanbimei.activity;

import org.json.JSONException;
import org.json.JSONObject;
import com.hanbimei.R;
import com.hanbimei.data.AppConstant;
import com.hanbimei.data.DataParser;
import com.hanbimei.entity.Adress;
import com.hanbimei.entity.Result;
import com.hanbimei.entity.User;
import com.hanbimei.utils.CommonUtil;
import com.hanbimei.utils.HttpUtils;
import com.hanbimei.wheel.widget.OnWheelChangedListener;
import com.hanbimei.wheel.widget.WheelView;
import com.hanbimei.wheel.widget.adapter.ArrayWheelAdapter;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") 
public class EditAdressActivity extends BaseActivity implements OnClickListener,OnWheelChangedListener{
	
	private TextView header;
	private ImageView back;
	private EditText name_edit;
	private EditText phone_edit;
	private EditText city_edit;
	private EditText adress_edit;
	private TextView add_adre;
	private String name;
	private String phone;
	private String city;
	private String address;
	private Adress old_Adress;
	private int isWhat;
	private JSONObject object;
	private Adress new_Adress;
	//
	private View popView;
	private View parenView;
	private PopupWindow pop;
	private LinearLayout show;
	
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	
	private ProgressDialog dialog;
	private User user;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.edit_adress_layout);
		getActionBar().hide();
		new_Adress = new Adress();
		user = getUser();
		findView();
		isWhat = getIntent().getIntExtra("isWhat", 0);
		if(isWhat == 1){
			old_Adress = (Adress) getIntent().getExtras().get("address");
			initView();
		}
		initWheel();
		setUpViews();
		setUpListener();
		setUpData();
	}
	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}
	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
	private void setUpListener() {
    	// 添加change事件
    	mViewProvince.addChangingListener(this);
    	// 添加change事件
    	mViewCity.addChangingListener(this);
    	// 添加change事件
    	mViewDistrict.addChangingListener(this);
    }
	private void setUpViews() {
		mViewProvince = (WheelView) popView.findViewById(R.id.id_province);
		mViewCity = (WheelView) popView.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) popView.findViewById(R.id.id_district);
	}

	private void initView() {
		add_adre.setText("保存修改");
		name_edit.setText(old_Adress.getName());
		phone_edit.setText(old_Adress.getPhone());
		city_edit.setText(old_Adress.getCity());
		adress_edit.setText(old_Adress.getAdress());
	}

	private void findView() {
		header = (TextView) findViewById(R.id.header);
		header.setText("管理地址");
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		name_edit = (EditText) findViewById(R.id.name);
		phone_edit = (EditText) findViewById(R.id.phone);
		city_edit = (EditText) findViewById(R.id.city);
		adress_edit = (EditText) findViewById(R.id.adress);
		add_adre = (TextView) findViewById(R.id.add);
		add_adre.setOnClickListener(this);
		city_edit.setOnClickListener(this);
		show = (LinearLayout) findViewById(R.id.show);
		show.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.add:
			checkInfo();
			break;
		case R.id.city:
			
		case R.id.show:
			parenView = LayoutInflater.from(this).inflate(R.layout.edit_adress_layout, null);
			pop.showAtLocation(parenView, Gravity.BOTTOM, 0, 0);
			break;
		default:
			break;
		}
	}

	private void initWheel() {
		pop = new PopupWindow(this);
		popView = LayoutInflater.from(this).inflate(R.layout.pop_wheel_layout, null);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(popView);
		popView.findViewById(R.id.cancle).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pop.dismiss();
			}
		});
		popView.findViewById(R.id.sure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setEditCity();
				pop.dismiss();
			}
		});
	}

	private void setEditCity() {
		city_edit.setText(mCurrentProviceName+"  "+mCurrentCityName+"  "+mCurrentDistrictName);
	}

	private void checkInfo(){
		name = name_edit.getText().toString();
		phone = phone_edit.getText().toString();
		city = city_edit.getText().toString();
		address = adress_edit.getText().toString();
		if(name.equals("")){
			Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
			return;
		}else if(phone.equals("")){
			Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
			return;
		}else if(city.equals("")){
			Toast.makeText(this, "请输入区域", Toast.LENGTH_SHORT).show();
			return;
		}else if(address.equals("")){
			Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
			return;
		}else if(!CommonUtil.isPhoneNum(phone)){
			Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			return;
		}else{
			toObject();
			addNewAdress();
		}
	}
	private void toObject() {
		object = new JSONObject();
		try {
			if(isWhat == 1)
				object.put("addId", old_Adress.getAdress_id());
			object.put("tel", phone);
			object.put("name", name);
			object.put("deliveryCity", city);
			object.put("deliveryDetail", address);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	private void addNewAdress() {
		dialog = CommonUtil.dialog(this, "正在提交，请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result ;
				if(isWhat == 1){
					result = HttpUtils.post("http://172.28.3.18:9004/api/address/update", object,"id-token", user.getToken());
				}else{
					result = HttpUtils.post("http://172.28.3.18:9004/api/address/add", object,"id-token", user.getToken());;
				}
				Result mResult = DataParser.parserResult(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = mResult;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Result result = (Result) msg.obj;
				if(result.getCode() == 200){
					new_Adress.setAdress_id(result.getResult_id());
					new_Adress.setName(name);
					new_Adress.setCity(city);
					new_Adress.setAdress(address);
					new_Adress.setPhone(phone);
					Bundle bundle = new Bundle();
					bundle.putSerializable("address", new_Adress);
					Intent intent = new Intent();
					intent.putExtras(bundle);
					if(isWhat == 1){
						setResult(AppConstant.ADR_UP_SU, intent);
					}else{
						setResult(AppConstant.ADR_ADD_SU, intent);
					}
					finish();
				}else{
					Toast.makeText(EditAdressActivity.this, result.getCode() + "   " + result.getMessage(), Toast.LENGTH_SHORT).show();
				}

				dialog.dismiss();
				break;

			default:
				break;
			}
		}
		
	};

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

}
