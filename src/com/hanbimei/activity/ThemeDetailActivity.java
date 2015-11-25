package com.hanbimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanbimei.R;
import com.hanbimei.adapter.ThemeAdapter;
import com.hanbimei.dao.ThemeItemDao;
import com.hanbimei.data.DataParser;
import com.hanbimei.entity.ThemeDetail;
import com.hanbimei.entity.ThemeItem;
import com.hanbimei.utils.CommonUtil;
import com.hanbimei.utils.HttpUtils;
import com.hanbimei.utils.InitImageLoader;
import com.hanbimei.utils.WaveAnimationUtil;
import com.hanbimei.view.RotateTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * @author eric
 *	主题商品的二级界面。
 */
@SuppressLint("NewApi") 
public class ThemeDetailActivity extends BaseActivity {

	private String url;
	private ThemeAdapter adapter;
	private GridView gridView;
	private List<ThemeItem> data;
	private ThemeItemDao itemDao;
	private ThemeItem themeItem;
	private ImageView img;
	private TextView title;
	private TextView price;
	private TextView header;
	private LinearLayout masterItem;
	private FrameLayout mframeLayout;
	private ImageLoader imageLoader;
	private DisplayImageOptions imageOptions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_layout);
		getActionBar().hide();
		itemDao = getDaoSession().getThemeItemDao();
		url = getIntent().getStringExtra("url");
		data = new ArrayList<ThemeItem>();
		adapter = new ThemeAdapter(data, this);
		findView();
		gridView.setAdapter(adapter);
		gridView.setFocusable(false);            
		loadUrl();
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(!data.get(arg2).getState().equals("Y")){
					return;
				}
				Intent intent = new Intent(getActivity(),GoodsDetailActivity.class);
				intent.putExtra("url", data.get(arg2).getItemUrl());
				startActivity(intent);
			}
		});
	}
	private void findView() {
		imageLoader = InitImageLoader.initLoader(this);
		imageOptions = InitImageLoader.initOptions();
		gridView = (GridView) findViewById(R.id.my_grid);
		masterItem= (LinearLayout) findViewById(R.id.masterItem);
		img = (ImageView) findViewById(R.id.img);
		title = (TextView) findViewById(R.id.title);
		price = (TextView) findViewById(R.id.price);
		header = (TextView) findViewById(R.id.header);
		header.setText("商品展示");
		mframeLayout = (FrameLayout) findViewById(R.id.mframeLayout);
		findViewById(R.id.back).setVisibility(View.VISIBLE);
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	private void loadUrl() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.get(url);
				ThemeDetail detail = DataParser.parserThemeItem(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = detail;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				ThemeDetail detail = (ThemeDetail) msg.obj;
				if(detail != null){
					initThemeView(detail);
					data.clear();
					data.addAll(detail.getThemeList());
					adapter.notifyDataSetChanged();
				}else{
					Toast.makeText(ThemeDetailActivity.this, "加载数据失败！", Toast.LENGTH_SHORT).show();
				}
				
				break;

			default:
				break;
			}
		}
		
	};
	private void initThemeView(ThemeDetail detail) {
		themeItem = detail.getMasterItem();
		if(themeItem ==null)
			return;
		masterItem.setVisibility(View.VISIBLE);
		imageLoader.displayImage(themeItem.getItemMasterImg(), img,imageOptions);
		title.setText(themeItem.getItemTitle());
		price.setText("US ¥ " + themeItem.getItemPrice());
		
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!themeItem.getState().equals("Y")){
					return;
				}
				Intent intent = new Intent(getActivity(),GoodsDetailActivity.class);
				intent.putExtra("url", themeItem.getItemUrl());
				startActivity(intent);
			}
		});
		
		try {
			JSONArray array = new JSONArray(themeItem.getMasterItemTag());
			int width = CommonUtil.dip2px(300);
			int height = CommonUtil.dip2px(200);
			View view = null;
			for(int i=0;i<array.length();i++){
				JSONObject json = array.getJSONObject(i);
				if(json.getInt("angle")>90){
					view = getLayoutInflater().inflate(R.layout.panel_biaoqian_180, null);
				}else{
					view = getLayoutInflater().inflate(R.layout.panel_biaoqian_0, null);
				}
				 TextView tag = (TextView) view.findViewById(R.id.tag);
				 ImageView point = (ImageView) view.findViewById(R.id.point);
				 WaveAnimationUtil.waveAnimation(point, 2f);
				 FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				 lp.setMargins(Integer.valueOf((int) (width * json.getDouble("left"))),
						 Integer.valueOf((int) (height * json.getDouble("top"))), 0, 0);
				 
				 tag.setText(json.getString("name"));
				 mframeLayout.addView(view,lp);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
