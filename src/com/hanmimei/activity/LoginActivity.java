package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.hanmimei.R;
import com.hanmimei.application.HMMApplication;
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
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author eric
 *
 */
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
	private boolean isPwdShow = false;
	private ImageView clear_phone;
	private ImageView clear_pwd;
	private ImageView show_pwd;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.login_layout);
		ActionBarUtil.setActionBarStyle(this, "账号登录");
		initView();
		registerReceivers();
	}

	private void initView() {
		phone_edit = (EditText) findViewById(R.id.phone_num);
		pwd_edit = (TextView) findViewById(R.id.pwd);
		forget = (TextView) findViewById(R.id.forget);
		login = (TextView) findViewById(R.id.login);
		regist = (TextView) findViewById(R.id.regist);
		attention = (TextView) findViewById(R.id.attention);
		clear_phone = (ImageView) findViewById(R.id.clear_phone);
		clear_pwd = (ImageView) findViewById(R.id.clear_pwd);
		show_pwd = (ImageView) findViewById(R.id.show_pwd);
		forget.setOnClickListener(this);
		login.setOnClickListener(this);
		regist.setOnClickListener(this);
		clear_phone.setOnClickListener(this);
		clear_pwd.setOnClickListener(this);
		show_pwd.setOnClickListener(this);
		findViewById(R.id.qq).setOnClickListener(this);
		findViewById(R.id.weixin).setOnClickListener(this);
		findViewById(R.id.sina).setOnClickListener(this);
		userDao = getDaoSession().getUserDao();
		goodsDao = getDaoSession().getShoppingGoodsDao();
		phone_edit.addTextChangedListener(phoneWatcher);
		pwd_edit.addTextChangedListener(pwdWatcher);
		User loginUser = userDao.queryBuilder().build().unique();
		if (loginUser != null) {
			phone_edit.setText(loginUser.getPhone());
			Selection.setSelection((Spannable) phone_edit.getText(), phone_edit
					.getText().toString().length());
		}
	}

	// 手机号输入的监听
	TextWatcher phoneWatcher = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (s.length() != 0) {
				clear_phone.setVisibility(View.VISIBLE);
			} else {
				clear_phone.setVisibility(View.INVISIBLE);
			}
		}

	};
	// 密码输入的监听
	TextWatcher pwdWatcher = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (s.length() != 0) {
				clear_pwd.setVisibility(View.VISIBLE);
			} else {
				clear_pwd.setVisibility(View.INVISIBLE);
			}
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forget:
			CommonUtil.doJump(this, ForgetPwdActivity.class);
			break;
		case R.id.login:
			// 关闭键盘
			CommonUtil.closeBoardIfShow(this);
			checkInput();
			break;
		case R.id.regist:
			CommonUtil.doJump(this, CheckPhoneActivity.class);
			break;
		case R.id.refresh:
			loadImg();
			break;
		case R.id.besure:
			code = codeEditText.getText().toString();
			code_attention.setVisibility(View.GONE);
			// 检查图形校验码的格式
			if (code.length() != 4 || !CommonUtil.isJiaoYan(code)) {
				code_attention.setText("验证码格式不正确");
				code_attention.setVisibility(View.VISIBLE);
				return;
			} else {
				imgDialog.dismiss();
				isDialogShow = false;
				doLogin();
			}
			break;
		case R.id.cancle:
			imgDialog.dismiss();
			isDialogShow = false;
			break;
		// 清空手机号的输入
		case R.id.clear_phone:
			phone_edit.setText("");
			break;
		// 清空输入的密码
		case R.id.clear_pwd:
			pwd_edit.setText("");
			break;
		// 密码的隐藏显示
		case R.id.show_pwd:
			if (!isPwdShow) {
				show_pwd.setImageDrawable(getResources().getDrawable(
						R.drawable.hmm_login_eye_open));
				isPwdShow = true;
				pwd_edit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				Selection.setSelection((Spannable) pwd_edit.getText(), pwd_edit
						.getText().toString().length());
			} else {
				show_pwd.setImageDrawable(getResources().getDrawable(
						R.drawable.hmm_login_eye_close));
				isPwdShow = false;
				pwd_edit.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				Selection.setSelection((Spannable) pwd_edit.getText(), pwd_edit
						.getText().toString().length());
			}
			break;
		case R.id.qq:
			//应用宝上线或添加测试账户
			platform = SHARE_MEDIA.QQ;
			doOtherLogin();
			break;
		case R.id.weixin:
			platform = SHARE_MEDIA.WEIXIN;
			doOtherLogin();
			break;
//		case R.id.sina:
//			platform = SHARE_MEDIA.SINA;
//			doOtherLogin();
//			break;
		default:
			break;
		}
	}

	private void doOtherLogin() {
		mShareAPI = UMShareAPI.get(this);
		mShareAPI.doOauthVerify(this, platform, umAuthListener);
	}
	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action,
				Map<String, String> data) {
//			Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT)
//					.show();
			ToastUtils.Toast(getApplicationContext(),  data.get("openid").toString());
			chekWinxin(data);

		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			ToastUtils.Toast(getApplicationContext(), "登陆失败");
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			ToastUtils.Toast(getApplicationContext(),  "登陆取消");
		}
	};
	private SHARE_MEDIA platform;
	private UMShareAPI mShareAPI = null;

	private void chekWinxin(Map<String, String> data) {
		Http2Utils.doPostRequestTask(this, "", new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				if(result == ""){
					
				}else{
				}
			}
			@Override
			public void onError() {
				ToastUtils.Toast(LoginActivity.this, "登录失败，请检查您的网络");
			}
		});
	}
	@SuppressLint("ShowToast")
	private void loadImg() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Bitmap bitmap = HttpUtils.getImg(UrlUtil.GET_IMG_CODE
						+ Math.round(Math.random() * 1000000));
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
			// setAttention("请输入正确的手机号");
			CommonUtil.setAttention(attention, "请输入正确的手机号");
			return;
		} else if (pwd.length() < 6) {
			CommonUtil.setAttention(attention, "密码至少应为6位");
		} else if (!CommonUtil.isPassWord(pwd)) {
			CommonUtil.setAttention(attention, "密码为数字和字母组合");
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
				params.add(new BasicNameValuePair("phone", phone));
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
				
				code = "-1";
				HMessage result = (HMessage) msg.obj;
				if (result.getCode() != null) {
					if (result.getCode() == 200) {
						User user = new User();
						user.setPhone(phone);
						user.setUserId(0);
						user.setToken(result.getTag());
						user.setIsBind(false);
						user.setExpired(DateUtil.turnToDate(result.getTime()));
						user.setLast_login(DateUtil.getCurrentDate());
						HMMApplication application = (HMMApplication) getApplication();
						application.setLoginUser(user);
						JPushInterface.setAlias(LoginActivity.this, result.getUserId(),null);
						// 登录用户存储到本地sql
						userDao.deleteAll();
						userDao.insert(user);
						if (goodsDao.queryBuilder().list() != null && goodsDao.queryBuilder().list().size() > 0) {
							sendShoppingCar();
						} else {
							sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
						}
					} else if (result.getCode() == 4001) {
						if (!isDialogShow)
							showDialog();
						loadImg();
					} else {
						CommonUtil.setAttention(attention, result.getMessage());
					}
				} else {
					CommonUtil.setAttention(attention, "网络连接异常，请检查网络");
				}
				dialog.dismiss();
				break;
			case 2:
				HMessage hmsg = (HMessage) msg.obj;
				if(hmsg.getCode() == 200){
					goodsDao.deleteAll();
				}
				sendBroadcast(new Intent(
						AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
//				finish();
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

	private List<ShoppingGoods> list;

	private void sendShoppingCar() {
		user = getUser();
		list = goodsDao.queryBuilder().list();
		if (list != null && list.size() > 0) {
			toObject();
			new Thread(new Runnable() {
				@Override
				public void run() {
					String result = HttpUtils.post(UrlUtil.GET_CAR_LIST_URL,
							array, "id-token", user.getToken());
					HMessage hMessage = DataParser.paserResultMsg(result);
					Message msg = mHandler.obtainMessage(2);
					msg.obj = hMessage;
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
				object.put("cartId", 0);
				object.put("skuId", goods.getGoodsId());
				object.put("amount", goods.getGoodsNums());
				object.put("state", goods.getState());
				object.put("skuType", goods.getSkuType());
				object.put("skuTypeId", goods.getSkuTypeId());
				array.put(object);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private ImageView jiaoyanImg;
	private EditText codeEditText;
	private TextView code_attention;

	private void showDialog() {
		isDialogShow = true;
		View view = LayoutInflater.from(this).inflate(R.layout.img_save_layout,
				null);
		imgDialog = new AlertDialog.Builder(this).create();
		imgDialog.setView(view);
		imgDialog.show();
		TextView title = (TextView) view.findViewById(R.id.title);
		jiaoyanImg = (ImageView) view.findViewById(R.id.img);
		codeEditText = (EditText) view.findViewById(R.id.code);
		code_attention = (TextView) view.findViewById(R.id.attention);
		title.setText("安全校验");
		view.findViewById(R.id.cancle).setOnClickListener(this);
		view.findViewById(R.id.besure).setOnClickListener(this);
		view.findViewById(R.id.refresh).setOnClickListener(this);
	}

	private void setDialogImg(Bitmap bitmap) {
		jiaoyanImg.setImageBitmap(bitmap);
	}
	private MyBroadCastReceiver netReceiver;
	private void registerReceivers() {
		netReceiver = new MyBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_QUIT_LOGIN_ACTION);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}
	private class MyBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
				LoginActivity.this.finish();
			}
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(netReceiver);
	}

}
