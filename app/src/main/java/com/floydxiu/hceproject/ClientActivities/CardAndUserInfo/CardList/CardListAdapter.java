package com.floydxiu.hceproject.ClientActivities.CardAndUserInfo.CardList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.ClientActivities.CardAndUserInfo.CardAndUserInfoActivity;
import com.floydxiu.hceproject.ClientActivities.CardTransactionActivity.CardTransactionActivity;
import com.floydxiu.hceproject.DataType.Card;
import com.floydxiu.hceproject.R;

import java.util.Date;
import java.util.List;

/**
 * Created by Floyd on 2016/9/27.
 */

public class CardListAdapter extends ArrayAdapter<Card> {

    private LayoutInflater layoutInflater;
    Context context;

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
        final TextView txvCardLevel = (TextView) convertView.findViewById(R.id.txvCardLevel);
        final TextView txvCardType = (TextView) convertView.findViewById(R.id.txvCardType);
        final TextView txvExpireTime = (TextView) convertView.findViewById(R.id.txvExpireTime);
        final Button btnStartTransaction = (Button) convertView.findViewById(R.id.btnStartTransaction);

        txvHeaderCardNum.setText(""+item.getCardNum());
        txvHeaderComName.setText(item.getComName());
        settxvCardLevel(txvCardLevel, item);
        settxvCardType(txvCardType, item);
        settxvExpireTime(txvExpireTime, item);

        final CardAndUserInfoActivity activity = (CardAndUserInfoActivity) this.context;

        btnStartTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionRequestTask transactionRequestTask = new TransactionRequestTask();
                transactionRequestTask.execute(new Integer(item.getComId()), new Integer(item.getCardNum()));
            }
        });

        layoutbtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutDetails.getVisibility() == LinearLayout.INVISIBLE){
                    layoutDetails.setVisibility(LinearLayout.VISIBLE);
                    layoutDetails.getLayoutParams().height = 300;
                    layoutDetails.requestLayout();
                    imgMore.setImageResource(R.drawable.ic_expand_less_black_24dp);
                }
                else{
                    layoutDetails.setVisibility(LinearLayout.INVISIBLE);
                    layoutDetails.getLayoutParams().height = 0;
                    layoutDetails.requestLayout();
                    imgMore.setImageResource(R.drawable.ic_expand_more_black_24dp);
                }

            }
        });

        return convertView;
    }

    void settxvCardLevel(TextView txvCardLevel, Card item){
        char CardLevel = item.getFormattedCardLevel().charAt(0);
        switch (CardLevel){
            case 'N':
                txvCardLevel.setText(context.getResources().getString(R.string.NLevel));
                break;
            case 'G':
                txvCardLevel.setText(context.getResources().getString(R.string.GLevel));
                break;
            case 'P':
                txvCardLevel.setText(context.getResources().getString(R.string.PLevel));
                break;
            case 'D':
                txvCardLevel.setText(context.getResources().getString(R.string.DLevel));
                break;
            case 'I':
                txvCardLevel.setText("InValid");
                break;
        }
    }

    void settxvCardType(TextView txvCardType, Card item){
        char CardType = item.getCardType().charAt(0);
        switch (CardType){
            case 'P':
                txvCardType.setText(context.getResources().getString(R.string.PCardType));
                break;
            case 'L':
                txvCardType.setText(context.getResources().getString(R.string.LCardType));
                break;
            case 'T':
                txvCardType.setText(context.getResources().getString(R.string.TCardType));
                break;
            default:
                txvCardType.setText("InValid");
                break;
        }
    }

    void settxvExpireTime(TextView txvExpireTime, Card item){
        char CardType = item.getCardType().charAt(0);
        switch (CardType){
            case 'P':
                txvExpireTime.setText("∞");
                break;
            case 'L':
                String timestamp = item.getFormattedExpireTime();
                Date date = new Date();
                date.setTime(new Integer(timestamp));
                txvExpireTime.setText(date.toString());
                break;
            case 'T':
                txvExpireTime.setText(item.getFormattedExpireTime());
                break;
            default:
                txvExpireTime.setText("InValid");
                break;
        }
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
                CardAndUserInfoActivity activity = (CardAndUserInfoActivity) CardListAdapter.this.context;
                Intent intent = new Intent();
                intent.setClass(activity, CardTransactionActivity.class);
                intent.putExtra("TransCode", s);
                activity.startActivity(intent);
            }
        }
    }
}
