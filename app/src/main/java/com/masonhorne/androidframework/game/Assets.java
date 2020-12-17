package com.masonhorne.androidframework.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;
import java.io.InputStream;

public class Assets {
    public static Bitmap home;
    private static SoundPool soundPool;
    public static Bitmap welcome;
    public static Bitmap defaultBrick, doubleBrick;
    public static Bitmap gameover;
    public static Bitmap replay;
    public static Bitmap share;
    public static Bitmap homeDown;
    public static Bitmap replayDown;
    public static Bitmap shareDown;
    public static Bitmap play;
    public static Bitmap playDown;
    public static int brickBounce;
    public static int paddleBounce;
    public static Bitmap line;
    public static Bitmap leaderboard;
    public static Bitmap leaderboardDown;
    public static Bitmap signIn, signInPressed, signOut, signOutPressed;
    public static Bitmap paused, unpaused;
    public static void load() {
        welcome = loadBitmap("welcome.png", false);
        defaultBrick = loadBitmap("default_brick.png", false);
        doubleBrick = loadBitmap("double_brick.png", false);
        gameover = loadBitmap("gameover.png", false);
        home = loadBitmap("home.png", false);
        replay = loadBitmap("replay.png", false);
        share = loadBitmap("share.png", false);
        homeDown = loadBitmap("home_down.png", false);
        replayDown = loadBitmap("replay_down.png", false);
        shareDown = loadBitmap("share_down.png", false);
        play = loadBitmap("play.png", false);
        playDown = loadBitmap("play_down.png", false);
        line = loadBitmap("line.png", true);
        brickBounce = loadSound("brick.wav");
        paddleBounce = loadSound("paddle.wav");
        leaderboard = loadBitmap("leaderboard.png", false);
        leaderboardDown = loadBitmap("leaderboard_down.png", false);
        signIn = loadBitmap("signIn.png", false);
        signInPressed = loadBitmap("signInPressed.png", false);
        signOut = loadBitmap("signOut.png", false);
        signOutPressed = loadBitmap("signOutPressed.png", false);
        paused = loadBitmap("paused.png", true);
        unpaused = loadBitmap("unpaused.png", true);
    }
    private static Bitmap loadBitmap(String filename, boolean transparency) {
        InputStream in = null;
        try {
            in = GameMainActivity.assets.open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        if(transparency) {
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        } else {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        return BitmapFactory.decodeStream(in, null, new BitmapFactory.Options());
    }
    private static int loadSound(String filename) {
        int soundID = 0;
        if(soundPool == null) {
            soundPool = new SoundPool(25, AudioManager.STREAM_MUSIC, 0);
        }
        try {
            soundID = soundPool.load(GameMainActivity.assets.openFd(filename), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return soundID;
    }
    public static void playSound(int soundID) {
        soundPool.play(soundID, 1, 1, 1, 0, 1);
    }
}
