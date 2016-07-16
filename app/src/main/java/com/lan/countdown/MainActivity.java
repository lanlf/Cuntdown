package com.lan.countdown;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int i;
    private Button bt_start;
    private Button bt_stop;
    private EditText et_time;
    private TextView tv_time;
    private Timer timer;
    private PickerView pv_sec;
    private PickerView pv_min;
    private Button bt_pause;
    private Button bt_resume;
    private TimerTask task;
    private int timerStatus;
    public static final int PREPARE = 0;
    public static final int START = 1;
    public static final int PAUSE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        List<String> min = new ArrayList<String>();
        List<String> sec = new ArrayList<String>();
        for (i = 0; i < 60; i++) {
            min.add(i < 10 ? "0" + i : i + "");
            sec.add(i < 10 ? "0" + i : i + "");
        }
        pv_min.setData(min);
        pv_sec.setData(sec);

    }

    private void initView() {
        bt_start = (Button) findViewById(R.id.bt_start);
        bt_stop = (Button) findViewById(R.id.bt_stop);
        et_time = (EditText) findViewById(R.id.et_time);
        tv_time = (TextView) findViewById(R.id.tv_time);
        pv_min = (PickerView) findViewById(R.id.pv_min);
        pv_sec = (PickerView) findViewById(R.id.pv_sec);
        bt_start.setOnClickListener(this);
        bt_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start:
                if (timerStatus == PREPARE) {
                    start();
                    bt_start.setText("暂停计时");
                } else if (timerStatus == START) {
                    pause();
                    bt_start.setText("继续计时");
                } else if (timerStatus == PAUSE) {
                    resume();
                    bt_start.setText("暂停计时");
                }
                break;
            case R.id.bt_stop:
                stop();
                break;
        }
    }

    private void resume() {
        timer = new Timer();
        timer.schedule(new MyTask(), 0, 1000);
        timerStatus = START;
    }

    private void pause() {
        if (timer != null) {
            timer.cancel();
            timerStatus = PAUSE;
        }
    }


    private void stop() {
        if (timer != null)
            timer.cancel();
        tv_time.setText(0 + "");
        bt_start.setText("开始计时");
        timerStatus = PREPARE;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_time.setText(i + "");
            if (i < 0) {
                timer.cancel();
            }
        }
    };

    private void start() {
        String text = et_time.getText().toString();
        i = Integer.parseInt(text);
        task = new MyTask();
        timer = new Timer();
        timer.schedule(task, 0, 1000);
        timerStatus = START;

    }

    class MyTask extends TimerTask {

        @Override
        public void run() {
            i--;
            Message msg = Message.obtain();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    }
}
