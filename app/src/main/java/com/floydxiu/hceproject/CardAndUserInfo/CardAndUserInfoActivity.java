package com.floydxiu.hceproject.CardAndUserInfo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoFragment.CardListFragment;
import com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoFragment.UserInfoFragment;
import com.floydxiu.hceproject.R;

/**
 * Created by Floyd on 2016/9/10.
 * The Main part of the app.
 * Display all cards of user, and user can maintain user information.
 */

public class CardAndUserInfoActivity extends AppCompatActivity {

    public enum CardAndUserInfoEnum{ CardList, UserInfo}
    public static CardAndUserInfoEnum FragmentChoice = CardAndUserInfoEnum.CardList;//Params for save the state of fragment of this activity

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Toolbar toolbarCardAndUserInfo;
    GridLayout containerCardAndUserInfo;
    Button btnCardList, btnUserInfo;
//    FragmentTabHost tabhostCardAndUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardanduserinfo);

        setToolbars();
//        setFragmentTabHost();
        containerCardAndUserInfo = (GridLayout) findViewById(R.id.containerCardAndUserInfo);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerCardAndUserInfo, new CardListFragment());
        fragmentTransaction.commit();

        btnCardList = (Button) findViewById(R.id.btnCardList);
        btnUserInfo = (Button) findViewById(R.id.btnUserInfo);

        btnCardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FragmentChoice == CardAndUserInfoEnum.UserInfo){
                    FragmentChoice = CardAndUserInfoEnum.CardList;
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerCardAndUserInfo, new CardListFragment());
                    fragmentTransaction.commit();

                    setToolbars();
                }
            }
        });

        btnUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FragmentChoice == CardAndUserInfoEnum.CardList){
                    FragmentChoice = CardAndUserInfoEnum.UserInfo;
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerCardAndUserInfo, new UserInfoFragment());
                    fragmentTransaction.commit();

                    setToolbars();
                }
            }
        });

    }

    private void setToolbars(){

        //Toolbar setting
        toolbarCardAndUserInfo = (Toolbar) findViewById(R.id.toolbarCardAndUserInfo);
        toolbarCardAndUserInfo.setTitle(R.string.app_name);
        //Clear Menu
        toolbarCardAndUserInfo.getMenu().clear();
        if(FragmentChoice == CardAndUserInfoEnum.CardList){
            toolbarCardAndUserInfo.inflateMenu(R.menu.menu_cardlist);
            //Attention to reference of app:showAsAction in menu xml, it can't be android:showAsAction QQ
        }
        else if(FragmentChoice == CardAndUserInfoEnum.UserInfo){
            toolbarCardAndUserInfo.inflateMenu(R.menu.menu_userinfo);
        }
        //bind action on the item in menu
        toolbarCardAndUserInfo.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.itemCardListAdd:
                        //do something
                        break;
                }
                return true;
            }
        });
    }

//    private void setFragmentTabHost(){
//        tabhostCardAndUserInfo = (FragmentTabHost) findViewById(R.id.tabhostCardAndUserInfo);
//
////        fragmentManager = getFragmentManager();
////        fragmentTransaction = fragmentManager.beginTransaction();
////
////        CardListFragment cardListFragment = new CardListFragment();
////        fragmentTransaction.add
////        fragmentTransaction.commit();
//        tabhostCardAndUserInfo.setup(this, getSupportFragmentManager(), R.id.tabcontent);
//        Bundle bundle = new Bundle();
//        bundle.putString("value", "Card");
//        tabhostCardAndUserInfo.addTab(tabhostCardAndUserInfo.newTabSpec("Card").setIndicator("Card"), CardListFragment.class, bundle);
//        bundle = new Bundle();
//        bundle.putString("value", "UserInfo");
//        tabhostCardAndUserInfo.addTab(tabhostCardAndUserInfo.newTabSpec("UserInfo").setIndicator("UserInfo"), UserInfoFragment.class, bundle);
//    }
}
