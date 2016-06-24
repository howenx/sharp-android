package com.hanmimei.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hanmimei.R;
import com.hanmimei.application.HMMApplication;
import com.hanmimei.entity.ShareVo;
import com.hanmimei.utils.ToastUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

@SuppressLint("SdCardPath") public class ShareWindow extends AlertDialog implements OnClickListener {

	private Activity mActivity;
	private ShareVo vo;

	public ShareWindow(Context context, ShareVo vo) {
		super(context, R.style.BottomShowDialog);
		this.mActivity = (Activity) context;
		this.vo = vo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_layout);
		Config.OpenEditor = true;
		// 添加按钮监听
		findViewById(R.id.qq).setOnClickListener(this);
		findViewById(R.id.weixin).setOnClickListener(this);
		findViewById(R.id.weixinq).setOnClickListener(this);
		findViewById(R.id.sina).setOnClickListener(this);
		findViewById(R.id.copy).setOnClickListener(this);

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

	// 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		dismiss();
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qq:
			share(SHARE_MEDIA.QQ);
			break;
		case R.id.weixin:
			share(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.weixinq:
			share(SHARE_MEDIA.WEIXIN_CIRCLE);
			break;
		case R.id.sina:
			share(SHARE_MEDIA.SINA);
			break;
		case R.id.copy:
			doCopy();
			dismiss();
			break;
		default:
			break;
		}
		dismiss();
	}

	/**
	 * 
	 */
	private void share(SHARE_MEDIA media) {
		if(media == SHARE_MEDIA.SINA){
			new ShareAction(mActivity).setPlatform(media)
			.setCallback(umShareListener)
			.withMedia(new UMImage(mActivity, vo.getImgUrl()))
			.withTitle(vo.getTitle())
			.withText(vo.getContent()+ vo.getTargetUrl())
//			.withExtra(new UMImage(mActivity, vo.getImgUrl()))
			.share();
		}else{
			new ShareAction(mActivity).setPlatform(media)
			.setCallback(umShareListener)
			.withMedia(new UMImage(mActivity, vo.getImgUrl()))
			.withTitle(vo.getContent())
			.withText("我在韩秘美发现了一个不错的商品，赶快来看看吧。")
			.withTargetUrl(vo.getTargetUrl())
			.share();
		}
		
	}
	 /**
	 * 
	 */
//	private void loadImage() {
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				Bitmap bitmap = HttpUtils.getImg(vo.getImgUrl());
//				Message message = mHandler.obtainMessage(1);
//				message.obj = bitmap;
//				mHandler.sendMessage(message);
//			}
//		}).start();
//	}
//	private Handler mHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			UMImage umImage = new UMImage(mActivity, compressImage((Bitmap)msg.obj,"/mnt/sdcard/hanmimei/share"));
//			new ShareAction(mActivity).setPlatform(SHARE_MEDIA.SINA)
//			.setCallback(umShareListener)
////			.withMedia(new UMImage(mActivity, vo.getImgUrl()))
//			.withMedia(umImage)
//			.withTitle(vo.getTitle())
//			.withText(vo.getContent())
//			.withTargetUrl(vo.getTargetUrl())
//			.share();
//		}
//		
//	};


	
	private void doCopy(){
		String[] code = null;
		String url = "";
		if(vo.getType().equals("C")||vo.getType().equals("P")){
			code = vo.getInfoUrl().split("detail");
			url = "https://style.hanmimei.com/detail" + code[1];
		}else if(vo.getType().equals("T")){
			code = vo.getInfoUrl().split("activity");
			url =  "https://style.hanmimei.com/pin/activity" + code[1];
		}
		HMMApplication application = (HMMApplication) mActivity
				.getApplication();
		application.setKouling("KAKAO-HMM 复制这条信息,打开👉韩秘美👈即可看到<"+vo.getType()+">【"
				+ vo.getTitle() + "】," + url + "－🔑 M令 🔑");
		ToastUtils.Toast(mActivity, "复制成功，快去粘贴吧");
	}
	

	// ---------------友盟-----------------------
	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			dismiss();
			ToastUtils.Toast(mActivity, "分享成功");
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			dismiss();
			ToastUtils.Toast(mActivity, "分享失败");
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			dismiss();
//			ToastUtils.Toast(mActivity, "分享取消");
		}
	};

}
