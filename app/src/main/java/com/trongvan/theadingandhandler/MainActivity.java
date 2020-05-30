package com.trongvan.theadingandhandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final String TOKEN_MSG = "position_display";

    private Handler handler;
    private Thread bgWork;
    private Button[] buttons;

    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRefAndEvent();

    }

    private void setRefAndEvent() {
        this.buttons = new Button[]{
                this.findViewById(R.id.btn_top),
                this.findViewById(R.id.btn_top_right),
                this.findViewById(R.id.btn_right),
                this.findViewById(R.id.btn_right_bottom),
                this.findViewById(R.id.btn_bottom),
                this.findViewById(R.id.btn_bottom_left),
                this.findViewById(R.id.btn_left),
                this.findViewById(R.id.btn_left_top),
        };


        Log.e("EX", this.buttons.length + " " + buttons[0].getId());
        this.bgWork = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                try {
                    while (true) {
                        i = (i > (buttons.length - 1)) ? 0 : i;
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt(TOKEN_MSG, i);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        Thread.sleep(200);
                        i++;
                    }
                } catch (InterruptedException e) {
                    Log.e("EX", e.toString());
                }

            }
        });

        this.handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();
                int index = bundle.getInt(TOKEN_MSG);
                MainActivity.this.show(index);
            }
        };
    }

    private void hideAll() {
        for (int i = 0; i < this.buttons.length; i++) {
            this.buttons[i].setVisibility(View.INVISIBLE);
        }
    }

    private void show(int index) {
        buttons[index].clearAnimation();
        MainActivity.this.hideAll();
        if (index < 0 || index > (this.buttons.length - 1)) return;
        this.buttons[index].setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (bgWork.isAlive()) {
            bgWork.interrupt();
            bgWork = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.hideAll();
        this.bgWork.start();
    }
}
