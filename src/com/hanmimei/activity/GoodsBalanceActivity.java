package com.hanmimei.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.utils.ActionBarUtil;
import com.squareup.picasso.Picasso;

public class GoodsBalanceActivity extends BaseActivity implements
		OnClickListener {

	private RadioGroup group_pay_type, group_send_time;
	private TextView send_time, pay_type;
	private ListView mListView;
	private ShoppingCar car;
	private List<ShoppingGoods> goods;
	
	private TextView all_price,all_money;
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActionBarUtil.setActionBarStyle(this, "支付结算");
		setContentView(R.layout.goods_balance_layout);

		car = (ShoppingCar) getIntent().getSerializableExtra("car");
		goods = car.getList();
		findView();
		initViews();
	}

	private void findView() {
		group_pay_type = (RadioGroup) findViewById(R.id.group_pay_type);
		group_send_time = (RadioGroup) findViewById(R.id.group_send_time);
		mListView = (ListView) findViewById(R.id.mListView);

		pay_type = (TextView) findViewById(R.id.pay_type);
		send_time = (TextView) findViewById(R.id.send_time);
		
		all_price = (TextView) findViewById(R.id.all_price);
		all_money = (TextView) findViewById(R.id.all_money);

		findViewById(R.id.btn_pay_type).setOnClickListener(this);
		findViewById(R.id.btn_send_time).setOnClickListener(this);

		group_pay_type
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						RadioButton btn = (RadioButton) findViewById(arg1);
						pay_type.setText(btn.getText());
					}
				});
		group_send_time
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						RadioButton btn = (RadioButton) findViewById(arg1);
						send_time.setText(btn.getText());
					}
				});
		
	}
	
	private void initViews(){
		mListView.setAdapter(new GoodsAdapter());
		all_price.setText(car.getAllPrice()+"");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_pay_type:
			if (group_pay_type.getVisibility() == View.VISIBLE) {
				group_pay_type.setVisibility(View.GONE);
			} else {
				group_pay_type.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_send_time:
			if (group_send_time.getVisibility() == View.VISIBLE) {
				group_send_time.setVisibility(View.GONE);
			} else {
				group_send_time.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}
	}
	
	private class GoodsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return goods.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return goods.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(arg1 == null){
				arg1 = getLayoutInflater().inflate(R.layout.goods_balance_item_layout, null);
				holder = new ViewHolder(arg1);
				
				arg1.setTag(holder);
			}else{
				holder = (ViewHolder) arg1.getTag();
			}
			ShoppingGoods sg = goods.get(arg0);
			holder.pro_name.setText(sg.getGoodsName());
			holder.pro_num.setText(sg.getGoodsNums()+"");
			holder.pro_price.setText(sg.getGoodsPrice()+"");
			Picasso.with(getApplicationContext()).load(sg.getGoodsImg()).into(holder.pro_img);
			return arg1;
		}
		
		private class ViewHolder{
			private TextView pro_name,pro_price,pro_num;
			private ImageView pro_img;
			public ViewHolder(View view) {
				super();
				this.pro_name = (TextView) view.findViewById(R.id.pro_name);
				this.pro_price = (TextView) view.findViewById(R.id.pro_price);
				this.pro_num = (TextView) view.findViewById(R.id.pro_num);
				this.pro_img = (ImageView) view.findViewById(R.id.pro_img);
			}
		}
		
	}
	

}
