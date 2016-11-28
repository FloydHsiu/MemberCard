package com.floydxiu.hceproject.AdminActivities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.R;
import com.floydxiu.hceproject.UserCertificateActivities.UserCertificateActivity;

/**
 * Created by Floyd on 2016/11/23.
 */

public class AdminControlActivity extends AppCompatActivity {

    LinearLayout FragmentAdminControl;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Toolbar toolbarAdminControl;
    DrawerLayout drawerAdminControl;
    NavigationView navUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincontrol);

        setToolbars();
        setDrawerAdminControl();
        fragmentManager = getFragmentManager();

        FragmentAdminControl = (LinearLayout) findViewById(R.id.FragmentAdminControl);
        CardReaderFragment cardReaderFragment = new CardReaderFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentAdminControl, cardReaderFragment);
        fragmentTransaction.commit();
    }

    private void setToolbars(){

        //Toolbar setting
        toolbarAdminControl = (Toolbar) findViewById(R.id.toolbarAdminControl);
        drawerAdminControl = (DrawerLayout) findViewById(R.id.drawerAdminControl);
        toolbarAdminControl.setTitle("Admin Control Center");
        toolbarAdminControl.setTitleTextColor(Color.WHITE);
        //Clear Menu
        toolbarAdminControl.getMenu().clear();

        toolbarAdminControl.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbarAdminControl.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerAdminControl.isDrawerOpen(Gravity.LEFT)){
                    drawerAdminControl.closeDrawer(Gravity.LEFT);
                }
                else{
                    drawerAdminControl.openDrawer(Gravity.LEFT);
                }
            }
        });
    }

    private void setDrawerAdminControl(){
        //Set up navigation drawer
        drawerAdminControl = (DrawerLayout) findViewById(R.id.drawerAdminControl);
        navUser = (NavigationView) findViewById(R.id.navUser);
        navUser.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_logout:
                        APIConnection apiConnection = new APIConnection(AdminControlActivity.this);
                        apiConnection.logout();
                        Intent intent = new Intent();
                        intent.setClass(AdminControlActivity.this, UserCertificateActivity.class);
                        startActivity(intent);
                        AdminControlActivity.this.finish();
                }
                return true;
            }
        });
    }
}
