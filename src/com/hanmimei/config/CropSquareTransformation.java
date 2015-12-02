package com.hanmimei.config;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.hanmimei.utils.CommonUtil;
import com.squareup.picasso.Transformation;

public class CropSquareTransformation implements Transformation {
	
	private Context context;
	
	public CropSquareTransformation(Context context) {
		super();
		this.context = context;
	}
	public CropSquareTransformation() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override public Bitmap transform(Bitmap source) {
		int width = source.getWidth();
		int screenWidth = CommonUtil.getScreenWidth(context);
		int sx = screenWidth/width;
		 Matrix matrix = new Matrix(); 
		  matrix.postScale(sx,sx); //长和宽放大缩小的比例
		  Bitmap resizeBmp = Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
	if (resizeBmp != source) {
	source.recycle();
	}
	return resizeBmp;
	}@Override public String key() { return "square()"; }
	}
