package com.floydxiu.hceproject.AdminActivities;

import android.app.Fragment;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.floydxiu.hceproject.APIConnection.APIConnection;
import com.floydxiu.hceproject.R;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Floyd on 2016/11/23.
 */

public class CardReaderFragment extends Fragment implements NfcAdapter.ReaderCallback {
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        View v = inflater.inflate(R.layout.fragment_cardreader, container, false);
        return v;
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        NFCReader nfcReader = new NFCReader();
        nfcReader.execute(tag);
    }

    private class NFCReader extends AsyncTask<Tag, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Tag... params) {
            byte[] APDU_COMMAND = {
                    (byte)0x00,
                    (byte)0xA4,
                    (byte)0x04,
                    (byte)0x00,
                    (byte)0x07,
                    (byte)0xF0, (byte)0x39, (byte)0x41, (byte)0x48, (byte)0x14, (byte)0x81, (byte)0x00
            };

            byte[] APDU_COMMAND2 = {
                    (byte)0x00,
                    (byte)0xA4,
                    (byte)0x04,
                    (byte)0x00,
                    (byte)0x07,
                    (byte)0xF0, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04, (byte)0x05, (byte)0x06
            };

            IsoDep tag = IsoDep.get(params[0]);
            System.out.println("reading");
            try {
                tag.connect();
                byte[] result = tag.transceive(APDU_COMMAND);
                String TransCode = new String(result, Charset.forName("US-ASCII"));
                System.out.println(TransCode);

                APIConnection apiConnection = new APIConnection(CardReaderFragment.this.context);
                return apiConnection.TransactionResponse(TransCode);

            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if(b){
                Toast.makeText(CardReaderFragment.this.context, "Transaction Success", Toast.LENGTH_LONG);
            }
            else{
                Toast.makeText(CardReaderFragment.this.context, "Error", Toast.LENGTH_LONG);
            }
        }
    }
}
