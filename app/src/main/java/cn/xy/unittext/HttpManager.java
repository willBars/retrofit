package cn.xy.unittext;

import com.parkingwang.okhttp3.LogInterceptor.LogInterceptor;

import java.io.File;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by p on 18/5/26.
 */
public class HttpManager {

    private Retrofit mRetrofit;
    private OkHttpClient mHttpClient;
    private HttpRequestServer mNetServer;

    public HttpManager(String url, Interceptor interceptor, Interceptor netInterceptor){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.addInterceptor(new LoggingInterceptor());
        builder.addInterceptor(new LogInterceptor());
        builder.cache(new Cache(new File(MyApplication.getInstance().getApplicationContext().getCacheDir(), "caches"), 1024 * 1024 * 100));
        mHttpClient = builder.addInterceptor(interceptor).addNetworkInterceptor(netInterceptor).retryOnConnectionFailure(true).build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(mHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mNetServer = mRetrofit.create(HttpRequestServer.class);
    }

    public HttpRequestServer getServer(){
        return mNetServer;
    }
}
