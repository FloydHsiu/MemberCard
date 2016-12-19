package com.floydxiu.hceproject.DataType;

/**
 * Created by Floyd on 2016/12/19.
 */

public class ExpireNotice {
    public Card card;
    public String notice;

    public ExpireNotice(Card card, String notice){
        this.card = card;
        this.notice = notice;
    }
}
