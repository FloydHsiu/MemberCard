package com.floydxiu.hceproject.CardAndUserInfo.CardList;

/**
 * Created by Floyd on 2016/9/13.
 */

public class Card {

    private int ComId;
    private String ComName;
    private int CardNum;
    private String  ExpireTime;
    private String CardLevel;
    private String CardType;


    public Card(int ComId, String ComName, int CardNum, String CardType, String ExpireTime, String CardLevel){
        this.ComId = ComId;
        this.ComName = ComName;
        this.CardNum = CardNum;
        this.ExpireTime = ExpireTime;
        this.CardType = CardType;
        this.CardLevel = CardLevel;
    }

    public String getComName(){
        return this.ComName;
    }

    public int getCardNum(){
        return this.CardNum;
    }

    public String getExpireTime(){
        return this.ExpireTime;
    }

    public String getCardLevel(){
        return this.CardLevel;
    }

    public String getCardType() { return this.CardType;}
}
