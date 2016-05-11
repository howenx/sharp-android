/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-11 上午11:08:48 
**/
package com.hanmimei.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.hanmimei.R;
import com.hanmimei.override.SimpleAnimationListener;
import com.nineoldandroids.animation.Keyframe;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

/**
 * @author vince
 *
 */
public class AnimationTools {

	public static ObjectAnimator tada(View view) {  
	    return tada(view, 1f);  
	}  
	  
	public static ObjectAnimator tada(View view, float shakeFactor) {  
	  
	    PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe("scaleX",  
	            Keyframe.ofFloat(0f, 1f),  
	            Keyframe.ofFloat(.1f, .9f),  
	            Keyframe.ofFloat(.2f, .9f),  
	            Keyframe.ofFloat(.3f, 1.1f),  
	            Keyframe.ofFloat(.4f, 1.1f),  
	            Keyframe.ofFloat(.5f, 1.1f),  
	            Keyframe.ofFloat(.6f, 1.1f),  
	            Keyframe.ofFloat(.7f, 1.1f),  
	            Keyframe.ofFloat(.8f, 1.1f),  
	            Keyframe.ofFloat(.9f, 1.1f),  
	            Keyframe.ofFloat(1f, 1f)  
	    );  
	  
	    PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe("scaleY",  
	            Keyframe.ofFloat(0f, 1f),  
	            Keyframe.ofFloat(.1f, .9f),  
	            Keyframe.ofFloat(.2f, .9f),  
	            Keyframe.ofFloat(.3f, 1.1f),  
	            Keyframe.ofFloat(.4f, 1.1f),  
	            Keyframe.ofFloat(.5f, 1.1f),  
	            Keyframe.ofFloat(.6f, 1.1f),  
	            Keyframe.ofFloat(.7f, 1.1f),  
	            Keyframe.ofFloat(.8f, 1.1f),  
	            Keyframe.ofFloat(.9f, 1.1f),  
	            Keyframe.ofFloat(1f, 1f)  
	    );  
	  
	    PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe("rotate",  
	            Keyframe.ofFloat(0f, 0f),  
	            Keyframe.ofFloat(.1f, -3f * shakeFactor),  
	            Keyframe.ofFloat(.2f, -3f * shakeFactor),  
	            Keyframe.ofFloat(.3f, 3f * shakeFactor),  
	            Keyframe.ofFloat(.4f, -3f * shakeFactor),  
	            Keyframe.ofFloat(.5f, 3f * shakeFactor),  
	            Keyframe.ofFloat(.6f, -3f * shakeFactor),  
	            Keyframe.ofFloat(.7f, 3f * shakeFactor),  
	            Keyframe.ofFloat(.8f, -3f * shakeFactor),  
	            Keyframe.ofFloat(.9f, 3f * shakeFactor),  
	            Keyframe.ofFloat(1f, 0)  
	    );  
	  
	    return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).  
	            setDuration(1000);  
	}  
	
	public static ObjectAnimator nope(View view) {
        int delta = view.getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe("translationX",
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(500);
    }
	
	
	public static ObjectAnimator initAnimatorSetValue(Context context,View view,SimpleAnimationListener l) {
		
		int translationX = CommonUtils.getScreenWidth(context) * 4 / 11;

		PropertyValuesHolder pvhSX = PropertyValuesHolder.ofFloat("scaleX", 1f,
				0.3f);
		PropertyValuesHolder pvhSY = PropertyValuesHolder.ofFloat("scaleY", 1f,
				0.3f);
		PropertyValuesHolder pvhTY = PropertyValuesHolder.ofFloat(
				"translationY", 0, -250, 50);
		PropertyValuesHolder pvhTX = PropertyValuesHolder.ofFloat(
				"translationX", 0, -translationX);
		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, pvhSX,
				pvhSY, pvhTY, pvhTX).setDuration(1200);
		objectAnimator.setInterpolator(new DecelerateInterpolator());
		objectAnimator.addListener(l);
		return objectAnimator;
	}
}
