package com.kakao.kakaogift.activity.goods.comment.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.diegocarloslima.byakugallery.lib.TouchImageView;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.entity.RemarkVo;

/**
 * 商品详情页 轮播图展示
 * 
 * @author Administrator
 * 
 */
public class GoodsCommentImgPagerAdapter extends FragmentStatePagerAdapter {

	private List<RemarkVo> imgUrl;


	public GoodsCommentImgPagerAdapter(FragmentManager fm, List<RemarkVo> imgUrl) {
		super(fm);
		this.imgUrl = imgUrl;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return GalleryFragment.getInstance(this.imgUrl.get(arg0).getPicture());
	}

	@Override
	public int getCount() {
		return imgUrl.size();
	}

	public static final class GalleryFragment extends Fragment {
		
		private Activity mActivity;
		@Override
		public void onAttach(Context context) {
			super.onAttach(context);
			mActivity = (Activity) context;
		}

		public static GalleryFragment getInstance(String imageUrl) {
			final GalleryFragment instance = new GalleryFragment();
			final Bundle params = new Bundle();
			params.putString("imageUrl", imageUrl);
			instance.setArguments(params);

			return instance;
		}
		
		@SuppressLint("InflateParams")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View v = inflater.inflate(
					R.layout.good_detail_img_item_layout, null);

			final TouchImageView image = (TouchImageView) v
					.findViewById(R.id.mImageView);
			final String imageUrl = getArguments().getString("imageUrl");
//			GlideLoaderUtils.loadSquareImage(mActivity, imageUrl, image);
			Glide.with(this).load(imageUrl)
			.placeholder(R.drawable.hmm_place_holder_z).fitCenter()
			.animate(R.anim.abc_fade_in).thumbnail(0.1f).into(image);
			
			
			image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mActivity.finish();
				}
			});

			return v;
		}

	}
	
	private int mChildCount = 0;
	 
    @Override
    public void notifyDataSetChanged() {         
          mChildCount = getCount();
          super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object)   {          
          if ( mChildCount > 0) {
          mChildCount --;
          return POSITION_NONE;
          }
          return super.getItemPosition(object);
    }

}
