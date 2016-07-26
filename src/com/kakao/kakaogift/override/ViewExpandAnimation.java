/**
 * @Description: TODO(展开 折叠动画) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-10 下午3:52:01 
**/
package com.kakao.kakaogift.override;

import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author vince
 *
 */
public class ViewExpandAnimation extends Animation {

    private View mAnimationView = null;
    private LayoutParams mViewLayoutParams = null;
    private int mStart = 0;
    private int mEnd = 0;

    public ViewExpandAnimation(View view){
        animationSettings(view, 280);
    }

    public ViewExpandAnimation(View view, int duration){
        animationSettings(view, duration);
    }

    private void animationSettings(View view, int duration){
        setDuration(duration);
        mAnimationView = view;
        mViewLayoutParams = (LayoutParams) mAnimationView.getLayoutParams();
        mStart = mViewLayoutParams.bottomMargin;
        mEnd = (mStart == 0 ? (0 - mAnimationView.getHeight()) : 0);
        mAnimationView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if(interpolatedTime < 1.0f){
            mViewLayoutParams.bottomMargin = mStart + (int) ((mEnd - mStart) * interpolatedTime);
            // invalidate
            mAnimationView.requestLayout();
        }else{
            mViewLayoutParams.bottomMargin = mEnd;
            mAnimationView.requestLayout();
            if(mEnd != 0){
                mAnimationView.setVisibility(View.INVISIBLE);
            }
        }
    }

}
