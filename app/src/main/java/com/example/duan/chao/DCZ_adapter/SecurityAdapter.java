package com.example.duan.chao.DCZ_adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_bean.EquipmentBean;
import com.example.duan.chao.DCZ_bean.SecurityBean;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_selft.SwitchButton;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.R;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 2017/8/7.
 */

public class SecurityAdapter extends RecyclerView.Adapter<SecurityAdapter.ViewHolder>{
    private Context context;
    private List<SecurityBean.ListBean> list;
    private Dialog dialog;

    public SecurityAdapter(Context context, List<SecurityBean.ListBean> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.item_security,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getSystemName());
        if(list.get(position).getEnable().equals("1")){
            holder.button.setChecked(true);
        }else {
            holder.button.setChecked(false);
        }
        holder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==false){
                    getData((long) list.get(position).getId(), "2",position);
                }else {
                    getData((long) list.get(position).getId(), "1",position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView sdv;
        TextView name;
        SwitchButton button;
        public ViewHolder(View view) {
            super(view);
            sdv=(SimpleDraweeView)view.findViewById(R.id.sdv);
            name=(TextView)view.findViewById(R.id.name);
            button=(SwitchButton)view.findViewById(R.id.button);
        }
    }
    public void Notify(List<SecurityBean.ListBean> list){
        this.list=list;
        notifyDataSetChanged();
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(Long id, final String string, final int postion){
        dialog= DialogUtil.createLoadingDialog(context,"努力加载...","1");
        dialog.show();
        HttpServiceClient.getInstance().updateProtect(id,string).enqueue(new Callback<EquipmentBean>() {
            @Override
            public void onResponse(Call<EquipmentBean> call, Response<EquipmentBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            if(string.equals("1")){
                                list.get(postion).setEnable("1");
                            }else {
                                list.get(postion).setEnable("2");
                            }
                            new MiddleDialog(context,response.body().getDesc(),R.style.registDialog).show();
                        }else {
                            new MiddleDialog(context,response.body().getDesc(),R.style.registDialog).show();
                            Notify(list);
                        }
                    }else {
                        Notify(list);
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    Notify(list);
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<EquipmentBean> call, Throwable t) {
                dialog.dismiss();
                Notify(list);
                new MiddleDialog(context,context.getString(R.string.tishi72),R.style.registDialog).show();
            }
        });
    }
}
