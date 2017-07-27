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
    private List<EquipmentBean> list;

    public EquipmentAdapter(Context context, List<EquipmentBean>list){
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

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        TextView tv2;
        TextView tv3;
        public ViewHolder(View view) {
            super(view);
            tv1=(TextView)view.findViewById(R.id.tv1);
            tv2=(TextView)view.findViewById(R.id.tv2);
            tv3=(TextView)view.findViewById(R.id.tv3);
        }
    }
    public void notify(List<EquipmentBean>list){
        this.list=list;
        notifyDataSetChanged();
    }
}
