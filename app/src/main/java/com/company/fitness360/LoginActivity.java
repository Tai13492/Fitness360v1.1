package com.company.fitness360;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Volkan on 2017-11-16.
 */

public class LoginActivity extends AppCompatActivity {

    TwitterLoginButton twitter_login_button;
    LoginButton facebook_login_button;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_login);

        twitter_login_button = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitter_login_button.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                String username = session.getUserName();
                Intent intent = new Intent(LoginActivity.this, StartPage.class);
                intent.putExtra("username:", username);
                startActivity(intent);
            }
            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
            }
        });

        facebook_login_button = (LoginButton)findViewById(R.id.facebook_login_button);
        facebook_login_button.setReadPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();
        facebook_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String userid = loginResult.getAccessToken().getUserId();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                String firstName = null, lastName = null;
                                try {
                                    firstName = object.getString("first_name");
                                    lastName = object.getString("last_name");
                                    Intent intent = new Intent(LoginActivity.this, StartPage.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("first_name:", firstName);
                                    bundle.putString("last_name", lastName);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }
            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login cancelled by the user", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Error occurred while authenticate", Toast.LENGTH_LONG).show();
            }
        });

        Button button = (Button)findViewById(R.id.GuestButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent fireNewActivity = new Intent(LoginActivity.this, StartPage.class);
                startActivity(fireNewActivity);
            }
        });

        VideoView videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.lionate);
        videoview.setVideoURI(uri);
        videoview.start();

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button.
        if(requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
            twitter_login_button.onActivityResult(requestCode, resultCode, data);
        else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onStop() {
        LoginManager.getInstance().logOut();
        super.onStop();
    }
}