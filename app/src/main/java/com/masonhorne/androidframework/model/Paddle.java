package com.masonhorne.androidframework.model;

import android.graphics.Rect;

import com.masonhorne.androidframework.game.GameMainActivity;

public class Paddle {
    private float x, y;
    private int width, height;
    private Rect rect;
    public Paddle(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rect = new Rect((int) x, (int) y, (int) x + width, (int) y + height);
    }
    public void update(){
        updateRect();
    }
    private void updateRect(){
        rect.set((int) x, (int) y, (int) x + width, (int) y + height);
    }
    public void updatePosition(int x){
        if(x < 0) {
            x = 0;
        } else if (x + width > GameMainActivity.GAME_WIDTH) {
            x = GameMainActivity.GAME_WIDTH - width;
        }
        this.x = x;
    }
    public float getX() {
        return x;
    }
    public float getY(){
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Rect getRect() {
        return rect;
    }
}
