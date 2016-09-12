package com.floydxiu.hceproject.CardAndUserInfo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.floydxiu.hceproject.R;

/**
 * Created by Floyd on 2016/9/10.
 * The Main part of the app.
 * Display all cards of user, and user can maintain user information.
 */

public class CardAndUserInfoActivity extends AppCompatActivity {

    public enum CardAndUserInfoEnum{ CardList, UserInfo}
    public static CardAndUserInfoEnum FragmentChoice = CardAndUserInfoEnum.CardList;//Params for save the state of fragment of this activity

    Toolbar toolbarCardAndUserInfo;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_cardanduserinfo);
        setToolbars();
    }

    private void setToolbars(){

        //Toolbar setting
        toolbarCardAndUserInfo = (Toolbar) findViewById(R.id.toolbarCardAndUserInfo);
        toolbarCardAndUserInfo.setTitle(R.string.app_name);
        //Set Menu item
        toolbarCardAndUserInfo.inflateMenu(R.menu.menu_cardlist);
        //Clear Menu
        toolbarCardAndUserInfo.getMenu().clear();
        if(FragmentChoice == CardAndUserInfoEnum.CardList){
            toolbarCardAndUserInfo.inflateMenu(R.menu.menu_cardlist);
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
