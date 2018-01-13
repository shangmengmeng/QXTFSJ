package app.huangtong.qxtfsj;


import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import app.huangtong.baselibrary.base_activity.BaseActivity;
import app.huangtong.baselibrary.common_view.webview.WebViewActivity;
import app.huangtong.baselibrary.utils.GsonCallBack;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_tt)
    TextView tvTt;
    @Bind(R.id.activity_main)
    RelativeLayout activityMain;
    @Bind(R.id.tv_hh)
    TextView tvHh;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tvTt.setText("hhahahahahahahahaahaha");
        tvHh.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hh:
                WebViewActivity.loadUrl(this,"https://www.baidu.com/","呵呵");
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
