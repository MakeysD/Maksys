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

public class BService extends Service {
    public static Messenger to;
    public static  Message message;
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
            Log.e("kk", msg.getData().getString("data"));
            message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("data","你要说什么？");
            message.setData(bundle);
            to = msg.replyTo;
            super.handleMessage(msg);
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("kk", "绑定成功！");
        return messenger.getBinder();
    }
}
