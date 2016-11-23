package com.floydxiu.hceproject.ClientActivities.CardAndUserInfo.CardList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.DataType.Card;
import com.floydxiu.hceproject.R;

import java.util.List;

/**
 * Created by Floyd on 2016/9/27.
 */

public class CardListAdapter extends ArrayAdapter<Card> {

    private LayoutInflater layoutInflater;
    Context context;
    Intent APDUservice;

    public CardListAdapter(Context context, int resource, List<Card> objects) {
        super(context, resource, objects);
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Card item = (Card) getItem(position);

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.listview_singlecard, null);
        }

        final ImageView imgMore = (ImageView) convertView.findViewById(R.id.imgMore);
        final LinearLayout layoutDetails = (LinearLayout) convertView.findViewById(R.id.layoutDetails);
        final LinearLayout layoutbtnDetails = (LinearLayout) convertView.findViewById(R.id.layoutbtnDetails);
        final TextView txvHeaderComName = (TextView) convertView.findViewById(R.id.txvHeaderComName);
        final TextView txvHeaderCardNum = (TextView) convertView.findViewById(R.id.txvHeaderCardNum);

        txvHeaderCardNum.setText(""+item.getCardNum());
        txvHeaderComName.setText(item.getComName());

        layoutbtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutDetails.getVisibility() == LinearLayout.INVISIBLE){
                    layoutDetails.setVisibility(LinearLayout.VISIBLE);
                    layoutDetails.getLayoutParams().height = 300;
                    layoutDetails.requestLayout();
                    imgMore.setImageResource(R.drawable.ic_expand_less_black_24dp);

                    TransactionRequestTask transactionRequestTask = new TransactionRequestTask();
                    transactionRequestTask.execute(new Integer(item.getComId()), new Integer(item.getCardNum()));
                }
                else{
                    layoutDetails.setVisibility(LinearLayout.INVISIBLE);
                    layoutDetails.getLayoutParams().height = 0;
                    layoutDetails.requestLayout();
                    imgMore.setImageResource(R.drawable.ic_expand_more_black_24dp);

                    CardListAdapter.this.context.stopService(APDUservice);
                    Toast.makeText(CardListAdapter.this.context, "Stop NFC service", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return convertView;
    }

    class TransactionRequestTask extends AsyncTask<Integer, Void, String>{
        @Override
        protected String doInBackground(Integer... params) {
            APIConnection apiConnection = new APIConnection(CardListAdapter.this.context);
            String TransCode = apiConnection.TransactionRequest(params[0].intValue(), params[1].intValue());
            return TransCode;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null){
                AppCompatActivity activity = (AppCompatActivity) CardListAdapter.this.context;
                CardListAdapter.this.APDUservice = new Intent();
                CardListAdapter.this.APDUservice.setClass(activity, com.floydxiu.hceproject.Services.APDUservice.class);
                CardListAdapter.this.APDUservice.putExtra("TransCode", s);
                activity.startService(CardListAdapter.this.APDUservice);
                Toast.makeText(activity, "Start NFC service", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
