package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_adapter.OperationRecordAdapter;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.DCZ_bean.OperationRecordBean;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.ShebeiUtil;
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
 *      操作记录
 *
 * */
public class OperationRecordActivity extends BaseActivity {
    private OperationRecordActivity INSTANCE;
    private OperationRecordAdapter adapter;
    private List<OperationRecordBean.ListBean> list=new ArrayList();
    private Dialog dialog;
    private int num=1;
    private int size=10;
    @BindView(R.id.listview)
    XRecyclerView lv;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.error)
    LinearLayout error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_record);
        INSTANCE=this;
        ButterKnife.bind(this);
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        setListener();
        getData();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
    }
    /**
     *  初始化
     * */
    private void setViews() {
        if(list.size()>0){
            error.setVisibility(View.GONE);
        }else {
            error.setVisibility(View.VISIBLE);
        }
        if(adapter!=null){
            adapter.notify(list);
        }else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(INSTANCE);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            lv.setLayoutManager(linearLayoutManager);
            lv.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            lv.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
            lv.setArrowImageView(R.drawable.pull_icon_big);
            //lv.addItemDecoration(new SpacesItemDecoration(20));
            adapter=new OperationRecordAdapter(INSTANCE,list);
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
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        lv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                lv.refreshComplete();
                num=1;
                list.clear();
                getData();
            }

            @Override
            public void onLoadMore() {
                getData();
                num++;
                lv.loadMoreComplete();
            }
        });
    }

    /***
     * 调取接口拿到服务器数据
     * */
    public void getData(){
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog.show();
        HttpServiceClient.getInstance().getOperation(num,size,null,null).enqueue(new Callback<OperationRecordBean>() {
            @Override
            public void onResponse(Call<OperationRecordBean> call, Response<OperationRecordBean> response) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            if(response.body().getData().getList().size()>0){
                                for(int i=0;i<response.body().getData().getList().size();i++){
                                    list.add(response.body().getData().getList().get(i));
                                }
                                setViews();
                            }else {
                            }
                        }else {
                            if(!response.body().getCode().equals("20003")){
                                new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                            }
                        }
                    }else {
                        Log.d("dcz","返回的数据是空的");
                    }
                }else {
                    Log.d("dcz","获取数据失败");
                }
            }
            @Override
            public void onFailure(Call<OperationRecordBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof OperationRecordActivity){
                    dialog.dismiss();
                    Log.d("dcz异常",t.getMessage());
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }
}
