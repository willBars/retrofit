package cn.xy.unittext;

import java.io.IOException;

import cn.xy.unittext.bean.ConstantValue;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Created by p on 18/5/26.
 */
public class HttpSetProxy {

    private HttpManager mManger;
    private String BASE_URL = "http://"+ ConstantValue.URLHost+"/";

    public HttpSetProxy(){
        mManger = new HttpManager(BASE_URL, new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder();
                // HttpUrl originalHttpUrl = original.url();
                // String queryString = originalHttpUrl.encodeQuery();  // 获取url中的参数部分
                // String path = originalHttpUrl.url().getPath();       // 获取相对地址
                // Buffer buffer = new Buffer();
                // builder.body().writeTo(buffer);
                // String requestContent = buffer.readUtf8();  // 用于post请求获取form表单内容
                builder.addHeader("key", "value");
                builder.addHeader("Accept-Encoding", "gzip");
                Request request = builder.build();
                return chain.proceed(request);
            }
        }, new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                Response.Builder builder = response.newBuilder();
                if (response.header("Content-Encoding", "").contains("gzip")){
                    BufferedSource bufferedSource = Okio.buffer(new GzipSource(response.body().source()));
                    String temStr = bufferedSource.readUtf8();
                    bufferedSource.close();
                    ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), temStr);
                    builder.body(body);
                }else{
                    BufferedSource bufferedSource = Okio.buffer(response.body().source());
                    String temStr =bufferedSource.readUtf8();
                    bufferedSource.close();
                    ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), temStr);
                    builder.body(body);
                }
                return builder.build();
            }
        });
    }

    public HttpRequestServer getNetServer(){
        return mManger.getServer();
    }
}
