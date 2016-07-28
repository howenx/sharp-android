package com.kakao.kakaogift.activity.goods.detail.adapter;

import java.util.List;

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

import com.diegocarloslima.byakugallery.lib.TouchImageView;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.utils.GlideLoaderTools;
import com.squareup.picasso.Picasso;

/**
 * 商品详情页 轮播图展示
 * 
 * @author Administrator
 * 
 */
public class GoodsDetailImgPagerAdapter extends FragmentStatePagerAdapter {

	private List<String> imgUrl;


	public GoodsDetailImgPagerAdapter(FragmentManager fm, List<String> imgUrl) {
		super(fm);
		this.imgUrl = imgUrl;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return GalleryFragment.getInstance(this.imgUrl.get(arg0));
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
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View v = inflater.inflate(
					R.layout.good_detail_img_item_layout, null);

			final TouchImageView image = (TouchImageView) v
					.findViewById(R.id.mImageView);
			final String imageUrl = getArguments().getString("imageUrl");
			Picasso.with(getActivity()).load(imageUrl).placeholder(R.drawable.hmm_place_holder_z).into(image);
			image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mActivity.finish();
				}
			});
			return v;
		}

	}

}
