/**
 * @Title: Phoneuitls.java
 * @Package com.android.cansh.sdk.utils.phone
 * Copyright: Copyright (c) 2013
 * Company: 广州灿和信息科技有限公司
 * @author xiaoming.yuan
 * @date 2014-2-26 下午3:19:43
 * @version V1.0
 */

package cn.xy.unittext.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.List;

/**
 * @ClassName: PhoneUitl
 * @date 2014-2-26 下午3:19:43
 */

public class Phoneuitl {
	private static final String TAG = "Phoneuitls";

	// 手机常量
	public static final String MODE = android.os.Build.MODEL;

	public static final String OS = "android";

	public static final String OSVER = "android+" + android.os.Build.VERSION.RELEASE;

	/**
	 * getNumber(获取手机号。注：有些手机卡获取不到手机号) (这里描述这个方法适用条件 – 可选)
	 *
	 * @param context
	 * @return String
	 * @exception
	 * @since 1.0.0xiaoming.yuan
	 */
	public static String getNumber(Context context) {
		String number = "";
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		number = tm.getLine1Number();
		if (!TextUtils.isEmpty(number) && number.startsWith("+86")) {
			number = number.substring(3);
		}
		return number;
	}

	/**
	 * 获取cpu 型号
	 * @return
	 */
	public static String getCpuModel(){
		return android.os.Build.CPU_ABI;
	}

	/**
	 * Role:Telecom service providers获取手机服务商信息 <BR>
	 * 需要加入权限<uses-permission
	 * android:name="android.permission.READ_PHONE_STATE"/> <BR>
	 * Date:2012-3-12 <BR>
	 *
	 * @author CODYY)yuanxiaoming
	 */
	public String getProvidersName(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String ProvidersName = null;
		// 返回唯一的用户ID;就是这张卡的编号神马的
		String IMSI = telephonyManager.getSubscriberId();
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			ProvidersName = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			ProvidersName = "中国联通";
		} else if (IMSI.startsWith("46003")) {
			ProvidersName = "中国电信";
		}
		return ProvidersName;
	}

	/**
	 * getManufacture(获取制造厂商) (这里描述这个方法适用条件 – 可选)
	 *
	 * @return String
	 * @exception
	 * @since 1.0.0 xiaoming.yuan
	 */
	public static String getBrand() {
		return Build.MODEL;
	}

	/**
	 * @Title: getLocalMacAddress(取得手机的mac)
	 * @author xiaoming.yuan
	 * @data 2013-8-10 下午3:40:46
	 * @param: mContext
	 * @return
	 * @return String 返回类型
	 */
	public static String getLocalMacAddress(Context mContext) {

		WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();

		return info.getMacAddress() == null ? "" : info.getMacAddress();
	}

	/**
	 * @Title: getIMEI(设备的IMEI,IMSI)
	 * @author
	 * @data
	 * @param: @param mContext
	 * @param: @return
	 * @return String 返回类型
	 */
	public static String getIMEI(Context mContext) {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = telephonyManager.getDeviceId();
			if (TextUtils.isEmpty(imei)) {
				imei = telephonyManager.getSubscriberId();
				if (TextUtils.isEmpty(imei)) {
					imei = telephonyManager.getSimSerialNumber();
					if (TextUtils.isEmpty(imei)) {
						imei = Settings.System.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID);
					} else {
						// UUID.randomUUID().toString()
						return "";
					}
				}
			}
			return imei;
		} catch (Exception e) {
			return "";
		}

		// return new Imei(mContext).getimei();
	}

	/**
	 * 获取ip地址
	 *
	 * @param context
	 * @return
	 */
	public static String getIpAddress(Context context) {
		String ipAddress = "";
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) { // 判断wifi是否开启
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			ipAddress = intToIp(wifiInfo.getIpAddress());
		} else {
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							ipAddress = inetAddress.getHostAddress().toString();
						}
					}
				}
			} catch (Exception ex) {
				// Log.e("WifiPreference IpAddress", ex.toString());
			}

		}

		return ipAddress;
	}

	/**
	 * 获取系统版本号
	 *
	 * @return
	 */
	public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机型号
	 *
	 * @return
	 */
	public static String getPhoneModel() {
		return android.os.Build.MODEL;
	}

	public static String getCurrentTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	/**
	 * 手机厂商
	 *
	 * @return
	 */
	public static String getPhoneManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	private static String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

	/**
	 * @Title: getDisplayScreenResolution(手机的分辨率)
	 * @author
	 * @data
	 * @param: @param mContext
	 * @param: @return
	 * @return String 返回类型
	 */
	public static String getDisplayScreenResolution(Context mContext) {
		int screen_w = 0;
		int screen_h = 0;
		try {
			screen_h = mContext.getResources().getDisplayMetrics().heightPixels;
			screen_w = mContext.getResources().getDisplayMetrics().widthPixels;
		} catch (Exception e) {
			return screen_w + "*" + screen_h;
		}
		return screen_w + "*" + screen_h;
	}
	
	/**
	 * 悬浮框开启提示
	 * @param context
	 */
	public static void applyCommonPermission(Context context) {
        try {
            Class clazz = Settings.class;
            Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            Intent intent = new Intent(field.get(null).toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
//            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
        }
    }
	
	/** 
	    * 判断 悬浮窗口权限是否打开 
	    * 
	    * @param context 
	    * @return true 允许  false禁止 
	    */  
	   public static boolean getAppOps(Context context) {
	       try {  
	           Object object = context.getSystemService("appops");
	           if (object == null) {
	               return false;  
	           }  
	           Class localClass = object.getClass();
	           Class[] arrayOfClass = new Class[3];
	           arrayOfClass[0] = Integer.TYPE;
	           arrayOfClass[1] = Integer.TYPE;
	           arrayOfClass[2] = String.class;
	           Method method = localClass.getMethod("checkOp", arrayOfClass);
	           if (method == null) {  
	               return false;  
	           }  
	           Object[] arrayOfObject1 = new Object[3];
	           arrayOfObject1[0] = Integer.valueOf(24);
	           arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
	           arrayOfObject1[2] = context.getPackageName();  
	           int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
//	           return m == AppOpsManager.MODE_ALLOWED;  
	           return m == 0;  
	       } catch (Exception ex) {
	  
	       }  
	       return false;  
	   } 


	/**
	 * @Title: getWpixels(横屏的像素)
	 * @author
	 * @data
	 * @param: @param mContext
	 * @param: @return
	 * @return int 返回类型
	 */
	public static int getWpixels(Context mContext) {

		String screenResolution = getDisplayScreenResolution(mContext);
		String wpixels = screenResolution.substring(0, screenResolution.indexOf("*"));
		return Integer.valueOf(wpixels);
	}

	/**
	 * @Title: getHpixels(竖屏的像素)
	 * @author
	 * @data
	 * @param: @param mContext
	 * @param: @return
	 * @return int 返回类型
	 */
	public static int getHpixels(Context mContext) {
		String screenResolution = getDisplayScreenResolution(mContext);
		String hpixels = screenResolution.substring(screenResolution.indexOf("*") + 1);
		return Integer.valueOf(hpixels);
	}

	/**
	 * getOrientation(获取屏幕方向) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param context
	 * @return int
	 * @exception
	 * @since 1.0.0 xiaoming.yuan
	 */
	public static int getOrientation(Activity context) {
		Configuration config = context.getResources().getConfiguration();
		// if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){
		// //横屏，比如 480x320
		// }else if(config.orientation == Configuration.ORIENTATION_PORTRAIT){
		// //竖屏 ，标准模式 320x480
		// }else if(config.hardKeyboardHidden ==
		// Configuration.KEYBOARDHIDDEN_NO){
		// //横屏，物理键盘滑出了
		// }else if(config.hardKeyboardHidden ==
		// Configuration.KEYBOARDHIDDEN_YES){
		// //竖屏，键盘隐藏了
		// }
		return config.orientation;
	}

	/**
	 * bitmap等比缩放 type 1 为从下载里的图片的缩放;2 为从drawable获取的图片的缩放，
	 * 
	 * xxhdpi: 1080x1920
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap bitmapAdaptive(Context context, Bitmap bitmap, int type) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int scalX = dm.widthPixels;// 宽度
		int scalY = dm.heightPixels;// 高度

		if (scalX > scalY) {
			scalX = scalX ^ scalY;
			scalY = scalX ^ scalY;
			scalX = scalX ^ scalY;
		}

		float n = 1.5f;

		if (scalX > 2000) {
			n = 5.0f;
		} else if (scalX >= 1200) {
			n = 2.3f;
		} else if (scalX >= 800) {
			n = 1.1f;
		} else if (scalX >= 600) {
			n = 1.0f;
		} else if (scalX >= 400) {
			n = 1.2f;
		} else if (scalX < 400) {
			n = 1.5f;
		}

		if (bitmap == null) {
			return null;
		}

		if (type == 2) {
			n = 1.9f;
			if (scalX > 2000) {
				n = 4.5f;
			} else if (scalX >= 1200) {
				n = 3.0f;
			} else if (scalX >= 800) {
				n = 2.3f;
			} else if (scalX >= 600) {
				n = 1.6f;
			} else if (scalX >= 400) {
				n = 1.3f;
			} else if (scalX < 400) {
				n = 1.6f;
			}
		}

		Matrix matrix = new Matrix();
		int width = bitmap.getWidth();// 获取资源位图的宽
		int height = bitmap.getHeight();// 获取资源位图的高
		float ImageX = 1440;
		float w = scalX / ImageX;

		matrix.postScale(w / n, w / n);// 获取缩放比例
		// System.out.println(w/n);
		// matrix.postScale(0.2f, 0.2f);// 获取缩放比例
		// 根据缩放比例获取新的位图
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

		return newbmp;
	}

	/**
	 * 缩放floatButton
	 * 
	 * @param context
	 * @param bitmap
	 * @param type
	 * @return
	 */
	public static Bitmap bitmapAdaptive(Context context, Bitmap bitmap) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int scalX = dm.widthPixels;// 宽度
		int scalY = dm.heightPixels;// 高度

		if (scalX > scalY) { // 横屏
			scalX = scalX ^ scalY;
			scalY = scalX ^ scalY;
			scalX = scalX ^ scalY;
		}

		float n = 1.1f; // n越大，图片越小

		if (scalX <= 400 && scalY <= 800) {
			n = 1.5f;
		} else if ((400 < scalX && scalX <= 600) && (800 < scalY && scalY <= 900)) {
			n = 1.2f;
		} else if ((600 < scalX && scalX <= 800) && (900 < scalY && scalY <= 1300)) {
			n = 1.0f;
		} else if ((800 < scalX && scalX <= 1200) && (1300 < scalY && scalY <= 2000)) {
			n = 1.1f;
		} else if ((1200 < scalX && scalX <= 1600) && (2000 < scalY && scalY <= 2800)) { // 2560x1440
			n = 4.0f;
		}

		if (bitmap == null) {
			return null;
		}

		Matrix matrix = new Matrix();
		int width = bitmap.getWidth();// 获取资源位图的宽
		int height = bitmap.getHeight();// 获取资源位图的高
		// 1980x1080
		float ImageX = 700;
		// float ImageY = 2560;
		// float ImageX = 1080;
		// float ImageY = 1980;
		float w = scalX / ImageX;
		// float h = scalY / ImageY;

		matrix.postScale(w / n, w / n);// 获取缩放比例

		// matrix.postScale(0.2f, 0.2f);// 获取缩放比例
		// 根据缩放比例获取新的位图
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return newbmp;
	}

	/**
	 * 获取经纬度
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocation(Context context) {
		// 获取到LocationManager对象
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 创建一个Criteria对象
		Criteria criteria = new Criteria();
		// 设置粗略精确度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 设置是否需要返回海拔信息
		criteria.setAltitudeRequired(false);
		// 设置是否需要返回方位信息
		criteria.setBearingRequired(false);
		// 设置是否允许付费服务
		criteria.setCostAllowed(true);
		// 设置电量消耗等级
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		// 设置是否需要返回速度信息
		criteria.setSpeedRequired(false);
		// 根据设置的Criteria对象，获取最符合此标准的provider对象 41
		String currentProvider = locationManager.getBestProvider(criteria, true);
		// 根据当前provider对象获取最后一次位置信息 44
		Location currentLocation = locationManager.getLastKnownLocation(currentProvider);
		double latitude = 0.0;
		double longitude = 0.0;
		if (currentLocation != null) {
			latitude = currentLocation.getLatitude(); // 纬度
			longitude = currentLocation.getLongitude(); // 经度
		}

		// 如果位置信息为null，则请求更新位置信息 46
		// if (currentLocation == null) {
		// locationManager.requestLocationUpdates(currentProvider, 0,
		// 0,locationListener);
		// }

		// 解析地址并显示 69
		Geocoder geoCoder = new Geocoder(context);
		String locationStr = null;
		try {
			List<Address> list = geoCoder.getFromLocation(latitude, longitude, 2);
			for (int i = 0; i < list.size(); i++) {
				Address address = list.get(i);
				locationStr = address.getCountryName() + address.getAdminArea() + address.getLocality() + address.getFeatureName();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "latitude:longitude=" + latitude + ":" + longitude + ";位置:" + locationStr;
	}

	/**
	 * 基站讯息
	 * 
	 * @param context
	 * @return
	 */
	public static String getTelephonyInfo(Context context) {

		int mcc = 0, mnc = 0;
		StringBuffer sb = new StringBuffer();
		// try {
		TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		// 返回值MCC + MNC
		if (!(null == mTelephonyManager)) {
			String operator = mTelephonyManager.getNetworkOperator();
			if (TextUtils.isEmpty(operator) || operator.length() < 3) {
			} else {
				mcc = Integer.parseInt(operator.substring(0, 3));
				mnc = Integer.parseInt(operator.substring(3));
			}
		}
		// 中国移动和中国联通获取LAC、CID的方式
		// GsmCellLocation location = (GsmCellLocation)
		// mTelephonyManager.getCellLocation();
		// int lac = location.getLac();
		// int cellId = location.getCid();

		// 获取邻区基站信息
		List<NeighboringCellInfo> infos = mTelephonyManager.getNeighboringCellInfo();
		sb.append("－COUNT:" + infos.size());
		sb.append("-MCC:" + mcc + "-MNC:" + mnc);
		// } catch (Exception e) {
		//
		// }
		return sb.toString();
	}

	/**
	 * 获取系统中所有安装的APK包名，返回所有包名已逗号隔开的字符串
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneInstallPackage(Context context) {
		PackageManager pManager = context.getPackageManager();
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		StringBuilder sb = new StringBuilder();
		int count = paklist.size();
		for (int i = 0; i < count; i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				if (i != (count - 1)) {
					sb.append(pak.packageName + ",");
				} else {
					sb.append(pak.packageName);
				}
			}
		}
		return sb.toString();
	}


	/**
	 * 获取版本名
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	/**
	 * 获取版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}

	/**
	 * 获取屏幕长宽 arr[0][0] width arr[0][1] height
	 * 
	 * @param context
	 * @return
	 */
	public static int[] getScreenWidthHeight(Activity context) {
		WindowManager wm = context.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		int[] arr = new int[2];
		arr[0] = width;
		arr[1] = height;

		return arr;
	}

	@SuppressLint("NewApi")
	public static void copyContent(Context context, String content) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (TextUtils.isEmpty(content)) {
                Toast.makeText(context,"复制失败,请手动输入",Toast.LENGTH_LONG).show();
			} else {
				ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(content);
                Toast.makeText(context,"复制成功",Toast.LENGTH_LONG).show();

			}
		} else {
			if (TextUtils.isEmpty(content)) {
                Toast.makeText(context,"复制失败,请手动输入",Toast.LENGTH_LONG).show();
			} else {
				android.text.ClipboardManager cmb1 = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
				cmb1.setText(content);
                Toast.makeText(context,"复制成功",Toast.LENGTH_LONG).show();

			}
		}
	}
	
	/**
	 * 480x800
	 * 720x1280
	 * 1080x1920
	 * @param context
	 * @return
	 */
	public static int getScreenFlag(Context context){
		int n = 0;
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int scalX = dm.widthPixels;// 宽度
		int scalY = dm.heightPixels;// 高度

		if (scalX > scalY) { // 横屏
			scalX = scalX ^ scalY;
			scalY = scalX ^ scalY;
			scalX = scalX ^ scalY;
		}

		if (scalX <= 400 && scalY <= 700) {
			n = 1;
		} else if ((400 < scalX && scalX <= 600) && (700 < scalY && scalY <= 900)) {   //480x800
			n = 2;
		} else if ((600 < scalX && scalX <= 800) && (900 < scalY && scalY <= 1300)) {   //720x1280
			n = 3;
		} else if ((800 < scalX && scalX <= 1200) && (1300 < scalY && scalY <= 2000)) {  //1080x1920
			n = 4;
		} else if ((1200 < scalX && scalX <= 1600) && (2000 < scalY && scalY <= 2800)) { // 2560x1440
			n = 5;
		}
		return n;
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static String getAndroidId (Context context) {
	    String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
	    return ANDROID_ID;
	}
	
}
