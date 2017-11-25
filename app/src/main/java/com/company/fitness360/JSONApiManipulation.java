package com.company.fitness360;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by Tai on 11/17/2017.
 */

/*
======= This is an example of openweathermap api in JSON format ========
{"coord":{"lon":100.5,"lat":13.76},
"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}],
"base":"stations",
"main":{"temp":303.64,"pressure":1012,"humidity":74,"temp_min":303.15,"temp_max":304.15},
"visibility":10000,
"wind":{"speed":2.6,"deg":40},
"clouds":{"all":40},
"dt":1510887600,
"sys":{"type":1,"id":7917,"message":0.0118,"country":"TH","sunrise":1510874316,"sunset":1510915629},
"id":1608132,
"name":"Changwat Nonthaburi",
"cod":200}
====================================================================================
 */

public class JSONApiManipulation {
    String json;
    public JSONApiManipulation(String json){
        this.json = json;
    }
    public String getJSONObjectFromJsonObject(String requestname,String request){
        String result = null;
        String converter = null;
        try {
            JSONObject fulljsonobject = new JSONObject(this.json);
            converter = fulljsonobject.getString(requestname);
            JSONObject targetjsonobject = new JSONObject(converter);
            result = targetjsonobject.getString(request);
            return result;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public String getJSONObject(String request){
        String result = null;
        try{
            JSONObject jsonObject = new JSONObject(this.json);
            result = jsonObject.getString(request);
            return result;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public String getJSONObjectFromJsonArray(String requestname,String request){
        String result = null;
        String converter = null;
        try{
            JSONObject jsonObject = new JSONObject(this.json);
            converter = jsonObject.getString(requestname);
            JSONArray jsonArray = new JSONArray(converter);
            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonpart = jsonArray.getJSONObject(i);
                result = jsonpart.getString(request);
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String kelvinToCelcius(String kelvin){
        String result = "";
        double inputkelvin = Double.parseDouble(kelvin);
        inputkelvin = inputkelvin - 273;
        inputkelvin = Math.round(inputkelvin*10)/10;
        return result + inputkelvin;
    }
}



