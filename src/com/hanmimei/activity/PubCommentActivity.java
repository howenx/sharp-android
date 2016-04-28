/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-25 下午2:18:55 
**/
package com.hanmimei.activity;

import java.io.File;
import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.adapter.ImageGridAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Comment;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.Sku;
import com.hanmimei.http.MultipartRequestParams;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.GlideLoaderUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.CustomGridView;

/**
 * @author eric
 *
 */
public class PubCommentActivity extends BaseActivity implements OnClickListener{

	private ImageView img;
	private TextView content;
	private RatingBar ratingBar;
	private CustomGridView mGridView;
	private TextView fontNum;
	private String comment_str;
	private int grade;
	private Sku sku;
	private Comment comment;
	private ArrayList<String> photoList;
	private ImageGridAdapter adapter;
	private static final int REQUEST_IMAGE = 2;
	private static final int IMAGE_MAX_NUM = 5;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pub_comment_layout);
		ActionBarUtil.setActionBarStyle(this, "发表评价");
		sku = (Sku) getIntent().getSerializableExtra("sku");
		if(getIntent().getSerializableExtra("comment") != null){
			comment = (Comment) getIntent().getSerializableExtra("comment");
		}
//		orderId = getIntent().getStringExtra("orderId");
		photoList = new ArrayList<String>();
		if(comment != null){
			adapter = new ImageGridAdapter(this, photoList,true);
			findView();
			initView();
		}else{
			adapter = new ImageGridAdapter(this, photoList,false);
			findView();
		}
	}

	/**
	 * 
	 */
	private void initView() {
//		GlideLoaderUtils.loadImage(this, sku.getInvImg(), img, R.drawable.hmm_place_holder_z);
		content.setText(comment.getComment());
		content.setCursorVisible(false);      
		content.setFocusable(false);         
		content.setFocusableInTouchMode(false);
		photoList.addAll(comment.getPhotoList());
		adapter.notifyDataSetChanged();
		ratingBar.setRating(comment.getScore());
		ratingBar.setIsIndicator(true);
	}

	/**
	 * 
	 */
	private void findView() {
		img = (ImageView) findViewById(R.id.img);
		findViewById(R.id.submit).setOnClickListener(this);
		content = (TextView) findViewById(R.id.content);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		mGridView = (CustomGridView) findViewById(R.id.mGridView);
		fontNum = (TextView) findViewById(R.id.fontNum);
		mGridView.setAdapter(adapter);
		if(comment == null){
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == photoList.size() && arg2 != 5) {
					Intent intent = new Intent(PubCommentActivity.this,
							MultiImageSelectorActivity.class);
					// 最大可选择图片数量
					intent.putExtra(
							MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
							IMAGE_MAX_NUM);
					// 默认选择
					if (photoList != null
							&& photoList.size() > 0) {
						intent.putExtra(
								MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
								photoList);
					}
					startActivityForResult(intent,
							REQUEST_IMAGE);
				}
			}
		});
		}else{
			fontNum.setVisibility(View.GONE);
			findViewById(R.id.attention).setVisibility(View.GONE);
			findViewById(R.id.submit).setVisibility(View.GONE);
		}
		GlideLoaderUtils.loadImage(this, sku.getInvImg(), img, R.drawable.hmm_place_holder_z);
		content.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 500){
					ToastUtils.Toast(PubCommentActivity.this, "输入超出限制");
				}else{
					fontNum.setText(s.toString().length() + "/500");
				}
			}
		});
	}

	private void checkInput(){
		comment_str = content.getText().toString();
		grade = (int)ratingBar.getRating();
		if(10 <= comment_str.length() &&  comment_str.length()<= 100){
			pubComment();
		}else{
			ToastUtils.Toast(this, "评价在10到100字之间，请检查您输入的字数");
		}
	}

	/**
	 * 
	 */
	private void pubComment() {
		getLoading().show();
		VolleyHttp.doPostRequestTask3(getHeaders(),
				UrlUtil.PUB_COMMENT, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						HMessage message = DataParser.paserResultMsg(result);
						if(message.getCode() == 200){
							sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_UPDATE_COMMENT));
							finish();
						}else{
							ToastUtils.Toast(PubCommentActivity.this, message.getMessage());
						}
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				}, getParams());
	}
	private MultipartRequestParams getParams(){
		MultipartRequestParams params = new MultipartRequestParams();
		params.put("orderId", sku.getOrderId());
		params.put("skuType", sku.getSkuType());
		params.put("skuTypeId", sku.getSkuTypeId());
		params.put("grade", grade + "");
		params.put("content", comment_str);
		for (int i = 0; i < photoList.size(); i++) {
			String path = photoList.get(i);
			params.put("refundImg" + i, new File(path));
		}
		return params;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
			checkInput();
			break;

		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
			photoList.clear();
			photoList.addAll(data
							.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT));
			adapter.notifyDataSetChanged();
		}
	}

}
