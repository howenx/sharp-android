package com.hanbimei.activity;

import java.util.ArrayList;
import java.util.List;
import com.hanbimei.R;
import com.hanbimei.adapter.ThemeAdapter;
import com.hanbimei.dao.ThemeItemDao;
import com.hanbimei.data.DataParser;
import com.hanbimei.entity.ThemeItem;
import com.hanbimei.utils.HttpUtils;
import com.hanbimei.utils.InitImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
	private LinearLayout header;
	private ImageLoader imageLoader;
	private DisplayImageOptions imageOptions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_layout);
		getActionBar().hide();
		itemDao = getDaoSession().getThemeItemDao();
		url = getIntent().getStringExtra("url");
		themeItem = new ThemeItem();
		data = new ArrayList<ThemeItem>();
		adapter = new ThemeAdapter(data, this);
		findView();
		gridView.setAdapter(adapter);
		loadUrl();
	}
	private void findView() {
		imageLoader = InitImageLoader.initLoader(this);
		imageOptions = InitImageLoader.initOptions();
		gridView = (GridView) findViewById(R.id.my_grid);
		header = (LinearLayout) findViewById(R.id.header);
		img = (ImageView) findViewById(R.id.img);
		title = (TextView) findViewById(R.id.title);
		price = (TextView) findViewById(R.id.price);
	}
	private void loadUrl() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.get(url);
				List<ThemeItem> list = DataParser.parserThemeItem(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = list;
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
				List<ThemeItem> list = (List<ThemeItem>) msg.obj;
				if(list != null && list.size() > 0){
					itemDao.deleteAll();
					itemDao.insertInTx(list);
					data.clear();
					for(int i = 0; i < list.size(); i++){
						if(list.get(i).getOrMasterItem() == true){
							themeItem = list.get(i);
							list.remove(i);
							initThemeView();
						}
					}
					data.addAll(list);
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
	private void initThemeView() {
		header.setVisibility(View.VISIBLE);
		imageLoader.displayImage(themeItem.getMasterItemImg(), img,imageOptions);
		title.setText(themeItem.getItemTitle());
		price.setText("US ¥ " + themeItem.getItemPrice());
	}
	
}
