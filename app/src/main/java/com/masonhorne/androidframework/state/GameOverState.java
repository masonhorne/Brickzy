package com.masonhorne.androidframework.state;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;

import com.google.android.gms.games.LeaderboardsClient;
import com.masonhorne.androidframework.game.Assets;
import com.masonhorne.androidframework.game.GameMainActivity;
import com.masonhorne.androidframework.util.Painter;
import com.masonhorne.androidframework.util.UIButton;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_TEXT;
import static androidx.core.content.ContextCompat.startActivity;


public class GameOverState extends State {
    private String score;
    private UIButton replayButton, homeButton, shareButton;
    private LeaderboardsClient client;
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    public GameOverState(String score) {
        int val = Integer.parseInt(score);
        if(val > GameMainActivity.getHighScore()){
            GameMainActivity.setHighScore(val);
            GameMainActivity.btn2.callOnClick();
        }
        this.score = score;
    }
    @Override
    public void init() {
        homeButton = new UIButton(92, 615, 167, 690, Assets.home, Assets.homeDown);
        replayButton = new UIButton(194, 615, 269, 690, Assets.replay, Assets.replayDown);
        shareButton = new UIButton(300, 615, 375, 690, Assets.share, Assets.shareDown);
    }

    @Override
    public void update(float delta) {
        // DO NOTHING
    }

    @Override
    public void render(Painter g) {
        g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
        g.drawImage(Assets.gameover, 0, 0);
        g.setColor(Color.WHITE);
        if(score.length() > 1){
            g.drawString(score, 215, 450);
        } else {
            g.drawString(score, 220, 450);
        }
        homeButton.render(g);
        replayButton.render(g);
        shareButton.render(g);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            homeButton.onTouchDown(scaledX, scaledY);
            replayButton.onTouchDown(scaledX, scaledY);
            shareButton.onTouchDown(scaledX, scaledY);
        }
        if(e.getAction() == MotionEvent.ACTION_UP) {
            if(homeButton.isPressed(scaledX, scaledY)) {
                homeButton.cancel();
                setCurrentState(new MenuState());
            } else if (replayButton.isPressed(scaledX, scaledY)) {
                replayButton.cancel();
                setCurrentState(new PlayState());
            }  else if (shareButton.isPressed(scaledX, scaledY)) {
                startShare(score);
                shareButton.cancel();
            } else {
                shareButton.cancel();
                replayButton.cancel();
                homeButton.cancel();
            }
        }
        return true;
    }
    public void startShare(String score) {
        Intent shareIntent = new Intent(ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "I just broke " + score + " bricks! Download Brickzy and try to beat my score!\nhttps://play.google.com/store/apps/details?id=com.masonhorne.androidframework";
        shareIntent.putExtra(EXTRA_TEXT, shareBody);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(GameMainActivity.context, Intent.createChooser(shareIntent, "Share using"), null);
    }
}
