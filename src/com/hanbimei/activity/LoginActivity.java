package com.hanbimei.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hanbimei.R;
import com.hanbimei.application.MyApplication;
import com.hanbimei.dao.ShoppingGoodsDao;
import com.hanbimei.dao.UserDao;
import com.hanbimei.data.AppConstant;
import com.hanbimei.data.DataParser;
import com.hanbimei.entity.Result;
import com.hanbimei.entity.ShoppingGoods;
import com.hanbimei.entity.User;
import com.hanbimei.utils.CommonUtil;
import com.hanbimei.utils.DateUtil;
import com.hanbimei.utils.HttpUtils;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class LoginActivity extends BaseActivity implements OnClickListener {

	private TextView header;
	private EditText phone_edit;
	private TextView pwd_edit;
	private TextView login;
	private TextView regist;
	private TextView forget;
	private String phone;
	private String pwd;
	private ProgressDialog dialog;
	private ImageView back;

	private UserDao userDao;
	private ShoppingGoodsDao goodsDao;
	private User user;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getActionBar().hide();
		setContentView(R.layout.login_layout);
		initView();
	}

	private void initView() {
		header = (TextView) findViewById(R.id.header);
		header.setText("账号登录");
		phone_edit = (EditText) findViewById(R.id.phone_num);
		pwd_edit = (TextView) findViewById(R.id.pwd);
		forget = (TextView) findViewById(R.id.forget);
		login = (TextView) findViewById(R.id.login);
		regist = (TextView) findViewById(R.id.regist);
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
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
			CommonUtil.doJump(this, ForgetPwdActivity.class);
			break;
		case R.id.login:
			checkInput();
			break;
		case R.id.regist:
			CommonUtil.doJump(this, RegistActivity.class);
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	private void checkInput() {
		phone = phone_edit.getText().toString();
		pwd = pwd_edit.getText().toString();
		if (!CommonUtil.isPhoneNum(phone)) {
			Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			return;
		} else if (pwd.equals("")) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
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
				String result = HttpUtils.postCommon(
						"http://172.28.3.18:9004/api/login_user_name", params);
				Result loginInfo = DataParser.parserLoginResult(result);
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
				Result result = (Result) msg.obj;
				if (result.isSuccess() == true) {
					User user = new User();
					user.setUserId(100001);
					user.setUserName("福建一只肖毛驴");
					user.setToken(result.getTag());
					user.setIsBind(false);
					user.setExpired(DateUtil.turnToDate(result.getTime()));
					user.setUserImg("http://img5.duitang.com/uploads/item/201411/30/20141130201227_AfQWu.thumb.700_0.jpeg");
					user.setLast_login(DateUtil.getCurrentDate());
					MyApplication application = (MyApplication) getApplication();
					application.setLoginUser(user);
					// 登录用户存储到本地sql
					userDao.deleteAll();
					userDao.insert(user);
					if(goodsDao.queryBuilder().build().list() != null && goodsDao.queryBuilder().build().list().size() > 0){
						sendShoppingCar();
					}else{
						Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					// startActivity(intent);
						setResult(AppConstant.LOGIN_CODE, intent);
						finish();
					}
				} else if (result.isSuccess() == false) {
					Toast.makeText(LoginActivity.this, result.getMessage(),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(LoginActivity.this, "网络连接异常，请检查网络",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 2:
				goodsDao.deleteAll();
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				// startActivity(intent);
					setResult(AppConstant.LOGIN_CODE, intent);
					finish();
				break;
			default:
				break;
			}
		}

	};
	private List<ShoppingGoods> list;
	private JSONObject obj;
	private void sendShoppingCar() {
		user = getUser();
		list = goodsDao.queryBuilder().build().list();
		if (list != null && list.size() > 0) {
			toObject();
			new Thread(new Runnable() {
				@Override
				public void run() {
					String result = HttpUtils.post("http://172.28.3.18:9003/client/cart", array, "id-token", user.getToken());
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
				for(int i = 0; i < list.size(); i ++){
					ShoppingGoods goods = list.get(i);
					JSONObject object = new JSONObject();
					object.put("cartId", goods.getCartId());
					object.put("skuId", goods.getGoodsId());
					object.put("amount", goods.getGoodsNums());
					object.put("state", goods.getState());
					array.put(object);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
