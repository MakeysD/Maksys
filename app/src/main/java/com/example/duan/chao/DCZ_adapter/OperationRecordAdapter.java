package com.example.duan.chao.DCZ_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.duan.chao.DCZ_bean.OperationRecordBean;
import com.example.duan.chao.R;

import java.util.List;

/**
 * Created by DELL on 2017/7/12.
 */

public class OperationRecordAdapter extends RecyclerView.Adapter<OperationRecordAdapter.ViewHolder>{
    private Context context;
    private List<OperationRecordBean> list;
    private OperationRecord2Adapter adapter;
    private List<OperationRecordBean> list2;
    public OperationRecordAdapter(Context context, List<OperationRecordBean>list){
        this.context=context;
        this.list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.item_operation,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position==0){
            holder.xian1.setVisibility(View.GONE);
        }else {
            holder.xian1.setVisibility(View.VISIBLE);
        }
            adapter=new OperationRecord2Adapter(context,list);
            holder.lv.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView xian1;
        ListView lv;
        public ViewHolder(View view) {
            super(view);
            xian1=(TextView)view.findViewById(R.id.xian1);
            lv=(ListView) view.findViewById(R.id.lv);
        }
    }
    public void notify(List<OperationRecordBean> list){
        this.list=list;
        notifyDataSetChanged();
    }

    private class OperationRecord2Adapter extends BaseAdapter{
        private Context context;
        private List<OperationRecordBean> list;

        public OperationRecord2Adapter(Context context, List<OperationRecordBean>list){
            this.context=context;
            this.list=list;
        }
        @Override
        public int getCount() {
            return 3;
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
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_operation2, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }
    }

    public static class ViewHolder2 {
        TextView tv_goods_fits_name;


        public ViewHolder2(View convertView) {

        }
    }
}
