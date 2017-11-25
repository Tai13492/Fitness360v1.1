package com.company.fitness360;

/**
 * Created by Tai on 11/19/2017.
 */

public class ChangeTheWeather {
    String id;
    public ChangeTheWeather(String id) {
        this.id = id;
    }
    public String idToWeather() {
        String first = id.substring(0, 1);
        String second = id.substring(1, 2);
        String third = id.substring(2);
        String weather = "snow";
        if (first.equals("2")) {
            weather = "thunderstorm";
        } else if (first.equals("3")) {
            weather = "rain";
        } else if (first.equals("5")) {
            weather = "rain";
        } else if (first.equals("6")) {
            weather = "snow";
        } else if (first.equals("7")) {
            weather = "misty";
        } else if (first.equals("8")) {
            if (third.equals("0")) {
                weather = "clear";
            } else {
                weather = "cloudy";
            }
        } else if (first.equals("9")) {
            if (second.equals("0")) {
                weather = "extreme";
            } else {
                weather = "misty";
            }
        }
        return weather;
    }
}
