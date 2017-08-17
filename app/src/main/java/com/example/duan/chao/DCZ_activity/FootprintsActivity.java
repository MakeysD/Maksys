package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.duan.chao.DCZ_adapter.Footprints1Adapter;
import com.example.duan.chao.DCZ_adapter.Footprints2Adapter;
import com.example.duan.chao.DCZ_bean.Footprints2Bean;
import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.DCZ_selft.GridViewForScrollView;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_selft.PullToRefreshLayout;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *      足迹
 *
 * */
public class FootprintsActivity extends BaseActivity {
    private FootprintsActivity INSTANCE;
    private Footprints1Adapter adapter1;
    private Footprints2Adapter adapter2;
    private Dialog dialog;
    private List<FootprintsBean.ListBean> list=new ArrayList<>();
    private int num=1;
    private int size=10;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.lv1)
    GridViewForScrollView lv1;
    @BindView(R.id.lv2)
    GridViewForScrollView lv2;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout pullToRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprints);
        INSTANCE=this;
        ButterKnife.bind(this);
        setListener();
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        //getData2();
        getData();
    }

    /**
     *  初始化
     * */
    private void setViews() {
        adapter1=new Footprints1Adapter(INSTANCE,list);
        lv1.setAdapter(adapter1);
        adapter2=new Footprints2Adapter(INSTANCE,list);
        lv2.setAdapter(adapter2);
    }
    /**
     *  监听
     * */
    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                Log.i("dcz","onRefresh");
                num=1;
                size=10;
                getData();
                //getData2();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData();
                num++;
                size=size+10;
            }

            @Override
            public void onEvent() {

            }
        });
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(){
        HttpServiceClient.getInstance().getFoot(num,size,"",null,null).enqueue(new Callback<FootprintsBean>() {
            @Override
            public void onResponse(Call<FootprintsBean> call, Response<FootprintsBean> response) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            Log.i("dcz","data1返回成功");
                            list=response.body().getData().getList();
                            if(adapter2==null){
                                adapter2=new Footprints2Adapter(INSTANCE,list);
                                lv2.setAdapter(adapter2);
                            }else {
                                adapter2.notify(list);
                            }
                            setListener();
                        }else {
                            new MiddleDialog(INSTANCE,response.body().getDesc(),R.style.registDialog).show();
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
                new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
            }
        });
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData2(){
        HttpServiceClient.getInstance().getOnline(null).enqueue(new Callback<Footprints2Bean>() {
            @Override
            public void onResponse(Call<Footprints2Bean> call, Response<Footprints2Bean> response) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            Log.i("dcz","data2返回成功");
                            if(adapter1==null){
                                adapter1=new Footprints1Adapter(INSTANCE,list);
                                lv1.setAdapter(adapter1);
                            }else {
                                adapter1.notify(list);
                            }
                        }else {
                            new MiddleDialog(INSTANCE,response.body().getDesc(),R.style.registDialog).show();
                        }
                    }else {
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<Footprints2Bean> call, Throwable t) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
            }
        });
    }
}
