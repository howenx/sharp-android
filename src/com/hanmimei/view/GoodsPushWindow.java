package com.hanmimei.view;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.hanmimei.R;
import com.hanmimei.activity.goods.detail.GoodsDetailActivity;
import com.hanmimei.activity.goods.pin.PingouDetailActivity;
import com.hanmimei.adapter.GoodsPushAdapter;
import com.hanmimei.entity.HGoodsVo;

public class GoodsPushWindow extends AlertDialog {

	private Activity mActivity;
	private List<HGoodsVo> push;

	public GoodsPushWindow(Context context, List<HGoodsVo> push) {
		super(context, R.style.BottomShowDialog);
		this.mActivity = (Activity) context;
		this.push = push;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_push_layout);

		HorizontalListView more_grid = (HorizontalListView) findViewById(R.id.more_grid);
//		TextView titleView = (TextView) findViewById(R.id.title);
//		titleView.setText(R.string.goods_over_notice);
		more_grid.setAdapter(new GoodsPushAdapter(push, mActivity));
		more_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = null;
				if (push.get(arg2).getItemType().equals("pin")) {
					intent = new Intent(mActivity, PingouDetailActivity.class);
				} else {
					intent = new Intent(mActivity, GoodsDetailActivity.class);
				}
				intent.putExtra("url", push.get(arg2).getItemUrl());
				mActivity.startActivityForResult(intent, 1);
			}
		});
		more_grid.setFocusable(false);

		Window window = getWindow();
		// 可以在此设置显示动画
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = mActivity.getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		// 设置显示位置
		onWindowAttributesChanged(wl);
	}

}
