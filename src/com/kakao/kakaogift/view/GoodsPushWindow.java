package com.kakao.kakaogift.view;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.adapter.GoodsPushAdapter;
import com.kakao.kakaogift.adapter.GoodsPushAdapter.OnRecyclerItemClickListener;
import com.kakao.kakaogift.entity.HGoodsVo;
import com.kakao.kakaogift.override.SyLinearLayoutManager;

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

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.more_grid);
		recyclerView.setHasFixedSize(true);
		SyLinearLayoutManager layoutManager = new SyLinearLayoutManager(
				mActivity,LinearLayoutManager.HORIZONTAL,false);
		//画横线
//		recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL_LIST));
		recyclerView.setLayoutManager(layoutManager);

		GoodsPushAdapter  adapter = new GoodsPushAdapter(mActivity,push);
		adapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
			
			@Override
			public void onItemClick(int position) {
				Intent intent = null;
				if (push.get(position).getItemType().equals("pin")) {
					intent = new Intent(mActivity, PingouDetailActivity.class);
				} else {
					intent = new Intent(mActivity, GoodsDetailActivity.class);
				}
				intent.putExtra("url", push.get(position).getItemUrl());
				mActivity.startActivityForResult(intent, 1);
			}
		});
		recyclerView.setAdapter(adapter);
		
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
