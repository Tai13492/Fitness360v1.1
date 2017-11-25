package com.company.fitness360;

/**
 * Created by Tai on 11/13/2017.
 */

public class Healthy {
    double bmi;
    int tdee;
    public Healthy(double bmi, int tdee){
        this.bmi=bmi;
        this.tdee=tdee;
    }
    public String healthyBMI(){
        if(this.bmi <= 18.5){
            return "Your are extremely underweight, please consider following our calorie recommendation in order to gain weight and become more healthy";
        }else if(this.bmi <=24.9){
            return "Your weight are considered as healthy! we recommend you to follow our calorie recommendation to maintain your weight!";
        }else if(this.bmi<=29.9){
            return "Your are a little bit overweight, we recommend you to follow our calorie recommendation to lose weight slowly, meaning that you can stil enjoy lots of food!";
        }else{
            return "Your are extremely overweight, please consider following our calorie recommendation in order to lose weight as quickly as possible and avoid diseases that comes with obesity";
        }
    }

    public String calorieRecommendationString(){
        if(this.bmi <= 18.5){
            return "Your tdee is " + this.tdee + " kcal/day but you are under-weight, you should be in a caloric surplus;hence, you should be consuming " + (this.tdee + 800) + " kcal/day.";
        }else if(this.bmi <=24.9){
            return "Your tdee is " + this.tdee + " kcal/day and your weight is considered as healthy so our recommendation for you is to consume according to your tdee.";
        }else if(this.bmi<=29.9){
            return "Your tdee is " + this.tdee + " kcal/day but you are over-weight, you should be in a slight caloric deficit;hence, you should be consuming " + (this.tdee - 500) + " kcal/day.";
        }else{
            return "Your tdee is " + this.tdee + " kcal/day but you are extremely over-weight, you should be in a caloric deficit;hence, you should be consuming " + (this.tdee - 1000) + " kcal/day.";
        }
    }
    public String calorieRecommendation(){
        if(this.bmi <= 18.5){
            return (this.tdee + 800) + " kcal/day";
        }else if(this.bmi <=24.9){
            return this.tdee + " kcal/day";
        }else if(this.bmi<=29.9){
            return (this.tdee-500) + " kcal/day";
        }else{
            return (this.tdee-1000) + " kcal/day.";
        }
    }
}
