package com.masonhorne.androidframework.state;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import com.masonhorne.androidframework.game.Assets;
import com.masonhorne.androidframework.game.GameMainActivity;
import com.masonhorne.androidframework.model.Ball;
import com.masonhorne.androidframework.model.Brick;
import com.masonhorne.androidframework.model.Paddle;
import com.masonhorne.androidframework.util.Painter;

import java.util.ArrayList;

public class PlayState extends State{
    private Paddle paddle;
    private Ball ball;
    private ArrayList<Brick> bricks;
    private static final int BRICK_WIDTH = 60;
    private static final int BRICK_HEIGHT = 25;
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 20;
    private static final int BALL_DIAMETER = 15;
    private int playerScore = 0;
    private static float timeElapsed;
    @Override
    public void init() {
        paddle = new Paddle((GameMainActivity.GAME_WIDTH - PADDLE_WIDTH) / 2, GameMainActivity.GAME_HEIGHT - PADDLE_HEIGHT - 10, PADDLE_WIDTH, PADDLE_HEIGHT);
        ball = new Ball((GameMainActivity.GAME_WIDTH - BALL_DIAMETER) / 2, GameMainActivity.GAME_HEIGHT - PADDLE_HEIGHT - BALL_DIAMETER - 10, BALL_DIAMETER);
        bricks = new ArrayList<Brick>();
        for(int i = 0; i < 4; i++){
            for(int j = -3; j < 5; j++){
                Brick b = new Brick(50 + i * 100, 50 + j * 100, BRICK_WIDTH, BRICK_HEIGHT);
                bricks.add(b);
            }
        }
        timeElapsed = 0;
    }

    @Override
    public void update(float delta) {
        if(ball.isDead()){
            setCurrentState(new GameOverState("" + playerScore));
        }
        updateBricks(delta);
        if(isOver()){
            setCurrentState(new GameOverState("" + playerScore));
        }
        ball.update(delta);
        paddle.update();
        // Ball is at bottom of screen so checks for paddle collisions
        if(ball.getY() > GameMainActivity.GAME_HEIGHT - PADDLE_HEIGHT - 50) {
            if(Rect.intersects(ball.getRect(), paddle.getRect())){

                ball.onCollideWith(paddle);
            }
        // Ball is at top of screen so checks for brick collisions
        } else {
            Brick collision = checkBrickCollisions();
            if(collision != null){
                ball.onCollideWith(collision);
                collision.destroy();
                playerScore++;
                if(playerScore % 5 == 0 && playerScore <= 100){
                    ball.increaseSpeed();
                }
            }
        }
    }
    private void updateBricks(float delta){
        timeElapsed += delta;
        if(timeElapsed > 8){
            timeElapsed = 0;
            Brick.startMove();
        }
        for(int i = 0; i < bricks.size(); i++) {
            bricks.get(i).update(playerScore);
            if(bricks.get(i).isDead()){
                setCurrentState(new MenuState());
            }
        }
        // 640 = 32 Bricks * 20 Moves
        if(Brick.getMovedAmount() == 640){
            Brick.stopMove();
        }
    }
    private Brick checkBrickCollisions() {
        for(int i = 0; i < bricks.size(); i++){
            if(!bricks.get(i).isDestroyed() && Rect.intersects(ball.getRect(), bricks.get(i).getRect())){
                return bricks.get(i);
            }
        }
        return null;
    }
    @Override
    public void render(Painter g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameMainActivity.GAME_WIDTH, GameMainActivity.GAME_HEIGHT);
        g.setColor(Color.WHITE);
        g.fillRect((int) paddle.getX(), (int) paddle.getY(), paddle.getWidth(), paddle.getHeight());
        for(int i = 0; i < bricks.size(); i++){
            if(!bricks.get(i).isDestroyed() && bricks.get(i).getHitPoints() == 1) {
                g.drawImage(Assets.doubleBrick, (int) bricks.get(i).getX(), (int) bricks.get(i).getY());
            } else if (!bricks.get(i).isDestroyed()){
                g.drawImage(Assets.defaultBrick, (int) bricks.get(i).getX(), (int) bricks.get(i).getY());
            }
        }
        g.drawImage(Assets.line, 0, 695);
        g.setColor(Color.GRAY);
        g.fillOval((int) ball.getX(), (int) ball.getY(), BALL_DIAMETER, BALL_DIAMETER);
        g.setColor(Color.DKGRAY);
        g.setFont(Typeface.DEFAULT_BOLD, 50);
        g.drawString("" + playerScore, 15, 55);
        g.drawRect(0, 0, GameMainActivity.GAME_WIDTH - 1, GameMainActivity.GAME_HEIGHT - 1);
    }
    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        paddle.updatePosition(scaledX - (paddle.getWidth() / 2));
        return true;
    }
    private boolean isOver(){
        for(int i = 0; i < bricks.size(); i++){
            if(bricks.get(i).isDead()){
                return true;
            }
        }
        return false;
    }

}
