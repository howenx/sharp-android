package com.hanmimei.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;

import com.hanmimei.R;

public class ListViewUtils {

	// listview添加加载动画
	public static void setListViewAnim(Context mContext, ListView mListView) {
		Animation animation = (Animation) AnimationUtils.loadAnimation(
				mContext, R.anim.list_anim);
		LayoutAnimationController lac = new LayoutAnimationController(animation);
		lac.setDelay(0.3f); // 设置动画间隔时间
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL); // 设置列表的显示顺序
		mListView.setLayoutAnimation(lac); // 为ListView 添加动画
	}
	
}
