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

    public class NFCReader extends AsyncTask<Tag, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Tag... params) {
            IsoDep tag = IsoDep.get(params[0]);
            System.out.println("reading");
            try {
                tag.connect();
                byte[] result = tag.transceive(ApduCommand.GET_TRANSCODE_COMMAND);
                String TransCode = new String(result, Charset.forName("US-ASCII"));
                System.out.println(TransCode);

                APIConnection apiConnection = new APIConnection(CardReaderFragment.this.context);
                Boolean transresponse = apiConnection.TransactionResponse(TransCode);
                if(transresponse){
                    result = tag.transceive(ApduCommand.TRANS_SUCCESS_COMMAND);
                    TransCode = new String(result, Charset.forName("US-ASCII"));
                    System.out.println(TransCode);
                }
                else{
                    result = tag.transceive(ApduCommand.TRANS_SUCCESS_COMMAND);
                    TransCode = new String(result, Charset.forName("US-ASCII"));
                    System.out.println(TransCode);
                }
                return transresponse;

            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean != null){
                if(aBoolean){
                    Toast.makeText(CardReaderFragment.this.context, "Accept", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(CardReaderFragment.this.context, "InValid", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(CardReaderFragment.this.context, "Try Again", Toast.LENGTH_LONG).show();
            }
        }
    }
}
