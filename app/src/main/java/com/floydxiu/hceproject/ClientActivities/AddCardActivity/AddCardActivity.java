package com.floydxiu.hceproject.ClientActivities.AddCardActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.floydxiu.hceproject.R;

/**
 * Created by Floyd on 2016/10/31.
 */

public class AddCardActivity extends AppCompatActivity {

    FrameLayout framelayoutAddCard;
    Toolbar toolbarAddCard;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public int ComId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcard);
        setToolbarAddCard();

        //chose Company First
        fragmentManager = this.getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        SelectCompanyFragment selectCompanyFragment = new SelectCompanyFragment();
        //selectCompanyFragment.setParentActivity(AddCardActivity.this);
        fragmentTransaction.replace(R.id.framelayoutAddCard, selectCompanyFragment);
        fragmentTransaction.commit();


    }

    public void setToolbarAddCard(){
        toolbarAddCard = (Toolbar) findViewById(R.id.toolbarAddCard);
        toolbarAddCard.setTitle(R.string.choseCardCompany);
        toolbarAddCard.setTitleTextColor(Color.parseColor("#ffffff"));
    }

    public void changeToolbarAddCardTitle(String title){
        toolbarAddCard.setTitle(title);
        toolbarAddCard.setTitleTextColor(Color.parseColor("#ffffff"));
    }
}
