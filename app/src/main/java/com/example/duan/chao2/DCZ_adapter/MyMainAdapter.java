package com.example.duan.chao2.DCZ_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/5/18.
 */
public class MyMainAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;

    public MyMainAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.list = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return POSITION_NONE;
    }

    public void datechange(ArrayList<Fragment> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }
}