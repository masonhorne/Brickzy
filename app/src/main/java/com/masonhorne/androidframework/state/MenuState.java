package com.masonhorne.androidframework.state;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;

import com.masonhorne.androidframework.game.Assets;
import com.masonhorne.androidframework.game.GameMainActivity;
import com.masonhorne.androidframework.util.MuteButton;
import com.masonhorne.androidframework.util.Painter;
import com.masonhorne.androidframework.util.SignInButton;
import com.masonhorne.androidframework.util.UIButton;

public class MenuState extends State {
    private UIButton playButton, leaderboardButton;
    private MuteButton muteButton;
    private SignInButton btn;
    private String version = "Version: ";
    private String credit = "By Mason Horne";

    @Override
    public void init() {
        playButton = new UIButton(107, 550, 207, 650, Assets.play, Assets.playDown);
        leaderboardButton = new UIButton(257, 550, 357, 650, Assets.leaderboard, Assets.leaderboardDown);
        muteButton = new MuteButton(400, 15, 435, 45, Assets.unpaused, Assets.paused);
        btn = new SignInButton(10, 706, 135, 790, Assets.signIn, Assets.signInPressed, Assets.signOut, Assets.signOutPressed);
    }

    @Override
    public void update(float delta) {
        btn.update();
    }

    @Override
    public void render(Painter g) {
        g.drawImage(Assets.welcome, 0, 0);
        playButton.render(g);
        btn.render(g);
        leaderboardButton.render(g);
        muteButton.render(g);
        g.setFont(Typeface.SANS_SERIF, 20);
        g.setColor(Color.BLACK);
        g.drawString(version + GameMainActivity.version, 10, 18);
        g.setFont(Typeface.DEFAULT_BOLD, 15);
        g.drawString(credit, 325, 780);
    }
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            playButton.onTouchDown(scaledX, scaledY);
            leaderboardButton.onTouchDown(scaledX, scaledY);
            muteButton.onTouchDown(scaledX, scaledY);
            btn.onTouchDown(scaledX, scaledY);
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            if (playButton.isPressed(scaledX, scaledY)) {
                playButton.cancel();
                setCurrentState(new PlayState());
            } else if (leaderboardButton.isPressed(scaledX, scaledY)) {
                GameMainActivity.btn.callOnClick();
                leaderboardButton.cancel();
            } else if(!btn.isSignedIn() && btn.isPressed(scaledX, scaledY)) {
                GameMainActivity.btn3.callOnClick();
                btn.cancel();
            } else if(btn.isSignedIn() && btn.isPressed(scaledX, scaledY)) {
                GameMainActivity.btn4.callOnClick();
                btn.cancel();
            } else if(muteButton.isPressed(scaledX,scaledY)){
                GameMainActivity.muted = !GameMainActivity.muted;
                muteButton.cancel();
            } else {
                muteButton.cancel();
                btn.cancel();
                playButton.cancel();
                leaderboardButton.cancel();
            }
        }
        return true;
    }

}
