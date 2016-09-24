package com.floydxiu.hceproject.CardAndUserInfo.CardList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floydxiu.hceproject.R;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/9/13.
 */

public class CardExpandableListAdapter extends BaseExpandableListAdapter{
    //layout id
    private Context context;
    //All Cards to show
    private ArrayList<Card> cards;

    private CardListDBHelper dbCardList;

    public CardExpandableListAdapter(Context context){
        this.context = context;
        dbCardList = new CardListDBHelper(context);
        dbCardList.insert(12,33333,14123212,0);
        cards = dbCardList.query();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.listview_cardheader, null);
        TextView txvCardHeader = (TextView) linearLayout.findViewById(R.id.txvCardHeader);
        txvCardHeader.setText(cards.get(groupPosition).getCompany());

        return  linearLayout;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.listview_carddetails, null);
        TextView txvCompany = (TextView) linearLayout.findViewById(R.id.txvCompany);
        TextView txvId = (TextView) linearLayout.findViewById(R.id.txvId);
        TextView txvExpiredate = (TextView) linearLayout.findViewById(R.id.txvExpiredate);
        TextView txvRank = (TextView) linearLayout.findViewById(R.id.txvRank);

        Card card = cards.get(groupPosition);
        txvCompany.setText(card.getCompany());
        txvId.setText(""+card.getId());
        txvExpiredate.setText(""+card.getExpiredate());
        txvRank.setText(""+card.getRank());

        return linearLayout;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
