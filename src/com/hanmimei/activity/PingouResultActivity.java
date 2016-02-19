package com.hanmimei.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.activity.listener.TimeEndListner;
import com.hanmimei.entity.PinActivity;
import com.hanmimei.entity.PinResult;
import com.hanmimei.entity.PinUser;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ImageLoaderUtils;
import com.hanmimei.utils.KeyWordUtil;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.RoundImageView;
import com.hanmimei.view.TimeDownView;

public class PingouResultActivity extends BaseActivity implements
		TimeEndListner {

	private TimeDownView timer;
	private ListView mListView;
	private GridView gridlayout;
	private PinResult pinResult;
	private TextView pro_title, tuan_guige, master_name, master_time,
			btn_xiadan, about;
	private ImageView tuan_status, pro_img, tuan_state, master_face;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "组团详情");
		setContentView(R.layout.pingou_result_layout);

		findView();
		loadPinUrl();
	}

	private void findView() {
		tuan_status = (ImageView) findViewById(R.id.tuan_status);
		pro_img = (ImageView) findViewById(R.id.imageView1);
		pro_title = (TextView) findViewById(R.id.textView1);
		tuan_guige = (TextView) findViewById(R.id.tuan_guige);
		tuan_state = (ImageView) findViewById(R.id.tuan_state);
		master_face = (ImageView) findViewById(R.id.master_face);
		master_name = (TextView) findViewById(R.id.master_name);
		master_time = (TextView) findViewById(R.id.master_time);
		btn_xiadan = (TextView) findViewById(R.id.btn_xiadan);
		about = (TextView) findViewById(R.id.about);

		timer = (TimeDownView) findViewById(R.id.timer);
		mListView = (ListView) findViewById(R.id.mListView);
		gridlayout = (GridView) findViewById(R.id.gridlayout);

	}

	private void loadPinUrl() {
		Http2Utils.doGetRequestTask(this,getHeaders(), getIntent().getStringExtra("url"),
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							pinResult = new Gson().fromJson(result,
									PinResult.class);
							if (pinResult.getMessage().getCode() == 200) {
								initPageData(pinResult.getActivity());
							} else {
								ToastUtils.Toast(getActivity(), pinResult
										.getMessage().getMessage());
							}
						} catch (Exception e) {
							ToastUtils.Toast(getActivity(), R.string.error);
						}
					}

					@Override
					public void onError() {
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				});
	}

	private void initPageData(final PinActivity pinActivity) {

		if (pinActivity.getStatus().equals("Y")) {
			if (pinActivity.getPay().equals("new")) {
				if (pinActivity.getUserType().equals("master")) {
					tuan_status
							.setImageResource(R.drawable.hmm_kaituan_success);
				} else {
					tuan_status
							.setImageResource(R.drawable.hmm_cantuan_success);
				}
				tuan_state.setImageResource(R.drawable.hmm_zutuan);

				btn_xiadan.setText("还差"
						+ (pinActivity.getPersonNum() - pinActivity
								.getJoinPersons()) + "人，让小伙伴们都来组团吧！");
				btn_xiadan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

					}
				});
			} else {
				tuan_status.setVisibility(View.GONE);
				if (pinActivity.getOrJoinActivity() == 1) {
					btn_xiadan.setText("还差"
							+ (pinActivity.getPersonNum() - pinActivity
									.getJoinPersons()) + "人，让小伙伴们都来组团吧！");
					btn_xiadan.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {

						}
					});
				} else {
					btn_xiadan.setText("立即下单");
					btn_xiadan.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {

						}
					});
				}
			}
			about.setText("还差"
					+ (pinActivity.getPersonNum() - pinActivity
							.getJoinPersons()) + "人，让小伙伴们都来组团吧！");

		} else if (pinActivity.getStatus().equals("F")) {
			tuan_status.setImageResource(R.drawable.hmm_pingou_fail);
			tuan_state.setImageResource(R.drawable.hmm_zutuan_fail);
			about.setVisibility(View.GONE);
			btn_xiadan.setVisibility(View.GONE);
			findViewById(R.id.jishiView).setVisibility(View.GONE);

		} else if (pinActivity.getStatus().equals("C")) {
			tuan_status.setImageResource(R.drawable.hmm_pingou_success);
			tuan_state.setImageResource(R.drawable.hmm_zutuan_success);
			about.setText("对于诸位大侠的相助，团长感激涕零");
			findViewById(R.id.jishiView).setVisibility(View.GONE);

			btn_xiadan.setVisibility(View.GONE);
		}

		ImageLoaderUtils.loadImage(pinActivity.getPinImg().getUrl(), pro_img);
		pro_title.setText(pinActivity.getPinTitle() + "");
		String guige = getResources().getString(R.string.tuan_gui,
				pinActivity.getPersonNum(), pinActivity.getPinPrice());
		KeyWordUtil.setDifferentFontColor13(this, tuan_guige,guige , guige.indexOf("¥")+1, guige.length());
//		tuan_guige.setText(getResources().getString(R.string.tuan_gui,
//				pinActivity.getPersonNum(), pinActivity.getPinPrice()));

		PinUser master = pinActivity.getPinUsersForMaster();
		ImageLoaderUtils.loadImage(master.getUserImg(), master_face);
		master_name.setText("团长" + master.getUserNm());
		master_time.setText(master.getJoinAt() + "开团");
		gridlayout
				.setAdapter(new PinTuanGridAdapter(pinActivity.getPinUsers()));
		mListView.setAdapter(new PinTuanListAdapter(pinActivity
				.getPinUsersForMember()));
		
		findViewById(R.id.btn_see_goods).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PingouDetailActivity.class);
				intent.putExtra("url", pinActivity.getPinSkuUrl());
				startActivity(intent);
			}
		});

		pinActivity.getEndCountDown();
		int[] time = { pinActivity.getEndCountDownForDay(),
				pinActivity.getEndCountDownForHour(),
				pinActivity.getEndCountDownForMinute(),
				pinActivity.getEndCountDownForSecond() };
		timer.setTimes(time);
		timer.setTimeEndListner(this);
		timer.run();
	}

	@Override
	public void isTimeEnd() {
		findViewById(R.id.xiadanView).setVisibility(View.INVISIBLE);
	}

	private class PinTuanListAdapter extends BaseAdapter {

		private List<PinUser> members;

		public PinTuanListAdapter(List<PinUser> members) {
			this.members = members;
		}

		@Override
		public int getCount() {
			return members.size();
		}

		@Override
		public Object getItem(int arg0) {
			return members.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if (arg1 == null) {
				arg1 = getLayoutInflater().inflate(
						R.layout.pingou_result_list_item_layout, null);
				holder = new ViewHolder(arg1);

				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}

			PinUser p = members.get(arg0);
			holder.nameView.setText(p.getUserNm());
			ImageLoaderUtils.loadImage(p.getUserImg(), holder.faceView);
			holder.timeView.setText(p.getJoinAt() + "参团");

			return arg1;
		}

		private class ViewHolder {
			TextView nameView, timeView;
			ImageView faceView;

			public ViewHolder(View view) {
				super();
				this.nameView = (TextView) view.findViewById(R.id.nameView);
				this.timeView = (TextView) view.findViewById(R.id.timeView);
				this.faceView = (ImageView) view.findViewById(R.id.faceView);
			}
		}
	}

	private class PinTuanGridAdapter extends BaseAdapter {

		private List<PinUser> members;

		public PinTuanGridAdapter(List<PinUser> members) {
			this.members = members;
		}

		@Override
		public int getCount() {
			return pinResult.getActivity().getPersonNum();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if (arg1 == null) {
				arg1 = getLayoutInflater().inflate(
						R.layout.pingou_result_grid_item_layout, null);
				holder = new ViewHolder(arg1);

				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			if (arg0 < members.size()) {
				if (members.get(arg0).isOrMaster()) {
					holder.roleView.setVisibility(View.VISIBLE);
				} else {
					holder.roleView.setVisibility(View.INVISIBLE);
				}
				ImageLoaderUtils.loadImage(members.get(arg0).getUserImg(),
						holder.faceView);
				holder.faceView.setBorderColor(getResources().getColor(R.color.theme));
			}else{
				holder.faceView.setBorderColor(getResources().getColor(R.color.qianhui));
			}

			return arg1;
		}

		private class ViewHolder {
			TextView roleView;
			RoundImageView faceView;

			public ViewHolder(View view) {
				super();
				this.roleView = (TextView) view.findViewById(R.id.roleView);
				this.faceView = (RoundImageView) view.findViewById(R.id.faceView);
			}
		}
	}

}
