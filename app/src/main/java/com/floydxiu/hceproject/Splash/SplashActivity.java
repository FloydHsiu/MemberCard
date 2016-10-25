package com.floydxiu.hceproject.Splash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoActivity;
import com.floydxiu.hceproject.R;
import com.floydxiu.hceproject.UserCertificate.UserCertificateActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashDisplayTimer splashDisplayTimer = new SplashDisplayTimer(this);
        splashDisplayTimer.execute(2.5);
    }

    private class SplashDisplayTimer extends AsyncTask<Double, Void, Intent>{
        private AppCompatActivity activity;

        public SplashDisplayTimer(AppCompatActivity activity){
            this.activity = activity;
        }

        @Override
        protected Intent doInBackground(Double... params) {
            //Display Splash
            int timer = params[0].intValue();
            try {
                Thread.sleep(timer*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Which flow to go
            //Direct App into true user interactive interface
            SplashInitialCheck splashInitialCheck = new SplashInitialCheck(getSharedPreferences(SplashInitialCheck.PreferenceName, MODE_PRIVATE));
            int AppStatus = splashInitialCheck.checkAppStatus();
            Intent nextIntent = new Intent();
            //if first time use this app
            if(AppStatus == splashInitialCheck.FIRST_USE){
               nextIntent.setClass(this.activity, UserCertificateActivity.class);
            }
            //if user have not login
            else if(AppStatus == splashInitialCheck.UN_LOGIN){
                nextIntent.setClass(this.activity, UserCertificateActivity.class);
            }
            //if user have login
            else if(AppStatus == splashInitialCheck.LOGIN){
                //connect to Server make check
                APIConnection apiConnection = new APIConnection(this.activity);
                //server check session is "login"
                if( apiConnection.checkSessionIsLogin()){
                    nextIntent.setClass(this.activity, CardAndUserInfoActivity.class);
                }
                else{
                    nextIntent.setClass(this.activity, UserCertificateActivity.class);
                }
            }
            else{
                nextIntent.setClass(this.activity, UserCertificateActivity.class);
            }
            return nextIntent;
        }

        @Override
        protected void onPostExecute(Intent nextIntent) {
            super.onPostExecute(nextIntent);
            this.activity.startActivity(nextIntent);
            this.activity.finish();
        }
    }
}
