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

    public static String TableName = "COMPANY";
    public static String ID = "ID";
    public static String NAME = "NAME";
    public static String ICON = "ICON";

    public CompanyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        //dbCard = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS COMPANY(" +
                "    ID INT NOT NULL," +
                "    NAME VARCHAR(20) NOT NULL," +
                "    ICON VARCHAR(25) NOT NULL," +
                "    PRIMARY KEY(ID));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String queryCompanyName(int id){
        SQLiteDatabase companydb = this.getReadableDatabase();
        Cursor queryResult = companydb.rawQuery("SELECT NAME FROM COMPANY WHERE ID=" + id, null);
        queryResult.moveToFirst();
        String result = queryResult.getString(0);
        companydb.close();
        return result;
    }

    public ArrayList<Company> queryAll(){
        ArrayList<Company> list = new ArrayList<>();
        SQLiteDatabase companydb = this.getReadableDatabase();
        Cursor queryResult = companydb.rawQuery("SELECT * FROM COMPANY WHERE 1", null);
        queryResult.moveToFirst();
        while(!queryResult.isAfterLast()){
            list.add(new Company(queryResult.getInt(0), queryResult.getString(1)));
            queryResult.moveToNext();
        }
        companydb.close();
        return list;
    }

    private boolean isCompanyIdExist(int id){
        SQLiteDatabase companydb = this.getReadableDatabase();
        Cursor queryResult = companydb.rawQuery("SELECT * FROM COMPANY WHERE ID="+id, null);
        return (queryResult.getCount() > 0);
    }

    public void insertCompany(int id, String name, String icon){
        SQLiteDatabase companydb = this.getReadableDatabase();
        if(!isCompanyIdExist(id)){
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.ID, id);
            contentValues.put(this.NAME, name);
            contentValues.put(this.ICON, icon);
            companydb.insertOrThrow(this.TableName, null, contentValues);
        }
        companydb.close();
    }
}
