package com.floydxiu.hceproject.CardAndUserInfo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridLayout;

import com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoFragment.CardListFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardanduserinfo);

        setToolbars();
        containerCardAndUserInfo = (GridLayout) findViewById(R.id.containerCardAndUserInfo);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        CardListFragment cardListFragment = new CardListFragment();
        fragmentTransaction.replace(R.id.containerCardAndUserInfo, cardListFragment);
        fragmentTransaction.commit();

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
}
