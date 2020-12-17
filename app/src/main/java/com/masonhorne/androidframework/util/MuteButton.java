package com.masonhorne.androidframework.util;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.masonhorne.androidframework.game.GameMainActivity;

public class MuteButton {
    private Rect buttonRect;
    private boolean buttonDown = false;
    private Bitmap buttonImage, buttonDownImage;
    public MuteButton(int left, int top, int right, int bottom, Bitmap buttonImage, Bitmap buttonPressedImage) {
        buttonRect = new Rect(left, top, right, bottom);
        this.buttonImage = buttonImage;
        this.buttonDownImage = buttonPressedImage;
    }
    public void render (Painter g) {
        Bitmap currentButtonImage = GameMainActivity.muted ? buttonDownImage : buttonImage;
        g.drawImage(currentButtonImage, buttonRect.left, buttonRect.top, buttonRect.width(), buttonRect.height());
    }
    public void onTouchDown(int touchX, int touchY) {
        buttonDown = buttonRect.contains(touchX, touchY);
        if(buttonRect.contains(touchX, touchY)){
            buttonDown = true;
        } else {
            buttonDown = false;
        }
    }
    public void cancel() {
        buttonDown = false;
    }
    public boolean isPressed(int touchX, int touchY) {
        return buttonDown && buttonRect.contains(touchX, touchY);
    }
}
