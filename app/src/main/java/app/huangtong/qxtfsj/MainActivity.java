package app.huangtong.qxtfsj;

import android.os.Bundle;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import app.huangtong.baselibrary.base_activity.BaseActivity;
import app.huangtong.baselibrary.utils.GsonCallBack;
import butterknife.Bind;


public class MainActivity extends BaseActivity {

    @Bind(R.id.tv_tt)
    TextView tvTt;
    @Bind(R.id.activity_main)
    RelativeLayout activityMain;



    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tvTt.setText("hhahahahahahahahaahaha");
        OkHttpUtils.post()
                .url(NetConstant.PERSON_MAIL)
                .build()
                .execute(new GsonCallBack<PersonMailListBean>() {
                    @Override
                    public void onSuccess(final PersonMailListBean response) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

}
