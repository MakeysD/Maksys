package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_adapter.EquipmentAdapter;
import com.example.duan.chao.DCZ_adapter.SecurityAdapter;
import com.example.duan.chao.DCZ_bean.EquipmentBean;
import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.DCZ_bean.SecurityBean;
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
 *     安全保护
 *
 * */
public class SecurityProtectActivity extends BaseActivity {
    private SecurityProtectActivity INSTANCE;
    private SecurityAdapter adapter;
    private Dialog dialog;
    private int pageNumber=1;
    private int pageSize=5;
    private List<SecurityBean.ListBean> list=new ArrayList();
    @BindView(R.id.back)
    View back;
    @BindView(R.id.listview)
    XRecyclerView lv;
    @BindView(R.id.tv)
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_protect);
        INSTANCE=this;
        ButterKnife.bind(this);
        getData();
    }

    /**
     *  初始化
     * */
    private void setViews() {
        if(list.size()>0){
            tv.setText("已开启"+list.size()+"类业务账户授权保护");
        }else {
            tv.setText("未开启账户授权保护");
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
            adapter=new SecurityAdapter(INSTANCE,list);
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
                lv.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                lv.refreshComplete();
            }
        });
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(){
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().protect("").enqueue(new Callback<SecurityBean>() {
            @Override
            public void onResponse(Call<SecurityBean> call, Response<SecurityBean> response) {
                dialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            Toast.makeText(INSTANCE,response.body().getDesc(), Toast.LENGTH_SHORT).show();
                            list=response.body().getData().getList();
                            setViews();
                            setListener();
                        }else {
                            Toast.makeText(INSTANCE,response.body().getDesc(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }else {
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<SecurityBean> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(INSTANCE, "服务器异常", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
