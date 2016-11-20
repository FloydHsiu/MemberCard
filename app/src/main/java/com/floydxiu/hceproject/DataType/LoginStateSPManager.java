package com.floydxiu.hceproject.DataType;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Floyd on 2016/11/20.
 */

public class LoginStateSPManager {
    private Context context;

    //PreferenceNames : LoginState
    private String LoginState = "LoginState";
    private String isLogin = "isLogin";
    private String Session = "Session";
    private String OpenTimes = "OpenTimes";

    public LoginStateSPManager(Context context){
        this.context = context;
    }

    private SharedPreferences getSharedPreference(String PreferenceName){
        return context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
    }

    //Preference "LoginState" manipulate
    /* isLogin */

    public boolean getLoginState(){
        SharedPreferences sp = getSharedPreference(LoginState);
        return sp.getBoolean(isLogin, false);
    }

    public void setLoginState(boolean islogin){
        SharedPreferences sp = getSharedPreference(LoginState);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(isLogin, islogin);
        editor.apply();
    }

    /* Session */
    public void setSession(String session){
        SharedPreferences sp = getSharedPreference(LoginState);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Session, session);
        editor.apply();
    }

    public String getSession(){
        SharedPreferences sp = getSharedPreference(LoginState);
        return sp.getString(Session, "");
    }

    /* OpenTimes */
    public void setOpenTimes(int opentimes){
        SharedPreferences sp = getSharedPreference(LoginState);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(OpenTimes, opentimes);
    }

    public int getOpenTimes(){
        SharedPreferences sp = getSharedPreference(LoginState);
        return sp.getInt(OpenTimes, 0);
    }
}
