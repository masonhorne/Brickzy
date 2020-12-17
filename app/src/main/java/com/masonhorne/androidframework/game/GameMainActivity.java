package com.masonhorne.androidframework.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.masonhorne.androidframework.BuildConfig;
import com.masonhorne.androidframework.R;

public class GameMainActivity extends Activity {
    private static GameMainActivity instance;
    public static final int GAME_WIDTH = 450;
    public static final int GAME_HEIGHT = 800;
    public static GameView game;
    public static AssetManager assets;
    public static Button btn;
    public static Button btn2, btn3, btn4;
    public static String version;
    public static GoogleSignInAccount account;
    public static Context context;
    private static SharedPreferences prefs;
    private static final String highScoreKey = "highScore";
    private static int highScore;
    private static final int RC_LEADERBOARD_UI = 9004;
    public static boolean signedIn;
    private static final int RC_SIGN_IN = 9001;
    private static GoogleSignInClient client;
    public static boolean muted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        prefs = getPreferences(Activity.MODE_PRIVATE);
        highScore = retrieveHighScore();
        btn = new Button(this);
        btn.setOnClickListener(v -> {
            if(GoogleSignIn.getLastSignedInAccount(GameMainActivity.context) != null) {
                showLeaderboard();
            }
        });
        btn2 = new Button(this);
        btn2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(GoogleSignIn.getLastSignedInAccount(GameMainActivity.context) != null) {
                    Games.getLeaderboardsClient((Activity) GameMainActivity.context, GoogleSignIn.getLastSignedInAccount(GameMainActivity.context))
                            .submitScore(getString(R.string.leaderboard_top_scores), retrieveHighScore());
                }
            }
        });
        btn3 = new Button(this);
        btn3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        btn4 = new Button(this);
        btn4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        signInSilently();
        signedIn = account != null;
        this.version = BuildConfig.VERSION_NAME;
        assets = getAssets();
        game = new GameView(this, GAME_WIDTH, GAME_HEIGHT);
        setContentView(game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    private void signInSilently() {
        GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);
        this.client = client;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            this.account = account;
            Games.getGamesClient(GameMainActivity.context, account).setViewForPopups(game);
        } else {
            // Haven't been signed-in before. Try the silent sign-in first.
            client
                    .silentSignIn()
                    .addOnCompleteListener(
                            this,
                            task -> {
                                if (task.isSuccessful()) {
                                    // The signed in account is stored in the task's result.
                                    GoogleSignInAccount account1 = task.getResult();
                                    GameMainActivity.account = account1;
                                    Games.getGamesClient(GameMainActivity.context, account1).setViewForPopups(GameMainActivity.game);
                                } else {
                                    startInteractiveSignIn(client);
                                }
                            });
        }
    }
    private void startInteractiveSignIn(GoogleSignInClient client){
        startActivityForResult(client.getSignInIntent(), 69);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 69) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                this.account = result.getSignInAccount();
                Games.getGamesClient(this, account).setViewForPopups(game);
            } else {
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error);
                }
                new AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        }
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            this.account = completedTask.getResult(ApiException.class);
            Games.getGamesClient(this, account).setViewForPopups(game);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Sign In", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    public static void setHighScore(int highScore){
        GameMainActivity.highScore = highScore;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(highScoreKey, highScore);
        editor.commit();
    }
    private int retrieveHighScore(){
        return prefs.getInt(highScoreKey, 0);
    }
    public static int getHighScore(){
        return highScore;
    }
    public static GameMainActivity getInstance(){
        if (instance == null){
            instance = new GameMainActivity();
        }
        return instance;
    }
    private void showLeaderboard() {
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getLeaderboardIntent(getString(R.string.leaderboard_top_scores))
                .addOnSuccessListener(intent -> startActivityForResult(intent, RC_LEADERBOARD_UI));
    }
    private void signIn(){
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signOut(){
        client.signOut();
        signedIn = false;
        account = null;
    }
}
