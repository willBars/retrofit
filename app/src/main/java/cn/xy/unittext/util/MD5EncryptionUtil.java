package cn.xy.unittext.util;

import android.content.Context;

import java.security.MessageDigest;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Created by pxw on 2018/5/25.
 */
public class MD5EncryptionUtil {

    public static String encryption(String action, TreeMap<String, String> treeMap, Context context) {
        String sign = null;
        String keyString = action;
        String privatekey = "eb2711fa54974018d4cfdc0fbbea464c";

        for (String key : treeMap.keySet()) {
            keyString += "&" + treeMap.get(key);
        }
        keyString += privatekey;
        sign = getMd5toLowerCase(keyString);

        return sign;
    }
    /**
     * 获取MD5
     *
     * @param input 字符串
     * @return
     */
    public static String getMd5toLowerCase(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            return toHexString(md5.digest()).toLowerCase(Locale.getDefault());
        } catch (Exception e) {
            return "";
        }
    }
    private static String toHexString(byte[] b) throws Exception {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    private static char[] hexChar = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
}
