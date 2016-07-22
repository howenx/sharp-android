package com.hanmimei.activity.mine.user;

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
import android.app.AlertDialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanmimei.R;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.User;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.AlertDialogUtils.OnPhotoSelListener;
import com.hanmimei.utils.CommonUtils;
import com.hanmimei.utils.DateUtils;
import com.hanmimei.utils.GlideLoaderTools;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ImageUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.CircleImageView;
import com.hanmimei.view.SexDialog;
import com.hanmimei.view.SexDialog.SexSelectListener;

/**
 * @author eric
 *
 */
@SuppressLint({ "SdCardPath", "InflateParams" })
public class EditUserInfoActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout up_header;
	private RelativeLayout up_name;
	private RelativeLayout up_sex;
	private CircleImageView header;
	private TextView name;
	private TextView sex;
	private TextView phone;

	private String header_str = null;
	private String sex_str = "null";

	private AlertDialog sexDialog, photoDialog;
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
		ActionBarUtil.setActionBarStyle(this, "基本信息");
		oldUser = getUser();
		findView();
		initView();
		initSelectPop();
		initSexWindow();
	}

	// 初始化控件
	private void findView() {
		header = (CircleImageView) findViewById(R.id.headerImg);
		up_header = (RelativeLayout) findViewById(R.id.up_header);
		up_name = (RelativeLayout) findViewById(R.id.up_name);
		up_sex = (RelativeLayout) findViewById(R.id.up_sex);
		name = (TextView) findViewById(R.id.name);
		sex = (TextView) findViewById(R.id.sex);
		phone = (TextView) findViewById(R.id.phone);
		up_header.setOnClickListener(this);
		up_name.setOnClickListener(this);
		up_sex.setOnClickListener(this);
	}

	// 填充数据
	protected void initView() {
		GlideLoaderTools.loadCirlceImage(getActivity(),oldUser.getUserImg(), header);
		name.setText(oldUser.getUserName());
		if (oldUser.getSex().equals("M")) {
			sex.setText("男");
		} else {
			sex.setText("女");
		}
		phone.setText(CommonUtils.phoneNumPaser(oldUser.getPhone()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.up_header:
			photoDialog.show();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.up_sex:
			sexDialog.show();
			break;
		case R.id.up_name:
			Intent intent = new Intent(this, EditUserNameActivity.class);
			intent.putExtra("name", oldUser.getUserName());
			startActivityForResult(intent, AppConstant.UP_USER_NAME_CODE);
			break;
		default:
			break;
		}
	}

	// 更新用户信息到服务器
	private void UpUserInfo() {
		dialog = CommonUtils.dialog(this, "正在修改，请稍后...");
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
			if (what == 1) {
				isHeader = false;
				object.put("gender", sex_str);
			} else if (what == 0) {
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
						if (isHeader) {
							header.setImageDrawable(headerDrawable);
						} else {
							setSex();
						}
						sendBroadcast(new Intent(
								AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
					} else {
						ToastUtils.Toast(EditUserInfoActivity.this,
								"修改失败,请检查您的网络");
					}
				} else {
					ToastUtils.Toast(EditUserInfoActivity.this, "修改失败,请检查您的网络");
				}
				break;

			default:
				break;
			}
		}

	};

	private void setSex() {
		if (sex_str.equals("M")) {
			sex.setText("男");
		} else {
			sex.setText("女");
		}
	}

	// 初始化性别popwindow
	private void initSexWindow() {
		sexDialog = new SexDialog(this, new SexSelectListener() {

			@Override
			public void onClick(String sex) {
				sex_str = sex;
				toObject(1);
			}
		});
	}

	// 初始化选择头像popwindow
	private void initSelectPop() {
		photoDialog = AlertDialogUtils.showPhotoDialog(this, new OnPhotoSelListener() {
			
			@Override
			public void onSelPlay() {
				Intent intentFromCapture = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				if (CommonUtils.hasSdcard()) {

					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									IMAGE_FILE_NAME)));
				}
				startActivityForResult(intentFromCapture,CAMERA_REQUEST_CODE);
			}
			
			@Override
			public void onSelLocal() {
				ImageUtils.getInstance().selectPicture(EditUserInfoActivity.this);
				
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
//				Log.d("tag", "uri=" + uri);
				// 调用裁剪方法
				ImageUtils.getInstance().cropPicture(this, uri);
				break;
			case AppConstant.KITKAT_ABOVE:
				uri = data.getData();
//				Log.d("tag", "uri=" + uri);
				// 先将这个uri转换为path，然后再转换为uri
				String thePath = ImageUtils.getInstance().getPath(this, uri);
				ImageUtils.getInstance().cropPicture(this,
						Uri.fromFile(new File(thePath)));
				break;
			case CAMERA_REQUEST_CODE:
				if (CommonUtils.hasSdcard()) {
					ImageUtils.getInstance().cropPicture(
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
		if (resultCode == AppConstant.UP_USER_NAME_CODE) {
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
			// header.setImageDrawable(drawable);
		}
		return saveBitmapFile(photo);
	}

	private Drawable headerDrawable;

	private static String IMG_PATH = "/mnt/sdcard/hanmimei/header"; // 图片本地存储路径
	private String file_name = ""; // 图片本地存储的名称

	// 将图片保存至本地制定文件夹
	@SuppressLint("SdCardPath")
	private File saveBitmapFile(Bitmap bitmap) {
		file_name = DateUtils.getStringDate() + ".jpg";
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

}
