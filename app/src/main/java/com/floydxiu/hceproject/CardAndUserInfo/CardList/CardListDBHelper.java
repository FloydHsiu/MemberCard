package com.floydxiu.hceproject.CardAndUserInfo.CardList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/9/24.
 */

public class CardListDBHelper extends SQLiteOpenHelper {
    // 資料庫名稱
    public static final String DATABASE_NAME = "CardList.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;
    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase dbCard;

    public CardListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        dbCard = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        dbCard.execSQL("CREATE TABLE IF NOT EXISTS Cardlist(" +
                "company int not null," +
                "id int not null auto_increment," +
                "primary key(id)," +
                "cardid int not null," +
                "expiredate int not null," +
                "rank int not null" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Card> query(){
        ArrayList<Card> allCards = new ArrayList<Card>();
        dbCard.beginTransaction();
        Cursor queryalldata = dbCard.rawQuery("select * from CardList", null);
        if(queryalldata != null && queryalldata.getCount()>0){
            queryalldata.moveToFirst();
            for(int i=0; i<queryalldata.getCount(); i++){
                allCards.add(new Card(""+queryalldata.getInt(0), queryalldata.getInt(1), queryalldata.getInt(2), queryalldata.getInt(3)));
                queryalldata.moveToNext();
            }
        }
        dbCard.endTransaction();
        return  allCards;
    }

    public void insert(int company, int cardid, int expiredate, int rank){
        dbCard.beginTransaction();
        dbCard.execSQL("insert into CardList(company, cardid, expiredate, rank) values ("+company +"," + cardid + "," + expiredate + "," + rank + ")");
        dbCard.endTransaction();
    }
}
