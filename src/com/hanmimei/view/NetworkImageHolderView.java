package com.hanmimei.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.hanmimei.R;
import com.hanmimei.activity.GoodsDetailImgActivity;
import com.hanmimei.utils.InitImageLoader;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements CBPageAdapter.Holder<String>{
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(final Context context,List<String> datas, final int position, String data) {
        imageView.setImageResource(R.drawable.ic_launcher);
        InitImageLoader.loadImage(context,data,imageView);
        final ArrayList<String> list = new ArrayList<String>(datas);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件
                Intent intent = new Intent(context, GoodsDetailImgActivity.class);
                intent.putStringArrayListExtra("imgUrls", list);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }
}
