/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-28 上午9:37:52 
**/
package com.hanmimei.activity.goods.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.diegocarloslima.byakugallery.lib.GalleryViewPager;
import com.hanmimei.R;
import com.hanmimei.activity.goods.comment.adapter.GoodsCommentImgPagerAdapter;
import com.hanmimei.entity.GoodsCommentVo;
import com.hanmimei.override.ViewPageChangeListener;

/**
 * 
 * @author vince
 * 
 */
public class GoodsCommentImgActivity extends AppCompatActivity {
	
	private GalleryViewPager mViewPager;
	private TextView pageNum;
	private ImageView ratingBar;
	private TextView content;
	
	
	private GoodsCommentVo vo;
	private int position = 0;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_comment_img_layout);
		getSupportActionBar().hide();
		
		vo = (GoodsCommentVo) getIntent().getSerializableExtra("GoodsCommentVo");
		position = getIntent().getIntExtra("position", 0);

		mViewPager = (GalleryViewPager) findViewById(R.id.mViewPager);
		pageNum = (TextView) findViewById(R.id.pageNum);
		ratingBar = (ImageView) findViewById(R.id.ratingBar);
		content = (TextView) findViewById(R.id.content);

		pageNum.setText((position + 1) + "/" + vo.getCount_num());
		ratingBar.setImageResource(vo.getRemarkList().get(position).getGradeResource());
		content.setText(vo.getRemarkList().get(position).getContent());
		
		
		mViewPager.setAdapter(new GoodsCommentImgPagerAdapter(
				getSupportFragmentManager(), vo.getRemarkList()));
		
		mViewPager.setCurrentItem(position);
		mViewPager.addOnPageChangeListener(new ViewPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				pageNum.setText((arg0 + 1) + "/" + vo.getCount_num());
				ratingBar.setImageResource(vo.getRemarkList().get(arg0).getGradeResource());
				content.setText(vo.getRemarkList().get(arg0).getContent());
			}

		});
	}

}
