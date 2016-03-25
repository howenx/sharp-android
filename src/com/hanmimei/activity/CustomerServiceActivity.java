package com.hanmimei.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.processbutton.ProcessButton;
import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.GoodsBalance;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.Sku;
import com.hanmimei.upload.AlbumActivity;
import com.hanmimei.upload.Bimp;
import com.hanmimei.upload.FileUtils;
import com.hanmimei.upload.PhotoActivity;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.AlertDialogUtils.OnPhotoSelListener;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ImageLoaderUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.utils.upload.Http3Utils;
import com.hanmimei.utils.upload.MultipartRequestParams;
import com.hanmimei.view.CustomGridView;
import com.hanmimei.view.UDialog;
import com.hanmimei.view.UDialog.CallBack;

public class CustomerServiceActivity extends BaseActivity implements
		OnClickListener {

	private Sku sku;
	private ImageView imgView;
	private TextView name, price, num, apply_num, num_sel_max;
	private EditText discription;
	private CustomGridView mGridView;
	private List<Drawable> imgList;
	private GridAdapter adapter;
	private AlertDialog photoDialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_service_main_layout);
		ActionBarUtil.setActionBarStyle(this, "申请售后服务");
		sku = (Sku) getIntent().getSerializableExtra("sku");
		findView();
	}

	/**
	 * 初始化获取view
	 */
	private void findView() {
		imgView = (ImageView) findViewById(R.id.imgView);
		name = (TextView) findViewById(R.id.name);
		price = (TextView) findViewById(R.id.price);
		num = (TextView) findViewById(R.id.num);
		apply_num = (TextView) findViewById(R.id.apply_num);
		num_sel_max = (TextView) findViewById(R.id.num_sel_max);
		discription = (EditText) findViewById(R.id.discription);
		mGridView = (CustomGridView) findViewById(R.id.mGridView);

		findViewById(R.id.jian).setOnClickListener(this);
		findViewById(R.id.plus).setOnClickListener(this);
		findViewById(R.id.btn_next).setOnClickListener(this);

		initViewData();
	}

	private void initViewData() {
		ImageLoaderUtils.loadImage(this, sku.getInvImg(), imgView);
		name.setText(sku.getSkuTitle());
		price.setText(getResources().getString(R.string.price, sku.getPrice()));
		num.setText(getResources().getString(R.string.num, sku.getAmount()));
		apply_num.setText(sku.getAmount() + "");
		num_sel_max.setText(getResources().getString(R.string.sel_max,
				sku.getAmount()));
		imgList = new ArrayList<Drawable>();
		imgList.add(getResources().getDrawable(R.drawable.ic_launcher));
		adapter = new GridAdapter(this);
		adapter.update();
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					showPopwindowForPhoto();
				} else {
					Intent intent = new Intent(CustomerServiceActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	}

	private void showPopwindowForPhoto() {
		if(photoDialog == null){
			photoDialog = AlertDialogUtils.showPhotoDialog(this,new OnPhotoSelListener() {
				
				@Override
				public void onSelPlay() {
					photo();
				}
				
				@Override
				public void onSelLocal() {
					Intent intent = new Intent(getActivity(), AlbumActivity.class);
					startActivity(intent);
				}
			});
		}
		photoDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jian:
			int size = Integer.parseInt(apply_num.getText().toString());
			if (size <= 1)
				return;
			size--;
			apply_num.setText(size + "");
			break;
		case R.id.plus:
			size = Integer.parseInt(apply_num.getText().toString());
			if (size >= sku.getAmount())
				return;
			size++;
			apply_num.setText(size + "");
			break;
		case R.id.btn_next:
			next();
			break;
		default:
			break;
		}
	}

	private UDialog dialog;

	private void next() {
		if (TextUtils.isEmpty(discription.getText())) {
			ToastUtils.Toast(this, "请填写问题描述");
			return;
		}
		dialog = new UDialog(this);
		dialog.setCallBack(new CallBack() {

			@Override
			public void onClick(ProcessButton button, String name, String phone) {
				submitData(button, name, phone);
			}

			@Override
			public void onCompleteClick() {
				dialog.dismiss();
				getActivity().finish();
			}
		});
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface arg0) {
				if(dialog.getState()){
					finish();
				}else{
					getMyApplication().getRequestQueue().cancelAll(UrlUtil.CUSTOMER_SERVICE_APPLY);
				}
			}
		});
		dialog.show();
	}

	private void submitData(final ProcessButton button, String name,
			String phone) {
		MultipartRequestParams params = new MultipartRequestParams();

		params.put("orderId", getIntent().getStringExtra("orderId"));
		params.put("splitOrderId", getIntent().getStringExtra("splitOrderId"));
		params.put("skuId", sku.getSkuId() + "");
		params.put("reason", discription.getText().toString());
		params.put("amount", apply_num.getText().toString());
		params.put("contactName", name);
		params.put("contactTel", phone);
		for (int i = 0; i < Bimp.drr.size(); i++) {
			String path = Bimp.drr.get(i);
			params.put("refundImg" + i, new File(path));
		}

		Http3Utils.doPostRequestTask(this, getHeaders(),
				UrlUtil.CUSTOMER_SERVICE_APPLY, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						GoodsBalance b = new Gson().fromJson(result,
								GoodsBalance.class);
						HMessage msg = b.getMessage();
						if (msg.getCode() == 200) {
							button.setProgress(100);
						} else {
							ToastUtils.Toast(getActivity(), msg.getMessage());
							button.setProgress(-1);
							dialog.dismiss();
							CommonUtil.closeBoardIfShow(getActivity());
						}
					}

					@Override
					public void onError() {
						ToastUtils.Toast(getActivity(), R.string.error);
						button.setProgress(-1);
					}
				}, params);
	}

	private static final int TAKE_PICTURE = 110;
	private String capturePath = "";

	public void photo() {
		if (Bimp.drr.size() >= Bimp.MAX_SIZE) {
			ToastUtils.Toast(this, "最多上传3张");
			return;
		}
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String out_file_path = FileUtils.SAVED_IMAGE_DIR_PATH;
		File dir = new File(out_file_path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		capturePath = FileUtils.SAVED_IMAGE_DIR_PATH
				+ System.currentTimeMillis() + ".jpg";
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(capturePath)));
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < Bimp.MAX_SIZE && resultCode == RESULT_OK) {
				Bimp.drr.add(capturePath);
			}
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(
						R.layout.upload_item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.image.setVisibility(View.VISIBLE);
			if (position == Bimp.bmp.size()) {
				holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
				if (position == Bimp.MAX_SIZE) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Bimp.clearAll();
		FileUtils.deleteDir();
	}
	
	

}
