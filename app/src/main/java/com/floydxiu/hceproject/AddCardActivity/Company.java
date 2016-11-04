package com.floydxiu.hceproject.AddCardActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/11/4.
 */

public class Company {
    private String ComName;
    private int ComId;

    public Company(int ComId, String ComName){
        this.ComId = ComId;
        this.ComName = ComName;
    }

    public String getComName(){ return this.ComName; }
    public int getComId(){ return this.ComId; }

    public static ArrayList<Company> parseCompanyList(String CompanyListString){
        ArrayList<Company> CompanyList = new ArrayList<>();

        try {
            JSONObject CompanyListJSON = new JSONObject(CompanyListString);
            JSONArray CompanyListJSONArray = CompanyListJSON.getJSONArray("CompanyList_array");
            for(int i=0; i< CompanyListJSONArray.length(); i++){
                JSONObject temp = CompanyListJSONArray.getJSONObject(i);
                CompanyList.add(new Company(temp.getInt("ComId"), temp.getString("ComName")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return CompanyList;
    }
}
