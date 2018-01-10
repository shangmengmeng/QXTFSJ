package app.huangtong.baselibrary.app;

import android.app.Application;
import android.util.Log;
import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by FancyMenG on 2018/1/7.
 */

public class LibraryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(true,"--------->");
        //-----------------------------------------网络请求-----------------------------------------
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.w("---------Net:",message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

    }
}
