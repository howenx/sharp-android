package com.hanbimei.activity;

import java.util.ArrayList;
import java.util.List;

import com.hanbimei.R;
import com.hanbimei.application.MyApplication;
import com.hanbimei.dao.ShoppingGoodsDao;
import com.hanbimei.dao.UserDao;
import com.hanbimei.entity.ShoppingCar;
import com.hanbimei.entity.ShoppingGoods;
import com.hanbimei.entity.User;
import com.hanbimei.utils.DateUtil;
import com.hanbimei.utils.SharedPreferencesUtil;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

@SuppressLint("NewApi")
public class FirstShowActivity extends BaseActivity {

	private static final String FIRST = "first";
	private static final String FIRST_LOG_FLAG = "first_log_flag";
	private User user;
	private UserDao userDao;
	private MyApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.first_show_layout);
		//判断是否自动登录，以及更新token
		loginUser();
		initShoppingCar();
		//判断是否是第一次进入app
		SharedPreferencesUtil util = new SharedPreferencesUtil(
				FirstShowActivity.this, FIRST);
		String flag = util.getString(FIRST_LOG_FLAG);
		if (flag == null) {
			util.putString(FIRST_LOG_FLAG, "not_first");
			startActivity(new Intent(FirstShowActivity.this,
					ViewPagerActivity.class));
			finish();
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(3000);
						mHandler.obtainMessage(1).sendToTarget();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	private void initShoppingCar() {
		ShoppingGoodsDao goodsDao = getDaoSession().getShoppingGoodsDao();
		List<ShoppingGoods> list = new ArrayList<ShoppingGoods>();
		for(int i = 0; i < 5; i ++){
			list.add(new ShoppingGoods(500321, 100243, "", "", "超级爽肤补水100ml经典还是的的", 59, "G", 2, ""));
		}
		goodsDao.deleteAll();
		goodsDao.insertInTx(list);
	}

	private void loginUser() {
		userDao = getDaoSession().getUserDao();
		user = userDao.queryBuilder().build().unique();
		if(user != null){
		int difDay = DateUtil.getDate(user.getExpired());
		if(difDay < 24 && difDay >=0){
			getNewToken();
		}else if(difDay <0){
			
		}else{
			application = (MyApplication) getApplication();
			application.setLoginUser(user);
		}
		}
	}

	private void getNewToken() {
		// TODO Auto-generated method stub
		
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				startActivity(new Intent(FirstShowActivity.this,
						MainActivity.class));
				finish();
				break;

			default:
				break;
			}
		}

	};
}
