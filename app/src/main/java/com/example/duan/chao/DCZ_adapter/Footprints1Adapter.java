package com.example.duan.chao.DCZ_adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.Footprints2Bean;
import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_selft.PullToRefreshLayout;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 2017/7/12.
 */

public class Footprints1Adapter extends BaseAdapter{
    private Context context;
    private  List<Footprints2Bean.DataBean> list;
    private Dialog dialog;

    public Footprints1Adapter(Context context, List<Footprints2Bean.DataBean> list){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_footprints1, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv1.setText(list.get(position).getSystemName().toString());
        viewHolder.tv2.setText(list.get(position).getIp().toString());
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(list.get(position).getSystemId().toString(),list.get(position).getUsername().toString(),list.get(position).getSessionId(),position);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView tv1;
        TextView tv2;
        TextView button;
        public ViewHolder(View view) {
            tv1=(TextView)view.findViewById(R.id.tv1);
            tv2=(TextView)view.findViewById(R.id.tv2);
            button=(TextView)view.findViewById(R.id.button);
        }
    }
    public void Notify(List<Footprints2Bean.DataBean> list){
        this.list=list;
        notifyDataSetChanged();
    }


    /***
     * 踢出用户的登录
     * */
    public void getData(String systemId, String username, String sessionid, final int postion){
        dialog= DialogUtil.createLoadingDialog(context,"努力加载...","1");
        dialog.show();
        HttpServiceClient.getInstance().kickout(systemId,username,sessionid,"8579558852").enqueue(new Callback<FootprintsBean>() {
            @Override
            public void onResponse(Call<FootprintsBean> call, Response<FootprintsBean> response) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            Log.i("dcz","data1返回成功");
                            list.remove(postion);
                            Notify(list);
                        }else {
                            new MiddleDialog(context, MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                        }
                    }else {
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<FootprintsBean> call, Throwable t) {
                dialog.dismiss();
                new MiddleDialog(context,context.getString(R.string.tishi72),R.style.registDialog).show();
            }
        });
    }
}
