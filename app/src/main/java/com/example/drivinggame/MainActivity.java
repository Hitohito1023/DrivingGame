package com.example.drivinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.RenderNode;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
    private int carHeight;
    private int screenWidth;
    private int screenHeight;


    private float carX;
    private float carY;
    private float orangeX;
    private float orangeY;
    private float pinkX;
    private float pinkY;
    private float blackX;
    private float blackY;

    private int score = 0;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //trueの時は右へ、falseの時は左へ
    private boolean action_flg = true;

    private boolean start_flg = false;

    private SoundPlayer soundPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPlayer = new SoundPlayer(this);

        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        car = findViewById(R.id.car);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        black = findViewById(R.id.black);

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        orange.setX(-80.0f);
        orange.setY(-80.0f);
        pink.setX(-80.0f);
        pink.setY(-80.0f);
        black.setX(-80.0f);
        black.setY(-80.0f);

        scoreLabel.setText("Score : 0");

    }


    public void changePos() {

        hitCheck();

        // orange
        // orangeの落ちる速さ
        orangeY += 10;
        if (orangeY > screenHeight) {
            //orangeが再登場する速度
            orangeY = -10;
            orangeX = (float)Math.floor(Math.random() * (frameWidth - orange.getWidth()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        // pink
        pinkY += 50;
        if (pinkY > screenHeight) {
            pinkY = -20;
            pinkX = (float)Math.floor(Math.random() * (frameWidth - pink.getWidth()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        // black
        blackY += 30;
        if (blackY > screenHeight) {
            blackY = -5;
            blackX = (float)Math.floor(Math.random() * (frameWidth - black.getWidth()));
        }
        black.setX(blackX);
        black.setY(blackY);

        if (action_flg) {
            carX += 20;
        } else {
            carX -= 20;
        }

        if (carX <= 0) {
            carX = 0;
        } else if (carX > frameWidth - carSize) {
            carX = frameWidth - carSize;
        }

        car.setX(carX);

        scoreLabel.setText("Score : " + score);
    }

    public void hitCheck() {

        //orange
        float orangeCenterX = orangeX + orange.getWidth() / 2;
        float orangeCenterY = orangeY + orange.getHeight() / 2;

        if (hitStatus(orangeCenterX, orangeCenterY)) {
            orangeY = screenHeight + 100;
            score += 10;
            soundPlayer.playHitSound();
        }

        //pink
        float pinkCenterX = pinkX + pink.getWidth() / 2;
        float pinkCenterY = pinkY + pink.getHeight() / 2;

        if (hitStatus(pinkCenterX, pinkCenterY)) {
            pinkY = screenHeight + 100;
            score += 50;
            soundPlayer.playHitSound();
        }

        //black
        float blackCenterX = blackX + black.getWidth() / 2;
        float blackCenterY = blackY + black.getHeight() / 2;

        if (hitStatus(blackCenterX, blackCenterY)) {
            if (timer != null) {
                timer.cancel();
                timer = null;
                soundPlayer.playOverSound();
            }

        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);

        }

    }

    public boolean hitStatus(float centerX, float centerY) {
        return (carX <= centerX && centerX <= carX + carSize &&
                carY <= centerY && centerY <= screenHeight) ? true : false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg == false) {
            start_flg = true;

            FrameLayout frame = findViewById(R.id.frame);
            frameWidth = frame.getWidth();

            carX = car.getX();
            carY = car.getY();
            carSize = car.getWidth();
            carHeight = car.getHeight();

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

    @Override
    public void onBackPressed() {}

}