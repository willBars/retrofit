package cn.xy.unittext.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 */
public class VerifyUtil {

	/**
	 * 校验手机号
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNo(String mobiles) {
		boolean flag = false;
		try {
			// Pattern p =
			// Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Pattern p = Pattern.compile("^[1]+[3,5,8]+\\d{9}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

//	public static String[] verifyRegistUserNameAndPassWord(String userName, String passWord) {
//		String[] str = new String[2];
//
//		if (TextUtils.isEmpty(userName)) {
//			str[0] = "0";
//			str[1] = "账号或密码不能为空。";
//			return str;
//		}
//
//		if (TextUtils.isEmpty(passWord)) {
//			str[0] = "1";
//			str[1] = "账号或密码不能为空。";
//			return str;
//		}
//
//		if (userName.length() > 22) {
//			str[0] = "0";
//			// str[1] = "账号过长，支持6~22个字母数字下划线组合";
//			str[1] = PromptWords.unRegist1;
//			return str;
//		}
//
//		if (userName.length() < 6) {
//			str[0] = "0";
//			// str[1] = "账号过短，支持6~22个字母数字下划线组合";
//			str[1] = PromptWords.unRegist2;
//			return str;
//		}
//
//		if (!AppUtil.verifyUserNameHasLetter(userName)) {
//			str[0] = "0";
//			// str[1] = "用户名必须包含字母";
//			str[1] = PromptWords.unRegist4;
//			return str;
//		}
//
//		if (AppUtil.verifyUserNameOrPassWord(userName)) {
//			str[0] = "0";
//			// str[1] = "账号仅支持字母数字下划线字组合";
//			str[1] = PromptWords.unRegist3;
//			return str;
//		}
//
//		if (passWord.length() < 6) {
//			str[0] = "1";
//			str[1] = "密码过短，支持6~22个英文字符";
//			// str[1] = PromptWords.unRegist6;
//			return str;
//		}
//		if (passWord.length() > 22) {
//			str[0] = "1";
//			str[1] = "密码过长，支持6~22个英文字符";
//			// str[1] = PromptWords.unRegist5;
//			return str;
//		}
//		if (AppUtil.verifyUserNameOrPassWord(passWord)) {
//			str[0] = "1";
//			// str[1] = "密码中包含不支持的字符，仅支持字母、数字和下划线。";
//			str[1] = PromptWords.unRegist7;
//			return str;
//		}
//		return str;
//	}

	public static String[] verifyLoginUserNameAndPassWord(String userName, String passWord) {
		String[] messages = new String[2];
		if (TextUtils.isEmpty(userName)) {
			messages[0] = "0";
			messages[1] = "账号或密码不能为空";
			return messages;
		}
		if (TextUtils.isEmpty(passWord)) {
			messages[0] = "1";
			messages[1] = "账号或密码不能为空";
			return messages;
		}
		return messages;
	}

	// 用户名是否格式良好
	public static boolean isUserNameCorrect(String userName) {
		if (!TextUtils.isEmpty(userName)) {
			Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]{4,20}$");
			Matcher matcher = pattern.matcher(userName);
			return matcher.find();
		}
		return false;
	}

	// 验证密码是否格式良好
	public static boolean isPasswordCorrect(String password) {
		if (!TextUtils.isEmpty(password)) {
			Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]{6,12}$");
			Matcher matcher = pattern.matcher(password);
			return matcher.find();
		}
		return false;
	}


	/**
	 * @Title: paramsGet(hashmap转化成get请求的参数)
	 * @author xiaoming.yuan
	 * @data 2013-10-8 下午5:05:30
	 * @param params
	 * @param url
	 * @return String 返回类型
	 */
	public static synchronized String hashMapTOgetParams(HashMap<String, Object> params, String url) {
		if (params == null || params.size() <= 0 || TextUtils.isEmpty(url)) {
			return null;
		}
		StringBuilder buf = new StringBuilder(url);
		if (url.contains("?")) {
			buf.append("&");
		} else {
			buf.append("?");
		}

		Iterator<String> iterators = params.keySet().iterator();
		while (iterators.hasNext()) {
			String key = iterators.next();
			try {
				buf.append(key).append("=").append(URLEncoder.encode(params.get(key) == null ? "" : params.get(key).toString(), "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				// LogHelper.e(TAG, e.getLocalizedMessage());
			}
		}
		buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	/**
	 * 获取渠道 id
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("all")
	public static String getChannelID(Context context) {
		ApplicationInfo appInfo;
		// String cid = null;
		// try {
		// appInfo =
		// context.getPackageManager().getApplicationInfo(context.getPackageName(),
		// PackageManager.GET_META_DATA);
		// cid = appInfo.metaData.getString("CHANNELID");
		// if (TextUtils.isEmpty(cid)) {
		// cid = appInfo.metaData.getInt("CHANNELID") + "";
		// }
		// } catch (ClassCastException e) {
		//
		// }catch (NameNotFoundException e) {
		// // TODO Auto-generated catch block
		// ToastUtil.showToast(context, "没有channelid");
		// } catch (NullPointerException e) {
		// ToastUtil.showToast(context, "channelid不能为空");
		// }

		String cid = null;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			cid = appInfo.metaData.getInt("CHANNELID") + "";
			if (TextUtils.isEmpty(cid)) {
				cid = appInfo.metaData.getString("CHANNELID");
			}
		} catch (ClassCastException e) {

		} catch (NameNotFoundException e) {
			Toast.makeText(context,"没有channelid",Toast.LENGTH_LONG).show();
		} catch (NullPointerException e) {
			Toast.makeText(context,"channelid不能为空",Toast.LENGTH_LONG).show();
		}

		return cid;
	}

	/**
	 * 获取游戏appid
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppID(Context context) {
		ApplicationInfo appInfo;

		String appid = null;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			appid = appInfo.metaData.getInt("APPID") + "";
			if (TextUtils.isEmpty(appid)) {
				appid = appInfo.metaData.getString("APPID");
			}
		} catch (ClassCastException e) {
			
		} catch (NameNotFoundException e) {
			Toast.makeText(context,"没有channelid",Toast.LENGTH_LONG).show();
		} catch (NullPointerException e) {
			Toast.makeText(context,"channelid不能为空",Toast.LENGTH_LONG).show();
		}

		return appid;
	}

	/**
	 * 获取 appkey
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppkey(Context context) {
		ApplicationInfo appInfo;
		String appkey = null;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			appkey = appInfo.metaData.getString("APPKEY");
			if (TextUtils.isEmpty(appkey)) {
				appkey = appInfo.metaData.getInt("APPKEY") + "";
			}
		} catch (NameNotFoundException e) {
			Toast.makeText(context,"没有appkey",Toast.LENGTH_LONG).show();
		} catch (ClassCastException e) {

		} catch (NullPointerException e) {
			Toast.makeText(context,"appkey不能为空",Toast.LENGTH_LONG).show();
		}

		return appkey;
	}

	/**
	 * 获取渠道 privatekey
	 * 
	 * @param context
	 * @return
	 */
	public static String getPrivateKey(Context context) {
		ApplicationInfo appInfo;
		String privatekey = null;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			privatekey = appInfo.metaData.getString("PRIVATEKEY");
			if (TextUtils.isEmpty(privatekey)) {
				privatekey = appInfo.metaData.getInt("PRIVATEKEY") + "";
			}
		} catch (NameNotFoundException e) {
			Toast.makeText(context,"没有privatekey",Toast.LENGTH_LONG).show();
		} catch (ClassCastException e) {

		} catch (NullPointerException e) {
			Toast.makeText(context,"privatekey不能为空",Toast.LENGTH_LONG).show();
		}

		return privatekey;
	}

	/**
	 * 获取百度统计开关
	 * 
	 * @param context
	 * @return
	 */
	// public static boolean getBaiduFlag(Context context) {
	// ApplicationInfo appInfo;
	// String baiduFlag = "";
	// try {
	// appInfo = context.getPackageManager().getApplicationInfo(
	// context.getPackageName(), PackageManager.GET_META_DATA);
	// baiduFlag = appInfo.metaData.getString("BAIDU_FLAG");
	// if (TextUtils.isEmpty(baiduFlag)) {
	// baiduFlag = appInfo.metaData.getInt("BAIDU_FLAG") + "";
	// }
	// } catch (NameNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// if (baiduFlag.equals("1")) { // 1： 开启百度统计
	// return true;
	// }
	// return false;
	// }

	/**
	 * @author K
	 * @Description: TODO(获取包名)
	 * @date 2014-12-22 上午10:47:54
	 * @param context
	 * @return
	 */
	public static String getAppInfo(Context context) {
		try {
			String pkName = context.getPackageName();
			// String versionName =
			// context.getPackageManager().getPackageInfo(pkName,
			// 0).versionName;
			// int versionCode =
			// context.getPackageManager().getPackageInfo(pkName,
			// 0).versionCode;
			return pkName;
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 随机生成6-12位密码 密码可以由字母、数字、下划线组成
	 * 
	 * length : 密码长度
	 * 
	 * @return
	 */
	public static String getRandomPassword(int length) {
		StringBuffer pwd = new StringBuffer();
		char[] arr = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_' };

		int number = arr.length;
		int i = 0;
		int count = 0;

		Random ran = new Random();
		while (count < length) {
			i = Math.abs(ran.nextInt(number)); // 生成 0~(number-1)之间的数字
			if (i >= 0 && i < number) {
				pwd.append(arr[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	/**
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomUserName(int length) {
		StringBuffer pwd = new StringBuffer();
		char[] arr = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_' };

		int number = arr.length;
		int i = 0;
		int count = 0;

		Random ran = new Random();
		while (count < length) {
			i = Math.abs(ran.nextInt(number)); // 生成 0~(number-1)之间的数字
			if (i >= 0 && i < number) {
				pwd.append(arr[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	/**
	 * 根据账号获取密码
	 * 
	 * 账号md5加密后，获取前6位
	 * 
	 * @param account
	 * @param n
	 *            生成几位
	 * 
	 * @return
	 */
	/*public static String getPwdFromAccount(String account, int n) {
		String str = MD5Util.getMd5(account);
		String pwd = str.substring(0, n);
		return pwd;
	}*/

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
	 * 获取 baiduAppkey
	 * 
	 * @param context
	 * @return
	 */
	public static String getBDAppkey(Context context) {
		ApplicationInfo appInfo;
		String appkey = null;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			appkey = appInfo.metaData.getString("BaiduMobAd_STAT_ID");
			if (TextUtils.isEmpty(appkey)) {
				appkey = appInfo.metaData.getInt("BaiduMobAd_STAT_ID") + "";
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return appkey;
	}

	/**
	 * 获取渠道 id
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("all")
	public static String getBDChannelID(Context context) {
		ApplicationInfo appInfo;
		String cid = null;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			cid = appInfo.metaData.getString("BaiduMobAd_CHANNEL");
			if (TextUtils.isEmpty(cid)) {
				cid = appInfo.metaData.getInt("BaiduMobAd_CHANNEL") + "";
			}
		} catch (ClassCastException e) {

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(context,"没有BaiduMobAd_CHANNEL",Toast.LENGTH_LONG).show();
		} catch (NullPointerException e) {
			Toast.makeText(context,"BaiduMobAd_CHANNEL不能为空",Toast.LENGTH_LONG).show();
		}

		return cid;
	}
}
