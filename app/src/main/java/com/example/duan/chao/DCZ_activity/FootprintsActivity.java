package com.example.duan.chao.DCZ_activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan.chao.DCZ_adapter.Footprints1Adapter;
import com.example.duan.chao.DCZ_adapter.Footprints2Adapter;
import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.Footprints2Bean;
import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.DCZ_selft.BounceScrollView;
import com.example.duan.chao.DCZ_selft.GridViewForScrollView;
import com.example.duan.chao.DCZ_selft.MiddleDialog;
import com.example.duan.chao.DCZ_selft.OnScrollChangedCallback;
import com.example.duan.chao.DCZ_selft.PullToRefreshLayout;
import com.example.duan.chao.DCZ_util.ActivityUtils;
import com.example.duan.chao.DCZ_util.DialogUtil;
import com.example.duan.chao.DCZ_util.HttpServiceClient;
import com.example.duan.chao.DCZ_util.RandomUtil;
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
    private int Int=2;
    private Dialog dialog;
    private List<FootprintsBean.ListBean> list=new ArrayList<>();
    private List<Footprints2Bean.DataBean> list2=new ArrayList<>();
    private int num=1;
    private int size=30;
    private int newAlpha;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.lv1)
    GridViewForScrollView lv1;
    @BindView(R.id.lv2)
    GridViewForScrollView lv2;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout pullToRefreshLayout;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.error)
    TextView error;
    @BindView(R.id.sv)
    BounceScrollView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprints);
        INSTANCE=this;
        ButterKnife.bind(this);
        setListener();
        RandomUtil.lo();
        dialog= DialogUtil.createLoadingDialog(this,getString(R.string.loaddings),"1");
        dialog.show();
        getData();
    }

    /**
     *  初始化
     * */
    private void setViews() {
        if(list.size()>0||list2.size()>0){
            error.setVisibility(View.GONE);
        }else {
            error.setVisibility(View.VISIBLE);
        }
    }
    /**
     *  监听
     * */
    private void setListener() {
    /*    sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    Log.i("dcz","手指停下来了");
                    if(newAlpha>Int){
                        if(newAlpha-Int>3){
                            Log.i("dcz2","开始调接口");
                            Int=newAlpha;
                            getData();
                            num++;
                        }
                    }
                }
                return false;
            }
        });
        sv.setOnScrollChangedCallback(new OnScrollChangedCallback() {
            @Override
            public void onScroll(int l, int t) {
                newAlpha = t / 400+2;
            }
        });*/

    lv2.setOnScrollListener(new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Log.i("dcz_当前可见",totalItemCount+"");
        }
    });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.getInstance().popActivity(INSTANCE);
            }
        });
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                Log.i("dcz","onRefresh");
                pullToRefreshLayout.setCanPullUp(true);
                num=1;
                list.clear();
                getData();
                getData2();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData();
                num++;
            }

            @Override
            public void onEvent() {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("dcz","按下了返回键");
        ActivityUtils.getInstance().popActivity(this);
    }

    /***
     * 获取最近登录记录
     * */
    public void getData(){
        HttpServiceClient.getInstance().getFoot(num,size,"",null,null).enqueue(new Callback<FootprintsBean>() {
            @Override
            public void onResponse(Call<FootprintsBean> call, Response<FootprintsBean> response) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                getData2();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        if(response.body().getCode().equals("20000")){
                            Log.i("dcz","data1返回成功");
                            if(response.body().getData().getList().size()>0){
                                for(int i=0;i<response.body().getData().getList().size();i++){
                                    list.add(response.body().getData().getList().get(i));
                                }
                            }else {
                                Log.i("dcz","setCanPullUp");
                                pullToRefreshLayout.setCanPullUp(false);
                            }
                            if(list.size()>0){
                                tv2.setVisibility(View.VISIBLE);
                                if(adapter2==null){
                                    adapter2=new Footprints2Adapter(INSTANCE,list);
                                    lv2.setAdapter(adapter2);
                                }else {
                                    adapter2.notify(list);
                                }
                            }else {
                                tv2.setVisibility(View.GONE);
                            }

                        }else {
                            new MiddleDialog(INSTANCE,MyApplication.map.get(response.body().getCode()).toString(),R.style.registDialog).show();
                            //Toast.makeText(INSTANCE,response.body().getDesc(), Toast.LENGTH_SHORT).show();
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
     * 获取各子系统的在线情况
     * */
    public void getData2(){
        HttpServiceClient.getInstance().getOnline(num,size,MyApplication.username,"5896523256").enqueue(new Callback<Footprints2Bean>() {
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
                                list2 = response.body().getData();
                                setViews();
                                if(list2.size()>0){
                                    adapter1=new Footprints1Adapter(INSTANCE,list2);
                                    lv1.setAdapter(adapter1);
                                    tv1.setVisibility(View.VISIBLE);
                                }else {
                                    tv1.setVisibility(View.GONE);
                                }
                            }else {
                                adapter1.Notify(list2);
                            }
                        }else {
                            //new MiddleDialog(INSTANCE,response.body().getDesc(),R.style.registDialog).show();
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
