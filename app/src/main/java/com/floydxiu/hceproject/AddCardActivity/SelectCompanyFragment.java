package com.floydxiu.hceproject.AddCardActivity;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.floydxiu.hceproject.CardAndUserInfo.CardList.CardListSync;
import com.floydxiu.hceproject.R;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/10/31.
 */

public class SelectCompanyFragment extends Fragment{
    Context parentActivity;

    ListView lvCompany;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.parentActivity = getActivity();
        View v = inflater.inflate(R.layout.fragment_selectcompany, container, false);

        lvCompany = (ListView) v.findViewById(R.id.lvCompany);

        SharedPreferences sharedPreferences = parentActivity.getSharedPreferences(CardListSync.PreferenceName, Context.MODE_PRIVATE);
        String CompanyListString = sharedPreferences.getString("CompanyList", "{}");

        ArrayList<Company> list = Company.parseCompanyList(CompanyListString);

        CompanyListAdapter companyListAdapter = new CompanyListAdapter(this.parentActivity, R.id.lvCompany, list);
        lvCompany.setAdapter(companyListAdapter);

        return v;
    }
}
