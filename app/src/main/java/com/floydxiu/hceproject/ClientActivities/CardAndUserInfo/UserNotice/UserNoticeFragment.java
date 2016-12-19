package com.floydxiu.hceproject.ClientActivities.CardAndUserInfo.UserNotice;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.floydxiu.hceproject.DBHelper.CardDBHelper;
import com.floydxiu.hceproject.DataType.ExpireNotice;
import com.floydxiu.hceproject.R;

import java.util.ArrayList;

/**
 * Created by Floyd on 2016/12/19.
 */

public class UserNoticeFragment extends Fragment {

    ListView lvNotice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_usernotice, container, false);

        lvNotice = (ListView) v.findViewById(R.id.lvNotice);
        CardDBHelper cardDBHelper = new CardDBHelper(getActivity());
        ArrayList<ExpireNotice> list = cardDBHelper.noticeCardExpire();
        UserNoticeAdapter adapter = new UserNoticeAdapter(getActivity(), R.id.lvNotice, list);
        lvNotice.setAdapter(adapter);

        return v;
    }
}
