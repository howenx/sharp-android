package com.hanbimei.activity;

import com.hanbimei.R;
import com.hanbimei.fragment.AboutMyFragment;
import com.hanbimei.fragment.HomeFragment;
import com.hanbimei.fragment.ShoppingCartFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


@SuppressLint("NewApi") 
public class MainActivity extends BaseActivity implements OnTabChangeListener{

	private TabHost tabHost;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        inflater = LayoutInflater.from(this);
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        initFragment();
        tabHost.setup();
        tabHost.setOnTabChangedListener(this);
        initTab(TAB_HOME_ID, TAB_HOME, home_drawable);
        initTab(TAB_CAR_ID, TAB_CAR, shopping_drawable);
        initTab(TAB_MY_ID, TAB_MY, my_drawable);
        tabHost.setCurrentTab(0);
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
		ft = fm.beginTransaction();
		/** 如果当前选项卡是home */

		if (tabId.equals(TAB_HOME_ID)) {
			isTabHome();
			/** 如果当前选项卡是shopping */
		} else if (tabId.equals(TAB_CAR_ID)) {
			isTabShopping();
			/** 如果当前选项卡是my*/
		} else if (tabId.equals(TAB_MY_ID)) {
			isTabMy();
		}
		ft.commit();
	}
	private void isTabMy() {
		if(myFragment == null){
			ft.replace(R.id.realtabcontent, new AboutMyFragment(), TAB_HOME_ID);
		}else{
			ft.attach(myFragment);
		}
	}
	private void isTabShopping() {
		if(cartFragment == null){
			ft.replace(R.id.realtabcontent, new ShoppingCartFragment(), TAB_CAR_ID);
		}else{
			ft.attach(cartFragment);
		}
	}
	private void isTabHome() {
		if(homeFragment == null){
			ft.replace(R.id.realtabcontent, new HomeFragment(), TAB_HOME_ID);
		}else{
			ft.attach(homeFragment);
		}
	}
}
