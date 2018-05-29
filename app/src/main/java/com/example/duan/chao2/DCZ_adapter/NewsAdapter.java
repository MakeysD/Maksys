package com.example.duan.chao2.DCZ_adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duan.chao2.DCZ_application.MyApplication;
import com.example.duan.chao2.DCZ_bean.NewsBean;
import com.example.duan.chao2.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by duan on 2017/6/14.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private Context context;
    private List<NewsBean.DataBean.ListBean>list;

    public NewsAdapter(Context context, List<NewsBean.DataBean.ListBean>list){
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.item_newslist,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsBean.DataBean.ListBean data = list.get(position);
        holder.sdv.setImageURI(Uri.parse(MyApplication.qiniu+data.getFileName()));
        holder.title.setText(data.getTitle());
        holder.type.setText(data.getXxlxTitle());
        holder.time.setText(data.getFbsj());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView sdv;
        TextView title;
        TextView time;
        TextView type;
        public ViewHolder(View view) {
            super(view);
            sdv= (SimpleDraweeView) view.findViewById(R.id.sdv);
            title=(TextView)view.findViewById(R.id.title);
            time=(TextView)view.findViewById(R.id.time);
            type=(TextView)view.findViewById(R.id.type);
        }
    }
    public void notify(List<NewsBean.DataBean.ListBean>list){
        this.list=list;
        notifyDataSetChanged();
    }
}
