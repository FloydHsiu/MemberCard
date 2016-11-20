package com.floydxiu.hceproject.DataType;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Floyd on 2016/11/20.
 */

public class CardListSPManager {
    private Context context;

    //PreferenceName : CardList
    private String CardList = "CardList";
    private String CardList_JSON = "CardList_JSON";

    public CardListSPManager(Context context){
        this.context = context;
    }

    private SharedPreferences getSharedPreference(String PreferenceName){
        return context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
    }

    public void setCardList_JSON(String cardList_json){
        SharedPreferences sp = getSharedPreference(CardList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CardList_JSON, cardList_json);
        editor.apply();
    }

    public String getCardList_JSON(){
        SharedPreferences sp = getSharedPreference(CardList);
        return sp.getString(CardList_JSON, "");
    }
}
