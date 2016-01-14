package com.hanmimei.activity;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hanmimei.wheel.widget.OnWheelChangedListener;
import com.hanmimei.wheel.widget.WheelView;
import com.hanmimei.wheel.widget.adapter.ArrayWheelAdapter;
import com.umeng.analytics.MobclickAgent;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

@SuppressLint("NewApi") 
public class EditAdressActivity extends BaseActivity implements OnClickListener,OnWheelChangedListener{
	
//	private TextView header;
//	private ImageView back;
	private EditText name_edit;
	private EditText phone_edit;
	private EditText idCard_edit;
	private TextView city_edit;
	private EditText adress_edit;
	private TextView add_adre;
	private ToggleButton check_box;
	private String name;
	private String phone;
	private String city;
	private String address;
	private String idCard;
	private int isDefaut = 0;
	private HMMAddress old_Adress;
	private int isWhat;
	private JSONObject object;
	private HMMAddress new_Adress;
	//
	private View popView;
	private View parenView;
	private PopupWindow pop;
	
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	
	private ProgressDialog dialog;
	private User user;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.edit_adress_layout);
//		getActionBar().hide();
		ActionBarUtil.setActionBarStyle(this, "管理地址");
		new_Adress = new HMMAddress();
		user = getUser();
		findView();
		isWhat = getIntent().getIntExtra("isWhat", 0);
		if(isWhat == 1){
			old_Adress = (HMMAddress) getIntent().getExtras().get("address");
			initView();
		}
		initWheel();
		setUpViews();
		setUpListener();
		setUpData();
	}
	//设置地区选择器
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
		mCurrentProviceEnName = mProvinceEnDatas[pCurrent];
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
	//初始化控件
	private void setUpViews() {
		mViewProvince = (WheelView) popView.findViewById(R.id.id_province);
		mViewCity = (WheelView) popView.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) popView.findViewById(R.id.id_district);
	}
	//初始化数据
	private void initView() {
		add_adre.setText("保存修改");
		name_edit.setText(old_Adress.getName());
		phone_edit.setText(old_Adress.getPhone());
		city_edit.setText(old_Adress.getCity());
		adress_edit.setText(old_Adress.getAdress());
		idCard_edit.setText(old_Adress.getIdCard());
		if(old_Adress.isDefault()){
			isDefaut = 1;
			check_box.setToggleOn();
			check_box.setClickable(false);
		}else{
			isDefaut = 0;
			check_box.setToggleOff();
		}
	}
	//初始化控件
	private void findView() {
		add_adre = (TextView) findViewById(R.id.add);
		name_edit = (EditText) findViewById(R.id.name);
		phone_edit = (EditText) findViewById(R.id.phone);
		city_edit = (TextView) findViewById(R.id.city);
		adress_edit = (EditText) findViewById(R.id.address);
		idCard_edit = (EditText) findViewById(R.id.card);
		check_box = (ToggleButton) findViewById(R.id.btn_dufault);
		check_box.setOnToggleChanged(new OnToggleChanged() {
			
			@Override
			public void onToggle(boolean on) {
				if(on){
					isDefaut = 1;
				}else{
					isDefaut = 0;
				}
			}
		});
		add_adre.setOnClickListener(this);
		findViewById(R.id.show).setOnClickListener(this);
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
		case R.id.show:
			//关闭键盘
			CommonUtil.closeBoardIfShow(this);
			parenView = LayoutInflater.from(this).inflate(R.layout.edit_adress_layout, null);
			pop.showAtLocation(parenView, Gravity.BOTTOM, 0, 0);
			break;
		default:
			break;
		}
	}
	//地区选择器 popwindow初始化
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
	//根据选择设置地区
	private void setEditCity() {
		city_edit.setText(mCurrentProviceName+"  "+mCurrentCityName+"  "+mCurrentDistrictName);
	}

	//检查输入
	private void checkInfo(){
		name = name_edit.getText().toString();
		phone = phone_edit.getText().toString();
		city = city_edit.getText().toString();
		address = adress_edit.getText().toString();
		idCard = idCard_edit.getText().toString();
		
		if(!CommonUtil.inputIsName(name,2,15).equals("")){
			Toast.makeText(this, "姓名"+ CommonUtil.inputIsName(name,2,15), Toast.LENGTH_SHORT).show();
			return;
		}else if(phone.equals("")){
			Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
			return;
		}else if(city.equals("")){
			Toast.makeText(this, "请选择省市区", Toast.LENGTH_SHORT).show();
			return;
		}else if(!CommonUtil.inputIsName(address,5,50).equals("")){
			Toast.makeText(this, "地址"+ CommonUtil.inputIsName(address,5,50), Toast.LENGTH_SHORT).show();
			return;
		}else if(!CommonUtil.isPhoneNum(phone)){
			Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			return;
		}else if(!CommonUtil.IDCardValidate(idCard).equals("")){
			Toast.makeText(this, CommonUtil.IDCardValidate(idCard), Toast.LENGTH_SHORT).show();
			return;
		}else{
			toObject();
			addNewAdress();
		}
	}
	
	//封装json，用于请求参数
	private void toObject() {
		object = new JSONObject();
		try {
			if(isWhat == 1)
				object.put("addId", old_Adress.getAdress_id());
			JSONObject cityObject = new JSONObject();
			cityObject.put("province", mCurrentProviceName);
			cityObject.put("city", mCurrentCityName);
			cityObject.put("area", mCurrentDistrictName);
			cityObject.put("area_code", "");
			cityObject.put("city_code", "");
			cityObject.put("province_code", mCurrentProviceEnName);
			object.put("tel", phone);
			object.put("name", name);
			object.put("deliveryCity", cityObject.toString());
			object.put("deliveryDetail", address);
			object.put("orDefault", isDefaut);
			object.put("idCardNum", idCard);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	//增加或是更新请求网络
	private void addNewAdress() {
		dialog = CommonUtil.dialog(this, "正在提交，请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result ;
				if(isWhat == 1){
					result = HttpUtils.post(UrlUtil.ADDRESS_UPDATE_URL, object,"id-token", user.getToken());
				}else{
					result = HttpUtils.post(UrlUtil.ADDRESS_ADD_URL, object,"id-token", user.getToken());;
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
					new_Adress.setIdCard(idCard);
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
	
	//地区选择器 滑动改变的方法
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
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("EditAdressActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("EditAdressActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
	    MobclickAgent.onPause(this);
	}

}
