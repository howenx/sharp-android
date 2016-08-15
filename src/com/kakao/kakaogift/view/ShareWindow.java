package com.kakao.kakaogift.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.dialog.widget.base.BottomBaseDialog;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.application.KKApplication;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.ShareVo;
import com.kakao.kakaogift.utils.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

@SuppressLint("SdCardPath") 
public class ShareWindow extends BottomBaseDialog<ShareWindow> implements OnClickListener {

	private Activity mActivity;
	private ShareVo vo;
	
	private View qq,weixin,weixinq,sina,copy;

	public ShareWindow(Context context) {
		super(context);
		this.mActivity = (Activity) context;
	}
	
	public ShareWindow(Context context, ShareVo vo) {
		super(context,((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content));
		this.mActivity = (Activity) context;
		this.vo = vo;
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
			.withText("我在KakaoGift发现了一个不错的礼物，赶快来看看吧")
			.withTargetUrl(vo.getTargetUrl())
			.share();
		}
		
	}
	
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
			url = UrlUtil.SERVERY6 + "/detail" + code[1];
		}else if(vo.getType().equals("T")){
			code = vo.getInfoUrl().split("activity");
			url = UrlUtil.SERVERY6 + "/pin/activity" + code[1];
		}
		KKApplication application = (KKApplication) mActivity
				.getApplication();
		application.setKouling("复制这条信息，打开👉KakaoGift👈立即购买<"+vo.getType()+">【"
				+ vo.getContent()+ "】" + url);
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

	/* (non-Javadoc)
	 * @see com.flyco.dialog.widget.base.BaseDialog#onCreateView()
	 */
	@Override
	public View onCreateView() {
		// TODO Auto-generated method stub
		View inflate = View.inflate(mContext, R.layout.share_layout, null);
		qq = inflate.findViewById(R.id.qq);
		weixin = inflate.findViewById(R.id.weixin);
		weixinq= inflate.findViewById(R.id.weixinq);
		sina= inflate.findViewById(R.id.sina);
		copy = inflate.findViewById(R.id.copy);
		return inflate;
	}
	/* (non-Javadoc)
	 * @see com.flyco.dialog.widget.base.BaseDialog#setUiBeforShow()
	 */
	@Override
	public void setUiBeforShow() {
		// TODO Auto-generated method stub
		qq.setOnClickListener(this);
		weixin.setOnClickListener(this);
		weixinq.setOnClickListener(this);
		sina.setOnClickListener(this);
		copy.setOnClickListener(this);
	}

	private BaseAnimatorSet mWindowInAs;
    private BaseAnimatorSet mWindowOutAs;

    @Override
    protected BaseAnimatorSet getWindowInAs() {
        if (mWindowInAs == null) {
            mWindowInAs = new WindowsInAs();
        }
        return mWindowInAs;
    }

    @Override
    protected BaseAnimatorSet getWindowOutAs() {
        if (mWindowOutAs == null) {
            mWindowOutAs = new WindowsOutAs();
        }
        return mWindowOutAs;
    }

    class WindowsInAs extends BaseAnimatorSet {
        @Override
        public void setAnimation(View view) {
//            ObjectAnimator rotationX = ObjectAnimator.ofFloat(view, "rotationX", 10, 0f).setDuration(150);
//            rotationX.setStartDelay(200);
//            animatorSet.playTogether(
//                    ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.8f).setDuration(350),
//                    ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.8f).setDuration(350),
////                    ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f).setDuration(350),
//                    ObjectAnimator.ofFloat(view, "rotationX", 0f, 10).setDuration(200),
//                    rotationX,
//                    ObjectAnimator.ofFloat(view, "translationY", 0, -0.1f * mDisplayMetrics.heightPixels).setDuration(350)
//            );
        }
    }

    class WindowsOutAs extends BaseAnimatorSet {
        @Override
        public void setAnimation(View view) {
//            ObjectAnimator rotationX = ObjectAnimator.ofFloat(view, "rotationX", 10, 0f).setDuration(150);
//            rotationX.setStartDelay(200);
//            animatorSet.playTogether(
//                    ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1.0f).setDuration(350),
//                    ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1.0f).setDuration(350),
////                    ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f).setDuration(350),
//                    ObjectAnimator.ofFloat(view, "rotationX", 0f, 10).setDuration(200),
//                    rotationX,
//                    ObjectAnimator.ofFloat(view, "translationY", -0.1f * mDisplayMetrics.heightPixels, 0).setDuration(350)
//            );
        }
    }
	
}
