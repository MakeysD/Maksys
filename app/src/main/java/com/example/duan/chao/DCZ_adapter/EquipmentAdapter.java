package com.example.duan.chao.DCZ_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duan.chao.DCZ_bean.EquipmentBean;
import com.example.duan.chao.R;

import java.util.List;

/**
 * Created by DELL on 2017/7/12.
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder>{
    private Context context;
    private List<EquipmentBean.ListBean> list;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv1.setText(list.get(position).getGratSysName());
        holder.tv2.setText(list.get(position).getOs()+"");
        holder.tv3.setText(list.get(position).getGratTime()+"");
        holder.tv4.setText(list.get(position).getLastLoginTime()+"");

    }

    @Override
    public int getItemCount() {
        return 3;
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
    public void notify(List<EquipmentBean.ListBean>list){
        this.list=list;
        notifyDataSetChanged();
    }
}
