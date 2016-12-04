package com.floydxiu.hceproject.ClientActivities.CardTransactionActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.floydxiu.hceproject.R;
import com.floydxiu.hceproject.Services.APDUservice;

/**
 * Created by Floyd on 2016/12/3.
 */

public class CardTransactionActivity extends AppCompatActivity {
    TextView txvState;
    ProgressBar progressBarCountDown;

    Intent apduService;

    CountDownTask countDownTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardtransaction);

        txvState = (TextView) findViewById(R.id.txvState);
        progressBarCountDown = (ProgressBar) findViewById(R.id.progressBarCountDown);

        txvState.setText("請感應卡片");
        progressBarCountDown.setProgress(30);

        countDownTask = new CountDownTask();
        countDownTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTask.cancel(false);
    }

    class CountDownTask extends AsyncTask<Void, Integer, String>{
        CardTransactionActivity activity;
        int Counter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity = CardTransactionActivity.this;
            Counter = 30;
            activity.apduService = new Intent();
            activity.apduService.setClass(activity, APDUservice.class);
            activity.apduService.putExtra("TransCode", activity.getIntent().getStringExtra("TransCode"));
            startService(activity.apduService);
            Log.i("INTENT", activity.apduService.toString());
        }

        @Override
        protected String doInBackground(Void... params) {
            for(int i=0; i<30; i++){
                if(isCancelled()) break;
                try {
                    Thread.sleep(1*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(activity.apduService.getStringExtra("state") != null){
                    if(activity.apduService.getStringExtra("state").equals("success")){
                        publishProgress(new Integer(-1));
                        break;
                    }
                }
                publishProgress(new Integer(1));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0].intValue() == -1){

            }
            else{
                Counter--;
                activity.progressBarCountDown.setProgress(Counter);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            activity.stopService(activity.apduService);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            activity.stopService(activity.apduService);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            activity.stopService(activity.apduService);
        }
    }
}
