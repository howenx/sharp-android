package com.hanmimei.activity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.data.XmlParserHandler;
import com.hanmimei.entity.HMMAddress;
import com.hanmimei.entity.Result;
import com.hanmimei.entity.User;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.wheel.entity.CityModel;
import com.hanmimei.wheel.entity.DistrictModel;
import com.hanmimei.wheel.entity.ProvinceModel;
import com.hanmimei.wheel.widget.OnWheelChangedListener;
import com.hanmimei.wheel.widget.WheelView;
import com.hanmimei.wheel.widget.adapter.ArrayWheelAdapter;
import com.umeng.analytics.MobclickAgent;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

@SuppressLint("NewApi")
public class EditAdressActivity extends BaseActivity implements
		OnClickListener, OnWheelChangedListener {

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
	private boolean cityUp = false;

	/**
	 * 所有省
	 */
	private String[] mProvinceDatas;
	private String[] mProvinceEnDatas;
	/**
	 * key - 省 value - 市
	 */
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	private Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	private Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	private String mCurrentProviceName;
	/**
	 * 当前省的简称
	 */
	private String mCurrentProviceEnName;
	/**
	 * 当前市的名称
	 */
	private String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	private String mCurrentDistrictName = "";

	
	private boolean phoneFirstDel = true;
	private boolean cardFirstDel = true;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.edit_adress_layout);
		ActionBarUtil.setActionBarStyle(this, "管理地址");
		new_Adress = new HMMAddress();
		user = getUser();
		findView();
		isWhat = getIntent().getIntExtra("isWhat", 0);
		initProvinceDatas();
		initWheel();
		setUpViews();
		setUpListener();
		setUpData();
		if (isWhat == 1) {
			old_Adress = (HMMAddress) getIntent().getExtras().get("address");
			initView();
		}
	}

	// 设置地区选择器
	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(this,
				mProvinceDatas));
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
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[mViewDistrict.getCurrentItem()];
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

	// 初始化控件
	private void setUpViews() {
		mViewProvince = (WheelView) popView.findViewById(R.id.id_province);
		mViewCity = (WheelView) popView.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) popView.findViewById(R.id.id_district);
	}

	// 初始化数据
	private void initView() {
		add_adre.setText("保存修改");
		name_edit.setText(old_Adress.getName());
		phone_edit.setText(old_Adress.getPhone().substring(0, 3)
				+ "****"
				+ old_Adress.getPhone().substring(7,
						old_Adress.getPhone().length()));
		city_edit.setText(old_Adress.getCity());
		adress_edit.setText(old_Adress.getAdress());
		idCard_edit.setText(old_Adress.getIdCard().substring(0, 5)
				+ "********"
				+ old_Adress.getIdCard().substring(14,
						old_Adress.getIdCard().length()));
		if (old_Adress.isDefault()) {
			isDefaut = 1;
			check_box.setToggleOn();
			check_box.setClickable(false);
		} else {
			isDefaut = 0;
			check_box.setToggleOff();
		}
		phone_edit.addTextChangedListener(new TextWatcher() {
			int startLength;
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				phone_up = true;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				startLength = s.length();
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(phoneFirstDel){
					if(s.length() < startLength){
						phone_edit.setText("");
						phoneFirstDel = false;
					}
				}
			}
		});
		idCard_edit.addTextChangedListener(new TextWatcher() {
			int startLength ;
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				idcard_up = true;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				startLength = s.length();
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(cardFirstDel){
					if(s.length() < startLength){
						idCard_edit.setText("");
						cardFirstDel = false;
					}
				}
			}
		});

	}

	// 初始化控件
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
				if (on) {
					isDefaut = 1;
				} else {
					isDefaut = 0;
				}
			}
		});
		add_adre.setOnClickListener(this);
		findViewById(R.id.show).setOnClickListener(this);

	}

	private boolean idcard_up = false;
	private boolean phone_up = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			checkInfo();
			break;
		case R.id.show:
			// 关闭键盘
			CommonUtil.closeBoardIfShow(this);
			parenView = LayoutInflater.from(this).inflate(
					R.layout.edit_adress_layout, null);
			pop.showAtLocation(parenView, Gravity.BOTTOM, 0, 0);
			break;
		default:
			break;
		}
	}

	// 地区选择器 popwindow初始化
	private void initWheel() {
		pop = new PopupWindow(this);
		popView = LayoutInflater.from(this).inflate(R.layout.pop_wheel_layout,
				null);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new ColorDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(popView);
		popView.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						pop.dismiss();
					}
				});
		popView.findViewById(R.id.sure).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						cityUp = true;
						setEditCity();
						pop.dismiss();
					}
				});
	}

	// 根据选择设置地区
	private void setEditCity() {
		city_edit.setText(mCurrentProviceName + "  " + mCurrentCityName + "  "
				+ mCurrentDistrictName);
	}

	// 检查输入
	private void checkInfo() {
		if(isWhat != 1){
			phone_up = true;
			idcard_up = true;
		}
		name = name_edit.getText().toString();
		if (phone_up) {
			phone = phone_edit.getText().toString();
		} else {
			phone = old_Adress.getPhone();
		}
		if (idcard_up) {
			idCard = idCard_edit.getText().toString();
		} else {
			idCard = old_Adress.getIdCard();
		}
		city = city_edit.getText().toString();
		address = adress_edit.getText().toString();

		if (!CommonUtil.inputIsName(name, 2, 15).equals("")) {
			ToastUtils.Toast(this, "姓名" + CommonUtil.inputIsName(name, 2, 15));
			return;
		} else if (phone.equals("")) {
			ToastUtils.Toast(this, "请输入手机号");
			return;
		}  else if (!CommonUtil.isPhoneNum(phone)) {
			ToastUtils.Toast(this, "请输入正确的手机号");
			return;
		} else if (!CommonUtil.IDCardValidate(idCard.toLowerCase()).equals("")) {
			ToastUtils.Toast(this, CommonUtil.IDCardValidate(idCard.toLowerCase()));
			return;
		} else if (city.equals("")) {
			ToastUtils.Toast(this, "请选择省市区");
			return;
		} else if (!CommonUtil.inputIsName(address, 5, 50).equals("")) {
			ToastUtils.Toast(this,
					"地址" + CommonUtil.inputIsName(address, 5, 50));
			return;
		} else {
			toObject();
			addNewAdress();
		}
	}

	// 封装json，用于请求参数
	private void toObject() {
		object = new JSONObject();
		try {
			if (isWhat == 1)
				object.put("addId", old_Adress.getAdress_id());
			JSONObject cityObject = new JSONObject();
			if(cityUp){
				cityObject.put("province", mCurrentProviceName);
				cityObject.put("city", mCurrentCityName);
				cityObject.put("area", mCurrentDistrictName);
				cityObject.put("province_code", mCurrentProviceEnName);
				cityObject.put("area_code", "");
				cityObject.put("city_code", "");
				object.put("deliveryCity", cityObject.toString());
			}
			object.put("tel", phone);
			object.put("name", name);
			object.put("deliveryDetail", address);
			object.put("orDefault", isDefaut);
			object.put("idCardNum", idCard);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// 增加或是更新请求网络
	private void addNewAdress() {
		dialog = CommonUtil.dialog(this, "正在提交，请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result;
				if (isWhat == 1) {
					result = HttpUtils.post(UrlUtil.ADDRESS_UPDATE_URL, object,
							"id-token", user.getToken());
				} else {
					result = HttpUtils.post(UrlUtil.ADDRESS_ADD_URL, object,
							"id-token", user.getToken());
					;
				}
				Result mResult = DataParser.parserResult(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = mResult;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Result result = (Result) msg.obj;
				if (result.getCode() == 200) {
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
//					if (isWhat == 1) {
//						setResult(AppConstant.ADR_UP_SU, intent);
//					} else {
						setResult(AppConstant.ADR_ADD_SU, intent);
//					}
					finish();
				} else {
					ToastUtils.Toast(EditAdressActivity.this, result.getCode()
							+ "   " + result.getMessage());
				}

				dialog.dismiss();
				break;

			default:
				break;
			}
		}

	};

	// 地区选择器 滑动改变的方法
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			// mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}


	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				mCurrentProviceEnName = provinceList.get(0).getEnName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					// mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			mProvinceEnDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				mProvinceEnDatas[i] = provinceList.get(i).getEnName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

}
