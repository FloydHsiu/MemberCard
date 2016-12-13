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
    TextView txvState, txvProgressDisplay;
    ProgressBar progressBarCountDown;

    Intent apduService;

    CountDownTask countDownTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardtransaction);

        txvState = (TextView) findViewById(R.id.txvState);
        txvProgressDisplay = (TextView) findViewById(R.id.txvProgressDisplay);
        progressBarCountDown = (ProgressBar) findViewById(R.id.progressBarCountDown);

        txvState.setText("請感應卡片");
        progressBarCountDown.setProgress(30);

        countDownTask = new CountDownTask();
        countDownTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTask.cancel(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class CountDownTask extends AsyncTask<Void, Integer, String>{
        CardTransactionActivity activity;
        int Counter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity = CardTransactionActivity.this;
            Counter = 30;
            //Initial progress bar
            onProgressUpdate(new Integer(0));
            //apduService
            activity.apduService = new Intent();
            activity.apduService.setClass(activity, APDUservice.class);
            activity.apduService.putExtra("TransCode", activity.getIntent().getStringExtra("TransCode"));
            startService(activity.apduService);
            Log.i("INTENT", activity.apduService.toString());
        }

        @Override
        protected String doInBackground(Void... params) {
            String msg = null;
            for(int i=0; i<30; i++){
                if(isCancelled()){
                    msg = "cancel";

                    break;
                }
                try {
                    Thread.sleep(1*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(activity.apduService.getStringExtra("state") != null){
                    if(activity.apduService.getStringExtra("state").equals("success")){
                        //show success
                        publishProgress(new Integer(-1));
                        msg = "success";
                        break;
                    }
                }
                publishProgress(new Integer(1));
            }
            if(msg == null){
                //show timeout
                publishProgress(new Integer(-2));
                msg = "timeout";
            }
            return msg;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0].intValue() == -1){//success
                activity.txvProgressDisplay.setText("交易成功");
                activity.progressBarCountDown.setProgress(30);
            }
            else if(values[0].intValue() == -2){//timeout
                activity.txvProgressDisplay.setText("交易逾時");
            }
            else if(values[0].intValue() == 0){//initial
                activity.progressBarCountDown.setProgress(Counter);
                activity.txvProgressDisplay.setText(""+Counter);
            }
            else if(values[0].intValue() == -3){//cancel
                activity.progressBarCountDown.setProgress(0);
                activity.txvProgressDisplay.setText("請重新請求交易");
            }
            else{
                Counter--;
                activity.txvProgressDisplay.setText(""+Counter);
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
            Log.i("CardTransaction", "onCancelled()");
            activity.stopService(activity.apduService);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            Log.i("CardTransaction", "onCancelled(s): "+ s);
            onProgressUpdate(new Integer(-3));
            activity.stopService(activity.apduService);
        }
    }
}
