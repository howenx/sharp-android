package com.kakao.kakaogift.activity.mine.pin;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.mine.comment.CommentGoodsActivity;
import com.kakao.kakaogift.activity.mine.order.LogisticsActivity;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.Order;
import com.kakao.kakaogift.entity.OrderList;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.AlertDialogUtils;
import com.kakao.kakaogift.utils.GlideLoaderTools;
import com.kakao.kakaogift.utils.ToastUtils;

/**
 * 
 * @author vince
 * 
 */
public class MyPingouDetailActivity extends BaseActivity {

	private TextView order_state, order_price, order_addr, order_person,
			pro_title, pro_guige;
	private TextView order_no, order_time, btn_left, btn_right;
	private ImageView order_img, pro_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_pingou_detail_layout);
		ActionBarUtil.setActionBarStyle(this, "拼购详情");
		findView();
		loadPingouDetail();

	}

	private void loadPingouDetail() {
		getLoading().show();
		Log.i("orderId", getIntent().getStringExtra("orderId"));
		VolleyHttp.doGetRequestTask(getHeaders(), UrlUtil.GET_ORDER_LIST_URL
				+ "/" + getIntent().getStringExtra("orderId"),
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						OrderList orderList = DataParser.parserOrder(result);
						// List<Order> orders = DataParser.parserOrder(result);
						initPingouDetail(orderList.getList());
						getLoading().dismiss();
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				});
	}

	private void initPingouDetail(List<Order> orders) {
//		I:初始化即未支付状态，S:成功，C：取消， F:失败，R:已收货，D:已经发货，J:拒收， T已退款
		if (orders != null && orders.size() > 0) {
			final Order o = orders.get(0);
			if (o.getOrderStatus().equals("S")) {
				order_state.setText("等待配送");
				order_img.setImageResource(R.drawable.hmm_order_submit);
				btn_left.setVisibility(View.GONE);
				btn_right.setVisibility(View.GONE);
			} else if (o.getOrderStatus().equals("D")) {
				order_state.setText("配送中");
				order_img.setImageResource(R.drawable.hmm_distribution);
				btn_left.setVisibility(View.VISIBLE);
				btn_right.setVisibility(View.VISIBLE);
				btn_left.setText("查看物流");

				btn_left.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								LogisticsActivity.class);
						intent.putExtra("orderId", o.getOrderId());
						startActivity(intent);
					}
				});
				btn_right.setText("确认收货");
				btn_right.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						confirmDelivery(o);
					}
				});

			} else if (o.getOrderStatus().equals("R")) {
				order_state.setText("已签收");
				order_img.setImageResource(R.drawable.hmm_sign);
				btn_left.setVisibility(View.VISIBLE);
				btn_right.setVisibility(View.VISIBLE);
				btn_right.setText("评价晒单");
				btn_left.setText("查看物流");

				btn_left.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								LogisticsActivity.class);
						intent.putExtra("orderId", o.getOrderId());
						startActivity(intent);
					}
				});
				
				btn_right.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								CommentGoodsActivity.class);
						intent.putExtra("orderId", o.getOrderId());
						startActivity(intent);
					}
				});
			} else {
				order_state.setText("已过期");
				order_img.setImageResource(R.drawable.hmm_order_error);
				btn_left.setVisibility(View.GONE);
				btn_right.setVisibility(View.GONE);
			}
			order_addr.setText(o.getAdress().getCity()
					+ o.getAdress().getAdress());
			order_price.setText("¥" + o.getPayTotal());
			order_no.setText(o.getOrderId());
			order_person.setText(o.getAdress().getName() + "    "
					+ o.getAdress().getPhone());
			order_time.setText(o.getOrderCreateAt());

			GlideLoaderTools.loadSquareImage(getActivity(), o.getList().get(0)
					.getInvImg(), pro_img);
			pro_guige.setText("¥" + o.getList().get(0).getPrice());
			pro_title.setText(o.getList().get(0).getSkuTitle());
		} else {
			ToastUtils.Toast(this, R.string.error);
		}
	}

	private void findView() {
		order_state = (TextView) findViewById(R.id.order_state);
		order_price = (TextView) findViewById(R.id.order_price);
		order_addr = (TextView) findViewById(R.id.order_addr);
		order_person = (TextView) findViewById(R.id.order_person);
		pro_title = (TextView) findViewById(R.id.pro_title);
		pro_guige = (TextView) findViewById(R.id.pro_guige);
		order_no = (TextView) findViewById(R.id.order_no);
		order_time = (TextView) findViewById(R.id.order_time);
		btn_left = (TextView) findViewById(R.id.btn_left);
		btn_right = (TextView) findViewById(R.id.btn_right);
		order_img = (ImageView) findViewById(R.id.order_img);
		pro_img = (ImageView) findViewById(R.id.pro_img);
	}

	// 确认收货操作
	private void confirmDelivery(final Order order) {
		AlertDialogUtils.showDeliveryDialog(this, new OnClickListener() {

			@Override
			public void onClick(View v) {
				confirmDeliveryR(order);
			}
		});
	}

	private void confirmDeliveryR(Order order) {
		getLoading().show();
		VolleyHttp.doGetRequestTask(getHeaders(), UrlUtil.CONFIRM_DELIVERY
				+ order.getOrderId(), new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				getLoading().dismiss();
				
			}

			@Override
			public void onError() {
				getLoading().dismiss();
				loadPingouDetail();
			}
		});
	}

}
