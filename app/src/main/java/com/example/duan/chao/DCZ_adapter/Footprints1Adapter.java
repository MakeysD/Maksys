package com.example.duan.chao.DCZ_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.R;

import java.util.List;

/**
 * Created by DELL on 2017/7/12.
 */

public class Footprints1Adapter extends BaseAdapter{
    private Context context;
    private List<FootprintsBean.ListBean> list;

    public Footprints1Adapter(Context context, List<FootprintsBean.ListBean> list){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_footprints1, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public class ViewHolder {
        TextView tv1;
        TextView tv2;
        public ViewHolder(View view) {
            tv1=(TextView)view.findViewById(R.id.tv1);
            tv2=(TextView)view.findViewById(R.id.tv2);
        }
    }
    public void notify(List<FootprintsBean.ListBean> list){
        this.list=list;
        notifyDataSetChanged();
    }
}
