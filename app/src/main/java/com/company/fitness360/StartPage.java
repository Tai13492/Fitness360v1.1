package com.company.fitness360;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;

import at.markushi.ui.CircleButton;

import static android.R.attr.button;

/**
 * Created by Volkan on 2017-11-08.
 */

public class StartPage extends AppCompatActivity{
    CircleButton map_button;
    VideoView videoview = null;
    TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        VideoView videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.lionate);
        videoview.setVideoURI(uri);
        videoview.start();

        CircleButton mapButton = (CircleButton)findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fireNewActivity = new Intent(StartPage.this, GoogleMapsActivity.class);
                startActivity(fireNewActivity);
            }
        });

        CircleButton tdeeButton = (CircleButton)findViewById(R.id.tdeeCalcButton);
        tdeeButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fireNewActivity = new Intent(StartPage.this, TDEE_Activity.class);
                startActivity(fireNewActivity);
            }
        });
        CircleButton storageButton = (CircleButton)findViewById(R.id.storageButton);
        storageButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fireNewActivity = new Intent(StartPage.this, memo.class);
                startActivity(fireNewActivity);
            }
        });

      CircleButton weatherButton = (CircleButton)findViewById(R.id.forecastButton);
        weatherButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fireNewActivity = new Intent(StartPage.this, ForecastActivity.class);
                startActivity(fireNewActivity);
            }
        });

/*        String firstName, lastName;
        Intent intentextras = getIntent();
        Bundle extrabundles = intentextras.getExtras();
        firstName = extrabundles.getString("first_name");
        lastName = extrabundles.getString("last_name");
        TextView textView = (TextView) findViewById(R.id.textView8);
        textView.setText(firstName + " " + lastName);*/
/*        CircleButton progressButton = (CircleButton)findViewById(R.id.tdeeCalcButton);
        progressButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fireNewActivity = new Intent(StartPage.this, ProfileActivity.class);
                startActivity(fireNewActivity);
            }
        });*/

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    @Override
    protected void onPause() {
        if(videoview != null)
            videoview.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(videoview != null)
            videoview.resume();
        super.onResume();
    }
}
