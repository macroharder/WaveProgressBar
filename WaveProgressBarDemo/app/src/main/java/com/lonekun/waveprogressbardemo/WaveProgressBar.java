package com.lonekun.waveprogressbardemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * A progressbar update progress like wave
 * Created by liukun on 15/9/24.
 */
public class WaveProgressBar extends View {
    private int mWidth;
    private int mHeight;

    private Bitmap mBitmap; //用于创建PorterDuff画布的Bitmap
    private Canvas mBitmapCanvas; //用于PorterDuff图形混合的canvas

    private Path mPath; //绘制贝塞尔曲线的Path对象
    private Paint mPathPaint; //绘制波纹贝塞尔曲线的画笔对象
    private int mWaveColor;

    private Paint mCirclePaint; //绘制背景圆的画笔对象
    private int mCircleColor;
    private int mRadius;

    private Paint mTextPaint; //绘制进度文字的画笔对象
    private int mTextSize;
    private int mTextColor;

    private int mMaxProgress = 100; //最大进度,默认为100
    private int mCurrentProgress = 0; //当前进度

    private int mStartX = -80; //用于让贝塞尔曲线波动的起始绘制坐标

    private MyHandler mHandler = new MyHandler(this);
    private static final int MSG_INVALIDATE = 0X1111;

    private static class MyHandler extends Handler {
        private WeakReference<WaveProgressBar> waveProgressBarWeakReference;

        public MyHandler(WaveProgressBar progressBar){
            this.waveProgressBarWeakReference = new WeakReference<WaveProgressBar>(progressBar);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WaveProgressBar progressBar = waveProgressBarWeakReference.get();
            if(progressBar != null){
                switch (msg.what){
                    case MSG_INVALIDATE:
                        if(progressBar.mCurrentProgress < progressBar.mMaxProgress){
                            progressBar.mStartX += 5;
                            if(progressBar.mStartX > 0){
                                progressBar.mStartX = -80;
                            }
                            progressBar.invalidate();
                            sendEmptyMessageDelayed(MSG_INVALIDATE, 50);
                        }
                        break;
                }
            }
        }
    }

    public WaveProgressBar(Context context) {
        super(context);
        init();
    }

    public WaveProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mPath = new Path();
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setColor(Color.argb(0xff, 0xff, 0x69, 0x5a));
        mPathPaint.setStyle(Paint.Style.FILL);
        //只显示重合部分
        Xfermode mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mPathPaint.setXfermode(mode);

        //初始化绘制背景圆的画笔对象
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.argb(0xff, 0xff, 0x8e, 0x8b));

        //初始化绘制进度文字的画笔对象
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.argb(0xff, 0xFF, 0xF3, 0xF7));
        mTextPaint.setTextSize(50);

        //通知刷新
        mHandler.sendEmptyMessageDelayed(MSG_INVALIDATE, 50);
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    public void setmCurrentProgress(int currentProgress) {
        if(currentProgress > mMaxProgress){
            this.mCurrentProgress = mMaxProgress;
        }else{
            this.mCurrentProgress = currentProgress;
        }
        invalidate();
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        if(maxProgress < 0){
            throw new RuntimeException("the max progress must bigger than zero!");
        }else{
           this.mMaxProgress = maxProgress;
        }
    }


    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        mTextPaint.setColor(textColor);
    }

    public void setCircleColor(int circleColor) {
        this.mCircleColor = circleColor;
        mCirclePaint.setColor(circleColor);
    }

    public void setWaveColor(int waveColor) {
        this.mWaveColor = waveColor;
        mPathPaint.setColor(waveColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //the default size is 200px*200px
        if(mWidth <= 5 || mHeight <= 5 || widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST){
            mWidth = mHeight = 200;
            setMeasuredDimension(mWidth, mHeight);
        }

        mRadius = Math.min(mWidth, mHeight) / 2;
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mBitmapCanvas = new Canvas(mBitmap);
        mTextSize = mRadius / 3;
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBitmapCanvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mCirclePaint);

        mPath.reset();
        mPath.moveTo(mWidth / 2 + mRadius, mHeight / 2 + mRadius - (mCurrentProgress * mRadius * 2 / mMaxProgress));
        mPath.lineTo(mWidth / 2 + mRadius, mHeight);
        mPath.lineTo(mStartX, mHeight / 2 + mRadius);
        mPath.lineTo(mStartX, mHeight / 2 + mRadius - (mCurrentProgress * mRadius * 2 / mMaxProgress));
        for(int i = 0; i < 10; i++){
            mPath.rQuadTo(24, 6, 48, 0);
            mPath.rQuadTo(24, -6, 48, 0);
        }
        mPath.close();
        if(mCurrentProgress > 0)
            mBitmapCanvas.drawPath(mPath, mPathPaint);

        mBitmapCanvas.drawText(mCurrentProgress * 100f / mMaxProgress + "%", mWidth / 2, mHeight / 2 + mTextSize / 2, mTextPaint);

        canvas.drawBitmap(mBitmap, 0, 0, null);
    }
}
