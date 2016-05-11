/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-28 上午9:37:52 
**/
package com.hanmimei.activity.goods.comment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.diegocarloslima.byakugallery.lib.GalleryViewPager;
import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.activity.goods.comment.adapter.GoodsCommentImgPagerAdapter;
import com.hanmimei.entity.GoodsCommentVo;
import com.hanmimei.entity.RemarkVo;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.override.ViewPageChangeListener;
import com.hanmimei.utils.ToastUtils;

/**
 * 
 * @author vince
 * 
 */
public class GoodsCommentImgActivity extends AppCompatActivity {
	
	private GalleryViewPager mViewPager;
	private GoodsCommentImgPagerAdapter adapter;
	private TextView pageNum;
	private TextView size;
	private ImageView ratingBar;
	private TextView content;
	private List<RemarkVo> pics;
	
	
	private GoodsCommentVo vo;
	private int position = 0;
	
	private int index = 1;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_comment_img_layout);
		getSupportActionBar().hide();
		
		pics= new ArrayList<RemarkVo>();
		vo = (GoodsCommentVo) getIntent().getSerializableExtra("GoodsCommentVo");
		position =vo.getPosition();
		index = vo.getIndex();
		pics.addAll(vo.getPics());

		mViewPager = (GalleryViewPager) findViewById(R.id.mViewPager);
		pageNum = (TextView) findViewById(R.id.pageNum);
		size = (TextView) findViewById(R.id.size);
		ratingBar = (ImageView) findViewById(R.id.ratingBar);
		content = (TextView) findViewById(R.id.content);

		pageNum.setText((position + 1) + "/" + vo.getCount_num());
		ratingBar.setImageResource(pics.get(position).getGradeResource());
		content.setText(pics.get(position).getContent());
		size.setText("规格："+pics.get(position).getSize());
		
		 adapter =new GoodsCommentImgPagerAdapter(
				getSupportFragmentManager(), pics);
		mViewPager.setAdapter(adapter);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setCurrentItem(position);
		mViewPager.addOnPageChangeListener(new ViewPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				pageNum.setText((arg0 + 1) + "/" + vo.getCount_num());
				ratingBar.setImageResource(pics.get(arg0).getGradeResource());
				content.setText(pics.get(arg0).getContent());
				size.setText("规格："+pics.get(arg0).getSize());
				if(arg0+2 >= pics.size()){
					loadMorePics();
				}
			}

		});
	}
	
	private void loadMorePics(){
		if(vo.getLoadUrl() == null)
			return;
		if(index >= vo.getPage_count())
			return;
		index++;
		VolleyHttp.doGetRequestTask(vo.getLoadUrl()+index, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				GoodsCommentVo vo = new Gson().fromJson(result, GoodsCommentVo.class);
				pics.addAll(vo.getRemarkList());
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onError() {
				ToastUtils.Toast(GoodsCommentImgActivity.this, R.string.error);
			}
		});
	}

}
