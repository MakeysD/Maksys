package com.example.duan.chao.DCZ_util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.duan.chao.DCZ_application.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Stack;


/**
 * activity 栈和堆
 * */
public class ActivityUtils {
	private static Stack activityStack;
	private static ActivityUtils instance;

	private ActivityUtils() {

	}

	public void application(String key,String valus){
		MyApplication.sf.edit().putString(key, valus).commit();
	}
	public void application2(String key,Boolean valus){
		MyApplication.sf.edit().putBoolean(key, valus).commit();
	}

	public static ActivityUtils getInstance() {
		if (instance == null) {
			instance = new ActivityUtils();
		}
		return instance;
	}

	/**
	 * 将当前activity压入栈内
	 * 
	 * @param activity
	 */
	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack();
		}
		activityStack.add(activity);
	}
	public void removeActivity(Activity activity){
		if(activityStack.size()>0){
			activityStack.remove(activity);
		}
	}

	/**
	 * 结束当前activity
	 * 
	 * @return
	 */
	public void popActivity(Activity activity) {
		if (activity == null) {
			return;
		}
		activity.finish();
		activityStack.remove(activity);
		activity = null;
	}

	public int ActivitySize(){
		return activityStack.size();
	}

	/**
	 * 获取当前activity
	 * 
	 * @return
	 */
	public Activity getCurrentActivity() {
		return (Activity) activityStack.lastElement();
	}

	/**
	 * 获取指定位置的activity
	 * 
	 * @param location
	 * @return
	 */
	public Activity getActivity(int location) {
		return (Activity) activityStack.get(location);
	}

	/**
	 * 结束所有的activity
	 */
	public void popAllActivities() {
		if (activityStack == null) {
			return;
		}
		while (!activityStack.empty()) {
			popActivity(getCurrentActivity());
		}
	}
	/**
	 * 在进程中去寻找当前APP的信息，判断是否在前台运行
	 */
	public boolean isAppOnForeground(Context context) {
		ActivityManager activityManager =(ActivityManager) context.getApplicationContext().getSystemService(
				Context.ACTIVITY_SERVICE);
		String packageName =context.getApplicationContext().getPackageName();
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断某个界面是否在前台
	 *
	 * @param context
	 * @param className
	 *            某个界面名称
	 */
	public boolean isForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}

		return false;
	}

	public static String ToString(InputStream is, String charset) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				} else {
					sb.append(line).append("\n");
				}
			}
			reader.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
