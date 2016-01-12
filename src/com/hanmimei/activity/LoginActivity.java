package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.application.MyApplication;
import com.hanmimei.dao.ShoppingGoodsDao;
import com.hanmimei.dao.UserDao;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.User;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.DateUtil;
import com.hanmimei.utils.HttpUtils;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText phone_edit;
	private TextView pwd_edit;
	private TextView login;
	private TextView regist;
	private TextView forget;
	private String phone;
	private String pwd;
	private String code = "-1";
	private ProgressDialog dialog;
	private TextView attention;
	private UserDao userDao;
	private ShoppingGoodsDao goodsDao;
	private User user;
	private AlertDialog imgDialog;

	private boolean isDialogShow = false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// getActionBar().hide();
		setContentView(R.layout.login_layout);
		ActionBarUtil.setActionBarStyle(this, "账号登录");
		initView();
	}

	private void initView() {
		phone_edit = (EditText) findViewById(R.id.phone_num);
		pwd_edit = (TextView) findViewById(R.id.pwd);
		forget = (TextView) findViewById(R.id.forget);
		login = (TextView) findViewById(R.id.login);
		regist = (TextView) findViewById(R.id.regist);
		attention = (TextView) findViewById(R.id.attention);
		forget.setOnClickListener(this);
		login.setOnClickListener(this);
		regist.setOnClickListener(this);
		userDao = getDaoSession().getUserDao();
		goodsDao = getDaoSession().getShoppingGoodsDao();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forget:
			CommonUtil.doJump(this, ForgetPhoneActivity.class);
//			Intent intent1 = new Intent(this, CheckPhoneActivity.class);
//			intent1.putExtra("use", "forget");
//			startActivity(intent1);
			// showDialog();
			break;
		case R.id.login:
			checkInput();
			break;
		case R.id.regist:
			CommonUtil.doJump(this, CheckPhoneActivity.class);
//			Intent intent = new Intent(this, CheckPhoneActivity.class);
//			intent.putExtra("use", "regist");
//			startActivity(intent);
			break;
		case R.id.refresh:
			loadImg();
			break;
		case R.id.besure:
			imgDialog.dismiss();
			isDialogShow = false;
			code = codeEditText.getText().toString();
			doLogin();
			break;
		case R.id.cancle:
			imgDialog.dismiss();
			isDialogShow = false;
			break;
		default:
			break;
		}
	}

	@SuppressLint("ShowToast")
	private void loadImg() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Bitmap bitmap = HttpUtils
						.getImg("http://172.28.3.51:9004/getImageCodes/" + Math.round(Math.random()*1000000));
				Message msg = mHandler.obtainMessage(4);
				msg.obj = bitmap;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void checkInput() {
		phone = phone_edit.getText().toString();
		pwd = pwd_edit.getText().toString();
		if (!CommonUtil.isPhoneNum(phone)) {
			// Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			setAttention("请输入正确的手机号");
			return;
		} else if (pwd.equals("")) {
			// Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			setAttention("请输入密码");
			return;
		} else {
			doLogin();
		}
	}

	private void doLogin() {
		dialog = CommonUtil.dialog(this, "正在登录，请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name", phone));
				params.add(new BasicNameValuePair("password", pwd));
				params.add(new BasicNameValuePair("code", code));
				String result = HttpUtils.postCommon(UrlUtil.LOGIN_URL, params);
				HMessage loginInfo = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = loginInfo;
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
				dialog.dismiss();
				code = "-1";
				HMessage result = (HMessage) msg.obj;
				if(result.getCode() != null){
				if (result.getCode() == 200) {
					User user = new User();
					user.setUserId(0);
					user.setToken(result.getTag());
					user.setIsBind(false);
					user.setExpired(DateUtil.turnToDate(result.getTime()));
					user.setLast_login(DateUtil.getCurrentDate());
					MyApplication application = (MyApplication) getApplication();
					application.setLoginUser(user);
					// 登录用户存储到本地sql
					userDao.deleteAll();
					userDao.insert(user);
					if (goodsDao.queryBuilder().build().list() != null
							&& goodsDao.queryBuilder().build().list().size() > 0) {
						sendShoppingCar();
					} else {
						// startActivity(intent);
						sendBroadcast(new Intent(
								AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
						finish();
					}
				} else if (result.getCode() == 4001) {
					// Toast.makeText(LoginActivity.this, result.getMessage(),
					// Toast.LENGTH_SHORT).show();
					// setAttention("提示：" + result.getMessage());
					if (!isDialogShow)
						showDialog();
					loadImg();
				} else {
					 setAttention(result.getMessage());
				}
				}else {
					// Toast.makeText(LoginActivity.this, "网络连接异常，请检查网络",
					// Toast.LENGTH_SHORT).show();
					setAttention("网络连接异常，请检查网络");
				}
				break;
			case 2:
				goodsDao.deleteAll();
				sendBroadcast(new Intent(
						AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
				finish();
				break;
			case 3:
				attention.setVisibility(View.INVISIBLE);
				break;
			case 4:
				Bitmap bitmap = (Bitmap) msg.obj;
				setDialogImg(bitmap);
				break;

			default:
				break;
			}
		}

	};

	private void setAttention(String att) {
		attention.setText(att);
		attention.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					Message msg = mHandler.obtainMessage(3);
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private List<ShoppingGoods> list;

	private void sendShoppingCar() {
		user = getUser();
		list = goodsDao.queryBuilder().build().list();
		if (list != null && list.size() > 0) {
			toObject();
			new Thread(new Runnable() {
				@Override
				public void run() {
					String result = HttpUtils.post(UrlUtil.GET_CAR_LIST_URL,
							array, "id-token", user.getToken());
					Message msg = mHandler.obtainMessage(2);
					mHandler.sendMessage(msg);
				}
			}).start();
		}
	}

	private JSONArray array;

	private void toObject() {
		try {
			array = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				ShoppingGoods goods = list.get(i);
				JSONObject object = new JSONObject();
				object.put("cartId", goods.getCartId());
				object.put("skuId", goods.getGoodsId());
				object.put("amount", goods.getGoodsNums());
				object.put("state", goods.getState());
				array.put(object);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private ImageView jiaoyanImg;
	private EditText codeEditText;

	private void showDialog() {
		isDialogShow = true;
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout,
				null);
		imgDialog = new AlertDialog.Builder(this).create();
		imgDialog.setView(view);
		imgDialog.show();
		TextView title = (TextView) view.findViewById(R.id.title);
		jiaoyanImg = (ImageView) view.findViewById(R.id.img);
		codeEditText = (EditText) view.findViewById(R.id.code);
		view.findViewById(R.id.linear).setVisibility(View.VISIBLE);
		title.setText("安全校验");
		view.findViewById(R.id.cancle).setOnClickListener(this);
		view.findViewById(R.id.besure).setOnClickListener(this);
		view.findViewById(R.id.refresh).setOnClickListener(this);
	}

	private void setDialogImg(Bitmap bitmap) {
		jiaoyanImg.setImageBitmap(bitmap);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("LoginActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("LoginActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
													// onPageEnd 在onPause
													// 之前调用,因为 onPause
													// 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}

}
