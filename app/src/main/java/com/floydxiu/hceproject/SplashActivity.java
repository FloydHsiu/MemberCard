package com.floydxiu.hceproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private class SplashDisplayTimer extends AsyncTask<Integer, Void, Void>{
        @Override
        protected Void doInBackground(Integer... params) {
            int timer = params[0].intValue();
            try {
                Thread.sleep(timer*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Direct App into true user interactive interface
            SplashInitialCheck splashInitialCheck = new SplashInitialCheck(getSharedPreferences("AppSetting", MODE_PRIVATE));
            int AppStatus = splashInitialCheck.checkAppStatus();
            //if first time use this app
            if(AppStatus == splashInitialCheck.FIRST_USE){

            }
            //if user have not login
            else if(AppStatus == splashInitialCheck.UN_LOGIN){

            }
            //if user have login
            else if(AppStatus == splashInitialCheck.LOGIN){

            }
        }
    }
}
