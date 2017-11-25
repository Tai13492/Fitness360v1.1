package com.company.fitness360;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import at.markushi.ui.CircleButton;

/**
 * Created by Tai on 11/13/2017.
 */

public class TDEEResult extends AppCompatActivity {
    double bmi,bmr,tdee;
    int userkcal;
    TextView userbmi,usercalrec,bmirec,tdeeinfo;
    Healthy user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdee_result);
        Intent intentextras = getIntent();
        Bundle extrabundles = intentextras.getExtras();
        bmi = extrabundles.getDouble("bmi");
        bmr = extrabundles.getDouble("bmr");
        tdee = extrabundles.getDouble("tdee");
        userkcal = (int) tdee;
        user = new Healthy(bmi,userkcal);
        userbmi = (TextView) findViewById(R.id.userbmi);
        usercalrec = (TextView) findViewById(R.id.usercalrec);
        bmirec = (TextView) findViewById(R.id.bmirec);
        tdeeinfo = (TextView) findViewById(R.id.tdeeinfo);
    }
    protected void onStart(){
        super.onStart();
        userbmi.setText("" + bmi);
        usercalrec.setText(user.calorieRecommendation());
        bmirec.setText(user.healthyBMI());
        tdeeinfo.setText(user.calorieRecommendationString());
    }
    public void onBackPressed(View view) {
        Intent goBack = new Intent(TDEEResult.this, StartPage.class);
        startActivity(goBack);
    }
}