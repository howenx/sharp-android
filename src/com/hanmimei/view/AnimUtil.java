package com.hanmimei.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by kai.wang on 3/18/14.
 */
public class AnimUtil {

    // height change animation
    public static void collapse(final View v, final int minHeight) {
        final int initialHeight = v.getMeasuredHeight();

        final int offset = initialHeight - minHeight;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.getLayoutParams().height = minHeight;
                    v.requestLayout();
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (offset * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (minHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
    
    // height and width change animation
    public static void collapse2(final View v, final int minHeight,final int minWidth) {
        final int initialHeight = v.getMeasuredHeight();
        final int initialWidth = v.getMeasuredWidth();

        final int offsetHeight = initialHeight - minHeight;
        final int offsetWidth = initialWidth - minWidth;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.getLayoutParams().height = minHeight;
                    v.getLayoutParams().width = minWidth;
                    v.requestLayout();
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (offsetHeight * interpolatedTime);
                    v.getLayoutParams().width = initialWidth - (int) (offsetWidth * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (minHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
