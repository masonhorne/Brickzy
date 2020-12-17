package com.masonhorne.androidframework.util;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.masonhorne.androidframework.game.GameMainActivity;

public class SignInButton {
    private Rect rect;
    private Bitmap signIn, signInPressed, signOut, signOutPressed;
    private boolean signedIn;
    private boolean buttonDown;
    public SignInButton(int left, int top, int right, int bottom, Bitmap signIn, Bitmap signInPressed, Bitmap signOut, Bitmap signOutPressed){
        rect = new Rect(left, top, right, bottom);
        this.signIn = signInPressed;
        this.signInPressed = signIn;
        this.signOut = signOut;
        this.signOutPressed = signOutPressed;
        signedIn = GameMainActivity.account != null;
    }
    public void update(){
        signedIn = GameMainActivity.account != null;
    }
    public void render (Painter g) {
        Bitmap currentButtonImage;
        if (!signedIn) {
            currentButtonImage = buttonDown ? signIn : signInPressed;
        } else {
            currentButtonImage = buttonDown ? signOut : signOutPressed;
        }
        g.drawImage(currentButtonImage, rect.left, rect.top, rect.width(), rect.height());
    }
    public void onTouchDown(int touchX, int touchY) {
        buttonDown = rect.contains(touchX, touchY);
        if(rect.contains(touchX, touchY)){
            buttonDown = true;
        } else {
            buttonDown = false;
        }
    }
    public void cancel() {
        buttonDown = false;
    }
    public boolean isPressed(int touchX, int touchY) {
        return buttonDown && rect.contains(touchX, touchY);
    }
    public boolean isSignedIn(){
        return signedIn;
    }
}
