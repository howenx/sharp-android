package com.kakao.kakaogift.activity.goods.pin;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import cn.jpush.a.a.ab;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.CommonUtils;

/**
 * 
 * @author vince
 * 
 */
public class PingouLiuChengActivity extends BaseActivity {
	
	Drawable able;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pingou_liucheng_layout);
		ActionBarUtil.setActionBarStyle(this, "拼团流程");
		ImageView my_image_view = (ImageView) findViewById(R.id.my_image_view);
		 able = getResources().getDrawable(
				R.drawable.hmm_pingou_liucheng);
		
		int height = able.getIntrinsicHeight();
		int width = able.getIntrinsicWidth();
		int faceWidth = CommonUtils.getScreenWidth(getActivity());
		int factHeight = faceWidth * height / width;
		Glide.with(getActivity()).load(R.drawable.hmm_pingou_liucheng)
				.diskCacheStrategy(DiskCacheStrategy.NONE)
				.override(faceWidth, factHeight).into(my_image_view);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	
}
