package com.company.fitness360;

/**
 * Created by Tai on 11/13/2017.
 */

public class Calculate {
    String gender;
    double weight;
    double height;
    double age;
    double act;
    public Calculate(String gender,double weight,double height,double age,double act){
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.act = act;
    }
    public double calculateBMI(){
        double bmi = (this.weight/this.height/this.height) *10000;
        bmi = Math.round((bmi*10))/10;
        return bmi;
    }
    public double calculateBMR(){
        double bmr;
        if(this.gender.equals("Male")){
            bmr = 66 + (13.7*this.weight) + (5*this.height) - (6.8*this.age);
        }else {
            bmr = 655 + (9.6*this.weight) + (1.8*this.height) - (4.7*this.age);
        }
        bmr = Math.round((bmr*10))/10;
        return bmr;
    }
    public double calculateTDEE(){
        Calculate c = new Calculate(this.gender,this.weight,this.height,this.age,this.act);
        double bmr = c.calculateBMR();
        return bmr*this.act;
    }
}