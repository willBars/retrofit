package cn.xy.unittext.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by p on 2018/5/25.
 */
public class ToastUtil {

    private static Toast mToast;
    private static int DisPlayTimeLong = Toast.LENGTH_SHORT;
    public static void showToast(Context context, String content, boolean isDisplayTime){
        if(isDisplayTime) {
            DisPlayTimeLong = Toast.LENGTH_LONG;
        }

        if(mToast == null) {
            mToast= Toast.makeText(context,content,DisPlayTimeLong);
        }else {
            mToast.setText(content);
        }
        mToast.show();
    }
}
