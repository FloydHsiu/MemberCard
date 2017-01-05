package com.floydxiu.hceproject.ClientActivities.CardTransactionActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.R;
import com.floydxiu.hceproject.Services.APDUservice;

import java.io.IOException;

/**
 * Created by Floyd on 2016/12/3.
 */

public class CardTransactionActivity extends AppCompatActivity {
    TextView txvState, txvProgressDisplay;
    ProgressBar progressBarCountDown, progressBarWaiting;

    Intent apduService;

    ClientTransactionTask countDownTask;

    boolean STATE = false;

    final BroadcastReceiver hceNotificationsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CardTransactionActivity.this.STATE = intent.getBooleanExtra("STATE", false);
            // TODO: do something with the received data
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardtransaction);

        txvState = (TextView) findViewById(R.id.txvState);
        txvProgressDisplay = (TextView) findViewById(R.id.txvProgressDisplay);
        progressBarCountDown = (ProgressBar) findViewById(R.id.progressBarCountDown);
        progressBarWaiting = (ProgressBar) findViewById(R.id.progressBarWaiting);

        txvState.setText("請感應卡片");
        progressBarCountDown.setProgress(30);

        Intent intent = getIntent();
        int ComId = intent.getIntExtra("ComId", 0);
        int CardNum = intent.getIntExtra("CardNum", 0);
        countDownTask = new ClientTransactionTask(CardTransactionActivity.this);
        countDownTask.execute(new Integer(ComId), new Integer(CardNum));
    }

    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter hceNotificationsFilter = new IntentFilter();
        hceNotificationsFilter.addAction("MemberCard.hce.app.action.NOTIFY_STATE");
        registerReceiver(hceNotificationsReceiver, hceNotificationsFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(hceNotificationsReceiver);
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

    class ClientTransactionTask extends AsyncTask<Integer, Integer, Void>{

        CardTransactionActivity activity;
        int progressCount;
        public ClientTransactionTask(CardTransactionActivity activity){
            this.activity = activity;
            this.progressCount = 30;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.progressBarWaiting.setVisibility(View.VISIBLE);
            activity.progressBarCountDown.setVisibility(View.GONE);
            activity.txvProgressDisplay.setText("取得交易授權碼");
        }

        @Override
        protected Void doInBackground(Integer... params) {
            //Get TransCode
            APIConnection apiConnection = new APIConnection(activity);
            String TransCode = null;
            int ComId = params[0].intValue(), CardNum = params[1].intValue();
            for( int i=0; i < 5; i++){
                try{
                    TransCode = apiConnection.TransactionRequest(ComId, CardNum);
                    if( TransCode != null) break;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            //start nfc card
            if( TransCode != null ){
                //apduService
                activity.apduService = new Intent();
                activity.apduService.setClass(activity, APDUservice.class);
                activity.apduService.putExtra("TransCode", TransCode);
                startService(activity.apduService);
                publishProgress(0);
                for( int i=0; i<60; i++){
                    if( activity.STATE ){
                        //nfc card transition complete
                        activity.stopService( activity.apduService );
                        publishProgress(2);
                        //confirm transaction
                        for( int j=0; j < 5; j++){
                            try {
                                if( apiConnection.ClientTransactionConfirm(ComId, CardNum, TransCode)){
                                    publishProgress(6);
                                    return null;
                                }
                            }catch (IOException e){
                                Log.i("Check Transaction", "TimeOut");
                            }
                        }
                        publishProgress(7);
                        return null;
                    }
                    if(isCancelled()){
                        publishProgress(4);
                        return null;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if( i % 2 == 1)  publishProgress(1);
                }
                activity.stopService(activity.apduService);
                publishProgress(3);
            }
            //can't get transcode
            publishProgress(5);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if( values[0].intValue() == 0){
                //initial count down
                activity.progressBarWaiting.setVisibility(View.GONE);
                activity.progressBarCountDown.setVisibility(View.VISIBLE);
                activity.progressBarCountDown.setProgress(this.progressCount);
                activity.txvProgressDisplay.setText(""+progressCount);
            }
            else if( values[0].intValue() == 1){
                //count one second
                this.progressCount--;
                activity.progressBarCountDown.setProgress(this.progressCount);
                activity.txvProgressDisplay.setText(""+progressCount);
            }
            else if( values[0].intValue() == 2){
                //check transaction
                activity.progressBarWaiting.setVisibility(View.VISIBLE);
                activity.progressBarCountDown.setVisibility(View.GONE);
                activity.txvProgressDisplay.setText("確認交易結果");
            }
            else if( values[0].intValue() == 3){
                activity.txvProgressDisplay.setText("交易逾時");
            }
            else if( values[0].intValue() == 4){
                activity.txvProgressDisplay.setText("取消交易");
            }
            else if( values[0].intValue() == 5){
                activity.txvProgressDisplay.setText("無法取得交易授權碼");
            }
            else if( values[0].intValue() == 6){
                activity.progressBarWaiting.setVisibility(View.GONE);
                activity.progressBarCountDown.setVisibility(View.VISIBLE);
                activity.txvProgressDisplay.setText("交易確認成功");
                new AlertDialog.Builder(activity)
                        .setTitle("交易訊息").setMessage("交易確認成功")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
            else if( values[0].intValue() == 7){
                activity.progressBarWaiting.setVisibility(View.GONE);
                activity.progressBarCountDown.setVisibility(View.VISIBLE);
                activity.txvProgressDisplay.setText("交易未成功確認");
                new AlertDialog.Builder(activity)
                        .setTitle("交易訊息").setMessage("交易未成功確認")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i("CardTransaction", "onCancelled()");
            activity.txvProgressDisplay.setText("取消交易");
            activity.stopService(activity.apduService);
        }

        @Override
        protected void onCancelled(Void s) {
            Log.i("CardTransaction", "onCancelled(s): "+ s);
            activity.txvProgressDisplay.setText("取消交易");
            activity.stopService(activity.apduService);
        }
    }
}
