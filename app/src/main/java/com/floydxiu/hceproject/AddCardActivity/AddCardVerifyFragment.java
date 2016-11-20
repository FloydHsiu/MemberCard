package com.floydxiu.hceproject.AddCardActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoActivity;
import com.floydxiu.hceproject.DBHelper.CompanyDBHelper;
import com.floydxiu.hceproject.R;

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

        CompanyDBHelper companyDBHelper = new CompanyDBHelper(this.context);
        String selectedComName = companyDBHelper.queryComName(addCardActivity.ComId);
        txvCompanyNameSelected.setText(selectedComName);

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

                AddCardTask addCardTask = new AddCardTask(AddCardVerifyFragment.this.context);
                addCardTask.execute(""+addCardActivity.ComId, CardNum, Phonenumber, NationId);
            }
        });


        return v;
    }

    class AddCardTask extends AsyncTask<String, Void, Boolean>{
        Context context;

        AddCardTask(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            APIConnection apiConnection = new APIConnection(this.context);
            return apiConnection.addNewCard(params[0], params[1], params[2], params[3]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean == true){
                Toast.makeText( this.context, "Success to add your card", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(this.context, CardAndUserInfoActivity.class);
                this.context.startActivity(intent);
                ((AddCardActivity)this.context).finish();
            }
            else{
                Toast.makeText( this.context, "Fail to add your card", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
