package cn.xy.unittext;

/**
 * Created by p on 18/5/28.
 */
public interface HttpResultCallback{

    void onSuccess(int code, String msg);

    void onFailed(int code, String msg);

}
