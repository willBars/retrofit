package cn.xy.unittext;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import cn.xy.unittext.bean.ServiceResultBean;
import cn.xy.unittext.util.SharePrefrenceUtil;

/**
 * Created by p on 18/5/27.
 */
public class HttpStartModel {

    public static HttpResProcessor getActiveState(Context context, HttpResultCallback callback){
        Map<String, String> params = new HashMap<>();
        params.put("action", "active");
        params.put("idfv", SharePrefrenceUtil.getInstance().getActiveIdfv(context));
        return HttpResProcessor.<ServiceResultBean>get()
            .onCallback(callback)
            .onRetry(true)
           // .onClazz(ServiceResultBean.class)
           // .onUrl("/get/user")
            .onQueryMap(params,context)
            .excute();
    }

    public static HttpResProcessor getLoginState(MainActivity mainActivity,Map<String, String> mapParams, HttpResultCallback httpResultCallback) {
       // mapParams.put("token", "");
        return HttpResProcessor.<ServiceResultBean>get()
                .onCallback(httpResultCallback)
                .onRetry(true)
                // .onClazz(ServiceResultBean.class)
                // .onUrl("/get/user")
                .onQueryMap(mapParams,mainActivity)
                .excute();
    }
}
