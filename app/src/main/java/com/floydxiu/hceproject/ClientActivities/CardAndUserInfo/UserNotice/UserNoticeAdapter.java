package com.floydxiu.hceproject.ClientActivities.CardAndUserInfo.UserNotice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.floydxiu.hceproject.DataType.ExpireNotice;
import com.floydxiu.hceproject.R;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/12/19.
 */

public class UserNoticeAdapter extends ArrayAdapter<ExpireNotice> {

    Context context;
    LayoutInflater layoutInflater;

    public UserNoticeAdapter(Context context, int resource, ArrayList<ExpireNotice> objects) {
        super(context, resource, objects);
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ExpireNotice item = getItem(position);

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.listview_notice, null);
        }

        final TextView txvCardInfo = (TextView) convertView.findViewById(R.id.txvCardInfo);
        final TextView txvNotice = (TextView) convertView.findViewById(R.id.txvNotice);

        txvCardInfo.setText(item.card.getComName()+" - "+item.card.getCardNum());
        txvNotice.setText(item.notice);

        return convertView;
    }
}
