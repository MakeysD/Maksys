package com.example.duan.chao.DCZ_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.DCZ_selft.GridViewForScrollView;
import com.example.duan.chao.R;

import java.util.List;

/**
 * Created by DELL on 2017/7/12.
 */

public class Footprints2Adapter extends BaseAdapter{
    private Context context;
    private List<FootprintsBean> list;
    private Footprints3Adapter adapter;

    public Footprints2Adapter(Context context, List<FootprintsBean>list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_footprints2, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position==0){
            viewHolder.xian1.setVisibility(View.GONE);
        }else {
            viewHolder.xian1.setVisibility(View.VISIBLE);
        }
        adapter=new Footprints3Adapter(context,list);
        viewHolder.lv.setAdapter(adapter);
        return convertView;
    }

    public class ViewHolder {
        GridViewForScrollView lv;
        TextView tv1;
        TextView tv2;
        TextView xian1;
        public ViewHolder(View view) {
            lv=(GridViewForScrollView)view.findViewById(R.id.lv);
            tv1=(TextView)view.findViewById(R.id.tv1);
            tv2=(TextView)view.findViewById(R.id.tv2);
            xian1=(TextView)view.findViewById(R.id.xian1);
        }
    }
    public void notify(List<FootprintsBean> list){
        this.list=list;
        notifyDataSetChanged();
    }
/**
 *      嵌套的listview
 *
 * */
    private class Footprints3Adapter extends BaseAdapter{
        private Context context;
        private List<FootprintsBean> list;

        public Footprints3Adapter(Context context, List<FootprintsBean>list){
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
    }
}
