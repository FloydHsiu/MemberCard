package com.floydxiu.hceproject.Splash;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoActivity;
import com.floydxiu.hceproject.DataType.LoginStateSPManager;
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

    class SplashDisplayTimer extends AsyncTask<Double, Void, Intent>{
        private Context context;

        public SplashDisplayTimer(Context context){
            this.context = context;
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

            /* Decide next activity */
            LoginStateSPManager loginStateSPManager = new LoginStateSPManager(this.context);
            int OpenTimes = loginStateSPManager.getOpenTimes();
            boolean isLogin = loginStateSPManager.getLoginState();
            Log.d("LoginState", ""+ OpenTimes + ", " + isLogin);
            Intent intent = new Intent();

            //First time open this app
            if(OpenTimes == 0){
                intent.setClass(this.context, UserCertificateActivity.class);
            }
            else{
                if(isLogin){
                    //The app is Login
                    //check exact login state with server
                    APIConnection apiConnection = new APIConnection(this.context);
                    boolean[] result = apiConnection.checkSessionIsLogin();
                    boolean islogin = false;
                    boolean isadmin = false;

                    if(result != null){
                        islogin = result[0];
                        isadmin = result[1];
                    }

                    if(islogin){
                        if(isadmin){//admin login
                            intent.setClass(this.context, CardAndUserInfoActivity.class);
                        }
                        else{//client login
                            intent.setClass(this.context, CardAndUserInfoActivity.class);
                        }
                    }
                    else{//not login
                        intent.setClass(this.context, UserCertificateActivity.class);
                    }
                }
                else{//Ths app is not login
                    intent.setClass(this.context, UserCertificateActivity.class);
                }
            }

            loginStateSPManager.setOpenTimes(OpenTimes+1);
            return intent;
        }

        @Override
        protected void onPostExecute(Intent intent) {
            context.startActivity(intent);
            ((SplashActivity)context).finish();
        }
    }
}
