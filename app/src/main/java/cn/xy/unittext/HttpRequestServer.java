package cn.xy.unittext;

import java.util.Map;

import cn.xy.unittext.bean.ServiceResultBean;
import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by p on 18/5/26.
 */
public interface HttpRequestServer {

    @GET("{path}")
    Observable<ServiceResultBean> getRequest(@Path("path") String path, @QueryMap Map<String, String> map );

    @FormUrlEncoded
    @POST("{path}")
    Observable<ServiceResultBean> postRequest( @Path("path") String path, @FieldMap Map<String, String> postMap);
}
