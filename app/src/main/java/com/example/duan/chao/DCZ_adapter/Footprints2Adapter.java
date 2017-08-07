package com.example.duan.chao.DCZ_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.DCZ_selft.GridViewForScrollView;
import com.example.duan.chao.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by DELL on 2017/7/12.
 */

public class Footprints2Adapter extends BaseAdapter{
    private Context context;
    private List<FootprintsBean.ListBean> list;

    public Footprints2Adapter(Context context, List<FootprintsBean.ListBean> list){
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_operation2, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Long time = list.get(position).getCreateTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String datetime = format.format(time);

        if(position==0){
            viewHolder.tou.setVisibility(View.VISIBLE);
            viewHolder.title.setText(datetime);
        }else {
            Long btime = list.get(position-1).getCreateTime();
            SimpleDateFormat bformat = new SimpleDateFormat("yyyy-MM-dd");
            String bdatetime = bformat.format(btime);

            if(bdatetime.equals(datetime)){
                viewHolder.tou.setVisibility(View.GONE);
            }else {
                viewHolder.tou.setVisibility(View.VISIBLE);
                viewHolder.title.setText(datetime);
            }
        }
        viewHolder.name.setText(list.get(position).getSystemName());
        viewHolder.ip.setText(list.get(position).getIpAddr());

        Long timec = list.get(position).getCreateTime();
        SimpleDateFormat formatc = new SimpleDateFormat("HH:mm:ss");
        String datetimec = formatc.format(timec);
        /*String[] strs = datetimec.toString().split(" ");
        String timeb = strs[1];*/
        viewHolder.time.setText(datetimec);

        return convertView;
    }

    public class ViewHolder {
        LinearLayout tou;
        TextView title;
        TextView time;
        TextView name;
        TextView ip;
        public ViewHolder(View view) {
            tou=(LinearLayout)view.findViewById(R.id.tou);
            title=(TextView)view.findViewById(R.id.title);
            time=(TextView)view.findViewById(R.id.time);
            name=(TextView)view.findViewById(R.id.name);
            ip=(TextView)view.findViewById(R.id.ip);
        }
    }
    public void notify(List<FootprintsBean.ListBean> list){
        this.list=list;
        notifyDataSetChanged();
    }
/**
 *      嵌套的listview
 *
 * */
 /*   private class Footprints3Adapter extends BaseAdapter{
        private Context context;
        private List<FootprintsBean.ListBean> list;

        public Footprints3Adapter(Context context, List<FootprintsBean.ListBean> list){
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_footprints3, null);
                viewHolder = new ViewHolder2(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder2) convertView.getTag();
            }

            return convertView;
        }
    }

    public  class ViewHolder2 {
        TextView tv_goods_fits_name;
        public ViewHolder2(View convertView) {

        }
    }*/
}
