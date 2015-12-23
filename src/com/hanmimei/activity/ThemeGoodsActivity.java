package com.hanmimei.activity;

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
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.adapter.ThemeAdapter;
import com.hanmimei.data.DataParser;
import com.hanmimei.entity.HMMGoods;
import com.hanmimei.entity.HMMThemeGoods;
import com.hanmimei.manager.BadgeViewManager;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.utils.WaveAnimationUtil;
import com.squareup.picasso.Picasso;

/**
 * @author eric 主题商品的二级界面。
 */
@SuppressLint("NewApi")
public class ThemeGoodsActivity extends BaseActivity implements OnClickListener {

	private String url;
	private ThemeAdapter adapter;	//商品适配器
	private GridView gridView;	//
	private List<HMMGoods> data;//显示的商品数据
	private HMMGoods themeItem;	//主推商品
	private ImageView img;	//主推商品图片
	private FrameLayout mframeLayout;	//主推商品容器  添加tag使用
	private View cartView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_layout);
		View view = ActionBarUtil.setActionBarStyle(this, "商品展示",
				R.drawable.white_shoppingcar, true, this);
		cartView = view.findViewById(R.id.setting);
		// getActionBar().hide();
		url = getIntent().getStringExtra("url");
		data = new ArrayList<HMMGoods>();
		adapter = new ThemeAdapter(data, this);
		findView();
		gridView.setAdapter(adapter);
		gridView.setFocusable(false);
		//获取数据
		loadUrl();

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (!data.get(arg2).getState().equals("Y")) {
					return;
				}
				Intent intent = new Intent(getActivity(),
						GoodsDetailActivity.class);
				intent.putExtra("url", data.get(arg2).getItemUrlAndroid());
				startActivity(intent);
			}
		});
	}
	//初始化view对象
	private void findView() {
		gridView = (GridView) findViewById(R.id.my_grid);
		img = (ImageView) findViewById(R.id.img);
		mframeLayout = (FrameLayout) findViewById(R.id.mframeLayout);
	}
	//获取显示数据
	private void loadUrl() {
		Http2Utils.doGetRequestTask(this, getHeaders(), url, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				HMMThemeGoods detail = DataParser.parserThemeItem(result);
				if (detail.getMessage().getCode() ==200) {
					initThemeView(detail);
					data.clear();
					data.addAll(detail.getThemeList());
					adapter.notifyDataSetChanged();
				} else {
					ToastUtils.Toast(getActivity(), detail.getMessage().getMessage());
				}
			}
			
			@Override
			public void onError() {
				ToastUtils.Toast(getActivity(), R.string.error);
			}
		});
	}

	//初始化主推商品显示
	private void initThemeView(HMMThemeGoods detail) {
		if(detail.getCartNum() !=null){
			BadgeViewManager.getInstance().showCartNum(this, cartView, detail.getCartNum());
		}
		themeItem = detail.getMasterItem();
		if (themeItem == null)
			return;
		Picasso.with(this)
				.load(themeItem.getItemMasterImg())
				.resize(CommonUtil.getScreenWidth(this),
						CommonUtil.getScreenWidth(this) *10/12).into(img);

		try {
			JSONArray array = new JSONArray(themeItem.getMasterItemTag());
			int width = CommonUtil.getScreenWidth(this);
			int height = CommonUtil.getScreenWidth(this)*10 /12;
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
				//整理显示主推商品小标签
				TextView tag = (TextView) view.findViewById(R.id.tag);
				ImageView point_b = (ImageView) view.findViewById(R.id.point_b);
				WaveAnimationUtil.waveAnimation(point_b, 5.0f);
				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(
						Integer.valueOf((int) (width * json.getDouble("left"))),
						Integer.valueOf((int) (height * json.getDouble("top"))),
						0, 0);

				tag.setText(json.getString("name"));
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(getActivity(),
								GoodsDetailActivity.class);
						intent.putExtra("url", themeItem.getItemUrlAndroid());
						startActivity(intent);
					}
				});
				mframeLayout.addView(view, lp);
			}
		} catch (JSONException e) {
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
