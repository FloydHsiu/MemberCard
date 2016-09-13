package com.floydxiu.hceproject.CardAndUserInfo.CardList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floydxiu.hceproject.R;

import java.util.List;

/**
 * Created by Floyd on 2016/9/13.
 */

public class CardAdapter extends ArrayAdapter<Card>{
    //layout id
    private int resource;
    //All Cards to show
    private List<Card> cards;

    public CardAdapter(Context context, int resource, List<Card> cards){
        super(context, resource, cards);
        this.resource = resource;
        this.cards = cards;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout cardView;
        // 讀取目前位置的記事物件
        final Card card = getItem(position);

        if (convertView == null) {
            // 建立項目畫面元件
            cardView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(inflater);
            li.inflate(resource, cardView, true);
        }
        else {
            cardView = (LinearLayout) convertView;
        }

        TextView txvCompany = (TextView) cardView.findViewById(R.id.txvCompany);
        TextView txvId = (TextView) cardView.findViewById(R.id.txvId);
        TextView txvExpiredate = (TextView) cardView.findViewById(R.id.txvExpiredate);
        TextView txvRank = (TextView) cardView.findViewById(R.id.txvRank);

        txvCompany.setText(card.getCompany());
        txvId.setText(""+card.getId());
        txvExpiredate.setText(""+card.getExpiredate());
        txvRank.setText(""+card.getRank());

        return cardView;
    }
}
