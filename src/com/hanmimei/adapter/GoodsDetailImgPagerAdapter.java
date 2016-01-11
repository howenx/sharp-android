package com.hanmimei.adapter;

import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.diegocarloslima.byakugallery.lib.TouchImageView;
import com.hanmimei.R;
import com.hanmimei.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
		// TODO Auto-generated constructor stub
		this.imgUrl = imgUrl;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return GalleryFragment.getInstance(this.imgUrl.get(arg0));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgUrl.size();
	}

	public static final class GalleryFragment extends Fragment {

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
			final ProgressBar progress = (ProgressBar) v
					.findViewById(R.id.mProgressBar);
			ImageLoaderUtils.loadImage(getActivity(), imageUrl, image,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
							progress.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
							// TODO Auto-generated method stub
							progress.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
								Bitmap arg2) {
							// TODO Auto-generated method stub
							progress.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
							progress.setVisibility(View.GONE);
						}
					});
			image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					getActivity().finish();
				}
			});

			return v;
		}

	}

}
