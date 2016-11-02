package com.floydxiu.hceproject.AddCardActivity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.floydxiu.hceproject.R;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/10/31.
 */

public class SelectCompanyFragment extends Fragment{
    Context parentActivity;

    ListView lvCompany;

    public void setParentActivity(Context parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selectcompany, container, false);

        lvCompany = (ListView) v.findViewById(R.id.lvCompany);
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        lvCompany.setAdapter(arrayAdapter);

        return v;
    }
}
