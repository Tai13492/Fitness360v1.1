package com.company.fitness360;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;


public class ForecastActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    ChangeTheWeather changeTheWeather;
    double latitude, longitude;
    TextView climate,userclimate, usertemperature,usercity,windspeed,userwindspeed,humidity,userhumidity,userdate;
    DownloadTask task;
    LinearLayout firstlinearLayout;
    JSONApiManipulation jsonApiManipulation;
    String temp,weathermain, weatherdescription, city, country, firstpart, apiid,id,weatherid,swindspeed,shumidity = null;
    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    int count=0;
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        task = new DownloadTask();
        firstlinearLayout = (LinearLayout) findViewById(R.id.firstlinearlayout);
        climate = (TextView) findViewById(R.id.climate);
        userclimate = (TextView) findViewById(R.id.userclimate);
        usertemperature = (TextView) findViewById(R.id.usertemperature);
        usercity = (TextView) findViewById(R.id.usercity);
        userdate = (TextView) findViewById(R.id.userdate);
        windspeed = (TextView) findViewById(R.id.windspeed);
        userwindspeed = (TextView) findViewById(R.id.userwindspeed);
        humidity = (TextView) findViewById(R.id.humidity);
        userhumidity  = (TextView) findViewById(R.id.userhumidity);
    }
    protected void onStart(){
        super.onStart();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.i("Test Location", "Latitude: " + latitude + "" + "Longitude: " + longitude);
                firstpart = "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude;
                apiid = "&appid=8a9cbc8db325a1ff4287792759faf76c";
                Log.i("test coordinates", "" + latitude + " " + longitude);
                if (count == 0) {
                    task.execute(firstpart + apiid);
                    //task.execute("http://api.openweathermap.org/data/2.5/weather?q=Berlin&APPID=8a9cbc8db325a1ff4287792759faf76c");
                    //task.execute("http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a1");
                    count++;
                }
                Log.i("test", "task executed");
                Log.i("test", "finished successfully");
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Log.i("WTF DUDE","YOU HERE YET?");
        if(location == null){
            Log.i("ERROR","NETWORK_PROVIDER NOT AVAILABLE");
            userclimate.setText("OPEN YOUR INTERNET");
        }else{
            locationListener.onLocationChanged(location);
        }
    }
    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (true) {
                    if (data == -1) {
                        break;
                    }
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                jsonApiManipulation = new JSONApiManipulation(result);
                temp = jsonApiManipulation.getJSONObjectFromJsonObject("main", "temp");
                weathermain = jsonApiManipulation.getJSONObjectFromJsonArray("weather", "main");
                weatherdescription = jsonApiManipulation.getJSONObjectFromJsonArray("weather", "description");
                city = jsonApiManipulation.getJSONObject("name");
                id = jsonApiManipulation.getJSONObjectFromJsonArray("weather","id");
                country = jsonApiManipulation.getJSONObjectFromJsonObject("sys", "country");
                swindspeed = jsonApiManipulation.getJSONObjectFromJsonObject("wind","speed");
                shumidity = jsonApiManipulation.getJSONObjectFromJsonObject("main","humidity");
                changeTheWeather = new ChangeTheWeather(id);
                weatherid = changeTheWeather.idToWeather();
                Log.i("weatherid",weatherid);
                String celcius = JSONApiManipulation.kelvinToCelcius(temp) + " \u2103";
                userclimate.setText(weatherdescription);
                usertemperature.setText(celcius);
                usercity.setText(city + "," + country);
                userwindspeed.setText(swindspeed + " m/s");
                userhumidity.setText(shumidity + " %");
                userdate.setText(currentDateTimeString);
                changeBackground(weatherid);
                Log.i("test", "test id " + id + "test method " + weatherid);
                Log.i("test", "JSON finished successfully");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void changeBackground(String weatherid){
        switch (weatherid){
            case "thunderstorm": firstlinearLayout.setBackgroundResource(R.drawable.thunderstorm);setTextWhite(); break;
            case "rain": firstlinearLayout.setBackgroundResource(R.drawable.rain);setTextWhite(); break;
            case "snow": firstlinearLayout.setBackgroundResource(R.drawable.snow); break;
            case "misty": firstlinearLayout.setBackgroundResource(R.drawable.misty);setTextWhite(); break;
            case "clear": firstlinearLayout.setBackgroundResource(R.drawable.clear); break;
            case "cloudy": firstlinearLayout.setBackgroundResource(R.drawable.cloudy); break;
            case "extreme": firstlinearLayout.setBackgroundResource(R.drawable.extreme);setTextWhite(); break;
            default: firstlinearLayout.setBackgroundResource(R.drawable.rain);setTextWhite(); break;
        }
    }
    public void setTextWhite(){
        usercity.setTextColor(Color.parseColor("#FFFFFF"));
        userdate.setTextColor(Color.parseColor("#FFFFFF"));
        usertemperature.setTextColor(Color.parseColor("#FFFFFF"));
        climate.setTextColor(Color.parseColor("#FFFFFF"));
        userclimate.setTextColor(Color.parseColor("#FFFFFF"));
        windspeed.setTextColor(Color.parseColor("#FFFFFF"));
        userwindspeed.setTextColor(Color.parseColor("#FFFFFF"));
        humidity.setTextColor(Color.parseColor("#FFFFFF"));
        userhumidity.setTextColor(Color.parseColor("#FFFFFF"));
    }
}
