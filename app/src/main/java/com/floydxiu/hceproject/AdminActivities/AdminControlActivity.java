package com.floydxiu.hceproject.AdminActivities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.floydxiu.hceproject.R;

/**
 * Created by Floyd on 2016/11/23.
 */

public class AdminControlActivity extends AppCompatActivity {

    LinearLayout FragmentAdminControl;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincontrol);

        fragmentManager = getFragmentManager();

        FragmentAdminControl = (LinearLayout) findViewById(R.id.FragmentAdminControl);
        CardReaderFragment cardReaderFragment = new CardReaderFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentAdminControl, cardReaderFragment);
        fragmentTransaction.commit();
    }


}
