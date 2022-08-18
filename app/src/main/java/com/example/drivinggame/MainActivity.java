package com.example.drivinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.RenderNode;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView car;
    private ImageView orange;
    private ImageView pink;
    private ImageView black;

    private int frameWidth;
    private int carSize;

    private float carX;

    private Handler handler = new Handler();
    private Timer timer = new Timer();


    //trueの時は右へ、falseの時は左へ
    private boolean action_flg = true;

    private boolean start_flg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        car = findViewById(R.id.car);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        black = findViewById(R.id.black);

        orange.setX(-80.0f);
        orange.setY(-80.0f);
        pink.setX(-80.0f);
        pink.setY(-80.0f);
        black.setX(-80.0f);
        black.setY(-80.0f);

    }


    public void changePos() {
        if (action_flg) {
            carX += 20;
        } else {
            carX -= 20;
        }

        if (carX < 0) {
            carX = 0;
        } else if (carX > frameWidth - carSize) {
            carX = frameWidth - carSize;
        }

        car.setX(carX);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg == false) {
            start_flg = true;

            FrameLayout frame = findViewById(R.id.frame);
            frameWidth = frame.getWidth();

            carX = car.getX();
            carSize = car.getWidth();

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (action_flg) {
                    action_flg = false;
                } else {
                    action_flg = true;
                }
            }
        }
        return true;
    }
}