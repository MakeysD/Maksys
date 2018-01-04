package com.example.duan.chao.DCZ_adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.duan.chao.DCZ_activity.FootprintsActivity;
import com.example.duan.chao.DCZ_activity.LoginEmailActivity;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.Footprints2Bean;
import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.DCZ_lockdemo.CustomLockView;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_selft.PullToRefreshLayout;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.RandomUtil;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

/**
 * Created by DELL on 2017/7/12.
 */

public class Footprints1Adapter extends BaseAdapter{
    private Context context;
    private  List<Footprints2Bean.DataBean> list;
    private Dialog dialog;

    public Footprints1Adapter(Context context, List<Footprints2Bean.DataBean> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_footprints1, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv1.setText(MyApplication.map.get(list.get(position).getSystemId())+"");
        Log.i("dcz",list.get(position).getIp()+"");
        viewHolder.tv2.setText(list.get(position).getIp()+"");
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String random= RandomUtil.RandomNumber();
                String str ="nonce="+random+"&sessionId="+list.get(position).getSessionId()+"&systemId="+list.get(position).getSystemId()+"&username="+list.get(position).getUsername();
                Log.i("dcz",str);
                byte[] data = str.getBytes();
                try {
                    String sign = DSA.sign(data, MyApplication.pri_key);
                    getData(list.get(position).getSystemId().toString(),list.get(position).getUsername().toString(),list.get(position).getSessionId(),random,position,sign);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView tv1;
        TextView tv2;
        TextView button;
        public ViewHolder(View view) {
            tv1=(TextView)view.findViewById(R.id.tv1);
            tv2=(TextView)view.findViewById(R.id.tv2);
            button=(TextView)view.findViewById(R.id.button);
        }
    }
    public void Notify(List<Footprints2Bean.DataBean> list){
        this.list=list;
        notifyDataSetChanged();
    }


    /***
     * 踢出用户的登录
     * */
    public void getData(String systemId, String username, String sessionid,String random, final int postion,String sign){
        if(ShebeiUtil.wang(context).equals("0")){
            new MiddleDialog(context,context.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(context,"努力加载...","1");
        dialog.show();
        HttpServiceClient.getInstance().kickout(systemId,username,sessionid,random,sign).enqueue(new Callback<FootprintsBean>() {
            @Override
            public void onResponse(Call<FootprintsBean> call, Response<FootprintsBean> response) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                if(response.isSuccessful()){
                    if(response.body().getCode().equals("10516")){
                        MyApplication.sf.edit().putString("cookie","").commit();
                        MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                        MyApplication.language="";MyApplication.sf.edit().putString("language","").commit();
                        new MiddleDialog(ActivityUtils.getInstance().getCurrentActivity(),context.getString(R.string.tishi101),context.getString(R.string.code42),"",new MiddleDialog.onButtonCLickListener2() {
                            @Override
                            public void onActivieButtonClick(Object bean, int position) {
                                ActivityUtils.getInstance().getCurrentActivity().startActivity(new Intent(ActivityUtils.getInstance().getCurrentActivity(), LoginEmailActivity.class));
                                ActivityUtils.getInstance().popAllActivities();
                            }
                        }, R.style.registDialog).show();
                        return;
                    }
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            Log.i("dcz","data1返回成功");
                            list.remove(postion);
                            Notify(list);
                        }else {
                            if(!response.body().getCode().equals("20003")){
                                new MiddleDialog(context,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                            }
                        }
                    }else {
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<FootprintsBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof FootprintsActivity){
                    dialog.dismiss();
                    new MiddleDialog(context,context.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
}
