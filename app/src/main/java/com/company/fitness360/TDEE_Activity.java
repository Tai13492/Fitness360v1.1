package com.company.fitness360;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import at.markushi.ui.CircleButton;

/**
 * Created by Tai on 11/13/2017.
 */

public class TDEE_Activity extends AppCompatActivity {
    EditText weight,height,age;
    String gender;
    CheckBox male,female,sed,lact,mact,vact,eact;
    TextView activitylevel;
    double act,myweight,myheight,myage,mybmi,mybmr,mytdee;
    Calculate calculate;
    boolean checkweight,checkheight,checkage,checkgender,checkactivitylevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdee_start_window);
        weight = (EditText) findViewById(R.id.weight);
        height = (EditText) findViewById(R.id.height);
        age = (EditText) findViewById(R.id.age);
        male = (CheckBox) findViewById(R.id.male);
        female = (CheckBox) findViewById(R.id.female);
        sed = (CheckBox) findViewById(R.id.sed);
        lact = (CheckBox) findViewById(R.id.lact);
        mact = (CheckBox) findViewById(R.id.mact);
        vact = (CheckBox) findViewById(R.id.vact);
        eact = (CheckBox) findViewById(R.id.eact);
        activitylevel = (TextView) findViewById(R.id.activitylevel);
    }
    public void onClicked(View view){
        checkweight = weight.getText() != null;
        checkheight = height.getText() != null;
        checkage = age.getText() != null;
        checkgender = male.isChecked() || female.isChecked();
        checkactivitylevel = sed.isChecked() || lact.isChecked() || mact.isChecked() || vact.isChecked() || eact.isChecked();
        if(checkweight&&checkheight&&checkage&&checkgender&&checkactivitylevel) {
            myweight = Double.parseDouble(weight.getText().toString());
            myheight = Double.parseDouble(height.getText().toString());
            myage = Double.parseDouble(age.getText().toString());
            calculate = new Calculate(gender, myweight, myheight, myage, act);
            mybmi = calculate.calculateBMI();
            mybmr = calculate.calculateBMR();
            mytdee = calculate.calculateTDEE();
            //Toast.makeText(this,"BMI: "+mybmi+" BMR: " + mybmr + " TDEE: " + mytdee ,Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putDouble("bmi",mybmi);
            bundle.putDouble("bmr",mybmr);
            bundle.putDouble("tdee",mytdee);
            Intent intent = new Intent(this,TDEEResult.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Please fill in all the information!", Toast.LENGTH_SHORT).show();
        }
    }
    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.sed:
                if (checked){
                    Log.i("test","sed checked");
                    changeChecked(sed);
                    activitylevel.setText("Sedentary - Little or no exercise");
                    act = 1.2;
                }
                break;
            case R.id.lact:
                if (checked) {
                    Log.i("test", "light act checked");
                    activitylevel.setText("Lightly active - Light exercise or sport 1-3 times per week");
                    changeChecked(lact);
                    act = 1.375;
                }
                break;
            case R.id.mact:
                if (checked) {
                    Log.i("test", "mact act checked");
                    activitylevel.setText("Moderately active - Moderate exercise or sports 3-5 times a week");
                    changeChecked(mact);
                    act = 1.55;
                }
                break;
            case R.id.vact:
                if(checked){
                    Log.i("test", "vact act checked");
                    activitylevel.setText("Very active - Heavy exercise or sports 6-7 times a week");
                    changeChecked(vact);
                    act = 1.725;
                }
                break;
            case R.id.eact:
                if (checked) {
                    Log.i("test", "eact act checked");
                    activitylevel.setText("Extremely active - Heavy exercise and physical job or exercise 2 times per day everyday");
                    changeChecked(eact);
                    act = 1.9;
                }
        }
    }
    public void onGenderClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.male : gender = "male"; changeGender(male);break;
            case R.id.female: gender = "female"; changeGender(female); break;
        }
    }
    public void changeChecked(CheckBox box){
        CheckBox chb [] = {sed,lact,mact,vact,eact};
        for(int i = 0; i<chb.length;i++){
            if(!(chb[i].equals(box))){
                chb[i].setChecked(false);
            }
        }
    }
    public void changeGender(CheckBox box){
        CheckBox chb [] = {male,female};
        for(int i = 0 ; i<chb.length;i++){
            if(!chb[i].equals(box)){
                chb[i].setChecked(false);
            }
        }
    }
    public void onBackPressed(View view) {
        Intent goBack = new Intent(TDEE_Activity.this, StartPage.class);
        startActivity(goBack);
    }
}
