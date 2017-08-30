package com.example.duan.chao.DCZ_jiguang;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.duan.chao.DCZ_activity.HavaMoneyActivity;
import com.example.duan.chao.DCZ_activity.HaveScanActivity;
import com.example.duan.chao.DCZ_activity.LoginActivity;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.HaveBean;
import com.example.duan.chao.DCZ_lockdemo.LockUtil;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JIGUANG-Example";
	private Bundle bundle;
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			bundle = intent.getExtras();
			Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				MyApplication.rid = regId;
				Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息2: " + bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE));
				//下线通知
				if(bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE).equals("2")){
					//停止推送服务
					JPushInterface.stopPush(context.getApplicationContext());
					MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
					Log.i("dcz_当前界面",ActivityUtils.getInstance().getCurrentActivity()+"");
					String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
					Gson mGson = new Gson();
					HaveBean result = mGson.fromJson(message, HaveBean.class);
					xiaxian(context,result.getLoginTime());
				}else if(bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE).equals("1")){
					isRunningForeground(context);
				}else {
					jiaoyi(context);
				}
			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
				/*Activity a = ActivityUtils.getInstance().getCurrentActivity();
				Intent inten = new Intent(Intent.ACTION_MAIN);
				inten.addCategory(Intent.CATEGORY_LAUNCHER);
				inten.setClass(a, a.getClass());
				inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
				a.startActivity(inten);*/
			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Logger.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Logger.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			String content_type=bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			msgIntent.putExtra(MainActivity.KEY_CONTENT_TYPE,content_type);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			Log.i("dcz","发送广播");
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}
	}

	private boolean isRunningForeground (Context context) {
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		//判断APP是否在前台
		if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
			Intent i = new Intent(context, HaveScanActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
			i.putExtra("message",message);
			context.startActivity(i);
			return true ;
		}else {
			Log.i("dcz","执行通知跳转");
			if (context == null){
				Log.i("dcz","空的");
			}
			Intent i = new Intent(context, HaveScanActivity.class);
			i.putExtra("message",message);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			return false ;
		}
	}
	private boolean xiaxian (Context context,String time) {
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		Activity a = ActivityUtils.getInstance().getCurrentActivity();
		//判断APP是否在前台
		if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
			new MiddleDialog(a,a.getString(R.string.tishi101), a.getString(R.string.tishi102)+time+a.getString(R.string.tishi103)+a.getString(R.string.tishi104),"",new MiddleDialog.onButtonCLickListener2() {
				@Override
				public void onActivieButtonClick(Object bean, int position) {
					JPushInterface.clearAllNotifications(MyApplication.getContext());
					MyApplication.sms_type="1";MyApplication.sf.edit().putString("sms_type","1").commit();
					MyApplication.nickname="";MyApplication.sf.edit().putString("nickname","").commit();
					MyApplication.username="";MyApplication.sf.edit().putString("username","").commit();
					if(bean==null){
						ActivityUtils.getInstance().popAllActivities();
					}else {
						processCustomMessage(MyApplication.getContext(), bundle);
					}

				}
			}, R.style.registDialog).show();
			return true ;
		}else {
			Log.i("dcz","执行通知跳转");
			if (context == null){
				Log.i("dcz","空的");
			}
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClass(a, a.getClass());
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			a.startActivity(intent);
			new MiddleDialog(a,a.getString(R.string.tishi101), a.getString(R.string.tishi102)+time+a.getString(R.string.tishi103)+a.getString(R.string.tishi104),"",new MiddleDialog.onButtonCLickListener2() {
				@Override
				public void onActivieButtonClick(Object bean, int position) {
					JPushInterface.clearAllNotifications(MyApplication.getContext());
					MyApplication.sms_type="1";MyApplication.sf.edit().putString("sms_type","1").commit();
					MyApplication.nickname="";MyApplication.sf.edit().putString("nickname","").commit();
					MyApplication.username="";MyApplication.sf.edit().putString("username","").commit();
					if(bean==null){
						ActivityUtils.getInstance().popAllActivities();
					}else {
						processCustomMessage(MyApplication.getContext(), bundle);
					}

				}
			}, R.style.registDialog).show();
			return false ;
		}
	}

	private boolean jiaoyi (Context context) {
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		//判断APP是否在前台
		if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
			Intent i = new Intent(context, HavaMoneyActivity.class);
			i.putExtra("message",message);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
			context.startActivity(i);
			return true ;
		}else {
			Intent i = new Intent(context, HavaMoneyActivity.class);
			i.putExtra("message",message);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			return false ;
		}
	}
}
