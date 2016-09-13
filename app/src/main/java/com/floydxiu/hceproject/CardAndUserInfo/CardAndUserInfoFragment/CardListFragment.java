package com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.floydxiu.hceproject.CardAndUserInfo.CardList.Card;
import com.floydxiu.hceproject.CardAndUserInfo.CardList.CardAdapter;
import com.floydxiu.hceproject.R;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/9/10.
 */

public class CardListFragment extends Fragment {

    public enum CARD_LIST_TYPE_ENUM { GRID, LINEAR_VERITCAL }
    public CARD_LIST_TYPE_ENUM cardlist_type = CARD_LIST_TYPE_ENUM.LINEAR_VERITCAL;

    private ListView lvCard;
    private ArrayList<Card> cards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cardlist, container, false);

        lvCard = (ListView) v.findViewById(R.id.lvCard);

        cards = new ArrayList<Card>();
        cards.add(new Card("AAA", 0, 100, 0));
        cards.add(new Card("AAA", 0, 100, 0));
        cards.add(new Card("AAA", 0, 100, 0));

        CardAdapter cardAdapter = new CardAdapter(getActivity(), R.layout.listview_singlecard, cards);
        lvCard.setAdapter(cardAdapter);

        return v;
    }
}
