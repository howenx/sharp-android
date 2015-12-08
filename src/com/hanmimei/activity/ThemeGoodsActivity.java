package com.hanmimei.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import com.hanmimei.R;
import com.hanmimei.adapter.ThemeAdapter;
import com.hanmimei.dao.ThemeItemDao;
import com.hanmimei.data.DataParser;
import com.hanmimei.entity.ThemeDetail;
import com.hanmimei.entity.ThemeItem;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.InitImageLoader;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.utils.WaveAnimationUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * @author eric 主题商品的二级界面。
 */
@SuppressLint("NewApi")
public class ThemeGoodsActivity extends BaseActivity implements OnClickListener {

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
	private ImageView shoppingCar;
	private LinearLayout masterItem;
	private FrameLayout mframeLayout;
	private ImageLoader imageLoader;
	private DisplayImageOptions imageOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_layout);
		ActionBarUtil.setActionBarStyle(this, "商品展示",
				R.drawable.white_shoppingcar, true, this);
		// getActionBar().hide();
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
				if (!data.get(arg2).getState().equals("Y")) {
					return;
				}
				Intent intent = new Intent(getActivity(),
						GoodsDetailActivity.class);
				intent.putExtra("url", data.get(arg2).getItemUrl());
				startActivity(intent);
			}
		});
	}

	private void findView() {
		imageLoader = InitImageLoader.initLoader(this);
		imageOptions = InitImageLoader.initOptions();
		gridView = (GridView) findViewById(R.id.my_grid);
		masterItem = (LinearLayout) findViewById(R.id.masterItem);
		img = (ImageView) findViewById(R.id.img);
		title = (TextView) findViewById(R.id.title);
		price = (TextView) findViewById(R.id.price);
		mframeLayout = (FrameLayout) findViewById(R.id.mframeLayout);
	}

	private void loadUrl() {
			Http2Utils.doGetRequestTask(this, url, new VolleyJsonCallback() {

				@Override
				public void onSuccess(String result) {
					// TODO Auto-generated method stub
					ThemeDetail detail = DataParser.parserThemeItem(result);
					if (detail != null) {
						initThemeView(detail);
						data.clear();
						data.addAll(detail.getThemeList());
						adapter.notifyDataSetChanged();
					} else {
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				}

				@Override
				public void onError() {
					// TODO Auto-generated method stub
					ToastUtils.Toast(getActivity(), R.string.error);
				}
			});
	}


	private void initThemeView(ThemeDetail detail) {
		themeItem = detail.getMasterItem();
		if (themeItem == null)
			return;
		masterItem.setVisibility(View.VISIBLE);
		Picasso.with(this)
				.load(themeItem.getItemMasterImg())
				.resize(CommonUtil.getScreenWidth(this),
						CommonUtil.getScreenWidth(this) / 2).into(img);
		title.setText(themeItem.getItemTitle());
		price.setText(getResources().getString(R.id.price,
				themeItem.getItemPrice()));
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!themeItem.getState().equals("Y")) {
					return;
				}
				Intent intent = new Intent(getActivity(),
						GoodsDetailActivity.class);
				intent.putExtra("url", themeItem.getItemUrl());
				startActivity(intent);
			}
		});

		try {
			JSONArray array = new JSONArray(themeItem.getMasterItemTag());
			int width = CommonUtil.getScreenWidth(this);
			int height = CommonUtil.getScreenWidth(this) / 2;
			View view = null;
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				if (json.getInt("angle") > 90) {
					view = getLayoutInflater().inflate(
							R.layout.panel_biaoqian_180, null);
				} else {
					view = getLayoutInflater().inflate(
							R.layout.panel_biaoqian_0, null);
				}
				TextView tag = (TextView) view.findViewById(R.id.tag);
				ImageView point_b = (ImageView) view.findViewById(R.id.point_b);
				WaveAnimationUtil.waveAnimation(point_b, 3.0f);
				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(
						Integer.valueOf((int) (width * json.getDouble("left"))),
						Integer.valueOf((int) (height * json.getDouble("top"))),
						0, 0);

				tag.setText(json.getString("name"));
				mframeLayout.addView(view, lp);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			startActivity(new Intent(this, ShoppingCarActivity.class));
			break;
		default:
			break;
		}
	}

}
