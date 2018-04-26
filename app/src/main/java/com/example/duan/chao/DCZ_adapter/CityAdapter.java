package com.example.duan.chao.DCZ_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_bean.CityBean;
import com.example.duan.chao.DCZ_bean.CountryBean;
import com.example.duan.chao.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DELL on 2017/7/17.
 */

public class CityAdapter extends BaseAdapter{
    private Context context;
    private  List<CountryBean.DataBean> list;
    private HashMap<String, Integer> indexMap = new HashMap<String, Integer>();
   // private final LayoutInflater mInflate;
    private CityCallback callback;

    public CityAdapter(Context context, List<CountryBean.DataBean> list, CityCallback callback){
        this.context=context;
        this.list=list;
        this.callback=callback;
        Collections.sort(list, new Comparator<CountryBean.DataBean>() {
            @Override
            public int compare(CountryBean.DataBean lhs, CountryBean.DataBean rhs) {
                if (String.valueOf(lhs.getCountryPinyin().charAt(0)).equals(String.valueOf(rhs.getCountryPinyin().charAt(0)))) {
                    return lhs.getCountryName().compareTo(rhs.getCountryName());
                } else {
                    if ("#".equals(String.valueOf(lhs.getCountryPinyin().charAt(0)))) {
                        return 1;
                    } else if ("#".equals(String.valueOf(rhs.getCountryPinyin().charAt(0)))) {
                        return -1;
                    }
                    return String.valueOf(lhs.getCountryPinyin().charAt(0)).compareTo(String.valueOf(rhs.getCountryPinyin().charAt(0)));
                }
            }
        });
       /* mInflate = LayoutInflater.from(context);
        // 列表特征和分组首项进行关联
        for (int i = 0; i <list.size(); i++) {
            CountryBean.DataBean city =list.get(i);
            String cityId = city.getCountryPinyin();
            if(cityId == null || "".equals(cityId)) continue;
            String section = cityId.toUpperCase().substring(0, 1);
            if(!indexMap.containsKey(section)){
                indexMap.put(section, i);
            }
        }*/
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_city, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("dcz",list.get(position).getPhoneCode()+"");
                callback.addAction(list.get(position));
            }
        });
        CountryBean.DataBean city =list.get(position);
        viewHolder.name.setText(list.get(position).getCountryName());
        char idFirstChar = city.getCountryPinyin().charAt(0);//得到当前id
        if (position == 0) {
            setIndex(viewHolder.section, String.valueOf(idFirstChar).toUpperCase());
        } else {
            String preLabel =list.get(position - 1).getCountryPinyin();
            char preFirstChar = preLabel.charAt(0);//得到前一个id
            if (idFirstChar != preFirstChar) {
                setIndex(viewHolder.section, String.valueOf(idFirstChar).toUpperCase());
                viewHolder.section.setVisibility(View.VISIBLE);
            } else { // same group
                viewHolder.section.setVisibility(View.GONE);
            }
        }
        viewHolder.phone.setText("+"+list.get(position).getPhoneCode());
        return convertView;
    }


    public class ViewHolder {
        TextView name;
        TextView section;
        TextView phone;
        RelativeLayout home;
        public ViewHolder(View view) {
            name=(TextView)view.findViewById(R.id.name);
            section=(TextView)view.findViewById(R.id.section);
            phone=(TextView)view.findViewById(R.id.phone);
            home=(RelativeLayout)view.findViewById(R.id.home);
        }
    }
    public void notify(List<CountryBean.DataBean> list){
        this.list=list;
        notifyDataSetChanged();
    }

    private void setIndex(TextView section, String str){
        section.setVisibility(View.VISIBLE);
        if("#".equals(str)) section.setText("当前城市");
        else section.setText(str);
    }

    public interface CityCallback {
        void addAction(CountryBean.DataBean bean);
    }
}
