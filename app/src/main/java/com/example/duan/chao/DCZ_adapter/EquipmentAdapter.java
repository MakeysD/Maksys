package com.example.duan.chao.DCZ_adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.EquipmentBean;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
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
                getData((long) list.get(position).getId(),position);
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
    public void getData(Long id, final int postion){
        dialog= DialogUtil.createLoadingDialog(context,"努力加载...","1");
        dialog.show();
        HttpServiceClient.getInstance().deleteEquipent(id).enqueue(new Callback<EquipmentBean>() {
            @Override
            public void onResponse(Call<EquipmentBean> call, Response<EquipmentBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body()!=null){
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
                dialog.dismiss();
                new MiddleDialog(context,context.getString(R.string.tishi72),R.style.registDialog).show();
            }
        });
    }
}
