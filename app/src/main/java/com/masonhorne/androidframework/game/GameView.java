package com.masonhorne.androidframework.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.Games;
import com.masonhorne.androidframework.state.LoadState;
import com.masonhorne.androidframework.state.State;
import com.masonhorne.androidframework.util.InputHandler;
import com.masonhorne.androidframework.util.Painter;

public class GameView extends SurfaceView implements Runnable {
    private Bitmap gameImage;
    private Rect gameImageSrc;
    private Rect gameImageDst;
    private Canvas gameCanvas;
    private Painter graphics;
    private Thread gameThread;
    private volatile boolean running = false;
    private volatile State currentState;
    private InputHandler handler;
    public GameView(Context context, int gameWidth, int gameHeight) {
        super(context);
        gameImage = Bitmap.createBitmap(gameWidth, gameHeight, Bitmap.Config.RGB_565);
        gameImageSrc = new Rect(0, 0, gameImage.getWidth(), gameImage.getHeight());
        gameImageDst = new Rect();
        gameCanvas = new Canvas(gameImage);
        graphics = new Painter(gameCanvas);
        SurfaceHolder holder = getHolder();
        holder.addCallback(new Callback() {

            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) { // THIS METHOD IS SIMILAR TO ADDNOTIFY IN JAVA
                initInput();
                if(currentState == null) {
                    setCurrentState(new LoadState());
                }
                initGame();
                signInSilently();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                // TODO
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                pauseGame();
            }
        });
    }
    public GameView(Context context) { // THIS CONSTRUCTOR IS STRICTLY FOR TOOLS TO FUNCTION
        super(context);
    }
    private void updateAndRender(long delta) {
        currentState.update(delta / 1000f);
        currentState.render(graphics);
        renderGameImage();
    }
    private void renderGameImage() {
        Canvas screen = getHolder().lockCanvas();
        if(screen != null) {
            screen.getClipBounds(gameImageDst);
            screen.drawBitmap(gameImage, gameImageSrc, gameImageDst, null);
            getHolder().unlockCanvasAndPost(screen);
        }
    }
    @Override
    public void run() {
        long updateDurationMillis = 0;
        long sleepDurationMillis = 0;
        while(running) {
            long beforeUpdateRender = System.nanoTime();
            long deltaMillis = sleepDurationMillis + updateDurationMillis;
            updateAndRender(deltaMillis);
            updateDurationMillis = (System.nanoTime() - beforeUpdateRender) / 1000000L;
            sleepDurationMillis = Math.max(2, 17 - updateDurationMillis);
            try {
                Thread.sleep(sleepDurationMillis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void setCurrentState(State newState) {
        System.gc();
        newState.init();
        currentState = newState;
        handler.setCurrentState(currentState);
    }
    private void initInput(){
        if(handler == null) {
            handler = new InputHandler();
        }
        setOnTouchListener(handler);
    }
    private void initGame() {
        running = true;
        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }
    private void pauseGame() {
        running = false;
        while(gameThread.isAlive()) {
            try{
                gameThread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void signInSilently() {
        GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(GameMainActivity.context);
        if (GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            GameMainActivity.account = account;
            Games.getGamesClient(GameMainActivity.context, account).setViewForPopups(GameMainActivity.game);
        }
    }
}
