package com.floydxiu.hceproject.DataType;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
        if(CardType.equals("Permanent")){
            return "Permanent";
        }
        else if(CardType.equals("Limited")){
            Timestamp ts = new Timestamp(Long.parseLong(this.ExpireTime));
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            return df.format(ts);
        }
        else if(CardType.equals("Times")){
            return this.ExpireTime;
        }
        else {
            return "InValid";
        }
    }

    public String getCardLevel(){
        if(this.CardLevel.equals("N")){
            return "Normal";
        }
        else if(this.CardLevel.equals("G")){
            return "Gold";
        }
        else if(this.CardLevel.equals("P")){
            return "Platinum";
        }
        else if(this.CardLevel.equals("D")){
            return "Diamond";
        }
        else{
            return "InValid";
        }
    }

    public String getCardType() { return this.CardType;}

    /* CardLevel def:
    *  Normal(一般), Gold(金), Platinum(白金), Diamond(鑽石)
    *  */

    /* CardType + ExpireTime def:
    *  Permanent(永久), Limited(限時), Times(計次)
    *
    *  Permanent: ExpireTime = (don't care)
    *  Limited: ExpireTime = TimeStamp
    *  Times: ExpireTime = USED / AllTimes
    * */
}
