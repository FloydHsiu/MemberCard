package com.floydxiu.hceproject.CardAndUserInfo.CardList;

/**
 * Created by Floyd on 2016/9/13.
 */

public class Card {

    private String company;
    private int id;
    private long expiredate;
    private int rank;

    public Card(String company, int id, long expiredate, int rank){
        this.company = company;
        this.id = id;
        this.expiredate = expiredate;
        this.rank = rank;
    }

    public String getCompany(){
        return this.company;
    }

    public int getId(){
        return this.id;
    }

    public long getExpiredate(){
        return this.expiredate;
    }

    public int getRank(){
        return this.rank;
    }
}
