package com.floydxiu.hceproject.APIConnection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoActivity;
import com.floydxiu.hceproject.Splash.SplashInitialCheck;

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
    /* !!!!!!!!!!!! */
    public static String SERVER_ADDR = "http://218.161.0.65/HCEprojectAPI/";
    private static String LOGIN_ADDR = SERVER_ADDR + "index.php";
    private static String IS_LOGIN_STATE_ADDR = SERVER_ADDR + "IsLoginState.php";

    public APIConnection(Context context){
        this.context = context;
    }

    public void login(String acc, String pwd){
        class LoginTask extends AsyncTask<String, Void, Boolean>{
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    URL url = new URL(LOGIN_ADDR);
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
                        SharedPreferences sharedPreferences = context.getSharedPreferences(SplashInitialCheck.PreferenceName, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(SplashInitialCheck.PreferenceKey, SplashInitialCheck.LOGIN);
                        editor.putString("Session", sessions.get(0));
                        editor.apply();
                        System.out.println("***** LOGIN SUCCESS");
                        return true;
                    }
                    else{
                        SharedPreferences sharedPreferences = context.getSharedPreferences(SplashInitialCheck.PreferenceName, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(SplashInitialCheck.PreferenceKey, SplashInitialCheck.UN_LOGIN);
                        editor.putString("Session", "");
                        editor.apply();
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

            @Override
            protected void onPostExecute(Boolean s) {
                if(s == true){
                    Intent intent = new Intent();
                    intent.setClass(context, CardAndUserInfoActivity.class);
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context, "Id or Password error!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        LoginTask loginTask = new LoginTask();
        loginTask.execute(acc, pwd);
    }

    public void getUserData(){
        class testTask extends AsyncTask<Void, Void, String>{
            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL(SERVER_ADDR+"valid.php");
                    HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                    httpconn.setConnectTimeout(1500);
                    httpconn.setReadTimeout(1500);
                    httpconn.setDoInput(true);
                    httpconn.setDoOutput(true);
                    httpconn.setRequestMethod("GET");

                    SharedPreferences sharedPreferences = context.getSharedPreferences(SplashInitialCheck.PreferenceName, Context.MODE_PRIVATE);
                    String session = sharedPreferences.getString("Session", "");
                    httpconn.setRequestProperty("Cookie", session);

                    httpconn.connect();

                    //Get Response data
                    InputStream is = httpconn.getInputStream();
                    String response_data = readAll(is);

                    return  response_data;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "GGGGG";
            }
            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(context, s, Toast.LENGTH_LONG);
                System.out.println("***** ID: " + s);
            }
        }
        testTask test = new testTask();
        test.execute();
    }

    public void logout(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SplashInitialCheck.PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SplashInitialCheck.PreferenceKey, SplashInitialCheck.UN_LOGIN);
        editor.putString("Session", "");
        editor.apply();
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
