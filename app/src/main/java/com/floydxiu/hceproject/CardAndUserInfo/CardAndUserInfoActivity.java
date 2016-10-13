package com.floydxiu.hceproject.CardAndUserInfo;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoFragment.CardListFragment;
import com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoFragment.UserInfoFragment;
import com.floydxiu.hceproject.R;

import java.util.ArrayList;

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

    MenuItem itemCardListAdd;

    AlertDialog dialogAdd;
    NavigationView navUser;
    DrawerLayout drawerCardAndUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardanduserinfo);
        setToolbars();

        //setAddDialog();
//        setFragmentTabHost();
        containerCardAndUserInfo = (GridLayout) findViewById(R.id.containerCardAndUserInfo);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerCardAndUserInfo, new CardListFragment());
        fragmentTransaction.commit();

        //Set up navigation drawer
        drawerCardAndUserInfo = (DrawerLayout) findViewById(R.id.drawerCardAndUserInfo);
        navUser = (NavigationView) findViewById(R.id.navUser);
        setupDrawerContent(navUser);
        drawerCardAndUserInfo.closeDrawers();

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
                        setAddDialog();
                        Toast.makeText(getApplicationContext(), "Add Cards", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void setAddDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View linearLayout = inflater.inflate(R.layout.dialog_add, null);
        Spinner spinnerAddCompany = (Spinner) linearLayout.findViewById(R.id.spinnerAddCompany);
        EditText edtAddCardId = (EditText) linearLayout.findViewById(R.id.edtAddCardId);
        Button btnAddConfirm = (Button) linearLayout.findViewById(R.id.btnAddConfirm);
        Button btnAddCancel = (Button) linearLayout.findViewById(R.id.btnAddCancel);

        //Set Spinner Adapter
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CardAndUserInfoActivity.this, android.R.layout.simple_spinner_item, list);
        spinnerAddCompany.setAdapter(arrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(CardAndUserInfoActivity.this);
        builder.setView(linearLayout);
        builder.create().show();
    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener()
                {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        menuItem.setChecked(true);
                        drawerCardAndUserInfo.closeDrawers();
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
