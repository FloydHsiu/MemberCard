package com.floydxiu.hceproject.UserCertificate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.floydxiu.hceproject.R;

/**
 * Created by Floyd on 2016/8/19.
 * This Class is the center of the UserCertificate page.
 * This Class will use two fragment to complete "Sign up" and "Login" function
 */

public class UserCertificateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercertificate);
    }
}
