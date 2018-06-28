package cn.xy.unittext;


import android.content.Context;
import android.support.annotation.IntDef;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import cn.xy.unittext.bean.ConstantValue;
import cn.xy.unittext.bean.ServiceResultBean;
import cn.xy.unittext.util.MD5EncryptionUtil;
import cn.xy.unittext.util.Phoneuitl;
import cn.xy.unittext.util.VerifyUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by P on 18/5/30.
 */
public class HttpResProcessor<T> {

    private HttpResultCallback mCallback;
    private Map<String, String> mQueryMap;
    private Map<String, String> mPostMap;
    private String mUrl;
    private Class<T> mClazz;
    private @MethodType
    int mMethodType;
    private boolean mNeedRetry = true;
    private HttpRequestServer mServer;
    private static final String TAG = "HttpResProcessor";

    public static <T> HttpResProcessor<T> get() {
        HttpResProcessor<T> netProcessor = new HttpResProcessor<T>();
        return netProcessor;
    }

    public static <T> HttpResProcessor<T> post() {
        HttpResProcessor<T> netProcessor = new HttpResProcessor<T>();
        netProcessor.mPostMap = new HashMap<String, String>();
        return netProcessor;
    }

    private HttpResProcessor() {
        mQueryMap = new HashMap<>();
        mServer = HttpController.getInstance().getmNetProxy().getNetServer();
    }

    public HttpResProcessor<T> putParam(String key, String value) {
        mQueryMap.put(key, value);
        return this;
    }

    public HttpResProcessor<T> postParam(String key, String value) {
        if (mPostMap == null) {
            synchronized (this) {
                mPostMap = new HashMap<String, String>();
            }
        }
        mPostMap.put(key, value);
        return this;
    }

    public HttpResProcessor<T> onQueryMap(Map<String, String> queryMap, Context context) {
        //排序参数
        Map<String, String> newQueryMap = sortParams(queryMap, context);
        this.mQueryMap.putAll(newQueryMap);
        return this;
    }

    private Map<String, String> sortParams(Map<String, String> queryMap, Context context) {

        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        String action = null;
        for (String key : queryMap.keySet()) {
            if (key.equals("action")) {
                action = queryMap.get(key);
            } else {
                treeMap.put(key, queryMap.get(key));
            }
        }

        String gid = "205";
        String app_id = gid;
        String game_key = "4f00a9fbf16889378a33f8ee8adc050f";
        String cid = "311395";
        String devicetoken = Phoneuitl.getIMEI(context);
        String deviceno = devicetoken;
        String privatekey = "eb2711fa54974018d4cfdc0fbbea464c";
        String sid = "1002";

        if (!game_key.isEmpty()) {
            treeMap.put(ConstantValue.GAMEKEY, game_key);
        }
        if (!cid.isEmpty()) {
            treeMap.put(ConstantValue.CHANNELID, cid);
        }
        if (!deviceno.isEmpty()) {
            treeMap.put(ConstantValue.DEVICENO, deviceno);
        }
        if (!devicetoken.isEmpty()) {
            treeMap.put(ConstantValue.DEVICETOKEN, Phoneuitl.getIMEI(context));
            treeMap.put("imei", devicetoken);
        }
        if (!gid.isEmpty()) {
            treeMap.put(ConstantValue.GAMEID, gid);
        }
        if (!app_id.isEmpty()) {
            treeMap.put("app_id", app_id);
        }
        if (!privatekey.isEmpty()) {
            treeMap.put(ConstantValue.PRIVATEKEY, privatekey);
        }
       /* if(!sid.isEmpty()){
            treeMap.put(ConstantValue.SERVICEID, sid);
        }*/

        treeMap.put("ver", "1.0");
        treeMap.put("type", "1");    //android type = 1;
        treeMap.put("ip", Phoneuitl.getIpAddress(context));
        treeMap.put("phone_model", Phoneuitl.getPhoneModel());
        treeMap.put("mac", Phoneuitl.getLocalMacAddress(context));


        /*
         * 激活时才需要传过去的参数
         */
        if ("active".equals(action)) {
            String jsonData = "";
            org.json.JSONObject json = new org.json.JSONObject();
            try {
                json.put("mac", Phoneuitl.getLocalMacAddress(context));
                json.put("phone_ip", Phoneuitl.getIpAddress(context));
                json.put("phone_model", Phoneuitl.getPhoneModel());
                json.put("phone_manufacturer", Phoneuitl.getPhoneManufacturer());
                json.put("phone_version", Phoneuitl.getSystemVersion());
                json.put("phone_resolution", Phoneuitl.getDisplayScreenResolution(context));
                json.put("versionName", VerifyUtil.getVersionName(context));
                json.put("versionCode", VerifyUtil.getVersionCode(context));
                jsonData = json.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            treeMap.put(ConstantValue.JSONDATA, jsonData);
        }

        treeMap.put("sign", MD5EncryptionUtil.encryption(action, treeMap, context));

        for (String key : treeMap.keySet()) {
            queryMap.put(key, treeMap.get(key));
        }

        return queryMap;
    }

    public HttpResProcessor<T> onPostMap(Map<String, String> postMap) {
        if (mPostMap == null) {
            mPostMap.putAll(postMap);
        }
        return this;
    }

    public HttpResProcessor<T> onUrl(String url) {
        this.mUrl = url;
        return this;
    }

    public HttpResProcessor onCallback(HttpResultCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public HttpResProcessor<T> onClazz(Class<T> clazz) {
        this.mClazz = clazz;
        return this;
    }

    public HttpResProcessor<T> onRetry(boolean needRetry) {
        this.mNeedRetry = needRetry;
        return this;
    }

    public HttpResProcessor<T> excute() {
        mServer.getRequest(ConstantValue.URLPath, mQueryMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServiceResultBean>() {

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mCallback != null) {
                            mCallback.onFailed(ErrorCode.CODE_error, e.getMessage());
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServiceResultBean serviceResultBean) {
                        if (mCallback != null) {
                            if (serviceResultBean == null) {
                                mCallback.onFailed(ErrorCode.CODE_RESPONSE_EMPTY, "网络出错，请稍后重试");
                                return;
                            }
                            if (serviceResultBean.code == ErrorCode.CODE_OK) {
                                mCallback.onSuccess(serviceResultBean.code, serviceResultBean.msg);
                            } else {
                                mCallback.onFailed(serviceResultBean.code, serviceResultBean.msg);
                            }
                        }
                    }
                });
        return this;
    }


    @IntDef({MethodType.METHOD_GET, MethodType.METHOD_POST})
    public @interface MethodType {
        // GET请求
        int METHOD_GET = 0;
        // POST请求
        int METHOD_POST = 1;
    }

    private String getQuertString() {
        if (mQueryMap == null) {
            return "";
        }
        List<QueryString> list = new ArrayList<>();
        QueryString query = null;
        Set<String> set = mQueryMap.keySet();
        StringBuilder queryBuilder = new StringBuilder();
        for (String key : set) {
            query = new QueryString(key, mQueryMap.get(key));
            list.add(query);
        }
        Collections.sort(list);
        int size = list.size();
        try {
            for (int i = 0; i < size; i++) {
                queryBuilder.append(list.get(i).toStringAddAnd(i == 0 ? false : true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryBuilder.toString();
    }

    private String getPostString() {
        if (mPostMap == null) {
            return "";
        }
        Set<String> set = mPostMap.keySet();
        StringBuilder postBuilder = new StringBuilder();
        for (String key : set) {
            try {
                postBuilder.append("&").append(key).append("=").append(URLEncoder.encode(mPostMap.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        postBuilder.deleteCharAt(0);
        return postBuilder.toString();
    }

}
