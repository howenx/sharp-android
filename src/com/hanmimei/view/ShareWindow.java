package com.hanmimei.view;

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
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.utils.Utility;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class ShareWindow extends AlertDialog implements OnClickListener {

	private Activity mActivity;
	private ShareVo vo;
	/** 微博微博分享接口实例 */
    private IWeiboShareAPI  mWeiboShareAPI;

	public ShareWindow(Context context, ShareVo vo) {
		super(context, R.style.BottomShowDialog);
		this.mActivity = (Activity) context;
		this.vo = vo;
	}
	public ShareWindow(Context context, ShareVo vo, IWeiboShareAPI mWeiboShareAPI) {
		super(context, R.style.BottomShowDialog);
		this.mActivity = (Activity) context;
		this.vo = vo;
		this.mWeiboShareAPI = mWeiboShareAPI;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_layout);
		Config.OpenEditor = true;
		// 添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity

//		if (vo.getType().equals("T")) {
//			findViewById(R.id.qq).setVisibility(View.GONE);
//			findViewById(R.id.weixin).setVisibility(View.GONE);
//			findViewById(R.id.weixinq).setVisibility(View.GONE);
//		} else {
			// 添加按钮监听
			findViewById(R.id.qq).setOnClickListener(this);
			findViewById(R.id.weixin).setOnClickListener(this);
			findViewById(R.id.weixinq).setOnClickListener(this);
			findViewById(R.id.sina).setOnClickListener(this);
//		}

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
			shareQQ();
			break;
		case R.id.weixin:
			shareWeiXin();
			break;
		case R.id.weixinq:
			shareCircle();
			break;
		case R.id.sina:
//			 mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mActivity, AppConstant.WEIBO_APPKEY);
			 if (!mWeiboShareAPI.isWeiboAppInstalled()) {
				 ToastUtils.Toast(mActivity, "请安装新浪微博客户端");
			 }else{
				shareSina();
			 }
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
	private void shareSina() {
//		new ShareAction(mActivity).setPlatform(SHARE_MEDIA.SINA)
//		.setCallback(umShareListener)
//		.withMedia(new UMImage(mActivity, vo.getImgUrl()))
//		.withTitle(vo.getTitle()).withText(vo.getContent())
//		.withTargetUrl(vo.getTargetUrl()).share();
		mWeiboShareAPI.registerApp();;
	     if (mWeiboShareAPI.getWeiboAppSupportAPI() >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
	    	 WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		     weiboMessage.textObject = (TextObject) getTextObj();
		     weiboMessage.imageObject = getImageObj();
		     weiboMessage.mediaObject = getWebpageObj();
	    	 // 2. 初始化从第三方到微博的消息请求
	         SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
	         // 用transaction唯一标识一个请求
	         request.transaction = String.valueOf(System.currentTimeMillis());
	         request.multiMessage = weiboMessage;
	         
	         // 3. 发送请求消息到微博，唤起微博分享界面
	         mWeiboShareAPI.sendRequest(request);
	     }else{
			 WeiboMessage weiboMessage = new WeiboMessage();
		     weiboMessage.mediaObject = getTextObj();
		     weiboMessage.mediaObject = getImageObj();
		     weiboMessage.mediaObject = getWebpageObj();
	    	 // 2. 初始化从第三方到微博的消息请求
		        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		        // 用transaction唯一标识一个请求
		        request.transaction = String.valueOf(System.currentTimeMillis());
		        request.message = weiboMessage;
		        
		        // 3. 发送请求消息到微博，唤起微博分享界面
		        mWeiboShareAPI.sendRequest(request);
	     }
		
	}

	/**
	 * @return
	 */
	private WebpageObject getWebpageObj() {
		 WebpageObject mediaObject = new WebpageObject();
	        mediaObject.identify = Utility.generateGUID();
	        mediaObject.title = vo.getTitle();
	        mediaObject.description = vo.getContent();
	        
	        // 设置 Bitmap 类型的图片到视频对象里
	        mediaObject.actionUrl = vo.getTargetUrl();
	        mediaObject.defaultText = "韩国正品，就来韩秘美";
	        return mediaObject;
	}

	/**
	 * @return
	 */
	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
        imageObject.imagePath = vo.getImgUrl();
        return imageObject;
	}

	/**
	 * @return
	 */
	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
        textObject.text = vo.getTitle();
        return textObject;
	}

	// 微信朋友圈分享设置
	private void shareCircle() {
		new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
				.setCallback(umShareListener)
				.withMedia(new UMImage(mActivity, vo.getImgUrl()))
				.withTitle(vo.getTitle()).withText(vo.getContent())
				.withTargetUrl(vo.getTargetUrl()).share();
	}

	// 微信分享设置
	private void shareWeiXin() {
		new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN)
				.setCallback(umShareListener)
				.withMedia(new UMImage(mActivity, vo.getImgUrl()))
				.withTitle(vo.getTitle()).withText(vo.getContent())
				.withTargetUrl(vo.getTargetUrl()).share();
	}

	// QQ分享设置
	private void shareQQ() {
		new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QQ)
				.setCallback(umShareListener)
				.withMedia(new UMImage(mActivity, vo.getImgUrl()))
				.withTitle(vo.getTitle()).withText(vo.getContent())
				.withTargetUrl(vo.getTargetUrl()).share();
	}
	
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
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			dismiss();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			dismiss();
		}
	};

}
