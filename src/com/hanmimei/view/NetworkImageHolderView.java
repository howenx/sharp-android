package com.hanmimei.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.hanmimei.R;
import com.hanmimei.activity.GoodsDetailImgActivity;
import com.hanmimei.entity.ImgInfo;
import com.hanmimei.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements CBPageAdapter.Holder<ImgInfo>{
    private ImageView imageView;
    private ProgressBar mProgressBar;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
    	View view = LayoutInflater.from(context).inflate(R.layout.goods_detail_slider_img_layout, null);
        imageView = (ImageView) view.findViewById(R.id.mImageView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.mProgressBar);
        return view;
    }

    @Override
    public void UpdateUI(final Context context,List<ImgInfo> datas, final int position, ImgInfo data) {
        ImageLoaderUtils.loadImage(context,data.getUrl(),imageView,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				mProgressBar.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				mProgressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				mProgressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				mProgressBar.setVisibility(View.GONE);
			}
		});
        final ArrayList<String> list = new ArrayList<String>();
        for(ImgInfo info :datas){
        	list.add(info.getUrl());
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件
                Intent intent = new Intent(context, GoodsDetailImgActivity.class);
                intent.putExtra("imgUrls", list);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }
}
