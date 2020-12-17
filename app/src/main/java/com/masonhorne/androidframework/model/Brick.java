package com.masonhorne.androidframework.model;

import android.graphics.Rect;

import com.masonhorne.androidframework.util.RandomNumberGenerator;

public class Brick {
    private boolean visible;
    private float x, y;
    private int width, height;
    private Rect rect;
    private static boolean moving;
    private static int movedAmount;
    private int hitPoints;
    public Brick(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rect = new Rect((int) x, (int) y, (int) x + width, (int) y + height);
        visible = true;
        moving = false;
        movedAmount = 0;
        hitPoints = 0;
    }
    public void destroy(){
        if(hitPoints == 0){
            visible = false;
        } else {
            hitPoints--;
        }
    }
    public boolean isDestroyed(){
        return !visible;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Rect getRect() {
        return rect;
    }
    public static void startMove() {
        moving = true;
    }
    private void updateRect() {
        rect.set((int) x, (int) y, (int) x + width, (int) y + height);
    }
    public static float getMovedAmount(){
        return movedAmount;
    }
    public static void stopMove(){
        moving = false;
        movedAmount = 0;
    }
    public void update(int playerScore){
        if(moving){
            y += 5;
            movedAmount += 1;
            updateRect();
        }
        if(y == 750 && !visible) {
            y -= 800;
            if(playerScore > 80){
                if(0 == RandomNumberGenerator.getRandInt(3)){
                    hitPoints = 1;
                }
            } else if (playerScore > 30){
                if(0 == RandomNumberGenerator.getRandInt(6)){
                    hitPoints = 1;
                }
            }
            visible = true;
        }
    }
    public boolean isDead(){
        return y > 700 && visible;
    }
    public int getHitPoints(){
        return hitPoints;
    }
}