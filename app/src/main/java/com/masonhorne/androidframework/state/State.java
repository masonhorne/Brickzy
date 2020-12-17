package com.masonhorne.androidframework.state;

import android.view.MotionEvent;

import com.masonhorne.androidframework.game.GameMainActivity;
import com.masonhorne.androidframework.util.Painter;

public abstract class State {
    public void setCurrentState(State newState) {
        GameMainActivity.game.setCurrentState(newState);
    }
    public abstract void init();
    public abstract void update(float delta);
    public abstract void render(Painter g);
    public abstract boolean onTouch(MotionEvent e, int scaledX, int scaledY);
}
