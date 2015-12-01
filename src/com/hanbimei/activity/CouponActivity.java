package com.hanbimei.activity;

import java.util.ArrayList;
import java.util.List;

import com.hanbimei.R;
import com.hanbimei.adapter.TicketAdapter;
import com.hanbimei.entity.Ticket;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi") 
public class CouponActivity extends BaseActivity implements OnClickListener{

	private PullToRefreshListView mListView;
	private TicketAdapter adapter;
	private List<Ticket> data;
	private TextView header;
	private ImageView back;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_coupon_layout);
		getActionBar().hide();
		data = new ArrayList<Ticket>();
		adapter = new TicketAdapter(data, this);
		mListView = (PullToRefreshListView) findViewById(R.id.mylist);
		header = (TextView) findViewById(R.id.header);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		back.setVisibility(View.VISIBLE);
		header.setText("购物券");
		mListView.setAdapter(adapter);
		loadData();
	}

	private void loadData() {
		for(int i = 0; i < 7; i ++){
			data.add(new Ticket(i*10+5, i*50+100, "2015-12-24"));
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}

}
