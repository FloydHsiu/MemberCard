package com.floydxiu.hceproject.ClientActivities.CardAndUserInfo.CardList;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.ClientActivities.CardAndUserInfo.CardAndUserInfoFragment.CardListFragment;
import com.floydxiu.hceproject.DBHelper.CardDBHelper;
import com.floydxiu.hceproject.DBHelper.CompanyDBHelper;
import com.floydxiu.hceproject.DataType.Card;
import com.floydxiu.hceproject.DataType.CardListSPManager;
import com.floydxiu.hceproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/11/2.
 */

public class CardListSync {
    private Context context;

    public CardListSync(Context context){
        this.context = context;
    }

    private class getCardListTask extends AsyncTask<Void, Void, ArrayList<Card>>{
        ProgressDialog mDialog;
//        @Override
//        protected void onPreExecute() {
//            mDialog = new ProgressDialog(CardListSync.this.context);
//            mDialog.setMessage("Loading...");
//            mDialog.setCancelable(false);
//            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            mDialog.show();
//        }

        @Override
        protected ArrayList<Card> doInBackground(Void... params) {
            //download list
            APIConnection apiConnection = new APIConnection(CardListSync.this.context);
            JSONArray CardList = apiConnection.getCardList();
            Boolean isGetCompanyList = apiConnection.getCompanyList();
            CompanyDBHelper companyDBHelper = new CompanyDBHelper(CardListSync.this.context);

            CardDBHelper cardDBHelper = new CardDBHelper(CardListSync.this.context);
            ArrayList<Card> local = cardDBHelper.queryAll();

            //trans jsonarray to arraylist
            CardListSPManager cardListSPManager = new CardListSPManager(CardListSync.this.context);
            cardListSPManager.setCardList_JSON(CardList.toString());

            //將雲端獲得的JSON轉乘list
            ArrayList<Card> list = new ArrayList<>();
            if(CardList != null){
                for(int i=0; i< CardList.length(); i++){
                    try {
                        JSONObject temp = CardList.getJSONObject(i);
                        list.add(new Card(
                                temp.getInt("ID"),
                                companyDBHelper.queryCompanyName(temp.getInt("ID")),
                                temp.getInt("CardNum"),
                                temp.getString("CardType"),
                                temp.getString("ExpireTime"),
                                temp.getString("CardLevel")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //將本地與遠端同步
                cardDBHelper.syncCardDB(local, list);
                local = cardDBHelper.queryAll();
                for(int i=0; i<local.size(); i++){
                    Card item = local.get(i);
                    item.setComName(companyDBHelper.queryCompanyName(item.getComId()));
                }
            }
            else{
                local = cardDBHelper.queryAll();
                for(int i=0; i<local.size(); i++){
                    Card item = local.get(i);
                    item.setComName(companyDBHelper.queryCompanyName(item.getComId()));
                }
            }

            return local;
        }

        @Override
        protected void onPostExecute(ArrayList<Card> list) {
            AppCompatActivity activity = (AppCompatActivity)CardListSync.this.context;
            FragmentManager fragmentManager = activity.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            CardListFragment cardListFragment = new CardListFragment();
            cardListFragment.setCardList(list);
            fragmentTransaction.replace(R.id.containerCardAndUserInfo, cardListFragment);
            fragmentTransaction.commit();
        }
    }

    public void download(){
        getCardListTask getCardListTask = new getCardListTask();
        getCardListTask.execute();
    }


}
