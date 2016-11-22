package com.floydxiu.hceproject.UserCertificateActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.R;

/**
 * Created by Floyd on 2016/10/14.
 */

public class CreateAccountActivity extends AppCompatActivity {
    EditText edtCreateAccountId, edtCreateAccountPwd, edtCreateAccountCheckPwd;
    Button btnCreateAccount;
    TextView txvHaveAccountSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        edtCreateAccountId = (EditText) findViewById(R.id.edtCreateAccountId);
        edtCreateAccountPwd = (EditText) findViewById(R.id.edtCreateAccountPwd);
        edtCreateAccountCheckPwd = (EditText) findViewById(R.id.edtCreateAccountCheckPwd);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        txvHaveAccountSignIn = (TextView) findViewById(R.id.txvHaveAccountSignIn);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkinput = checkInput();
                if(checkinput == 2){
                    String id, pwd, checkpwd;
                    id = edtCreateAccountId.getText().toString();
                    pwd = edtCreateAccountPwd.getText().toString();

                    APIConnection apiConnection = new APIConnection(CreateAccountActivity.this);
                    if(apiConnection.checkNetworkState()){
                        apiConnection.createAccount(id, pwd);
                    }
                }
                else if(checkinput == 1){
                    Toast.makeText(CreateAccountActivity.this, "Make sure your password is same!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CreateAccountActivity.this, "Some of black is Empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txvHaveAccountSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CreateAccountActivity.this, UserCertificateActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private int checkInput(){
        String id, pwd, checkpwd;
        id = edtCreateAccountId.getText().toString();
        pwd = edtCreateAccountPwd.getText().toString();
        checkpwd = edtCreateAccountCheckPwd.getText().toString();

        if(!id.equals("") && !pwd.equals("") && !checkpwd.equals("")){
            if(pwd.equals(checkpwd)){
                return 2;
            }
            else{
                return 1;
            }
        }
        return 0;
    }
}
