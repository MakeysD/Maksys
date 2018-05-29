/**
 *
 */
package com.example.duan.chao2.DCZ_util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.duan.chao2.R;


/**
 * 功能描述：  得到自定义的进度条
 */
public class DialogUtil {
    public static Dialog createLoadingDialog(Context context, String msg,String type) {
        View v;
        final AnimationDrawable animationDrawable;
        LayoutInflater inflater = LayoutInflater.from(context);
       // View v = inflater.inflate(R.layout.loaddialog, null);// 得到加载view
        if(type.equals("2")){
            v=inflater.inflate(R.layout.loaddialog2,null);
        }else {
            v=inflater.inflate(R.layout.loaddialog,null);
        }
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        final ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.text);// 提示文字

        spaceshipImage.setImageResource(R.drawable.progress_round);
        animationDrawable= (AnimationDrawable) spaceshipImage.getDrawable();

        // 加载动画
//        final Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                context, R.anim.dialog_animation);
//        LinearInterpolator lir = new LinearInterpolator();
//        hyperspaceJumpAnimation.setInterpolator(lir);

        // 使用ImageView显示动画
        tipTextView.setText(msg);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialogs);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        loadingDialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    //HttpUtils.cancel();
                    animationDrawable.stop();
//                    animationDrawable.stop();
                    //此处把dialog dismiss掉，然后把本身的activity finish掉
                    //   BarcodeActivity.this.finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
        loadingDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                animationDrawable.start();
//                spaceshipImage.startAnimation(hyperspaceJumpAnimation);
            }
        });
        return loadingDialog;


    }


    public static Dialog show(final Context context, String msg, final boolean closeOnEnd) {
        final View v;
        LayoutInflater inflater = LayoutInflater.from(context);
        /*if(lv.equals("ok")){
            v = inflater.inflate(R.layout.dialog, null);
        }else {*/
            v = inflater.inflate(R.layout.dialog_lock, null);
       // }
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.dialog_rl);
       /* TextView tv = (TextView) v.findViewById(R.id.dialog_tv);
        tv.setText(msg);*/
        final Dialog dialog = new Dialog(context, R.style.loading_dialogs);
        dialog.setContentView(rl, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        Animation a = AnimationUtils.loadAnimation(context, R.anim.animation_fade);
        v.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                v.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                if (closeOnEnd) ((Activity) context).finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        return dialog;
    }

/*
    public static Dialog addPoint(Context context, String text, String point, long time) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.qiandao_dialog, null);
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.qiandao_dialog_rl);
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setContentView(rl, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        ((TextView)v.findViewById(R.id.qiandao_ok_tv2)).setTypeface(MyApplication.font);
        if (text != null) ((TextView)v.findViewById(R.id.qiandao_ok_tv1)).setText(text);
        if (point != null) ((TextView)v.findViewById(R.id.qiandao_ok_tv2)).setText(point);
        Animation a = AnimationUtils.loadAnimation(context, R.anim.animation_fade);
        a.setDuration(time);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(a);

        return dialog;
    }*/




}
