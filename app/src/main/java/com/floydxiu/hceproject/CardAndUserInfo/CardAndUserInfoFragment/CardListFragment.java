package com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.floydxiu.hceproject.CardAndUserInfo.CardList.Card;
import com.floydxiu.hceproject.CardAndUserInfo.CardList.CardExpandableListAdapter;
import com.floydxiu.hceproject.R;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/9/10.
 */

public class CardListFragment extends Fragment {

    public enum CARD_LIST_TYPE_ENUM { GRID, LINEAR_VERITCAL }
    public CARD_LIST_TYPE_ENUM cardlist_type = CARD_LIST_TYPE_ENUM.LINEAR_VERITCAL;

    private ExpandableListView lvCard;
    private ArrayList<Card> cards;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cardlist, container, false);

        lvCard = (ExpandableListView) v.findViewById(R.id.lvCard);

        cards = new ArrayList<Card>();
        cards.add(new Card("AAA", 0, 100, 0));
        cards.add(new Card("AAA", 0, 100, 0));
        cards.add(new Card("AAA", 0, 100, 0));
        cards.add(new Card("AAA", 0, 100, 0));
        cards.add(new Card("AAA", 0, 100, 0));

        CardExpandableListAdapter cardAdapter = new CardExpandableListAdapter(getActivity());
        lvCard.setAdapter(cardAdapter);

        setListViewHeightBasedOnChildren(lvCard);

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
