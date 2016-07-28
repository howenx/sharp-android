package com.kakao.kakaogift.override;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class HMMBitmapImageViewTarget extends BitmapImageViewTarget {
	
	

	 public HMMBitmapImageViewTarget(ImageView view) {
	      super(view);
	   }

	   @Override
	   public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
	      if (bitmap != null && view.getScaleType() != ScaleType.FIT_CENTER) {
	         view.setScaleType(ScaleType.FIT_CENTER);
	      }
	      super.onResourceReady(bitmap, anim);
	   }

	   @Override
	   protected void setResource(Bitmap resource) {
	      super.setResource(resource);
	   }

	   @Override
	   public void onLoadFailed(Exception e, Drawable errorDrawable) {
	      if (errorDrawable != null && view != null && view.getScaleType() != ScaleType.FIT_CENTER) {
	         view.setScaleType(ScaleType.FIT_CENTER);
	      }
	      super.onLoadFailed(e, errorDrawable);
	   }

	   @Override
	   public void onLoadStarted(Drawable placeholder) {
	      if (placeholder != null && placeholder != null && view != null && view.getScaleType() != ScaleType.FIT_CENTER) {
	         view.setScaleType(ScaleType.FIT_CENTER);
	      }
	      super.onLoadStarted(placeholder);
	   }

	   @Override
	   public void onLoadCleared(Drawable placeholder) {
	      if (placeholder != null && placeholder != null && view != null && view.getScaleType() != ScaleType.FIT_CENTER) {
	         view.setScaleType(ScaleType.FIT_CENTER);
	      }
	      super.onLoadCleared(placeholder);
	   }

}
