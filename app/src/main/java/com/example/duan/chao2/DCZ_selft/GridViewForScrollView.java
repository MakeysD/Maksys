package com.example.duan.chao2.DCZ_selft;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by duan on 2017/5/4.
 * 这个gridview是为了使嵌套在scrollview中显示全部，而并非显示一行
 */

public class GridViewForScrollView extends ListView{

    public GridViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewForScrollView(Context context) {
        super(context);
    }

    public GridViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
