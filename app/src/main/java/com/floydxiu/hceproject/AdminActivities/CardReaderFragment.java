package com.floydxiu.hceproject.AdminActivities;

import android.app.Fragment;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.DataType.ApduCommand;
import com.floydxiu.hceproject.R;
import com.floydxiu.hceproject.Services.APDUservice;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Floyd on 2016/11/23.
 */

public class CardReaderFragment extends Fragment implements NfcAdapter.ReaderCallback {
    Context context;
    NfcAdapter nfcAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        View v = inflater.inflate(R.layout.fragment_cardreader, container, false);
        //Enable App As a Reader
        nfcAdapter = NfcAdapter.getDefaultAdapter(this.context);
        nfcAdapter.enableReaderMode((AppCompatActivity)this.context, this, NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK | NfcAdapter.FLAG_READER_NFC_A, null);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcAdapter.disableReaderMode((AppCompatActivity)this.context);
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcAdapter.enableReaderMode((AppCompatActivity)this.context, this, NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK | NfcAdapter.FLAG_READER_NFC_A, null);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        NFCReader nfcReader = new NFCReader();
        nfcReader.execute(tag);
    }

    public class NFCReader extends AsyncTask<Tag, Void, Integer> {
        private final int RESULT_STATE_APDU_NONACTIVE = 0;
        private final int RESULT_STATE_UNKNOWN_CMD = 1;
        private final int RESULT_STATE_TRANS_ACCEPT = 2;
        private final int RESULT_STATE_TRANS_INVALID = 3;
        private final int RESULT_STATE_NETWORK_ERROR = 4;
        private final int RESULT_STATE_APDU_CONN_ERROR = 5;

        @Override
        protected Integer doInBackground(Tag... params) {
            IsoDep tag = IsoDep.get(params[0]);
            System.out.println("reading");
            try {
                tag.connect();
                byte[] result = tag.transceive(ApduCommand.GET_TRANSCODE_COMMAND);
                String TransCode = new String(result, Charset.forName("US-ASCII"));
                if(TransCode.equals(APDUservice.APDU_RESPONSE_NONACTIVE)){
                    return RESULT_STATE_APDU_NONACTIVE;
                }
                else if(TransCode.equals(APDUservice.APDU_RESPONSE_UNKNOWN_CMD)){
                    return RESULT_STATE_UNKNOWN_CMD;
                }
                else{
                    APIConnection apiConnection = new APIConnection(CardReaderFragment.this.context);
                    try{
                        Boolean transresponse = apiConnection.TransactionSTEP0(TransCode);
                        if(transresponse){
                            return RESULT_STATE_TRANS_ACCEPT;
                        }
                        else{
                            return RESULT_STATE_TRANS_INVALID;
                        }
                    }catch (IOException e){
                        return RESULT_STATE_NETWORK_ERROR;
                    }
                }
            } catch (IOException e) {
                return RESULT_STATE_APDU_CONN_ERROR;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result){
                case RESULT_STATE_TRANS_ACCEPT:
                    Toast.makeText(context, "Accept", Toast.LENGTH_LONG).show();
                    break;
                case RESULT_STATE_TRANS_INVALID:
                    Toast.makeText(context, "InValid", Toast.LENGTH_LONG).show();
                    break;
                case RESULT_STATE_APDU_CONN_ERROR:
                    Toast.makeText(context, "Card Disconnect", Toast.LENGTH_LONG).show();
                    break;
                case RESULT_STATE_NETWORK_ERROR:
                    Toast.makeText(context, "NetWork Error", Toast.LENGTH_LONG).show();
                    break;
                default:

            }
        }
    }
}
