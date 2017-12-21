package com.example.duan.chao.DCZ_util;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.MainActivity;
import com.example.duan.chao.R;

import java.math.BigDecimal;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-04-25
 * @Time: 12:02
 * @des 圆形转圈的 View
 */
public class MyRoundProcess extends View {

    /**
     * 自定义属性：
     * <p/>
     * 1. 外层圆的颜色 roundColor
     * <p/>
     * 2. 弧形进度圈的颜色 rouncProgressColor
     * <p/>
     * 3. 中间百分比文字的颜色 textColor
     * <p/>
     * 4. 中间百分比文字的大小 textSize
     * <p/>
     * 5. 圆环的宽度（以及作为弧形进度的圆环宽度）
     * <p/>
     * 6. 圆环的风格（Paint.Style.FILL  Paint.Syle.Stroke）
     */


    private static final String TAG = "MyRoundProcess";

    private int mWidth;
    private int mHeight;

    private Paint mPaint;
    private Paint mTextPaint;

    public float progress = 0f;
    private final float maxProgress = 100f; // 不可以修改的最大值

    //region 自定义属性的值
    int roundColor;
    int roundProgressColor;
    int textColor;
    float textSize;
    //endregion

    // 画笔的粗细（默认为40f, 在 onLayout 已修改）
    private float mStrokeWidth = 40f;
    public  ValueAnimator mAnimator;
    private float mLastProgress = -1;

    public MyRoundProcess(Context context) {
        this(context, null);
    }

    public MyRoundProcess(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRoundProcess(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化属性
        initAttrs(context, attrs, defStyleAttr);

        // 初始化点击事件
        //initClickListener();
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.MyRoundProcess);

            roundColor = a.getColor(R.styleable.MyRoundProcess_roundColor, getResources().getColor(android.R.color.darker_gray));
            roundProgressColor = a.getColor(R.styleable.MyRoundProcess_roundProgressColor, getResources().getColor(android.R.color.holo_red_dark));
            textColor = a.getColor(R.styleable.MyRoundProcess_textColor, getResources().getColor(android.R.color.holo_blue_dark));
            textSize = a.getDimension(R.styleable.MyRoundProcess_textSize, 22f);

        } finally {
            // 注意，别忘了 recycle
            a.recycle();
        }
    }

    /**
     * 初始化点击事件
     */
    private void initClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 重新开启动画
                restartAnimate();
            }
        });
    }

    /**
     * 当开始布局时候调用
     */
    private static ObjectAnimator anima;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // 获取总的宽高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        // 初始化各种值
        initValue();

        // 设置圆环画笔
        setupPaint();

        // 设置文字画笔
        setupTextPaint();
    }

    /**
     * 初始化各种值
     */
    private void initValue() {
        // 画笔的粗细为总宽度的 1 / 15
        mStrokeWidth = mWidth / 27f;
    }

    /**
     * 设置圆环画笔
     */
    private void setupPaint() {
        // 创建圆环画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(roundColor);
        mPaint.setStyle(Paint.Style.STROKE); // 边框风格
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    /**
     * 设置文字画笔
     */
    private void setupTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
    }
    private RectF oval;
    private int cha=0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 第一步：绘制一个圆环
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(roundColor);

        float cx = mWidth / 2.0f;
        float cy = mHeight / 2.0f;
        float radius = mWidth / 2.0f - mStrokeWidth / 2.0f-cha;
        canvas.drawCircle(cx, cy, radius, mPaint);
        float ao = progress / 100 * 360-90;
        double x1 = cx + radius * cos(ao * PI / 180);
        double y1 = cy + radius * sin(ao * PI / 180);
        MyApplication.x=(int) x1;MyApplication.y=(int) y1;
        // 第二步：绘制文字
       /* String text = ((int) (progress / maxProgress * 100)) + "%";
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, mWidth / 2 - bounds.width() / 2, mHeight / 2 + bounds.height() / 2, mTextPaint);*/

        // 第三步：绘制动态进度圆环
        mPaint.setDither(true);                 //设置防抖动
        mPaint.setStrokeJoin(Paint.Join.BEVEL);//线段的连接处的样式(转弯连接的样式)
        mPaint.setStrokeCap(Paint.Cap.ROUND); //  设置圆环头部为圆形
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(roundProgressColor);
        //参数是左上右下
        oval = new RectF(cha + mStrokeWidth / 2, cha + mStrokeWidth / 2, mWidth - mStrokeWidth / 2-cha, mHeight - mStrokeWidth / 2-cha);
        canvas.drawArc(oval,270, progress / maxProgress * 360, false, mPaint);
        Paint paint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        paint.setColor(Color.YELLOW);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
    }

    /**
     * 重新开启动画
     */
    public void restartAnimate() {
        if (mLastProgress > 0) {
            // 取消动画
            cancelAnimate();
            // 重置进度
            setProgress(0f);
            // 重新开启动画
            runAnimate(mLastProgress);
        }
    }

    /**
     * 设置当前显示的进度条
     *
     * @param progress
     */
    public void setProgress(float progress) {
        this.progress = progress;
       // Log.i("dczq",progress+"");
        // 使用 postInvalidate 比 postInvalidat() 好，线程安全
        postInvalidate();
    }


    /**
     * 开始执行动画
     *
     * @param targetProgress 最终到达的进度
     */
    public void runAnimate(float targetProgress) {
        // 运行之前，先取消上一次动画
        cancelAnimate();
        //Log.i("dcz进度",targetProgress+"");
        mLastProgress = targetProgress;
        //FloatEvaluator估值器，估算从 0 度角到 targetProgress 度角中间的所有值
        mAnimator = ValueAnimator.ofObject(new FloatEvaluator(), 0, targetProgress);
        // 设置差值器
       // mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                getGress(value);
                setProgress(value);
                if((int)value==100){
                    restartAnimate();
                }
            }
        });
        //创建动画,这里的关键就是使用ArgbEvaluator, 后面2个参数就是 开始的颜色,和结束的颜色.
       /*
        final ValueAnimator colorAnimator1 = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#75E900"), Color.parseColor("#75E900"));
        colorAnimator1.setDuration(15000);
        colorAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();//之后就可以得到动画的颜色了
                roundProgressColor=color;
                if(Color.parseColor("#75E900")==(int)animation.getAnimatedValue()){
                    colorAnimator2.start();
                }
            }
        });
        colorAnimator1.start();*/
        mAnimator.setDuration((long) (targetProgress * (MyApplication.DEFAULT_INTERVAL*10)));
        mAnimator.start();
    }

    public void getGress(float progres){
        float progress=progres/100;
     //   Log.i("dcqa",progres+"");
     //   Log.i("dcqb",progress+"");
        int a=(int)(progress*MyApplication.DEFAULT_INTERVAL);
    //    Log.i("dcqc",a+"");
    //    Log.i("dczd",1-(float)5/(float)MyApplication.DEFAULT_INTERVAL+"");
        if(progress>=1-(float)5/(float)MyApplication.DEFAULT_INTERVAL){
            MainActivity.handler.sendEmptyMessage(MyApplication.DEFAULT_INTERVAL-a);
    //        Log.i("dcqq",MyApplication.DEFAULT_INTERVAL-a+"");
        }else {
     //       Log.i("dcqw","101");
            MainActivity.handler.sendEmptyMessage(101);
        }
        float r = 117;
        float g = 233;
        if (progress>=0.5 && progress< 0.75) {
            r += ((progress -0.5)/0.125) *(255 - 117);
            if (r>=255) {
                r = 255;
            }
            g -= ((progress -0.5)/0.125)*(233-222);
            if (g<=222) {
                g = 222;
            }
        }else if (progress>= 0.75){
            r = 255;
            g -= ((progress -0.75)/0.125)*(222-2);
            if (g<=2) {
                g = 2;
            }
        }
        roundProgressColor= Color.rgb((int) r,(int) g,0);
    }

    /**
     * 取消动画
     */
    public void cancelAnimate() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }
}
