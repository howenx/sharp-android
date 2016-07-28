package com.kakao.kakaogift.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailImgActivity;
import com.kakao.kakaogift.entity.ImageVo;
import com.kakao.kakaogift.utils.GlideLoaderTools;
import com.squareup.picasso.Picasso;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements CBPageAdapter.Holder<ImageVo>{
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
    	View view = LayoutInflater.from(context).inflate(R.layout.goods_detail_slider_img_layout, null);
        imageView = (ImageView) view.findViewById(R.id.mImageView);
        return view;
    }

    @Override
    public void UpdateUI(final Context context,List<ImageVo> datas, final int position, ImageVo data) {
//        GlideLoaderTools.loadSquareImage(context,data.getUrl(),imageView);
    	Picasso.with(context).load(data.getUrl()).placeholder(R.drawable.hmm_place_holder_z).into(imageView);
        final ArrayList<String> list = new ArrayList<String>();
        for(ImageVo info :datas){
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
