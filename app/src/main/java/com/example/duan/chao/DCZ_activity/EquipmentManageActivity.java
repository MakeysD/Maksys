package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.duan.chao.DCZ_adapter.EquipmentAdapter;
import com.example.duan.chao.DCZ_bean.EquipmentBean;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.R;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *      设备管理
 *
 * */
public class EquipmentManageActivity extends BaseActivity {
    private EquipmentManageActivity INSTANCE;
    private EquipmentAdapter adapter;
    private Dialog dialog;
    private int pageNumber=1;
    private int pageSize=5;
    private List<EquipmentBean.ListBean> list=new ArrayList();
    @BindView(R.id.listview)
    XRecyclerView lv;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.error)
    TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_manage);
        INSTANCE=this;
        ButterKnife.bind(this);
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        getData();
    }

    /**
     *  初始化
     * */
    private void setViews() {
        if(list.size()>0){
            tv.setText(R.string.tishi21);
            error.setVisibility(View.VISIBLE);
        }else {
            tv.setText(R.string.tishi22);
            error.setVisibility(View.GONE);
        }
        if(adapter!=null){
            lv.loadMoreComplete();
            adapter.Notify(list);
        }else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(INSTANCE);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            lv.setLayoutManager(linearLayoutManager);
            lv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            lv.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
            lv.setArrowImageView(R.mipmap.xiala);
            //lv.addItemDecoration(new SpacesItemDecoration(20));
            adapter=new EquipmentAdapter(INSTANCE,list);
            lv.setAdapter(adapter);
        }
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
        lv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
                getData();
            }
        });
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(){
        HttpServiceClient.getInstance().getEquipent("").enqueue(new Callback<EquipmentBean>() {
            @Override
            public void onResponse(Call<EquipmentBean> call, Response<EquipmentBean> response) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                lv.refreshComplete();
                lv.loadMoreComplete();
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            list = response.body().getData().getList();
                        }else {
                            new MiddleDialog(INSTANCE,response.body().getDesc(),R.style.registDialog).show();
                            finish();
                        }
                    }else {
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
                setViews();
                setListener();
            }
            @Override
            public void onFailure(Call<EquipmentBean> call, Throwable t) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                setViews();
                setListener();
                new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
            }
        });
    }
}
