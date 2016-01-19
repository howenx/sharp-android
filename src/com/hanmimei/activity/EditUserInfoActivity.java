package com.hanmimei.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanmimei.R;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.User;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.DateUtil;
import com.hanmimei.utils.HasSDCardUtil;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ImageLoaderUtils;
import com.hanmimei.utils.ImgUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.RoundImageView;
import com.umeng.analytics.MobclickAgent;

@SuppressLint({ "SdCardPath", "InflateParams" }) 
public class EditUserInfoActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout up_header;
	private RelativeLayout up_name;
	private RelativeLayout up_sex;
	private RoundImageView header;
	private TextView name;
	private TextView sex;
	private TextView phone;
	
	private String header_str = null;
	private String sex_str = "null";

	private PopupWindow popWindow;
	private PopupWindow sexPopupWindow;
	private View parenView;
	private static final String IMAGE_FILE_NAME = "header";
	private static final int CAMERA_REQUEST_CODE = 1;
	private Bitmap photo;

	private User oldUser;
	private JSONObject object;
	private ProgressDialog dialog;
	private boolean isHeader;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_layout);
		ActionBarUtil.setActionBarStyle(this, "修改信息");
		oldUser = getUser();
		findView();
		initView();
		initSelectPop();
		initSexWindow();
	}

	// 初始化控件
	private void findView() {
		header = (RoundImageView) findViewById(R.id.headerImg);
		up_header = (RelativeLayout) findViewById(R.id.up_header);
		up_name = (RelativeLayout) findViewById(R.id.up_name);
		up_sex = (RelativeLayout) findViewById(R.id.up_sex);
		name = (TextView) findViewById(R.id.name);
		sex = (TextView) findViewById(R.id.sex);
		phone = (TextView) findViewById(R.id.phone);
		up_header.setOnClickListener(this);
		up_name.setOnClickListener(this);
		up_sex.setOnClickListener(this);
		parenView = LayoutInflater.from(this).inflate(
				R.layout.user_info_layout, null);
	}

	// 填充数据
	protected void initView() {
		ImageLoaderUtils.loadImage(oldUser.getUserImg(),header);
		name.setText(oldUser.getUserName());
		if (oldUser.getSex().equals("M")) {
			sex.setText("男");
		} else {
			sex.setText("女");
		}
		phone.setText(oldUser.getPhone().substring(0, 3) + "****" + oldUser.getPhone().substring(7, oldUser.getPhone().length()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.up_header:
			popWindow.showAtLocation(parenView, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.back:
			finish();
			break;
		case R.id.up_sex:
			sexPopupWindow.showAtLocation(parenView, Gravity.CENTER, 0, 0);
			break;
		case R.id.up_name:
			Intent intent = new Intent(this,EditUserNameActivity.class);
			intent.putExtra("name", oldUser.getUserName());
			startActivityForResult(intent, AppConstant.UP_USER_NAME_CODE);
			break;
		default:
			break;
		}
	}

	// 更新用户信息到服务器
	private void UpUserInfo() {
		dialog = CommonUtil.dialog(this, "正在修改，请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.post(UrlUtil.UPDATE_USERINFO, object,
						"id-token", oldUser.getToken());
				HMessage hm = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = hm;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	// 封装json，作为请求参数
	private void toObject(int what) {
		try {
			object = new JSONObject();
			if(what == 1){
				isHeader = false;
				object.put("gender", sex_str);
			}else if(what == 0){
				isHeader = true;
				object.put("photoUrl", header_str);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		UpUserInfo();
	}

	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				HMessage hm = (HMessage) msg.obj;
				if (hm.getCode() != null) {
					if (hm.getCode() == 200) {
						if(isHeader){
							header.setImageDrawable(headerDrawable);
						}else{
							setSex();
						}
						sendBroadcast(new Intent(
								AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
					} else {
						ToastUtils.Toast(EditUserInfoActivity.this,"修改失败,请检查您的网络");
					}
				} else {
					ToastUtils.Toast(EditUserInfoActivity.this,"修改失败,请检查您的网络");
				}
				break;

			default:
				break;
			}
		}

	};
	private void setSex(){
		if(sex_str.equals("M")){
			sex.setText("男");
		}else{
			sex.setText("女");
		}
	}

	// 初始化性别popwindow
	@SuppressWarnings("deprecation")
	private void initSexWindow() {
		sexPopupWindow = new PopupWindow(this);
		View view = LayoutInflater.from(this).inflate(
				R.layout.select_sex_pop_layout, null);
		sexPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		sexPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		sexPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		sexPopupWindow.setFocusable(true);
		sexPopupWindow.setOutsideTouchable(true);
		sexPopupWindow.setContentView(view);
		view.findViewById(R.id.men).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sex_str = "M";
				sexPopupWindow.dismiss();
				toObject(1);
			}
		});
		view.findViewById(R.id.women).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sex_str = "F";
				sexPopupWindow.dismiss();
				toObject(1);
			}
		});
	}

	// 初始化选择头像popwindow
	@SuppressWarnings("deprecation")
	private void initSelectPop() {
		popWindow = new PopupWindow(this);
		View view = LayoutInflater.from(this).inflate(
				R.layout.select_img_pop_layout, null);
		popWindow.setWidth(LayoutParams.MATCH_PARENT);
		popWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setContentView(view);
		// 拍照
		view.findViewById(R.id.play_photo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intentFromCapture = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						if (HasSDCardUtil.hasSdcard()) {

							intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(Environment
											.getExternalStorageDirectory(),
											IMAGE_FILE_NAME)));
						}
						startActivityForResult(intentFromCapture,
								CAMERA_REQUEST_CODE);
						popWindow.dismiss();
					}
				});
		// 本地照片
		view.findViewById(R.id.my_photo).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						ImgUtils.getInstance().selectPicture(
								EditUserInfoActivity.this);
						popWindow.dismiss();
					}
				});
		// 取消
		view.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						popWindow.dismiss();
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			Uri uri = null;
			switch (requestCode) {
			case AppConstant.KITKAT_LESS:
				uri = data.getData();
				Log.d("tag", "uri=" + uri);
				// 调用裁剪方法
				ImgUtils.getInstance().cropPicture(this, uri);
				break;
			case AppConstant.KITKAT_ABOVE:
				uri = data.getData();
				Log.d("tag", "uri=" + uri);
				// 先将这个uri转换为path，然后再转换为uri
				String thePath = ImgUtils.getInstance().getPath(this, uri);
				ImgUtils.getInstance().cropPicture(this,
						Uri.fromFile(new File(thePath)));
				break;
			case CAMERA_REQUEST_CODE:
				if (HasSDCardUtil.hasSdcard()) {
					ImgUtils.getInstance().cropPicture(
							this,
							Uri.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									IMAGE_FILE_NAME)));
				} else {
					Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
							.show();
				}
				break;
			case AppConstant.INTENT_CROP:
				File file = null;
				if (data != null)
					file = getFile(data);
				header_str = toBuffer(file);
				toObject(0);
				break;
			}
		}
		if(resultCode == AppConstant.UP_USER_NAME_CODE){
			name.setText(data.getStringExtra("name"));
		}
	}

	// 将文件转成流 base64
	private String toBuffer(File file) {
		byte b[] = null;
		try {
			InputStream in = new FileInputStream(file);

			b = new byte[(int) file.length()];
			try {
				in.read(b);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new String(Base64.encode(b, Base64.NO_WRAP));
	}

	// 得到file 并展示到imageview
	@SuppressWarnings("deprecation")
	private File getFile(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			headerDrawable = new BitmapDrawable(photo);
//			header.setImageDrawable(drawable);
		}
		return saveBitmapFile(photo);
	}
	private Drawable headerDrawable;

	private static String IMG_PATH = "/mnt/sdcard/hanmimei/header"; // 图片本地存储路径
	private String file_name = ""; // 图片本地存储的名称

	// 将图片保存至本地制定文件夹
	@SuppressLint("SdCardPath")
	private File saveBitmapFile(Bitmap bitmap) {
		file_name = DateUtil.getStringDate() + ".jpg";
		File file = new File(IMG_PATH + "/" + file_name);// 将要保存图片的路径
		File mWorkingPath = new File(IMG_PATH);
		// 路径不存在就创建
		if (!mWorkingPath.exists()) {
			if (!mWorkingPath.mkdirs()) {

			}
		}
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("EditUserInfoActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("EditUserInfoActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
															// onPageEnd
															// 在onPause 之前调用,因为
															// onPause
															// 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}

}
