package com.floydxiu.hceproject.APIConnection;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
    public static String SERVER_CLIENT_ADDR = "http://118.165.146.233/HCEprojectAPI/Client/";
    public static String SERVER_ADMIN_ADDR = "http://118.165.146.233/HCEprojectAPI/Admin/";
    private static String LOGIN_ADDR = SERVER_CLIENT_ADDR + "login.php";
    private static String IS_LOGIN_STATE_ADDR = SERVER_CLIENT_ADDR + "IsLoginState.php";
    private static String CREATE_ACCOUNT_ADDR = SERVER_CLIENT_ADDR + "createAccount.php";
    private static String GET_CARDLSIT = SERVER_CLIENT_ADDR + "getCardList.php";
    private static String GET_COMPANYLIST = SERVER_CLIENT_ADDR + "getCompanyList.php";
    private static String Add_CARD_CLIENT = SERVER_CLIENT_ADDR + "addCard.php";
    private static String TRANSACTION_RESPONSE = SERVER_ADMIN_ADDR + "TransactionResponse.php";
    private static String TRANSACTION_REQUEST = SERVER_CLIENT_ADDR + "TransactionRequest.php";
    private static String CLIENT_TRANSACTION_CONFIRM = SERVER_CLIENT_ADDR + "TransactionConfirm.php";

    public APIConnection(Context context){
        this.context = context;
        this.loginStateSPManager = new LoginStateSPManager(this.context);
    }

    /**  Account API **/

    public boolean login(String acc, String pwd){
        try {
            URL url = new URL(LOGIN_ADDR);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(1500);
            httpconn.setReadTimeout(1500);
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);
            httpconn.setRequestMethod("POST");
                    /* !!!!!!!!!!! */
            String request_data = "ACC=" + acc + "&PWD=" + pwd;
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
            boolean loginState = responseJSON.getBoolean("valid");

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
                    httpconn.setConnectTimeout(1500);
                    httpconn.setReadTimeout(1500);
                    httpconn.setDoInput(true);
                    httpconn.setDoOutput(true);
                    httpconn.setRequestMethod("POST");
                    /* !!!!!!!!!!! */
                    String request_data = "ACC=" + params[0] + "&PWD=" + params[1];
                    OutputStream os = httpconn.getOutputStream();
                    os.write(request_data.getBytes());
                    os.close();
                    httpconn.connect();

                    //Get Response data
                    InputStream is = httpconn.getInputStream();
                    String response_data = readAll(is);
                    JSONObject responseJSON = new JSONObject(response_data);
                    /* !!!!!!!!!!! */
                    boolean createAccountState = responseJSON.getBoolean("valid");
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

    public boolean addNewCard(String ComId, String CardNum, String Phone, String NationId){
        try {
            URL url = new URL(Add_CARD_CLIENT);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(1500);
            httpconn.setReadTimeout(1500);
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);
            httpconn.setRequestMethod("POST");

            LoginStateSPManager loginStateSPManager = new LoginStateSPManager(this.context);
            String session = loginStateSPManager.getSession();
            httpconn.setRequestProperty("Cookie", session);

            /* !!!!!!!!!!! */
            String request_data = "ComId=" + ComId + "&CardNum=" + CardNum + "&Phone=" + Phone + "&NationId=" + NationId;
            OutputStream os = httpconn.getOutputStream();
            os.write(request_data.getBytes());
            os.close();

            httpconn.connect();

            //Get Response data
            InputStream is = httpconn.getInputStream();
            String response_data = readAll(is);
            JSONObject responseJSON = new JSONObject(response_data);

            String state = responseJSON.getString("state");

            if("success".equals(state)){
                return true;
            }
            else if("no this card".equals(state)){
                return false;
            }
            else if("not login".equals(state)){
                return false;
            }
            else if("Wrong card certificate".equals(state)){
                return false;
            }
            else if("Update error".equals(state)){
                return false;
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

    public JSONArray getCardList() {

        try {
            URL url = new URL(GET_CARDLSIT);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(1500);
            httpconn.setReadTimeout(1500);
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

            if("success".equals(responseJSON.getString("state"))){
                return responseJSON.getJSONArray("CardList");
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
            httpconn.setConnectTimeout(1500);
            httpconn.setReadTimeout(1500);
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

            if("success".equals(responseJSON.getString("state"))){
                CompanyDBHelper companyDBHelper = new CompanyDBHelper(this.context);

                JSONArray companylist_array = responseJSON.getJSONArray("CompanyList_array");

                for(int i=0; i < companylist_array.length() ; i++){
                    JSONObject company = companylist_array.getJSONObject(i);
                    companyDBHelper.insertCom(company.getInt(companyDBHelper.ComId), company.getString(companyDBHelper.ComName));
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

    public boolean TransactionResponse(String TransCode) throws IOException{
        try {
            URL url = new URL(TRANSACTION_RESPONSE);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(10000);
            httpconn.setReadTimeout(10000);
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

    public String TransactionRequest(int ComId, int CardNum) throws IOException{
        try{
            URL url = new URL(TRANSACTION_REQUEST);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(1500);
            httpconn.setReadTimeout(1500);
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);
            httpconn.setRequestMethod("POST");

            LoginStateSPManager loginStateSPManager = new LoginStateSPManager(this.context);
            String session = loginStateSPManager.getSession();
            httpconn.setRequestProperty("Cookie", session);

            String request_data = "ComId=" + ComId + "&CardNum=" + CardNum;
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

    public boolean ClientTransactionConfirm(int ComId, int CardNum, String TransCode) throws IOException
    {
        try{
            URL url = new URL(CLIENT_TRANSACTION_CONFIRM);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            httpconn.setConnectTimeout(5000);
            httpconn.setReadTimeout(5000);
            httpconn.setDoInput(true);
            httpconn.setDoOutput(true);
            httpconn.setRequestMethod("POST");

            LoginStateSPManager loginStateSPManager = new LoginStateSPManager(this.context);
            String session = loginStateSPManager.getSession();
            httpconn.setRequestProperty("Cookie", session);

            String request_data = "ComId=" + ComId + "&CardNum=" + CardNum + "&TransCode=" + TransCode;
            OutputStream os = httpconn.getOutputStream();
            os.write(request_data.getBytes());
            os.close();

            httpconn.connect();

            //Get Response data
            InputStream is = httpconn.getInputStream();
            String response_data = readAll(is);
            JSONObject responseJSON = new JSONObject(response_data);

            return responseJSON.getBoolean("STATE");
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return false;
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
