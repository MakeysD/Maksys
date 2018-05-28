/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.duan.chao.zxing_code.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.duan.chao.DCZ_application.MyApplication;
import com.example.duan.chao.DCZ_util.DensityUtils;
import com.example.duan.chao.DCZ_util.ScreenUtils;
import com.example.duan.chao.R;
import com.example.duan.chao.zxing_code.camera.CameraManager;
import com.google.zxing.ResultPoint;

import java.util.ArrayList;
import java.util.List;


/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 40L;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;

    private CameraManager cameraManager;
    private final Paint paint;
    private final int maskColor;
    private List<ResultPoint> possibleResultPoints;

    // This constructor is used when the class is built from an XML resource.

    private static final int OPAQUE = 0xFF;
    private Paint mPaint;
    private int mScannerAlpha;
    private int mFrameColor;
    private int mLaserColor;
    private int mTextColor;
    private int mFocusThick;
    private int mAngleThick;
    private int mAngleLength;
    private String mTipText;
    private int laserLineResId;//扫描线图片资源
    boolean isFirst;
    private int slideTop;
    private static final int SPEEN_DISTANCE = 15;
    public String getmTipText() {
        return mTipText;
    }

    public void setmTipText(String mTipText) {
        this.mTipText = mTipText;
    }

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.mp_touming);
        possibleResultPoints = new ArrayList<ResultPoint>(5);
        mPaint = new Paint();


        mFrameColor = resources.getColor(R.color.text02);
        mLaserColor = resources.getColor(R.color.colorPrimaryDark);
        mTextColor = resources.getColor(R.color.text02);
        mFocusThick = 1;
        mAngleThick = 8;
        mAngleLength = 40;
        mTipText = "";

        laserLineResId=resources.getIdentifier("chat_sao","mipmap",MyApplication.pkge);
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return; // not ready yet, early draw before done configuring
        }
        Rect frame = cameraManager.getFramingRect();
        if (frame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);
        drawFocusRect(canvas, frame);
        drawAngle(canvas, frame);
        drawText(canvas, frame);
//        drawLaser(canvas, frame);
        drawBitmapLaser(canvas, frame);
        // Request another update at the animation interval, but only repaint the laser line,
        // not the entire viewfinder mask.
        postInvalidateDelayed(ANIMATION_DELAY,
                frame.left - POINT_SIZE,
                frame.top - POINT_SIZE,
                frame.right + POINT_SIZE,
                frame.bottom + POINT_SIZE);
    }

    public void drawViewfinder() {
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        synchronized (points) {
            points.add(point);
            int size = points.size();
            if (size > MAX_RESULT_POINTS) {
                // trim it
                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
            }
        }
    }


    /**
     * 画聚焦框，白色的
     *
     * @param canvas
     * @param rect
     */
    private void drawFocusRect(Canvas canvas, Rect rect) {
        // 绘制焦点框（黑色）
        mPaint.setColor(mFrameColor);
        // 上
        canvas.drawRect(rect.left + mAngleLength, rect.top, rect.right - mAngleLength, rect.top + mFocusThick, mPaint);
        // 左
        canvas.drawRect(rect.left, rect.top + mAngleLength, rect.left + mFocusThick, rect.bottom - mAngleLength,
                mPaint);
        // 右
        canvas.drawRect(rect.right - mFocusThick, rect.top + mAngleLength, rect.right, rect.bottom - mAngleLength,
                mPaint);
        // 下
        canvas.drawRect(rect.left + mAngleLength, rect.bottom - mFocusThick, rect.right - mAngleLength, rect.bottom,
                mPaint);
    }

    /**
     * 画粉色的四个角
     *
     * @param canvas
     * @param rect
     */
    private void drawAngle(Canvas canvas, Rect rect) {
        mPaint.setColor(mLaserColor);
        mPaint.setAlpha(OPAQUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mAngleThick);
        int left = rect.left;
        int top = rect.top;
        int right = rect.right;
        int bottom = rect.bottom;
        // 左上角
        canvas.drawRect(left, top, left + mAngleLength, top + mAngleThick, mPaint);
        canvas.drawRect(left, top, left + mAngleThick, top + mAngleLength, mPaint);
        // 右上角
        canvas.drawRect(right - mAngleLength, top, right, top + mAngleThick, mPaint);
        canvas.drawRect(right - mAngleThick, top, right, top + mAngleLength, mPaint);
        // 左下角
        canvas.drawRect(left, bottom - mAngleLength, left + mAngleThick, bottom, mPaint);
        canvas.drawRect(left, bottom - mAngleThick, left + mAngleLength, bottom, mPaint);
        // 右下角
        canvas.drawRect(right - mAngleLength, bottom - mAngleThick, right, bottom, mPaint);
        canvas.drawRect(right - mAngleThick, bottom - mAngleLength, right, bottom, mPaint);
    }

    private void drawText(Canvas canvas, Rect rect) {
        int margin = 100;
        int len;
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(DensityUtils.dp2px(MyApplication.getContext(), 18));
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offY = fontTotalHeight / 2 - fontMetrics.bottom;
        float newY = rect.bottom + margin + offY;
        if(MyApplication.language.equals("ENGLISH")){
            len = mTipText.length() / 2-1;
        }else if(MyApplication.language.equals("TAI")){
            len = (int) (mTipText.length() / 1.5);
        }else if(MyApplication.language.equals("")){
            if(MyApplication.xitong.equals("en_US")||MyApplication.xitong.equals("en_GB")){
                len = mTipText.length() / 2-1;
            }else if(MyApplication.xitong.equals("th_TH")){
                len = (int) (mTipText.length() / 1.5);
            }else {
                len = mTipText.length();
            }
        }else {
            len = mTipText.length();
        }
        float left = (ScreenUtils.getScreenWidth(MyApplication.getContext()) - mPaint.getTextSize() * len) / 2;
        canvas.drawText(mTipText, left+15, newY, mPaint);
    }

    private void drawLaser(Canvas canvas, Rect rect) {
        // 绘制焦点框内固定的一条扫描线（红色）
        mPaint.setColor(mLaserColor);
        mPaint.setAlpha(SCANNER_ALPHA[mScannerAlpha]);
        mScannerAlpha = (mScannerAlpha + 1) % SCANNER_ALPHA.length;
        int middle = rect.height() / 2 + rect.top;
        canvas.drawRect(rect.left + 2, middle - 1, rect.right - 1, middle + 2, mPaint);
    }


    private void drawBitmapLaser(Canvas canvas, Rect rect) {
        if (!isFirst) {
            isFirst = true;
            slideTop = rect.top;
        }
        slideTop += SPEEN_DISTANCE;
        if (slideTop >= rect.bottom) {
            slideTop = rect.top;
        }
        Bitmap laserLineBitmap = BitmapFactory.decodeResource(getResources(), laserLineResId);
        int h = laserLineBitmap.getHeight()/2;//取原图高
        //如果没有设置线条高度，则用图片原始高度
//            if (laserLineHeight == Scanner.dp2px(getContext(), DEFAULT_LASER_LINE_HEIGHT)) {
//                laserLineHeight = laserLineBitmap.getHeight() / 2;
//            }
        Rect laserRect = new Rect(rect.left, slideTop, rect.right, slideTop + h);
        canvas.drawBitmap(laserLineBitmap, null, laserRect, paint);
    }


}
