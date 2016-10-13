package com.floydxiu.hceproject.UserCertificate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.R;

/**
 * Created by Floyd on 2016/8/19.
 * This Class is the center of the UserCertificate page.
 * This Class will use two fragment to complete "Sign up" and "Login" function
 */

public class UserCertificateActivity extends AppCompatActivity {

    Button btnLogin;

    TextView edtFragmentLoginAccount, edtFragmentLoginPwd;

    Intent CardAndUserInfoIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercertificate);

        //Button binding
        btnLogin = (Button)findViewById(R.id.btnLogin);
        edtFragmentLoginAccount = (TextView) findViewById(R.id.edtFragmentLoginAccount);
        edtFragmentLoginPwd = (TextView) findViewById(R.id.edtFragmentLoginPwd);

        final APIConnection apiConnection = new APIConnection(UserCertificateActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(apiConnection.checkNetworkState()){
                    apiConnection.login(edtFragmentLoginAccount.getText().toString(), edtFragmentLoginPwd.getText().toString());
                }
            }
        });

    }
}
