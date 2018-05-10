package com.example.duan.chao.DCZ_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.OperationRecordBean;
import com.example.duan.chao.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by DELL on 2017/7/12.
 */

public class OperationRecordAdapter extends RecyclerView.Adapter<OperationRecordAdapter.ViewHolder>{
    private Context context;
    private  List<OperationRecordBean.ListBean> list;
   /* private OperationRecord2Adapter adapter;
    private List<OperationRecordBean> list2;*/
    public OperationRecordAdapter(Context context,  List<OperationRecordBean.ListBean> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.item_operation2,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Long time = list.get(position).getCreateTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String datetime = format.format(time);

        if(position==0){
            holder.tou.setVisibility(View.VISIBLE);
            holder.xian.setVisibility(View.INVISIBLE);
            holder.title.setText(datetime);
        }else {
            holder.xian.setVisibility(View.VISIBLE);
            Long btime = list.get(position-1).getCreateTime();
            SimpleDateFormat bformat = new SimpleDateFormat("yyyy-MM-dd");
            String bdatetime = bformat.format(btime);

            if(bdatetime.equals(datetime)){
                holder.tou.setVisibility(View.GONE);
            }else {
                holder.tou.setVisibility(View.VISIBLE);
                holder.title.setText(datetime);
            }
        }
        holder.name.setText(MyApplication.map.get(String.valueOf(list.get(position).getSystemId())).toString());
        holder.ip.setText(list.get(position).getIpAddr());
       // holder.ip.setText(list.get(position).getIpAttr()+"  "+list.get(position).getIpAddr());

        Long timec = list.get(position).getCreateTime();
        SimpleDateFormat formatc = new SimpleDateFormat("HH:mm:ss");
        String datetimec = formatc.format(timec);
        /*String[] strs = datetimec.toString().split(" ");
        String timeb = strs[1];*/
        holder.time.setText(datetimec);
        /*    adapter=new OperationRecord2Adapter(context,list);
            holder.lv.setAdapter(adapter);*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView xian;
        LinearLayout tou;
        TextView title;
        TextView time;
        TextView name;
        TextView ip;
        public ViewHolder(View view) {
            super(view);
            //lv=(ListView) view.findViewById(R.id.lv);
            xian=(TextView)view.findViewById(R.id.xian1);
            tou=(LinearLayout)view.findViewById(R.id.tou);
            title=(TextView)view.findViewById(R.id.title);
            time=(TextView)view.findViewById(R.id.time);
            name=(TextView)view.findViewById(R.id.name);
            ip=(TextView)view.findViewById(R.id.ip);
        }
    }
    public void notify( List<OperationRecordBean.ListBean> list){
        this.list=list;
        notifyDataSetChanged();
    }

  /*  private class OperationRecord2Adapter extends BaseAdapter{
        private Context context;
        private  List<OperationRecordBean.ListBean> list;

        public OperationRecord2Adapter(Context context,  List<OperationRecordBean.ListBean> list){
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
    }*/
}
