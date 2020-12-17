package com.masonhorne.androidframework.model;

import android.graphics.Rect;

import com.google.android.gms.games.Game;
import com.masonhorne.androidframework.game.Assets;
import com.masonhorne.androidframework.game.GameMainActivity;

public class Ball {
    private float x, y;
    private int diameter, velX, velY;
    private Rect rect;
    private static final int MOVEMENT_SPEED = 200;
    public Ball(float x, float y, int diameter){
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.velX = MOVEMENT_SPEED;
        this.velY = -MOVEMENT_SPEED;
        rect = new Rect((int) x, (int) y, (int) x + diameter, (int) y + diameter);
    }
    public void update(float delta) {
        x += velX * delta;
        y += velY * delta;
        correctYCollisions();
        correctXCollisions();
        updateRect();
    }
    private void correctYCollisions() {
        if (y < 0) {
            y = 0;
        } else if (y >= GameMainActivity.GAME_HEIGHT) {
            y = GameMainActivity.GAME_HEIGHT + 1;
            velY = 0;
            return;
        } else {
            return;
        }
        velY = -velY;
    }
    public boolean isDead(){
        return y == GameMainActivity.GAME_HEIGHT + 1;
    }
    private void correctXCollisions() {
        if(x < 0) {
            x = 0;
        } else if(x + diameter > GameMainActivity.GAME_WIDTH) {
            x = GameMainActivity.GAME_WIDTH - diameter;
        } else {
            return;
        }
        velX = -velX;
    }
    private void updateRect() {
        rect.set((int) x, (int) y, (int) x + diameter, (int) y + diameter);
    }
    public void increaseSpeed(){
        this.velY = velY > 0 ? velY + 5 : velY - 5;
    }
    public void onCollideWith(Paddle p) {
        if(!GameMainActivity.muted) {
            Assets.playSound(Assets.paddleBounce);
        }
        y = p.getY() - diameter;
        int centerOfBall = (int) x + (diameter / 2);
        if(centerOfBall < p.getX() + 10) {
            velX = -200;
        } else if(centerOfBall < p.getX() + 20){
            velX = -150;
        } else if(centerOfBall < p.getX() + 30) {
            velX = -100;
        } else if(centerOfBall < p.getX() + 40){
            velX = 100;
        } else if(centerOfBall < p.getX() + 50){
            velX = 150;
        } else {
            velX = 200;
        }
        velY = -velY;
    }
    private enum Quadrant {
        Q1,
        Q2,
        Q3,
        Q4
    }
    public void onCollideWith(Brick b){
        if(!GameMainActivity.muted){
            Assets.playSound(Assets.brickBounce);
        }
        int brickCenterX = (int) (b.getX() + (b.getWidth() / 2));
        int brickCenterY = (int) (b.getY() + (b.getHeight() / 2));
        int ballCenterX = (int) (x + (diameter / 2));
        int ballCenterY = (int) (y + (diameter / 2));
        Quadrant collision;
        // Determines quadrant of the brick collision is made in
        if(ballCenterX >= brickCenterX) {
            if(ballCenterY >= brickCenterY){
                collision = Quadrant.Q4;
            } else {
                collision = Quadrant.Q1;
            }
        } else {
            if(ballCenterY >= brickCenterY) {
                collision = Quadrant.Q3;
            } else {
                collision = Quadrant.Q2;
            }
        }
        // Handles collision correctly for corresponding quadrant
        if(collision == Quadrant.Q1){
            if(ballCenterX - (b.getX() + b.getWidth()) > b.getY() - ballCenterY){
                collideRight(b);
            } else {
                collideTop(b);
            }
        } else if(collision == Quadrant.Q2){
            if(b.getX() - ballCenterX > b.getY() - ballCenterY) {
                collideLeft(b);
            } else {
                collideTop(b);
            }
        } else if(collision == Quadrant.Q3) {
            if(b.getX() - ballCenterX  > ballCenterY - (b.getY() + b.getHeight())) {
                collideLeft(b);
            } else {
                collideBottom(b);
            }
        } else if (collision == Quadrant.Q4) {
            if(ballCenterX - (b.getX() + b.getWidth()) > ballCenterY - (b.getY() + b.getHeight())) {
                collideRight(b);
            } else {
                collideBottom(b);
            }
        }

//        // Left is max
//        if(leftOverlap > rightOverlap && leftOverlap > bottomOverlap && leftOverlap > topOverlap){
//            collideLeft(b);
//        } else if (rightOverlap > leftOverlap && rightOverlap > topOverlap && rightOverlap > bottomOverlap) {
//            collideRight(b);
//        } else if(topOverlap > bottomOverlap && topOverlap > rightOverlap && topOverlap > leftOverlap) {
//            collideTop(b);
//        } else {
//            collideBottom(b);
//        }
    }
    private void collideTop(Brick b){
        y = b.getY() - diameter;
        velY = -velY;
    }
    private void collideLeft(Brick b){
        x = b.getX() - diameter;
        velX = -velX;
    }
    private void collideRight(Brick b){
        x = b.getX() + b.getWidth();
        velX = -velX;
    }
    private void collideBottom(Brick b){
        y = b.getY() + b.getHeight();
        velY = -velY;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public Rect getRect() {
        return rect;
    }
}
