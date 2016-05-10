package com.hanmimei.activity.apply;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.dd.processbutton.ProcessButton;
import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.GoodsBalanceVo;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.Order;
import com.hanmimei.entity.Sku;
import com.hanmimei.gallery.MultiImageSelectorActivity;
import com.hanmimei.http.MultipartRequestParams;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtils;
import com.hanmimei.utils.GlideLoaderTools;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.CustomGridView;
import com.hanmimei.view.LinkDialog;
import com.hanmimei.view.LinkDialog.CallBack;
/**
 * 
 * @author vince
 *
 */
public class CustomerServiceActivity extends BaseActivity implements
		OnClickListener {
	private static final int REQUEST_IMAGE = 2;
	private static final int IMAGE_MAX_NUM = 3;
	private Sku sku;
	private EditText discription;
	private CustomGridView mGridView;
	private List<Drawable> imgList;
	private GridAdapter adapter;
	private ArrayList<String> mSelectPath;
	private String refundType = "deliver";// 退款类型，pin：拼购自动退款，receive：收货后申请退款，deliver：发货前退款

	private Order order;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_service_main_layout);
		ActionBarUtil.setActionBarStyle(this, "售后服务");
		sku = (Sku) getIntent().getSerializableExtra("sku");
		order = (Order) getIntent().getSerializableExtra("order");
		mSelectPath = new ArrayList<String>();
		findView();
	}

	/**
	 * 初始化获取view
	 */
	private void findView() {
		discription = (EditText) findViewById(R.id.discription);
		mGridView = (CustomGridView) findViewById(R.id.mGridView);

		findViewById(R.id.btn_next).setOnClickListener(this);

		initViewData();
	}

	private void initViewData() {
		imgList = new ArrayList<Drawable>();
		imgList.add(getResources().getDrawable(R.drawable.ic_launcher));
		adapter = new GridAdapter(this);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == mSelectPath.size()) {
					Intent intent = new Intent(CustomerServiceActivity.this,
							MultiImageSelectorActivity.class);
					// 最大可选择图片数量
					intent.putExtra(
							MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
							IMAGE_MAX_NUM);
					// 默认选择
					if (mSelectPath != null && mSelectPath.size() > 0) {
						intent.putExtra(
								MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
								mSelectPath);
					}
					startActivityForResult(intent, REQUEST_IMAGE);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
			mSelectPath.clear();
			mSelectPath
					.addAll(data
							.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT));
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			next();
			break;
		default:
			break;
		}
	}

	private LinkDialog dialog;

	private void next() {
		if (TextUtils.isEmpty(discription.getText())) {
			ToastUtils.Toast(this, "请填写问题描述");
			return;
		}
		dialog = new LinkDialog(this);
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
				if (dialog.getState()) {
					finish();
				} else {
					VolleyHttp.getRequestQueue().cancelAll(UrlUtil.CUSTOMER_SERVICE_APPLY);
				}
			}
		});
		dialog.show();
	}

	private void submitData(final ProcessButton button, String name,
			String phone) {

		VolleyHttp.doPostRequestTask3( getHeaders(),
				UrlUtil.CUSTOMER_SERVICE_APPLY, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						GoodsBalanceVo b = new Gson().fromJson(result,
								GoodsBalanceVo.class);
						HMessage msg = b.getMessage();
						if (msg.getCode() == 200) {
							button.setProgress(100);
						} else {
							ToastUtils.Toast(getActivity(), msg.getMessage());
							button.setProgress(-1);
							dialog.dismiss();
							CommonUtils.closeBoardIfShow(getActivity());
						}
					}

					@Override
					public void onError() {
						ToastUtils.Toast(getActivity(), R.string.error);
						button.setProgress(-1);
					}
				}, getParams());
	}

	private MultipartRequestParams getParams(){
		MultipartRequestParams params = new MultipartRequestParams();

		if(order !=null){
			params.put("orderId", order.getOrderId());
			params.put("splitOrderId", order.getOrderSplitId());
			
			params.put("refundType", refundType);

			params.put("payBackFee", order.getTotalFee()+"");
			
			params.put("reason", discription.getText().toString());
			for (int i = 0; i < mSelectPath.size(); i++) {
				String path = mSelectPath.get(i);
				params.put("refundImg" + i, new File(path));
			}
		}else if(sku !=null){
				params.put("orderId", getIntent().getStringExtra("orderId"));
				params.put("splitOrderId", getIntent().getStringExtra("splitOrderId"));
				params.put("skuId", sku.getSkuId());
				
				params.put("skuType", sku.getSkuType());
				params.put("skuTypeId", sku.getSkuTypeId());
				params.put("refundType", refundType);

				params.put("reason", discription.getText().toString());
//				params.put("contactName", name);
//				params.put("contactTel", phone);
				for (int i = 0; i < mSelectPath.size(); i++) {
					String path = mSelectPath.get(i);
					params.put("refundImg" + i, new File(path));
				}
			}
		return params;
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return (mSelectPath.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(
						R.layout.upload_item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.btn_image_del = (ImageView) convertView
						.findViewById(R.id.btn_image_del);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.image.setVisibility(View.VISIBLE);
			if (position == mSelectPath.size()) {
				holder.btn_image_del.setVisibility(View.GONE);
				holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
				if (position == 3) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.btn_image_del.setVisibility(View.VISIBLE);
				GlideLoaderTools.loadSquareImage(getActivity(),
						mSelectPath.get(position), holder.image);
				holder.btn_image_del.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mSelectPath.remove(position);
						notifyDataSetChanged();
					}
				});
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image, btn_image_del;

		}
	}

}
