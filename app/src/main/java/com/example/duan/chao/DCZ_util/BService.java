package com.example.duan.chao.DCZ_util;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.duan.chao.DCZ_application.MyApplication;

public class BService extends Service {
    public static Messenger to;
    public static  Message message;
    public static String App_key;
    public static String redirect_uri;
    public static String scope;
    public static String state;
     /*try {
        if(BService.message==null||BService.to==null){
        }else {
            BService.to.send(BService.message);
        }
    } catch (RemoteException e) {
        e.printStackTrace();
    }*/
    Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            App_key=msg.getData().getString("App_key");
            redirect_uri=msg.getData().getString("redirect_uri");
            scope=msg.getData().getString("scope");
            state=msg.getData().getString("state");
            Log.e("kk","App_key:"+App_key+"+redirect_uri:"+redirect_uri+"+scope:"+scope+"+state:"+state);

            message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("type","0");
            if(MyApplication.token.equals("")){
                bundle.putString("intent",MyApplication.pkge+".DCZ_activity.LoginEmailActivity");
            }else {
                bundle.putString("intent",MyApplication.pkge+".DCZ_activity.AuthorActivity");
            }
            message.setData(bundle);
            to = msg.replyTo;
            try {
                to.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("kk", "绑定成功！");
        return messenger.getBinder();
    }
}
