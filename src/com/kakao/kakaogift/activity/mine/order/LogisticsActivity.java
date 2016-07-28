/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-15 上午10:14:00 
 **/
package com.kakao.kakaogift.activity.mine.order;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.mine.order.adapter.LogisticsAdapter;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.Logistics;
import com.kakao.kakaogift.entity.LogisticsData;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.KeyWordUtil;
import com.kakao.kakaogift.utils.ToastUtils;

/**
 * @author eric
 * 
 */
public class LogisticsActivity extends BaseActivity {
	private ListView mListView;
	private TextView from;
	private TextView order_id;
	private TextView state;
	private LogisticsAdapter adapter;
	private List<LogisticsData> data;
	private String orderId;
	private View headerView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logistics_layout);
		ActionBarUtil.setActionBarStyle(this, "物流信息");
		orderId = getIntent().getStringExtra("orderId");
		mListView = (ListView) findViewById(R.id.mylist);
		data = new ArrayList<LogisticsData>();
		adapter = new LogisticsAdapter(this, data);
		findHeaderView();
		mListView.setAdapter(adapter);
		loadData();
	}

	/**
	 * 
	 */
	private void findHeaderView() {
		headerView = LayoutInflater.from(this).inflate(
				R.layout.logistics_header_layout, null);
		state = (TextView) headerView.findViewById(R.id.state);
		from = (TextView) headerView.findViewById(R.id.from);
		order_id = (TextView) headerView.findViewById(R.id.id);
		mListView.addHeaderView(headerView);
	}

	private void initHeaderView(Logistics logistics) {
		String logistState = "";
		if (logistics.getState() == 0) {
			logistState = "物流状态：在途中";
		} else if (logistics.getState() == 1) {
			logistState = "物流状态：已收揽";
		} else if (logistics.getState() == 2) {
			logistState = "物流状态：疑难";
		} else if (logistics.getState() == 3) {
			logistState = "物流状态：已签收";
		} else if (logistics.getState() == 4) {
			logistState = "物流状态：退签";
		} else if (logistics.getState() == 5) {
			logistState = "物流状态：同城派送中";
		} else if (logistics.getState() == 6) {
			logistState = "物流状态：退回";
		} else if (logistics.getState() == 7) {
			logistState = "物流状态：转单";
		}
		KeyWordUtil.setDifferentFontColor(this, state, logistState, 5,
				logistState.length());
		from.setText("快递类型：" + logistics.getCom());
		order_id.setText("快递单号：" + logistics.getNu());
	}

	/**
	 * 
	 */
	private void loadData() {
		getLoading().show();
		VolleyHttp.doGetRequestTask( getHeaders(), UrlUtil.WULIU_LIST
				+ orderId, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				getLoading().dismiss();
				Logistics logistics = DataParser.parserLogistics(result);
				if(logistics != null){
					mListView.setVisibility(View.VISIBLE);
					initHeaderView(logistics);
					if (logistics.getList() != null) {
						data.addAll(logistics.getList());
						adapter.notifyDataSetChanged();
//						findViewById(R.id.no_data).setVisibility(View.VISIBLE);
					}else{
						findViewById(R.id.no_data).setVisibility(View.VISIBLE);
					}
				}
			}

			@Override
			public void onError() {
				getLoading().dismiss();
				ToastUtils.Toast(LogisticsActivity.this, "请求失败，青检查您的网络！");
			}
		});
	}

}
