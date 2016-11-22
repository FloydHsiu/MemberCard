package com.floydxiu.hceproject.ClientActivities.AddCardActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.floydxiu.hceproject.DataType.Company;
import com.floydxiu.hceproject.R;

import java.util.List;

/**
 * Created by Floyd on 2016/11/4.
 */

public class CompanyListAdapter extends ArrayAdapter<Company> {

    LayoutInflater layoutInflater;

    Context context;

    public CompanyListAdapter(Context context, int resource, List<Company> objects) {
        super(context, resource, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Company item = getItem(position);

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.listview_company, null);
        }

        final TextView lv_txvComName = (TextView) convertView.findViewById(R.id.lv_txvComName);
        lv_txvComName.setText(item.getComName());

        convertView.setClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //keep cardid

                AddCardActivity addCardActivity = (AddCardActivity)CompanyListAdapter.this.context;
                addCardActivity.ComId = item.getComId();

                //change fragment
                FragmentManager fragmentManager = ((AddCardActivity) CompanyListAdapter.this.context).getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayoutAddCard, new AddCardVerifyFragment());
                fragmentTransaction.commit();

                //change title
                addCardActivity.changeToolbarAddCardTitle(addCardActivity.getResources().getString(R.string.inputcardverifyinfo));
            }
        });

        return convertView;
    }
}
