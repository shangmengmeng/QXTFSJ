package app.huangtong.baselibrary.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by pig on 2017/5/24.
 */

public class ScaleUtils {
    public static void getScaleAnimator(View view){
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view,"ScaleX",1.1f,1.2f,1.1f,1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view,"ScaleY",1.1f,1.2f,1.1f,1f);
        set.playTogether(scaleX,scaleY);
        set.setDuration(100);
        set.start();
    }
    public static void getScaleAnimatorSmall(View view){
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view,"ScaleX",0.9f,0.8f,0.9f,1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view,"ScaleY",0.9f,0.8f,0.9f,1f);
        set.playTogether(scaleX,scaleY);
        set.setDuration(100);
        set.start();
    }
}
