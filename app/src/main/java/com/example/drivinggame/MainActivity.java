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
    private TextView gasLimitLabel;
    private TextView startLabel;
    private ImageView car;
    private ImageView gas;
    private ImageView puddle;
    private ImageView enemy;

    private int frameWidth;
    private int carSize;
    private int carHeight;
    private int screenWidth;
    private int screenHeight;


    private float carX;
    private float carY;
    private float gasX;
    private float gasY;
    private float puddleX;
    private float puddleY;
    private float enemyX;
    private float enemyY;

    private int score = 0;
    private int gasLimit = 1000;

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
        gasLimitLabel = findViewById(R.id.gasLimitLabel);
        startLabel = findViewById(R.id.startLabel);
        car = findViewById(R.id.car);
        gas = findViewById(R.id.gas);
        puddle = findViewById(R.id.puddle);
        enemy = findViewById(R.id.enemy);

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        gas.setX(80.0f);
        gas.setY(80.0f);
        puddle.setX(-80.0f);
        puddle.setY(-80.0f);
        enemy.setX(-80.0f);
        enemy.setY(-80.0f);

        scoreLabel.setText("Score : 0");
        gasLimitLabel.setText("ガソリン残量 : 1000");

    }


    public void changePos() {

        hitCheck();

        gasLimitCheck();

        // gas
        // gasの落ちる速さ
        gasY += 10;
        if (gasY > screenHeight) {
            //gasが再登場する速度
            gasY = -100;
            gasX = (float)Math.floor(Math.random() * (frameWidth - gas.getWidth()));
        }
        gas.setX(gasX);
        gas.setY(gasY);

        // puddle
        puddleY += 20;
        if (puddleY > screenHeight) {
            puddleY = -10;
            puddleX = (float)Math.floor(Math.random() * (frameWidth - puddle.getWidth()));
        }
        puddle.setX(puddleX);
        puddle.setY(puddleY);

        // enemy
        enemyY += 40;
        if (enemyY > screenHeight) {
            enemyY = -50;
            enemyX = (float)Math.floor(Math.random() * (frameWidth - enemy.getWidth()));
        }
        enemy.setX(enemyX);
        enemy.setY(enemyY);

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

        score += 1;
        gasLimit -= 2;

        scoreLabel.setText("Score : " + score);
        gasLimitLabel.setText("ガソリン残量 : " + gasLimit);

    }

    public void hitCheck() {

        //gas
        float gasCenterX = gasX + gas.getWidth() / 2;
        float gasCenterY = gasY + gas.getHeight() / 2;

        if (hitStatus(gasCenterX, gasCenterY)) {
            gasY = screenHeight + 100;
            gasLimit += 1000;
            soundPlayer.playHitSound();
        }

        //puddle
        float puddleCenterX = puddleX + puddle.getWidth() / 2;
        float puddleCenterY = puddleY + puddle.getHeight() / 2;

        if (hitStatus(puddleCenterX, puddleCenterY)) {
            puddleY = screenHeight + 100;
            gasLimit -= 200;
            soundPlayer.playHitSound();
        }

        //enemy
        float enemyCenterX = enemyX + enemy.getWidth() / 2;
        float enemyCenterY = enemyY + enemy.getHeight() / 2;

        if (hitStatus(enemyCenterX, enemyCenterY)) {
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

    public void gasLimitCheck() {
        if (gasLimit < 2) {
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