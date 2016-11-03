package com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.floydxiu.hceproject.CardAndUserInfo.CardList.Card;
import com.floydxiu.hceproject.CardAndUserInfo.CardList.CardExpandableListAdapter;
import com.floydxiu.hceproject.CardAndUserInfo.CardList.CardListAdapter;
import com.floydxiu.hceproject.R;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/9/10.
 */

public class CardListFragment extends Fragment {

    public enum CARD_LIST_TYPE_ENUM { GRID, LINEAR_VERITCAL }
    public CARD_LIST_TYPE_ENUM cardlit_type = CARD_LIST_TYPE_ENUM.LINEAR_VERITCAL;

    //private ExpandableListView lvCard;
    private ListView lvCard;
    public ArrayList<Card> CardList = null;

    public void setCardList(ArrayList<Card> CardList){
        this.CardList = CardList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cardlist, container, false);

        //lvCard = (ExpandableListView) v.findViewById(lvCard);
        lvCard = (ListView) v.findViewById(R.id.lvCard);

        CardExpandableListAdapter cardAdapter = new CardExpandableListAdapter(getActivity());
        //lvCard.setAdapter(cardAdapter);
        if(CardList != null){
            CardListAdapter cardListAdapter = new CardListAdapter(getActivity(), R.id.lvCard, CardList);
            lvCard.setAdapter(cardListAdapter);
        }

        //setListViewHeightBasedOnChildren(lvCard);

        return v;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
