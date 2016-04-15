/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-15 上午10:14:00 
**/
package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.adapter.LogisticsAdapter;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Logistics;
import com.hanmimei.entity.LogisticsData;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.KeyWordUtil;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;

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
		headerView = LayoutInflater.from(this).inflate(R.layout.logistics_header_layout, null);
		state = (TextView) headerView.findViewById(R.id.state);
		from = (TextView) headerView.findViewById(R.id.from);
		order_id = (TextView) headerView.findViewById(R.id.id);
		mListView.addHeaderView(headerView);
	}
	private void initHeaderView(Logistics logistics){
		String logistState = "";
		if(logistics.getState() == 0){
			logistState = "物流状态：在途中";
		}else if(logistics.getState() == 1){
			logistState = "物流状态：已收揽";
		}else if(logistics.getState() == 2){
			logistState = "物流状态：疑难";
		}else if(logistics.getState() == 3){
			logistState = "物流状态：已签收";
		}else if(logistics.getState() == 4){
			logistState = "物流状态：退签";
		}else if(logistics.getState() == 5){
			logistState = "物流状态：同城派送中";
		}else if(logistics.getState() == 6){
			logistState = "物流状态：退回";
		}else if(logistics.getState() == 7){
			logistState = "物流状态：转单";
		}
		KeyWordUtil.setDifferentFontColor(this, state, logistState, 5, logistState.length());
		from.setText("快递类型：" + logistics.getCom());
		order_id.setText("快递单号：" + logistics.getCodenumber());
	}

	/**
	 * 
	 */
	private void loadData() {
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.WULIU_LIST + orderId, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				getLoading().dismiss();
				Logistics logistics = DataParser.parserLogistics(result);
				if(logistics != null){
					mListView.setVisibility(View.VISIBLE);
					initHeaderView(logistics);
					data.addAll(logistics.getList());
					adapter.notifyDataSetChanged();
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
