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
import com.hanmimei.entity.LogisticsData;
import com.hanmimei.utils.ActionBarUtil;

/**
 * @author eric
 *
 */
public class LogisticsActivity extends BaseActivity {
	private ListView mListView;
	private TextView payMethod;
	private TextView from;
	private TextView orderId;
	private LogisticsAdapter adapter;
	private List<LogisticsData> data;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logistics_layout);
		ActionBarUtil.setActionBarStyle(this, "物流信息");
		mListView = (ListView) findViewById(R.id.mylist);
		data = new ArrayList<LogisticsData>();
		adapter = new LogisticsAdapter(this, data);
		addHeaderView();
		mListView.setAdapter(adapter);
		loadData();
	}

	/**
	 * 
	 */
	private void addHeaderView() {
		View headerView = LayoutInflater.from(this).inflate(R.layout.logistics_header_layout, null);
		payMethod = (TextView) headerView.findViewById(R.id.pay);
		from = (TextView) headerView.findViewById(R.id.from);
		orderId = (TextView) headerView.findViewById(R.id.id);
		mListView.addHeaderView(headerView);
	}

	/**
	 * 
	 */
	private void loadData() {

		
		for(int i = 0; i < 10; i ++){
			LogisticsData logisticsData = new LogisticsData();
			logisticsData.setContent("货物已完成分拣，离开【北京双树分拣中心】");
			logisticsData.setTime("2015-09-22 11:11:11");
			data.add(logisticsData);
		}
		adapter.notifyDataSetChanged();
		
//		Http2Utils.doGetRequestTask(this, "http://api.kuaidi100.com/api?id=425796724eeca6b3&com=jd&nu=12837698789&show=0&muti=1&order=desc", new VolleyJsonCallback() {
//			
//			@Override
//			public void onSuccess(String result) {
//				ToastUtils.Toast(LogisticsActivity.this, "111");
//			}
//			
//			@Override
//			public void onError() {
//				ToastUtils.Toast(LogisticsActivity.this, "222");
//			}
//		});
	}

}
