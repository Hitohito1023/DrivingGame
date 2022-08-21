package com.example.drivinggame;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int hitSound;
    private static int slipSound;
    private static int crushSound;
    private static int handleSound;
    private static int engineSound;
    private static int bgmSound;

    public SoundPlayer(Context context) {

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        hitSound = soundPool.load(context, R.raw.hit, 1);
        slipSound = soundPool.load(context, R.raw.slip, 1);
        crushSound = soundPool.load(context, R.raw.crush, 1);
        handleSound = soundPool.load(context, R.raw.handle, 1);
        engineSound = soundPool.load(context, R.raw.engine, 1);
    }

    public void playHitSound() {
        soundPool.play(hitSound, 1.0f, 1.0f, 3, 0, 1.0f);
    }

    public void playSlipSound() {
        soundPool.play(slipSound, 1.0f, 1.0f, 3, 0, 1.0f);
    }

    public void playCrushSound() {
        soundPool.play(crushSound, 1.0f, 1.0f, 3, 0, 1.0f);
    }

    public void playHandleSound() {
        soundPool.play(handleSound, 1.0f, 1.0f, 3, 0, 1.0f);
    }

    public void playEngineSound() {
        soundPool.play(engineSound, 1.0f, 1.0f, 3, 0, 1.0f);
    }

}
