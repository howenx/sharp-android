package com.hanmimei.utils;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

public class WaveAnimationUtil {

	
	public static void waveAnimation(final ImageView imageView,float animation_size){
		//创建一个AnimationSet对象，参数为Boolean型，
        //true表示使用Animation的interpolator，false则是使用自己的
		final AnimationSet animationSet = new AnimationSet(true);
        //参数1：x轴的初始值
        //参数2：x轴收缩后的值
        //参数3：y轴的初始值
        //参数4：y轴收缩后的值
        //参数5：确定x轴坐标的类型
        //参数6：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
        //参数7：确定y轴坐标的类型
        //参数8：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
        ScaleAnimation scaleAnimation = new ScaleAnimation(
               1, animation_size,1,animation_size,
               Animation.RELATIVE_TO_SELF,0.5f,
               Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1200);
        animationSet.addAnimation(scaleAnimation);
		
		
        //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0);
        //设置动画执行的时间
        alphaAnimation.setDuration(1200);
        //将alphaAnimation对象添加到AnimationSet当中
        animationSet.addAnimation(alphaAnimation);
        //使用ImageView的startAnimation方法执行动画
        animationSet.setFillAfter(true);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				imageView.startAnimation(animationSet);
			}
		});
        imageView.startAnimation(animationSet);
	}
	
	public static void waveAnimation(final View imageView){
		ValueAnimator animatorY = ObjectAnimator.ofFloat(
				imageView,"y",0,-100,0).setDuration(1000);
		ValueAnimator animatorX = ObjectAnimator.ofFloat(
				imageView,"x",0,-100).setDuration(1000);
		animatorY.start();
		animatorX.start();
	}
	
}
