package com.hanmimei.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.fragment.FragmentTabHost;


public class TabHostManager {
	
	private Context mContext;
	private FragmentTabHost mTabHost;
	private int indaicator;
	
	private static class TabHostManagerHolder {
		public static final TabHostManager instance = new TabHostManager();
	}

	public static TabHostManager getInstance() {
		return TabHostManagerHolder.instance;
	}
	
	/**
	 * 初始化tab管理者
	 * @param mContext
	 * @param mListView
	 */
	public void initTabHostManager(Context mContext,FragmentTabHost mTabHost,int indaicator) {
		this.mContext = mContext;
		this.mTabHost = mTabHost;
		this.indaicator  = indaicator;
	}
	
	
	/**
	 * 初始化tab
	 * @param tag	标签
	 * @param title	tab 标题
	 * @param img	tab 图标
	 * @param clzss	显示fragment 
	 */
	@SuppressLint("InflateParams")
	public void initTabItem(String tag, int img, String title,Class<?> clzss) {
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		View indaicatorView = mInflater.inflate(indaicator, null);
		ImageView tabIcon = (ImageView) indaicatorView.findViewById(R.id.img);
		TextView tabTitle = (TextView) indaicatorView.findViewById(R.id.name);
		if(tabIcon !=null && img!=0)
			tabIcon.setImageResource(img);
		if(tabTitle!=null && title!=null)
			tabTitle.setText(title);
			
			TabSpec tab = mTabHost.newTabSpec(tag).setIndicator(indaicatorView);
			mTabHost.addTab(tab, clzss, null);
	}
	
	
	/**
	 * 初始化tab
	 * @param tag 标签
	 * @param img tab 图标
	 * @param clzss 显示fragment 
	 */
	@SuppressLint("InflateParams")
	public void initImgTabItem(String tag, int img, Class<?> clzss) {
		initTabItem(tag, img, null, clzss);
	}
	
	/**
	 * 初始化tab
	 * @param tag 标签
	 * @param title tab 标题
	 * @param clzss 显示fragment 
	 */
	@SuppressLint("InflateParams")
	public void initTitleTabItem(String tag, String title, Class<?> clzss) {
		initTabItem(tag, 0, title, clzss);
	}
	
	
//	public void setCurrentTab(){
//		mainTabHost.setCurrentTab(mainTabHost.getLastTab());
//	}
//	
//	public void goToHomePage(){
//		mainTabHost.setCurrentTab(0);
//	}
}
