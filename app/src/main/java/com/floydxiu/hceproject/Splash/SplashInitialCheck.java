package com.floydxiu.hceproject.Splash;

import android.content.SharedPreferences;

/**
 * Created by Floyd on 2016/8/18.
 * Use this Class to check the Initial Status of App
 * Whether it is "First Time Open" or "Not Login" or "Login"
 */

public class SplashInitialCheck {
    public static String PreferenceName = "AppSetting";
    //Use SharedPreferences To Save Normal App Status
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    //Status define
    public static String PreferenceKey = "App Status";
    public static short FIRST_USE = 1;
    public static short UN_LOGIN = 2;
    public static short LOGIN = 3;

    public SplashInitialCheck(SharedPreferences sharedPreferences){
        //Get SharedPreferences Data From Class Newer.
        this.sharedPreferences = sharedPreferences;
        this.sharedPreferencesEditor = sharedPreferences.edit();
    }

    public int checkAppStatus(){
        int AppStatus = this.sharedPreferences.getInt(PreferenceKey, 1);
        if(AppStatus == this.FIRST_USE)
        {
            //if there is no "App Status" in sharedPreference -> FIRST_USE
            this.changeAppStatus(this.FIRST_USE);
            return this.FIRST_USE;
        }
        /**** ISSUE ****/
        //The LOGIN part maybe have to check another secure data part
        else if(AppStatus == this.UN_LOGIN)
        {
            return this.UN_LOGIN;
        }
        else if(AppStatus == this.LOGIN) {
            return this.LOGIN;
        }
        //Exception
        else return 0;
    }

    public void changeAppStatus(int val){
        this.sharedPreferencesEditor.putInt(PreferenceKey, val);
        this.sharedPreferencesEditor.commit();
    }
}
