package com.kakao.kakaogift.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.login.LoginActivity;
import com.kakao.kakaogift.activity.mine.config.SettingActivity;
import com.kakao.kakaogift.activity.mine.message.MessageTypeActivity;

public class ActionBarUtil {

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 */
	public static void setActionBarStyle(Context context, String title) {
		setActionBarStyle(context, title, 0, true, null, null, 0, false);
	}

	/**
	 * 
	 * @param context
	 * @param img
	 *            标题
	 */
	public static void setActionBarStyle(Context context, int img) {
		setActionBarStyle(context, null, img, true, null, null, 0, false);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param bl
	 *            返回按钮的响应事件
	 */
	public static void setActionBarStyle(Context context, String title,
			OnClickListener bl) {
		setActionBarStyle(context, title, 0, true, bl, null, 0, false);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param img
	 *            右侧按钮的图片
	 * @param isBack
	 *            是否显示返回按钮
	 * @param l
	 *            右侧按钮的响应事件
	 * @return
	 */
	public static View setActionBarStyle(Context context, String title,
			int img, Boolean isBack, OnClickListener l) {
		return setActionBarStyle(context, title, img, isBack, null, l, 0, false);
	}

	public static View setActionBarStyle(Context context, String title,
			int img, Boolean isBack, OnClickListener l, int color) {
		return setActionBarStyle(context, title, img, isBack, null, l, color,
				false);
	}

	public static View setActionBarStyle(Context context, String title,
			int img, OnClickListener l) {
		return setActionBarStyle(context, title, img, true, null, l, 0, false);
	}

	public static View setActionBarStyle(Context context, String title,
			int img, OnClickListener l, int colorRes) {
		return setActionBarStyle(context, title, img, true, null, l, colorRes,
				false);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param img
	 *            右侧按钮图片
	 * @param isBack
	 *            是否显示返回按钮
	 * @param bl
	 *            返回按钮的响应事件
	 * @param l
	 *            右侧按钮的响应事件
	 * @return
	 */
	public static View setActionBarStyle(Context context, String title,
			int img, Boolean isBack, OnClickListener bl, OnClickListener l) {
		return setActionBarStyle(context, title, img, isBack, bl, l, 0, false);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param img
	 *            右侧按钮图片
	 * @param isBack
	 *            是否显示返回按钮
	 * @param bl
	 *            返回按钮的响应事件
	 * @param l
	 *            右侧按钮的响应事件
	 * @param colorRes
	 *            背景颜色
	 * @return
	 */
	public static View setActionBarStyle(Context context, String title,
			int imgRes, Boolean isBack, OnClickListener bl, OnClickListener sl,
			int colorRes, boolean isShowLogo) {
		final AppCompatActivity activity = (AppCompatActivity) context;
		ActionBar actionbar = activity.getSupportActionBar();
		actionbar.show();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.actionbar_custom_layout);
		View view = actionbar.getCustomView();
		TextView titleView = (TextView) view.findViewById(R.id.header);
		ImageView btn_back = (ImageView) view.findViewById(R.id.back);
		ImageView logoView = (ImageView) view.findViewById(R.id.logo);
		ImageView btn_setting = (ImageView) view.findViewById(R.id.setting);
		if (colorRes != 0) {
			view.setBackgroundResource(colorRes);
		}
		if (isBack) {
			btn_back.setVisibility(View.VISIBLE);
			if (bl != null) {
				btn_back.setOnClickListener(bl);
			} else {
				btn_back.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						activity.finish();
					}
				});
			}
		}
		titleView.setText(title);
		if (isShowLogo) {
			logoView.setVisibility(View.VISIBLE);
			titleView.setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.logo).setVisibility(View.GONE);
			view.findViewById(R.id.header).setVisibility(View.VISIBLE);
		}
		if (imgRes != 0) {
			btn_setting.setVisibility(View.VISIBLE);
			btn_setting.setImageResource(imgRes);
		}
		if (sl != null)
			btn_setting.setOnClickListener(sl);
		return view;
	}

	/**
	 * 对首页actionbar初始化使用
	 * 
	 * @param context
	 * @param view
	 * @param position
	 * @return
	 */
	public static ImageView initMainActionBarStyle(final BaseActivity context,
			View view, final int position) {
		View actionbarView = view.findViewById(R.id.actionbarView);
		actionbarView.setVisibility(View.VISIBLE);
		actionbarView.setBackgroundResource(R.color.yellow);
		ImageView setting = (ImageView) view.findViewById(R.id.setting);
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position == 0) {
					if (context.getUser() == null) {
						context.startActivity(new Intent(context,
								LoginActivity.class));
					} else {
						context.startActivity(new Intent(context,
								MessageTypeActivity.class));
					}
				}
			}
		});
		if (position == 0) {
			view.findViewById(R.id.header).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.logo).setVisibility(View.VISIBLE);

			setting.setVisibility(View.VISIBLE);
			setting.setImageResource(R.drawable.hmm_icon_message_n);

		} else if (position == 3) {
			view.findViewById(R.id.header).setVisibility(View.VISIBLE);
			view.findViewById(R.id.logo).setVisibility(View.INVISIBLE);
			TextView header = (TextView) view.findViewById(R.id.header);
			header.setText("购物车");
			setting.setVisibility(View.INVISIBLE);
		} else if (position == 4) {
			view.findViewById(R.id.header).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.logo).setVisibility(View.INVISIBLE);
			setting.setVisibility(View.GONE);
//			setting.setImageResource(R.drawable.hmm_icon_setting);

		} else if (position == 1) {
			view.findViewById(R.id.header).setVisibility(View.VISIBLE);
			view.findViewById(R.id.logo).setVisibility(View.INVISIBLE);
			TextView header = (TextView) view.findViewById(R.id.header);
			header.setText("拼购");
			setting.setVisibility(View.INVISIBLE);

		} else if (position == 2) {
			view.findViewById(R.id.header).setVisibility(View.VISIBLE);
			view.findViewById(R.id.logo).setVisibility(View.INVISIBLE);
			TextView header = (TextView) view.findViewById(R.id.header);
			header.setText("礼品");
			setting.setVisibility(View.INVISIBLE);
		}

		return setting;
	}

}
