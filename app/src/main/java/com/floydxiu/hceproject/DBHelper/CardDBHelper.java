package com.floydxiu.hceproject.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.floydxiu.hceproject.DataType.Card;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/12/13.
 */

public class CardDBHelper extends SQLiteOpenHelper{
    // 資料庫名稱
    public static final String DATABASE_NAME = "Card.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;

    public static String TableName = "Card";
    public static String ComId = "ComId";
    public static String CardNum = "CardNum";
    public static String CardType = "CardType";
    public static String ExpireTime = "ExpireTime";
    public static String CardLevel = "CardLevel";
    public static String IsReadyToExpire = "IsReadyToExpire";

    public CardDBHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Card(" +
                "ComId INT NOT NULL," +
                "CardNum INT NOT NULL," +
                "CardType VARCHAR(10) NOT NULL," +
                "ExpireTime VARCHAR(20) NOT NULL," +
                "CardLevel VARCHAR(20) NOT NULL," +
                "IsReadyToExpire VARCHAR(1) NOT NULL," +
                "PRIMARY KEY (ComId, CardNum) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //判斷傳入的卡片資訊是否已經存在在手機的SQLite資料庫中
    public boolean isCardInDB(int ComId, int CardNum){
        SQLiteDatabase CardDB = this.getReadableDatabase();
        String sql = "SELECT * FROM Card WHERE ComId="+ComId+" AND CardNum="+CardNum;
        Cursor queryResult = CardDB.rawQuery(sql, null);

        CardDB.close();

        if(queryResult.getCount() > 0) return true;
        else return false;
    }

    public void delete(int ComId, int CardNum){
        SQLiteDatabase CardDB = this.getWritableDatabase();
        //String sql = "SELECT * FROM Card WHERE ((ComId, CardNum) IN (("+ComId+","+CardNum+")))";
        CardDB.delete(TableName, "ComId="+ComId+" AND CardNum="+CardNum, null);

        CardDB.close();
    }

    public void update(int ComId, int CardNum, ContentValues values){
        SQLiteDatabase CardDB = this.getWritableDatabase();
        CardDB.update(TableName, values, "ComId="+ComId+" AND CardNum="+CardNum, null);
        CardDB.close();
    }

    public ArrayList<Card> queryAll(){
        SQLiteDatabase CardDB = this.getReadableDatabase();
        ArrayList<Card> list = new ArrayList<>();
        Cursor queryResult = CardDB.rawQuery("SELECT * FROM Card WHERE 1", null);
        queryResult.moveToFirst();
        while(!queryResult.isAfterLast()){
            Card temp = new Card(queryResult.getInt(0), "", queryResult.getInt(1), queryResult.getString(2), queryResult.getString(3), queryResult.getString(4));
            list.add(temp);
            queryResult.moveToNext();
        }
        CardDB.close();
        return list;
    }

    public void insert(ContentValues values){
        SQLiteDatabase CardDb = this.getWritableDatabase();
        CardDb.insert(TableName, null, values);
        CardDb.close();
    }

    public void deleteAll(){
        SQLiteDatabase CardDb = this.getWritableDatabase();
        CardDb.delete(TableName, "1", null);
        CardDb.close();
    }

    public void syncCardDB(ArrayList<Card> local, ArrayList<Card> cloud){
        //建立一個array紀錄cloud選單同步的情形
        boolean[] IsCloudSync = new boolean[cloud.size()];
        for(int i=0; i<IsCloudSync.length; i++){
            IsCloudSync[i] = false;
        }
        //Search for element to DELETE
        for(int i=0; i<local.size(); i++){
            Card item = local.get(i);
            int positionInArrayList = item.searchInArrayList(cloud);
            if(positionInArrayList == -1){//在cloud選單中沒有此張卡片，所以在資料庫中將此卡刪除
                delete(item.getComId(), item.getCardNum());
                Log.i("SyncCardDB", "Delete");
            }
            else{//確認資料是否和遠端的一樣
                Card cloudItem = cloud.get(positionInArrayList);
                if(item.isNeedUpdate(cloudItem)){
                    ContentValues values = new ContentValues();
                    values.put(this.ExpireTime, cloudItem.getExpireTime());
                    values.put(this.CardType, cloudItem.getCardType());
                    values.put(this.CardLevel, cloudItem.getCardLevel());
                    update(cloudItem.getComId(), cloudItem.getCardNum(), values);
                    Log.i("SyncCardDB", "Update");
                }
                IsCloudSync[positionInArrayList] = true;
            }
        }
        //Search for element to INSERT
        for(int i=0; i<cloud.size(); i++){
            Card item = cloud.get(i);
            if(!IsCloudSync[i]){//如果尚未同步，則新增到資料庫中
                ContentValues values = new ContentValues();
                values.put(ComId, item.getComId());
                values.put(CardNum, item.getCardNum());
                values.put(CardType, item.getCardType());
                values.put(ExpireTime, item.getExpireTime());
                values.put(CardLevel, item.getCardLevel());
                values.put(IsReadyToExpire, false);
                insert(values);
                Log.i("SyncCardDB", "Insert");
            }
        }
    }

}
