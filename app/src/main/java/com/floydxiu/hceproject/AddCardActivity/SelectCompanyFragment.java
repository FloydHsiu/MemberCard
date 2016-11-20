package com.floydxiu.hceproject.AddCardActivity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.floydxiu.hceproject.DBHelper.CompanyDBHelper;
import com.floydxiu.hceproject.DataType.Company;
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

        CompanyDBHelper companyDBHelper = new CompanyDBHelper(parentActivity);

        ArrayList<Company> list = companyDBHelper.queryAll();

        CompanyListAdapter companyListAdapter = new CompanyListAdapter(this.parentActivity, R.id.lvCompany, list);
        lvCompany.setAdapter(companyListAdapter);

        return v;
    }
}
