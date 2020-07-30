package com.example.quizzme.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private SharedPreferences sharedPreferences;

    public Prefs(Activity activity){
        this.sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);

    }

    public void saveHighScore(int score){
        int currentScore = score;
        int highScore = sharedPreferences.getInt("high_score",0);

        if(currentScore>highScore){
            sharedPreferences.edit().putInt("high_score",currentScore).apply();
        }
    }

    public int getHighScore(){
       return sharedPreferences.getInt("high_score",0);
    }

    public void saveState(int questionIdx){
        sharedPreferences.edit().putInt("index",questionIdx).apply();
    }

    public int getState(){
        return sharedPreferences.getInt("index",0);
    }

}
