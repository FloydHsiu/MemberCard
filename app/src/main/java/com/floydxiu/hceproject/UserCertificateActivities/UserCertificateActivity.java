package com.floydxiu.hceproject.UserCertificateActivities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.ClientActivities.CardAndUserInfo.CardAndUserInfoActivity;
import com.floydxiu.hceproject.R;

/**
 * Created by Floyd on 2016/8/19.
 * This Class is the center of the UserCertificate page.
 * This Class will use two fragment to complete "Sign up" and "Login" function
 */

public class UserCertificateActivity extends AppCompatActivity {

    Button btnLogin;

    EditText edtFragmentLoginAccount, edtFragmentLoginPwd;

    TextView txvCreateAccount;

    Intent CardAndUserInfoIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercertificate);

        //Button binding
        btnLogin = (Button)findViewById(R.id.btnLogin);
        edtFragmentLoginAccount = (EditText) findViewById(R.id.edtFragmentLoginAccount);
        edtFragmentLoginPwd = (EditText) findViewById(R.id.edtFragmentLoginPwd);
        txvCreateAccount = (TextView) findViewById(R.id.txvCreateAccount);

        final APIConnection apiConnection = new APIConnection(UserCertificateActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(apiConnection.checkNetworkState()){
                    LoginTask loginTask = new LoginTask(UserCertificateActivity.this);
                    loginTask.execute(edtFragmentLoginAccount.getText().toString(), edtFragmentLoginPwd.getText().toString());
                }
            }
        });

        txvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserCertificateActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    class LoginTask extends AsyncTask<String, Void, Boolean> {
        Context context;

        public LoginTask(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            APIConnection apiConnection = new APIConnection(this.context);
            return apiConnection.login(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(s == true){
                Intent intent = new Intent();
                intent.setClass(context, CardAndUserInfoActivity.class);
                context.startActivity(intent);
                ((AppCompatActivity)context).finish();
            }
            else{
                Toast.makeText(context, "Id or Password error!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
