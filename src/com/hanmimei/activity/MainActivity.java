package com.hanmimei.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.fragment.AboutMyFragment;
import com.hanmimei.fragment.FragmentTabHost;
import com.hanmimei.fragment.HomeFragment;
import com.hanmimei.fragment.ShoppingCartFragment;
import com.hanmimei.manager.TabHostManager;
import com.hanmimei.utils.DoJumpUtils;


@SuppressLint("NewApi") 
public class MainActivity extends BaseActivity implements OnTabChangeListener, OnClickListener{

	private TabHost tabHost;
	private TextView header;
	private ImageView setting;
	private LayoutInflater inflater;
	private static final String TAB_HOME_ID = "tab_01";
	private static final String TAB_CAR_ID = "tab_02";
	private static final String TAB_MY_ID = "tab_03";
	private static final String TAB_HOME = "首页";
	private static final String TAB_CAR = "购物车";
	private static final String TAB_MY = "我的";
	private static final int home_drawable = R.drawable.tab_home;
	private static final int shopping_drawable = R.drawable.tab_shopping;
	private static final int my_drawable = R.drawable.tab_my;
	private HomeFragment homeFragment;
	private ShoppingCartFragment cartFragment;
	private AboutMyFragment myFragment;
	private FragmentManager fm;
	private FragmentTransaction ft;
	
	private FragmentTabHost mTabHost;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
//        inflater = LayoutInflater.from(this);
//        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        header = (TextView) findViewById(R.id.header);
        setting = (ImageView) findViewById(R.id.setting);
        setting.setOnClickListener(this);
//        initFragment();
//        tabHost.setup();
//        tabHost.setOnTabChangedListener(this);
//        initTab(TAB_HOME_ID, TAB_HOME, home_drawable);
//        initTab(TAB_CAR_ID, TAB_CAR, shopping_drawable);
//        initTab(TAB_MY_ID, TAB_MY, my_drawable);
//        tabHost.setCurrentTab(0);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
    	mTabHost.setup(this, getSupportFragmentManager(), R.id.realcontent);
		mTabHost.setOnTabChangedListener(this);
        TabHostManager.getInstance().initTabHostManager(this, mTabHost, R.layout.tab_item_layout);
        TabHostManager.getInstance().initTabItem(TAB_HOME_ID, home_drawable, TAB_HOME, HomeFragment.class);
        TabHostManager.getInstance().initTabItem(TAB_CAR_ID, shopping_drawable, TAB_CAR, ShoppingCartFragment.class);
        TabHostManager.getInstance().initTabItem(TAB_MY_ID, my_drawable, TAB_MY, AboutMyFragment  .class);
        
    }
	private void initFragment() {
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		homeFragment = (HomeFragment) fm.findFragmentByTag(TAB_HOME_ID);
		cartFragment = (ShoppingCartFragment) fm.findFragmentByTag(TAB_CAR_ID);
		myFragment = (AboutMyFragment) fm.findFragmentByTag(TAB_MY_ID);
		ft.commit();
	}
	private void initTab(String tab_id, String tab_name, int drawableId) {
		View view = inflater.inflate(R.layout.tab_item_layout, null);
		ImageView img = (ImageView) view.findViewById(R.id.img);
		TextView name = (TextView) view.findViewById(R.id.name);
		Drawable drawable = getResources().getDrawable(drawableId);
		img.setImageDrawable(drawable);
		name.setText(tab_name);
		TabSpec tabSpec = tabHost.newTabSpec(tab_id);
		tabSpec.setIndicator(view);
		tabSpec.setContent(android.R.id.tabcontent);
		tabHost.addTab(tabSpec);
	}
	@Override
	public void onTabChanged(String tabId) {
		/** 如果当前选项卡是home */
		if (tabId.equals(TAB_HOME_ID)) {
			setting.setVisibility(View.GONE);
			header.setText("韩秘美");
			/** 如果当前选项卡是shopping */
		} else if (tabId.equals(TAB_CAR_ID)) {
			setting.setVisibility(View.GONE);
			header.setText("购物车");
			/** 如果当前选项卡是my*/
		} else if (tabId.equals(TAB_MY_ID)) {
			setting.setVisibility(View.VISIBLE);
			header.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			DoJumpUtils.doJump(this,SettingActivity.class);
			break;

		default:
			break;
		}
	}

	//	主界面返回之后在后台运行
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            moveTaskToBack(false);  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }
	
}
