package com.lonekun.waveprogressbardemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private WaveProgressBar mProgressBar;
    private int mProgress = 0;
    private static final int MSG_UPDATE = 0X1111;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_UPDATE:
                    mProgress++;
                    if(mProgress <= 100)
                        mProgressBar.setmCurrentProgress(mProgress);
                    sendEmptyMessageDelayed(MSG_UPDATE, 100);
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = v(R.id.btn_start);
        mProgressBar = v(R.id.progressBar);
        mProgressBar.setWaveColor(Color.parseColor("#e00000ff"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mProgress >= 100){
                    mProgress = 0;
                }else{
                    mHandler.sendEmptyMessage(MSG_UPDATE);
                }
            }
        });
    }

    private <T extends View> T v(int id){
        return (T) findViewById(id);
    }
}
