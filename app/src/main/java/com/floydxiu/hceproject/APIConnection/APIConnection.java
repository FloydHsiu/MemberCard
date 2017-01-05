package com.floydxiu.hceproject.APIConnection;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.floydxiu.hceproject.DBHelper.CardDBHelper;
import com.floydxiu.hceproject.DBHelper.CompanyDBHelper;
import com.floydxiu.hceproject.DataType.LoginStateSPManager;
import com.floydxiu.hceproject.UserCertificateActivities.UserCertificateActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Floyd on 2016/10/2.
 */

public class APIConnection {

    Context context;
    LoginStateSPManager loginStateSPManager;
    /* !!!!!!!!!!!! */
    public static String SERVER_CLIENT_ADDR = "http://218.161.0.65/HCEprojectAPI/Client/";
    public static String SERVER_ADMIN_ADDR = "http://218.161.0.65/HCEprojectAPI/Admin/";
    public static String SERVER_ADDR = "http://218.161.0.65/HCEprojectAPI/Controller/";
    private static String LOGIN_ADDR = SERVER_ADDR + "AccountController.php?OPTION=LOGIN";
    private static String CREATE_ACCOUNT_ADDR = SERVER_ADDR + "AccountController.php?OPTION=CREATE";
    private static String GET_CARDLSIT_BY_ACCOUNT = SERVER_ADDR + "CardController.php?OPTION=SELECTBYACCOUNT";
    private static String GET_COMPANYLIST = SERVER_ADDR + "CompanyController.php?OPTION=SELECTALL";
    private static String ADD_CARD_CLIENT = SERVER_ADDR + "CardController.php?OPTION=ADD";
    private static String TRANSACTION_STEP0 = SERVER_ADDR + "TransactionController.php?OPTION=STEP0";
    private static String TRANSACTION_START = SERVER_ADDR + "TransactionController.php?OPTION=START";

    public APIConnection(Context context){
        this.context = context;
        this.loginStateSPManager = new LoginStateSPManager(this.context);
    }

    /**  Account API **/

    public boolean login(String acc, String pwd){
        try {
            URL url = new URL(LOGIN_ADDR);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(5000);
            httpconn.setReadTimeout(5000);
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);
            httpconn.setRequestMethod("POST");
                    /* !!!!!!!!!!! */
            String request_data = "ACCID=" + acc + "&PWD=" + pwd;
            OutputStream os = httpconn.getOutputStream();
            os.write(request_data.getBytes());
            os.close();
            httpconn.connect();

            //Get information of response header
            Map<String, List<String>> header = httpconn.getHeaderFields();
            List<String> sessions = header.get("Set-Cookie");

            //Get Response data
            InputStream is = httpconn.getInputStream();
            String response_data = readAll(is);
            JSONObject responseJSON = new JSONObject(response_data);
                    /* !!!!!!!!!!! */
            boolean loginState = responseJSON.getBoolean("STATE");

            System.out.println("***** Session GET : " + sessions.get(0));

            //Keep Session in Preference
            if (loginState== true){
                loginStateSPManager.setLoginState(true);
                loginStateSPManager.setSession(sessions.get(0));
                System.out.println("***** LOGIN SUCCESS");
                return true;
            }
            else{
                loginStateSPManager.setLoginState(false);
                loginStateSPManager.setSession("");
                Log.i("Account Login", responseJSON.getString("ERROR"));
                return  false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createAccount(String acc, String pwd)
    {
        class createAccountTask extends AsyncTask<String, Void, Boolean>{
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    URL url = new URL(CREATE_ACCOUNT_ADDR);
                    HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                    httpconn.setConnectTimeout(5000);
                    httpconn.setReadTimeout(5000);
                    httpconn.setDoInput(true);
                    httpconn.setDoOutput(true);
                    httpconn.setRequestMethod("POST");
                    /* !!!!!!!!!!! */
                    String request_data = "ACC=" + params[0] + "&PWD=" + params[1] + "&EMAIL=" + params[2];
                    OutputStream os = httpconn.getOutputStream();
                    os.write(request_data.getBytes());
                    os.close();
                    httpconn.connect();

                    //Get Response data
                    InputStream is = httpconn.getInputStream();
                    String response_data = readAll(is);
                    JSONObject responseJSON = new JSONObject(response_data);
                    /* !!!!!!!!!!! */
                    boolean createAccountState = responseJSON.getBoolean("STATE");
                    if( createAccountState == false) Log.i("Create Account", responseJSON.getString("ERROR"));
                    return createAccountState;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if(aBoolean == true){
                    Intent intent = new Intent();
                    intent.setClass(context, UserCertificateActivity.class);
                    context.startActivity(intent);
                    Toast.makeText(context, "Create Account Success!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context, "This Account Id Has Existed", Toast.LENGTH_LONG).show();
                }
            }
        }
        createAccountTask createAccountTask = new createAccountTask();
        createAccountTask.execute(acc, pwd);
    }

    public void logout(){
        loginStateSPManager.setSession("");
        loginStateSPManager.setLoginState(false);
        CardDBHelper cardDBHelper = new CardDBHelper(this.context);
        cardDBHelper.deleteAll();
    }

    public boolean[] checkSessionIsLogin(){
        if(checkNetworkState()) {
            try {
                URL url = new URL(SERVER_CLIENT_ADDR +"valid.php");
                HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                httpconn.setConnectTimeout(3000);
                httpconn.setReadTimeout(3000);
                httpconn.setDoInput(true);
                httpconn.setDoOutput(true);
                httpconn.setRequestMethod("GET");

                String session = loginStateSPManager.getSession();
                httpconn.setRequestProperty("Cookie", session);

                httpconn.connect();

                //Get Response data
                InputStream is = httpconn.getInputStream();
                String response_data = readAll(is);
                JSONObject responseJSON = new JSONObject(response_data);

                boolean[] result = new boolean[2];
                try{
                    result[0] = responseJSON.getBoolean("isLogin");
                    result[1] = responseJSON.getBoolean("isAdmin");
                }catch (Exception e){
                    e.printStackTrace();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**  CardList API **/

    public boolean addNewCard(String companyid, String num, String phone, String idcard){
        try {
            URL url = new URL(ADD_CARD_CLIENT);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(5000);
            httpconn.setReadTimeout(5000);
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);
            httpconn.setRequestMethod("POST");

            LoginStateSPManager loginStateSPManager = new LoginStateSPManager(this.context);
            String session = loginStateSPManager.getSession();
            httpconn.setRequestProperty("Cookie", session);

            /* !!!!!!!!!!! */
            String request_data = "COMPANYID" + companyid + "&NUM=" + num + "&PHONE=" + phone + "&IDCARD=" + idcard;
            OutputStream os = httpconn.getOutputStream();
            os.write(request_data.getBytes());
            os.close();

            httpconn.connect();

            //Get Response data
            InputStream is = httpconn.getInputStream();
            String response_data = readAll(is);
            JSONObject responseJSON = new JSONObject(response_data);

            Boolean state = responseJSON.getBoolean("STATE");

            if( state == false ) Log.i("Add Card", responseJSON.getString("ERROR"));

            return state;

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public JSONArray getCardList() {

        try {
            URL url = new URL(GET_CARDLSIT_BY_ACCOUNT);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(5000);
            httpconn.setReadTimeout(5000);
            httpconn.setDoInput(true);
            httpconn.setDoOutput(false);
            httpconn.setRequestMethod("GET");

            LoginStateSPManager loginStateSPManager = new LoginStateSPManager(this.context);
            String session = loginStateSPManager.getSession();
            httpconn.setRequestProperty("Cookie", session);

            httpconn.connect();

            //Get Response data
            InputStream is = httpconn.getInputStream();
            String response_data = readAll(is);
            JSONObject responseJSON = new JSONObject(response_data);

            Boolean state = responseJSON.getBoolean("STATE");

            if( state ){
                return responseJSON.getJSONArray("CARDS");
            }
            else{
                return null;
            }

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** Company List **/

    public boolean getCompanyList(){
        try {
            URL url = new URL(GET_COMPANYLIST);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(5000);
            httpconn.setReadTimeout(5000);
            httpconn.setDoInput(true);
            httpconn.setDoOutput(false);
            httpconn.setRequestMethod("GET");

            LoginStateSPManager loginStateSPManager = new LoginStateSPManager(this.context);
            String session = loginStateSPManager.getSession();
            httpconn.setRequestProperty("Cookie", session);

            httpconn.connect();

            //Get Response data
            InputStream is = httpconn.getInputStream();
            String response_data = readAll(is);
            JSONObject responseJSON = new JSONObject(response_data);

            Boolean state = responseJSON.getBoolean("STATE");

            if( state ){
                CompanyDBHelper companyDBHelper = new CompanyDBHelper(this.context);
                JSONArray companys = responseJSON.getJSONArray("COMPANY");

                for(int i=0; i<companys.length(); i++){
                    JSONObject company = companys.getJSONObject(i);
                    companyDBHelper.insertCompany( company.getInt("ID"), company.getString("NAME"), company.getString(("ICON")));
                }
                return true;
            }


        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Transaction **/

    public boolean TransactionSTEP0(String TransCode) throws IOException{
        try {
            URL url = new URL(TRANSACTION_STEP0);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(5000);
            httpconn.setReadTimeout(5000);
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);
            httpconn.setRequestMethod("POST");

            LoginStateSPManager loginStateSPManager = new LoginStateSPManager(this.context);
            String session = loginStateSPManager.getSession();
            httpconn.setRequestProperty("Cookie", session);

            String request_data = "TransCode=" + TransCode;
            OutputStream os = httpconn.getOutputStream();
            os.write(request_data.getBytes());
            os.close();

            httpconn.connect();

            //Get Response data
            InputStream is = httpconn.getInputStream();
            String response_data = readAll(is);
            JSONObject responseJSON = new JSONObject(response_data);

            if ("success".equals(responseJSON.getString("state"))) {
                return true;
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return false;
    }

    public String TransactionStart(int ComId, int CardNum) throws IOException{
        try{
            URL url = new URL(TRANSACTION_START);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(1500);
            httpconn.setReadTimeout(1500);
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);
            httpconn.setRequestMethod("POST");

            LoginStateSPManager loginStateSPManager = new LoginStateSPManager(this.context);
            String session = loginStateSPManager.getSession();
            httpconn.setRequestProperty("Cookie", session);

            String request_data = "ID=" + ComId + "&CardNum=" + CardNum;
            OutputStream os = httpconn.getOutputStream();
            os.write(request_data.getBytes());
            os.close();

            httpconn.connect();

            //Get Response data
            InputStream is = httpconn.getInputStream();
            String response_data = readAll(is);
            JSONObject responseJSON = new JSONObject(response_data);

            if("success".equals(responseJSON.getString("state"))){
                return responseJSON.getString("transcode");
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    //Use to check "if the device has connect to network."
    public boolean checkNetworkState(){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else{
            return false;
        }
    }

    private void setHTTPConnection(HttpURLConnection httpconn, String method, Boolean input, Boolean output){
        httpconn.setConnectTimeout(1500);
        httpconn.setReadTimeout(1500);
        httpconn.setDoInput(input);
        httpconn.setDoOutput(output);
        try {
            httpconn.setRequestMethod(method);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

    //read all the data in the InputStream
    private String readAll(InputStream stream) throws IOException, UnsupportedEncodingException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = stream.read(buffer)) != -1){
            bos.write(buffer,0,len);
        }
        stream.close();
        return new String(bos.toByteArray(), "UTF-8");
    }
}
