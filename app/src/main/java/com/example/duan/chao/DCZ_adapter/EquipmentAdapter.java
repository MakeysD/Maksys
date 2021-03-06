package com.example.duan.chao.DCZ_adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duan.chao.DCZ_activity.EquipmentManageActivity;
import com.example.duan.chao.DCZ_activity.LoginEmailActivity;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.EquipmentBean;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.RandomUtil;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.R;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 2017/7/12.
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder>{
    private Context context;
    private List<EquipmentBean.ListBean> list;
    private Dialog dialog;

    public EquipmentAdapter(Context context, List<EquipmentBean.ListBean>list){
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.item_equipment,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv1.setText(MyApplication.map.get(list.get(position).getGratSysId()).toString());
        holder.tv2.setText(list.get(position).getDeviceName()+"");

        Long updateTime = list.get(position).getGratTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetime = format.format(updateTime);
        holder.tv3.setText(context.getString(R.string.tishi86)+datetime);

        Long lasttime = list.get(position).getLastLoginTime();
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetime2 = format2.format(lasttime);
        holder.tv4.setText(context.getString(R.string.tishi87)+datetime2);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MiddleDialog(context,context.getString(R.string.tishi99),context.getString(R.string.tishi159),true,new MiddleDialog.onButtonCLickListener2() {
                    @Override
                    public void onActivieButtonClick(Object bean, int po) {
                        if(bean==null){
                        }else {
                            String random= RandomUtil.RandomNumber();
                            String str ="id="+(long)list.get(position).getId()+"&nonce="+random;
                            byte[] data = str.getBytes();
                            try {
                                String sign = DSA.sign(data, MyApplication.pri_key);
                                getData((long) list.get(position).getId(),position,random,sign);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, R.style.registDialog).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView button;
        public ViewHolder(View view) {
            super(view);
            tv1=(TextView)view.findViewById(R.id.tv1);
            tv2=(TextView)view.findViewById(R.id.tv2);
            tv3=(TextView)view.findViewById(R.id.tv3);
            tv4=(TextView)view.findViewById(R.id.tv4);
            button=(TextView)view.findViewById(R.id.button);
        }
    }
    public void Notify(List<EquipmentBean.ListBean>list){
        this.list=list;
        notifyDataSetChanged();
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(Long id, final int postion,String ran,String sign){
        if(ShebeiUtil.wang(context).equals("0")){
            new MiddleDialog(context,context.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(context,"努力加载...","1");
        dialog.show();
        HttpServiceClient.getInstance().deleteEquipent(id,ran,sign).enqueue(new Callback<EquipmentBean>() {
            @Override
            public void onResponse(Call<EquipmentBean> call, Response<EquipmentBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body()!=null){
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
                        if(response.body().getCode().equals("20000")){
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
            public void onFailure(Call<EquipmentBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof EquipmentManageActivity){
                    dialog.dismiss();
                    new MiddleDialog(context,context.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
}
