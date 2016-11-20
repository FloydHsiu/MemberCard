package com.floydxiu.hceproject.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.floydxiu.hceproject.DataType.Company;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/11/20.
 */

public class CompanyDBHelper extends SQLiteOpenHelper {
    // 資料庫名稱
    public static final String DATABASE_NAME = "Company.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;

    public static String TableName = "Company";
    public static String ComId = "ComId";
    public static String ComName = "ComName";

    public CompanyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        //dbCard = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Company(" +
                "ComId INT PRIMARY KEY NOT NULL," +
                "ComName VARCHAR(40) NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String queryComName(int ComId){
        SQLiteDatabase companydb = this.getReadableDatabase();
        Cursor queryResult = companydb.rawQuery("SELECT ComName FROM Company WHERE ComId=" + ComId, null);
        queryResult.moveToFirst();
        String result = queryResult.getString(0);
        companydb.close();
        return result;
    }

    public ArrayList<Company> queryAll(){
        ArrayList<Company> list = new ArrayList<>();
        SQLiteDatabase companydb = this.getReadableDatabase();
        Cursor queryResult = companydb.rawQuery("SELECT * FROM Company WHERE 1", null);
        queryResult.moveToFirst();
        while(!queryResult.isAfterLast()){
            list.add(new Company(queryResult.getInt(0), queryResult.getString(1)));
            queryResult.moveToNext();
        }
        companydb.close();
        return list;
    }

    private boolean isComIdExist(int ComId){
        SQLiteDatabase companydb = this.getReadableDatabase();
        Cursor queryResult = companydb.rawQuery("SELECT * FROM Company WHERE ComId="+ComId, null);
        return (queryResult.getCount() > 0);
    }

    public void insertCom(int ComId, String ComName){
        SQLiteDatabase companydb = this.getReadableDatabase();
        if(!isComIdExist(ComId)){
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.ComId, ComId);
            contentValues.put(this.ComName, ComName);
            companydb.insertOrThrow(this.TableName, null, contentValues);
        }
        companydb.close();
    }
}
