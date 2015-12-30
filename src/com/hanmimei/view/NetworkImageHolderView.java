package com.hanmimei.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.hanmimei.R;
import com.hanmimei.activity.GoodsDetailImgActivity;
import com.hanmimei.entity.HMMGoods.ImgInfo;
import com.hanmimei.utils.ImageLoaderUtils;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements CBPageAdapter.Holder<ImgInfo>{
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(final Context context,List<ImgInfo> datas, final int position, ImgInfo data) {
        imageView.setImageResource(R.drawable.ic_launcher);
        ImageLoaderUtils.loadImage(context,data.getUrl(),imageView);
        final ArrayList<ImgInfo> list = new ArrayList<ImgInfo>(datas);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件
                Intent intent = new Intent(context, GoodsDetailImgActivity.class);
                ImgInfos infos= new ImgInfos();
                infos.setList(list);
                intent.putExtra("imgUrls", infos);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }
    
    public class ImgInfos implements Serializable{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<ImgInfo> list;
		public ArrayList<ImgInfo> getList() {
			return list;
		}
		public void setList(ArrayList<ImgInfo> list) {
			this.list = list;
		}
    }
}
