package com.floydxiu.hceproject.UserCertificate;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.floydxiu.hceproject.R;
import com.floydxiu.hceproject.UserCertificate.UserCertificateFragment.LoginFragment;
import com.floydxiu.hceproject.UserCertificate.UserCertificateFragment.SignUpFragment;

/**
 * Created by Floyd on 2016/8/19.
 * This Class is the center of the UserCertificate page.
 * This Class will use two fragment to complete "Sign up" and "Login" function
 */

public class UserCertificateActivity extends AppCompatActivity {
    LoginFragment loginFragment = new LoginFragment();
    SignUpFragment signUpFragment = new SignUpFragment();
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Button btnSignup, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercertificate);

        //Initialize fragment state
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layoutUserCertificateFragment, loginFragment, "LOGIN");
        fragmentTransaction.commit();


        //Button binding
        btnSignup = (Button)findViewById(R.id.btnSignup);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment tempfragment = fragmentManager.findFragmentByTag("SIGNUP");
                if(tempfragment == null || !tempfragment.isVisible()) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.layoutUserCertificateFragment, signUpFragment, "SIGNUP");
                    fragmentTransaction.commit();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment tempfragment = fragmentManager.findFragmentByTag("LOGIN");
                if(tempfragment == null || !tempfragment.isVisible()) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.layoutUserCertificateFragment, loginFragment, "LOGIN");
                    fragmentTransaction.commit();
                }
            }
        });

    }
}
