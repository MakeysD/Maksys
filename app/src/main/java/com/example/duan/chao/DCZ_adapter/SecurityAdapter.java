package com.example.duan.chao.DCZ_adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duan.chao.DCZ_activity.LoginEmailActivity;
import com.example.duan.chao.DCZ_activity.SecurityProtectActivity;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.EquipmentBean;
import com.example.duan.chao.DCZ_bean.SecurityBean;
import com.example.duan.chao.DCZ_selft.GridViewForScrollView;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_selft.SwitchButton;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DSA;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.RandomUtil;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
import com.example.duan.chao.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 2017/8/7.
 */

public class SecurityAdapter extends RecyclerView.Adapter<SecurityAdapter.ViewHolder>{
    private Context context;
    private String sign;
    private List<SecurityBean.ListBean> list;
    private List<SecurityBean.ListBean> list2=new ArrayList<>();
    private Dialog dialog;
    private Boolean boo=true;
    private ActionCallback callback;

    public SecurityAdapter(Context context, List<SecurityBean.ListBean> list,ActionCallback callback){
        this.context=context;
        this.list=list;
        this.callback=callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.item_security,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(position==0){
            list2.clear();
        }
        if(list.get(position).getFrozenStatus().equals("2")){
            list2.add(list.get(position));
            holder.ll.setVisibility(View.GONE);
        }else {
            holder.ll.setVisibility(View.VISIBLE);
        }
        holder.name.setText(list.get(position).getSystemName());
        if(list.get(position).getEnable().equals("1")){
            holder.button.setChecked(true);
        }else {
            holder.button.setChecked(false);
        }
        holder.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(boo==true){      //是true才可以点击弹框
                        if(isChecked==false){
                        new MiddleDialog(context,null,context.getString(R.string.tishi105)+list.get(position).getSystemName()+context.getString(R.string.tishi106),true,new MiddleDialog.onButtonCLickListener2() {
                            @Override
                            public void onActivieButtonClick(Object bean, int po) {
                                if(bean==null){
                                    boo=false;
                                    holder.button.setChecked(true);
                                }else {
                                    getData((long) list.get(position).getId(), "2",position);
                                }
                            }
                        }, R.style.registDialog).show();
                    }else {
                        new MiddleDialog(context,null,context.getString(R.string.tishi105a)+list.get(position).getSystemName()+context.getString(R.string.tishi106),true,new MiddleDialog.onButtonCLickListener2() {
                            @Override
                            public void onActivieButtonClick(Object bean, int po) {
                                if(bean==null){
                                    boo=false;
                                    holder.button.setChecked(false);
                                }else {
                                    getData((long) list.get(position).getId(), "1",position);
                                }
                            }
                        }, R.style.registDialog).show();
                    }
                }else {
                    boo=true;
                }
            }
        });
        if(position+1==list.size()){
            holder.lv.setVisibility(View.VISIBLE);
            Security2Adapter adapter=new Security2Adapter(context,list2);
            holder.lv.setAdapter(adapter);
        }else {
            holder.lv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView sdv;
        TextView name;
        SwitchButton button;
        GridViewForScrollView lv;
        LinearLayout ll;
        public ViewHolder(View view) {
            super(view);
            sdv=(SimpleDraweeView)view.findViewById(R.id.sdv);
            name=(TextView)view.findViewById(R.id.name);
            button=(SwitchButton)view.findViewById(R.id.button);
            lv=(GridViewForScrollView)view.findViewById(R.id.lv);
            ll=(LinearLayout)view.findViewById(R.id.ll);
        }
    }
    public void Notify(List<SecurityBean.ListBean> list){
        this.list=list;
        notifyDataSetChanged();
    }
    public interface ActionCallback {
        void addAction(String string);
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(Long id, final String string, final int postion){
        if(ShebeiUtil.wang(context).equals("0")){
            new MiddleDialog(context,context.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(context,"努力加载...","1");
        dialog.show();
        String max= RandomUtil.RandomNumber();
        String str ="enable="+string+"id="+id+"nonce="+max;
        byte[] data = str.getBytes();
        try {
            sign = DSA.sign(data, MyApplication.pri_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpServiceClient.getInstance().updateProtect(id,string,max,sign).enqueue(new Callback<EquipmentBean>() {
            @Override
            public void onResponse(Call<EquipmentBean> call, Response<EquipmentBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
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
                            if(string.equals("1")){
                                callback.addAction("1");
                                list.get(postion).setEnable("1");
                            }else {
                                callback.addAction("2");
                                list.get(postion).setEnable("2");
                            }
                        }else {
                            boo=false;
                            if(!response.body().getCode().equals("20003")){
                                new MiddleDialog(context,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                            }
                            Notify(list);
                        }
                    }else {
                        boo=false;
                        Notify(list);
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    boo=false;
                    Notify(list);
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<EquipmentBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof SecurityProtectActivity){
                    dialog.dismiss();
                    boo=false;
                    Notify(list);
                    new MiddleDialog(context,context.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }

    /**
     *      嵌套的listview
     *
     * */
    private class Security2Adapter extends BaseAdapter {
        private Context context;
        private List<SecurityBean.ListBean> list;

        public Security2Adapter(Context context, List<SecurityBean.ListBean> list){
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder2 viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_security2, null);
                viewHolder = new ViewHolder2(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder2) convertView.getTag();
            }
            if(position==0){
                viewHolder.ll.setVisibility(View.VISIBLE);
            }else {
                viewHolder.ll.setVisibility(View.GONE);
            }
            viewHolder.name.setText(MyApplication.map.get(list.get(position).getSystemId()).toString());
            return convertView;
        }
    }

    public  class ViewHolder2 {
        SimpleDraweeView sdv;
        TextView name;
        SwitchButton button;
        LinearLayout ll;
        public ViewHolder2(View view) {
            sdv=(SimpleDraweeView)view.findViewById(R.id.sdv);
            name=(TextView)view.findViewById(R.id.name);
            button=(SwitchButton)view.findViewById(R.id.button);
            ll=(LinearLayout)view.findViewById(R.id.ll);
        }
    }
}
