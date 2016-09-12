package com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.floydxiu.hceproject.R;

/**
 * Created by Floyd on 2016/9/10.
 */

public class CardListFragment extends Fragment {

    public enum CARD_LIST_TYPE_ENUM { GRID, LINEAR_VERITCAL }
    public CARD_LIST_TYPE_ENUM cardlist_type = CARD_LIST_TYPE_ENUM.LINEAR_VERITCAL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cardlist, container, false);
    }
}
