package com.floydxiu.hceproject.AddCardActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floydxiu.hceproject.CardAndUserInfo.CardList.CardListSync;
import com.floydxiu.hceproject.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Floyd on 2016/11/4.
 */

public class AddCardVerifyFragment extends Fragment {

    Context context;

    Button btnAddCardConfirm, btnAddCardCancel;
    EditText edtFragmentNationId, edtFragmentPhoneNumber, edtFragmentCardNumber;

    LinearLayout linearlayoutCompanySelected;
    TextView txvCompanyNameSelected;
    ImageView imgCompanySelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();

        final AddCardActivity addCardActivity = (AddCardActivity) context;

        View v = inflater.inflate(R.layout.fragment_addcardverify, container, false);
        btnAddCardConfirm = (Button) v.findViewById(R.id.btnAddCardConfirm);
        btnAddCardCancel = (Button) v.findViewById(R.id.btnAddCardCancel);
        edtFragmentCardNumber = (EditText) v.findViewById(R.id.edtFragmentCardNumber);
        edtFragmentNationId = (EditText) v.findViewById(R.id.edtFragmentNationId);
        edtFragmentPhoneNumber = (EditText) v.findViewById(R.id.edtFragmentPhoneNumber);
        linearlayoutCompanySelected = (LinearLayout) v.findViewById(R.id.linearlayoutCompanySelected);
        txvCompanyNameSelected = (TextView) v.findViewById(R.id.txvCompanyNameSelected);
        imgCompanySelected = (ImageView) v.findViewById(R.id.imgCompanySelected);

        SharedPreferences sharedPreferences = context.getSharedPreferences(CardListSync.PreferenceName, Context.MODE_PRIVATE);
        try {
            JSONObject CompanyList = new JSONObject(sharedPreferences.getString("CompanyList", "{}")).getJSONObject("CompanyList");
            //selected company

            String selectedComName = CompanyList.getString(""+addCardActivity.ComId);
            txvCompanyNameSelected.setText(selectedComName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        linearlayoutCompanySelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = addCardActivity.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayoutAddCard, new SelectCompanyFragment());
                fragmentTransaction.commit();
                addCardActivity.changeToolbarAddCardTitle(addCardActivity.getResources().getString(R.string.choseCardCompany));
            }
        });

        btnAddCardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCardActivity.finish();
            }
        });

        btnAddCardConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CardNum = edtFragmentCardNumber.getText().toString();
                String Phonenumber = edtFragmentPhoneNumber.getText().toString();
                String NationId = edtFragmentNationId.getText().toString();
            }
        });


        return v;
    }
}
