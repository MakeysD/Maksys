package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_adapter.EquipmentAdapter;
import com.example.duan.chao.DCZ_adapter.SecurityAdapter;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.EquipmentBean;
import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.DCZ_bean.SecurityBean;
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
 *     安全保护
 *
 * */
public class SecurityProtectActivity extends BaseActivity implements SecurityAdapter.ActionCallback{
    private SecurityProtectActivity INSTANCE;
    private SecurityAdapter adapter;
    private Dialog dialog;
    private int dong=0;
    private int number=0;
    private int pageNumber=1;
    private int pageSize=5;
    private List<SecurityBean.ListBean> list=new ArrayList();
    @BindView(R.id.back)
    View back;
    @BindView(R.id.listview)
    XRecyclerView lv;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.error)
    LinearLayout error;
    @BindView(R.id.tou)
    LinearLayout tou;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_protect);
        INSTANCE=this;
        ButterKnife.bind(this);
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
        for(int i=0;i<list.size();i++){
            if(list.get(i).getEnable().equals("1")&&!list.get(i).getFrozenStatus().equals("2")){
                number=number+1;
            }
            if(list.get(i).getFrozenStatus().equals("2")){
                dong=dong+1;
            }
        }
        if(list.size()>0){
            tou.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
            if(number==0){
                title.setText(INSTANCE.getString(R.string.tishi98));
            }else {
                title.setText(INSTANCE.getString(R.string.tishi96)+number+INSTANCE.getString(R.string.tishi97));
            }
            if(list.size()==dong){
                tou.setVisibility(View.GONE);
            }
        }else {
            tou.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
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
            lv.setArrowImageView(R.drawable.pull_icon_big);
            //lv.addItemDecoration(new SpacesItemDecoration(20));
            adapter=new SecurityAdapter(INSTANCE,list,this);
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
        if(ShebeiUtil.wang(INSTANCE).equals("0")){
            new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi116),R.style.registDialog).show();
            return;
        }
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        HttpServiceClient.getInstance().protect("").enqueue(new Callback<SecurityBean>() {
            @Override
            public void onResponse(Call<SecurityBean> call, Response<SecurityBean> response) {
                dialog.dismiss();
                Log.i("dcz",response.body().getCode());
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("10516")){
                            MyApplication.sf.edit().putString("cookie","").commit();
                            MyApplication.token="";MyApplication.sf.edit().putString("token","").commit();
                            MyApplication.language="";MyApplication.sf.edit().putString("language","").commit();
                            new MiddleDialog(ActivityUtils.getInstance().getCurrentActivity(),INSTANCE.getString(R.string.tishi101),INSTANCE.getString(R.string.code42),"",new MiddleDialog.onButtonCLickListener2() {
                                @Override
                                public void onActivieButtonClick(Object bean, int position) {
                                    ActivityUtils.getInstance().getCurrentActivity().startActivity(new Intent(ActivityUtils.getInstance().getCurrentActivity(), LoginEmailActivity.class));
                                    ActivityUtils.getInstance().popAllActivities();
                                }
                            }, R.style.registDialog).show();
                            return;
                        }
                        if(response.body().getCode().equals("20000")){
                            list=response.body().getData().getList();
                            setViews();
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
            public void onFailure(Call<SecurityBean> call, Throwable t) {
                if(ActivityUtils.getInstance().getCurrentActivity() instanceof SecurityProtectActivity){
                    dialog.dismiss();
                    new MiddleDialog(INSTANCE,INSTANCE.getString(R.string.tishi72),R.style.registDialog).show();
                }
            }
        });
    }

    @Override
    public void addAction(String string) {
        if(string.equals("1")){
            number=number+1;
            title.setText(INSTANCE.getString(R.string.tishi96)+number+INSTANCE.getString(R.string.tishi97));
        }else {
            number=number-1;
            if(number==0){
                title.setText(INSTANCE.getString(R.string.tishi98));
            }else {
                title.setText(INSTANCE.getString(R.string.tishi96)+number+INSTANCE.getString(R.string.tishi97));
            }
        }
    }
}
