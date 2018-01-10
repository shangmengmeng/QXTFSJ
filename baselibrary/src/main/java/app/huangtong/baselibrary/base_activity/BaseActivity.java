package app.huangtong.baselibrary.base_activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import com.zhy.autolayout.AutoLayoutActivity;
import java.util.List;
import app.huangtong.baselibrary.utils.AppManager;
import butterknife.ButterKnife;


/**
 * Created by FancyMenG on 2017/10/10.
 */

public abstract class BaseActivity extends AutoLayoutActivity {
    private boolean isActive;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加Activity到堆栈
        AppManager.getInstance().addActivity(this);
        // 允许使用transitions,转场动画的配置
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(setLayoutId());
        ButterKnife.bind(this);
        logActivityName();
        initView();
    }

    protected abstract int setLayoutId();

    protected abstract void initView();


    //不带数据的跳转
    public <T extends Activity> void startActivity(Class<T> c) {
        startActivity(new Intent(BaseActivity.this, c));
    }

    //带有数据的跳转
    public <T extends Activity> void startDataActivity(Intent intent, Class<T> c) {
        startActivity(intent);
    }
    //延时跳转
    public <T extends Activity> void delayedStartActivity(final Class<T> c, long time, final boolean finish) {
        Handler han = new Handler();
        han.postDelayed(new Runnable(){

            @Override
            public void run()  {
                startActivity(new Intent(BaseActivity.this, c));
                if (finish) finish();
            }
        }, time);
    }
    //延时跳转附带数据
    public void delayedStartActivity(final Intent in, long time, final boolean finish) {
        Handler han = new Handler();
        han.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(in);
                if (finish) finish();
            }
        }, time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    protected void onDestroy() {
        // 结束Activity&从堆栈中移除
        AppManager.getInstance().finishActivity(this);
        super.onDestroy();
    }
    //用于定位当前activity位置
    public void logActivityName() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        String activityName = (runningTaskInfo.get(0).topActivity.getShortClassName()).toString();
        Log.e("------当前Activity-->:", activityName);
    }

    public boolean isActive() {
        return isActive;
    }
}
