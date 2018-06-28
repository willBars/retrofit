package cn.xy.unittext.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

import cn.xy.unittext.bean.ConstantValue;

/**
 * Created by pxw on 2018/5/25.
 */
public class SharePrefrenceUtil {

    private static SharePrefrenceUtil instance;
    private static byte[] lock = new byte[0];

    private SharePrefrenceUtil() {	}

    public static SharePrefrenceUtil getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new SharePrefrenceUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 新增设备激活统计 idfv
     * @param context
     * @param idfv
     */
    public void setActiveIdfv(Context context,String idfv){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstantValue.ACTIVE_IDFV, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(ConstantValue.ACTIVE_IDFV_FLAG, idfv).apply();
    }

    @SuppressLint("NewApi") public String getActiveIdfv(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConstantValue.ACTIVE_IDFV, Context.MODE_PRIVATE);
        String idfv = sharedPreferences.getString(ConstantValue.ACTIVE_IDFV_FLAG, "");
        if (idfv.isEmpty()) {
            idfv = UUID.randomUUID().toString();
            setActiveIdfv(context,idfv);
        }
        return idfv;
    }
    /**
     * 保存添加的子项目个数
     */
    public void saveAddItemView(Context context,int itemNumber){
        SharedPreferences sp = context.getSharedPreferences(ConstantValue.SAVE_ITEM, Context.MODE_PRIVATE);
        sp.edit().putInt(ConstantValue.SAVE_ITEM_VIEW,itemNumber).apply();
    }

    public int getAddItemView(Context context){
        SharedPreferences sp = context.getSharedPreferences(ConstantValue.SAVE_ITEM, Context.MODE_PRIVATE);
        int itemNumber = sp.getInt(ConstantValue.ACTIVE_IDFV_FLAG, 0);
        return itemNumber;
    }
}
