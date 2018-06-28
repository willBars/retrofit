package cn.xy.unittext.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by p on 2018/5/25.
 */
public class NetWorkUtil {

    static String strNetworkType = "";
    private static String iPs;

    /**
     * 检查当前是否有网络及其网络类型
     * */
    public static boolean checkNetStateInfo(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            ToastUtil.showToast(context, "当前无网络连接状态", false);
            return false;
        }
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            ToastUtil.showToast(context, "请检查当前网络状态", false);
            return false;
        } else {
            int type = info.getType();
            String subtypeName = info.getSubtypeName();
            switch (type) {
                case ConnectivityManager.TYPE_WIFI:
//                    String wifiIpAdress = getWifiIpAdress(context);
                    String wifiIpAdress = GetNetIp();
                    strNetworkType = "wifi"+"=="+wifiIpAdress;
                case ConnectivityManager.TYPE_MOBILE:
//                    String netpAdress = getNetInterIpAdress();
                    String netpAdress = GetNetIp();
                    int subtype = info.getSubtype();
                    switch (subtype) {
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                            strNetworkType = "2g"+"=="+netpAdress;

                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            strNetworkType = "3g"+"=="+netpAdress;

                        case TelephonyManager.NETWORK_TYPE_LTE:
                            strNetworkType = "4g"+"=="+netpAdress;
                    }
                    ToastUtil.showToast(context, strNetworkType, true);
                    return true;
                default:
//                    String netpIpAdress = getNetInterIpAdress();
                    String netpIpAdress = GetNetIp();
                    if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                            || subtypeName.equalsIgnoreCase("WCDMA")
                            || subtypeName.equalsIgnoreCase("CDMA2000")) {
                        strNetworkType = "3G"+"=="+netpIpAdress;
                    } else {
                        strNetworkType = subtypeName+"=="+netpIpAdress;
                    }
                    ToastUtil.showToast(context, strNetworkType, true);
                    return true;
            }
        }
    }

    //路由
    private static String getWifiIpAdress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return intIP2StringIP(wifiInfo.getIpAddress());
        }
       return "获取WiFi地址失败";
    }

    //路由ip
    private static String getNetInterIpAdress(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "获取ip地址失败";
    }
    /**
     * 获取url域名中的host
     * */
    public static  String getHost(final String url) {
        if (url == null || url.trim().equals("")) {
            return "";
        }
        String host = "";
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+(:\\d*)?");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }
        return host;
    }
    /**
     * 获取url中path路径
     */
    public static String getURIPath(String url){
        return  Uri.parse(url).getPath().substring(1);
    }


    /**
     * 将得到的int类型的IP转换为String类型
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
    /**
     * 获取外网
     */

    private static String GetNetIp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
                    URL url = new URL(address);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setUseCaches(false);

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        InputStream in = connection.getInputStream();

                        // 将流转化为字符串
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(in));

                        String tmpString = "";
                        StringBuilder retJSON = new StringBuilder();
                        while ((tmpString = reader.readLine()) != null)
                        {
                            retJSON.append(tmpString + "\n");
                        }
                        Log.e("tag",retJSON.toString());
                        JSONObject jsonObject = new JSONObject(retJSON.toString());
                        String code = jsonObject.getString("code");
                        if (code.equals("0")){
                            JSONObject data = jsonObject.getJSONObject("data");
                            iPs = data.getString("ip") + "(" + data.getString("country")
                                       + data.getString("area") + "区"
                                       + data.getString("region") + data.getString("city")
                                       + data.getString("isp") + ")";
                            Log.e("提示", "您的IP地址是：" + iPs);
                        } else{
                            Log.e("提示", "IP接口异常，无法获取IP地址！");
                        }
                    }else{
                        Log.e("提示", "网络连接异常，无法获取IP地址！");
                    }
                }catch (Exception e){
                    Log.e("提示", "获取IP地址时出现异常，异常信息是：" + e.toString());
                }
            }
        }).start();
        return iPs;
    }
}
