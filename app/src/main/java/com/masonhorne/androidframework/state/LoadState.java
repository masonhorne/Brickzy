package com.masonhorne.androidframework.state;

import android.view.MotionEvent;

import com.masonhorne.androidframework.game.Assets;
import com.masonhorne.androidframework.util.Painter;

public class LoadState extends State {
    @Override
    public void init() {
        Assets.load();
    }

    @Override
    public void update(float delta) {
        setCurrentState(new MenuState());
    }

    @Override
    public void render(Painter g) {
        // DO NOTHING LOADING FILES
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        return false;
    }
}
