//package com.hanmimei.activity;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import org.json.JSONObject;
//
//import com.hanmimei.R;
//import com.hanmimei.data.AppConstant;
//import com.hanmimei.data.DataParser;
//import com.hanmimei.entity.Result;
//import com.hanmimei.entity.User;
//import com.hanmimei.utils.DateUtil;
//import com.hanmimei.utils.HasSDCardUtil;
//import com.hanmimei.utils.HttpUtils;
//import com.hanmimei.utils.ImgUtils;
//
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.MediaStore;
//import android.util.Base64;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ImageView.ScaleType;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//@SuppressLint({ "NewApi", "SdCardPath", "InflateParams" })
//public class IdCardActivity extends BaseActivity implements OnClickListener {
//
//	private TextView header;
//	private ImageView back;
//	private EditText name_edit;
//	private EditText nums_edit;
//	private TextView add;
//	private ImageView left_img;
//	private ImageView right_img;
//	private ProgressDialog dialog;
//	private JSONObject object = new JSONObject();
//	private String name;
//	private String idcard_nums;
//	//
//	private PopupWindow popWindow;
//	private View parenView;
//
//	private static final String IMAGE_FILE_NAME = "idCard";
//	private static final int CAMERA_REQUEST_CODE = 1;
//	private Bitmap photo;
//	private boolean isLeft = true;
//	private String left_String = "";
//	private String right_String = "";
//	
//	private User user;
//
//	@Override
//	protected void onCreate(Bundle bundle) {
//		super.onCreate(bundle);
//		setContentView(R.layout.bind_idcard_layout);
////		getActionBar().hide();
//		findView();
//		initSelectPop();
//		user = getUser();
//	}
//
//	// 初始化控件
//	private void findView() {
//		header = (TextView) findViewById(R.id.header);
//		header.setText("上传身份证信息");
//		back = (ImageView) findViewById(R.id.back);
//		back.setVisibility(View.VISIBLE);
//		back.setOnClickListener(this);
//		name_edit = (EditText) findViewById(R.id.name);
//		nums_edit = (EditText) findViewById(R.id.nums);
//		add = (TextView) findViewById(R.id.add);
//		add.setOnClickListener(this);
//		left_img = (ImageView) findViewById(R.id.left);
//		right_img = (ImageView) findViewById(R.id.right);
//		left_img.setOnClickListener(this);
//		right_img.setOnClickListener(this);
//		parenView = LayoutInflater.from(this).inflate(
//				R.layout.bind_idcard_layout, null);
//	}
//
//	// onclick事件
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.back:
//			finish();
//			break;
//		case R.id.add:
//			checkInput();
//			break;
//		case R.id.left:
//			isLeft = true;
//			popWindow.showAtLocation(parenView, Gravity.BOTTOM, 0, 0);
//			break;
//		case R.id.right:
//			isLeft = false;
//			popWindow.showAtLocation(parenView, Gravity.BOTTOM, 0, 0);
//			break;
//		default:
//			break;
//		}
//	}
//
//	// 输入检查
//	private void checkInput() {
//		name = name_edit.getText().toString();
//		idcard_nums = nums_edit.getText().toString();
//		if (name.equals("")) {
//			Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
//			return;
//		} else if (idcard_nums.equals("")) {
//			Toast.makeText(this, "请输入身份证", Toast.LENGTH_SHORT).show();
//			return;
//		} else if (left_String.equals("")) {
//			Toast.makeText(this, "身份证证件信息不足", Toast.LENGTH_SHORT).show();
//			return;
//		} else if (right_String.equals("")) {
//			Toast.makeText(this, "身份证证件信息不足", Toast.LENGTH_SHORT).show();
//			return;
//		}else{
//			turnToObject();
//			doUpImg();
//		}
//	}
//
//	// 初始化popwindow
//	@SuppressWarnings("deprecation")
//	private void initSelectPop() {
//		popWindow = new PopupWindow(this);
//		View view = LayoutInflater.from(this).inflate(
//				R.layout.select_img_pop_layout, null);
//		popWindow.setWidth(LayoutParams.MATCH_PARENT);
//		popWindow.setHeight(LayoutParams.WRAP_CONTENT);
//		popWindow.setBackgroundDrawable(new BitmapDrawable());
//		popWindow.setFocusable(true);
//		popWindow.setOutsideTouchable(true);
//		popWindow.setContentView(view);
//		// 拍照
//		view.findViewById(R.id.play_photo).setOnClickListener(
//				new OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						Intent intentFromCapture = new Intent(
//								MediaStore.ACTION_IMAGE_CAPTURE);
//						if (HasSDCardUtil.hasSdcard()) {
//
//							intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
//									Uri.fromFile(new File(Environment
//											.getExternalStorageDirectory(),
//											IMAGE_FILE_NAME)));
//						}
//						startActivityForResult(intentFromCapture,
//								CAMERA_REQUEST_CODE);
//						popWindow.dismiss();
//					}
//				});
//		// 本地照片
//		view.findViewById(R.id.my_photo).setOnClickListener(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						ImgUtils.getInstance().selectPicture(
//								IdCardActivity.this);
//						popWindow.dismiss();
//					}
//				});
//		// 取消
//		view.findViewById(R.id.cancle).setOnClickListener(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						popWindow.dismiss();
//					}
//				});
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode != RESULT_CANCELED) {
//			Uri uri = null;
//			switch (requestCode) {
//			case AppConstant.KITKAT_LESS:
//				uri = data.getData();
//				Log.d("tag", "uri=" + uri);
//				// 调用裁剪方法
//				ImgUtils.getInstance().cropPicture(this, uri);
//				break;
//			case AppConstant.KITKAT_ABOVE:
//				uri = data.getData();
//				Log.d("tag", "uri=" + uri);
//				// 先将这个uri转换为path，然后再转换为uri
//				String thePath = ImgUtils.getInstance().getPath(this, uri);
//				ImgUtils.getInstance().cropPicture(this,
//						Uri.fromFile(new File(thePath)));
//				break;
//			case CAMERA_REQUEST_CODE:
//				if (HasSDCardUtil.hasSdcard()) {
//					ImgUtils.getInstance().cropPicture(
//							this,
//							Uri.fromFile(new File(Environment
//									.getExternalStorageDirectory(),
//									IMAGE_FILE_NAME)));
//				} else {
//					Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
//							.show();
//				}
//				break;
//			case AppConstant.INTENT_CROP:
//				File file = null;
//				if (data != null)
//					file = getFile(data);
//				if (isLeft) {
//					left_String = toBuffer(file);
//				} else {
//					right_String = toBuffer(file);
//				}
//
//				break;
//			}
//		}
//	}
//
//	// string 转object
//	private void turnToObject() {
//		try {
//			object.put("cardImgA", left_String);
//			object.put("aType", "jpg");
//			object.put("cardImgB", right_String);
//			object.put("bType", "jpg");
//			object.put("cardNum", idcard_nums);
//			object.put("realName", name);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	// 上传图片，证件号，姓名。
//	private void doUpImg() {
//		showLoading();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				String result = HttpUtils.post(
//						"http://172.28.3.51:9004/api/user/verify", object,
//						"id-token", user.getToken());
//				Result mResult = DataParser.parserUpImg(result);
//				Message msg = mHandler.obtainMessage(1);
//				msg.obj = mResult;
//				mHandler.sendMessage(msg);
//			}
//		}).start();
//	}
//
//	private void showLoading() {
//		// 设置推出等待窗口
//		dialog = new ProgressDialog(this);
//		dialog.setMessage("正在上传，请稍后...");
//		dialog.show();
//	}
//
//	@SuppressLint("HandlerLeak") 
//	private Handler mHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 1:
//				Result result = (Result) msg.obj;
//				if (result != null) {
//					Toast.makeText(IdCardActivity.this, result.getMessage(),
//							Toast.LENGTH_SHORT).show();
//					if (result.getCode() == 200) {
//						finish();
//					}
//				}
//				dialog.dismiss();
//				break;
//
//			default:
//				break;
//			}
//		}
//
//	};
//
//	// 将文件转成流 base64
//	private String toBuffer(File file) {
//		byte b[] = null;
//		try {
//			InputStream in = new FileInputStream(file);
//
//			b = new byte[(int) file.length()];
//			try {
//				in.read(b);
//				in.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		return new String(Base64.encode(b, Base64.NO_WRAP));
//	}
//
//	// 得到file 并展示到imageview
//	@SuppressWarnings("deprecation")
//	private File getFile(Intent data) {
//		Bundle extras = data.getExtras();
//		if (extras != null) {
//			photo = extras.getParcelable("data");
//			Drawable drawable = new BitmapDrawable(photo);
//			if (isLeft) {
//				left_img.setImageDrawable(drawable);
//				left_img.setScaleType(ScaleType.FIT_XY);
//			} else {
//				right_img.setImageDrawable(drawable);
//				right_img.setScaleType(ScaleType.FIT_XY);
//			}
//
//		}
//		return saveBitmapFile(photo);
//	}
//
//	private static String IMG_PATH = "/mnt/sdcard/hanmimei/pic"; // 图片本地存储路径
//	private String file_name = ""; // 图片本地存储的名称
//
//	// 将图片保存至本地制定文件夹
//	@SuppressLint("SdCardPath")
//	private File saveBitmapFile(Bitmap bitmap) {
//		file_name = DateUtil.getStringDate() + ".jpg";
//		File file = new File(IMG_PATH + "/" + file_name);// 将要保存图片的路径
//		File mWorkingPath = new File(IMG_PATH);
//		// 路径不存在就创建
//		if (!mWorkingPath.exists()) {
//			if (!mWorkingPath.mkdirs()) {
//
//			}
//		}
//		try {
//			BufferedOutputStream bos = new BufferedOutputStream(
//					new FileOutputStream(file));
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//			bos.flush();
//			bos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return file;
//	}
//
//}
