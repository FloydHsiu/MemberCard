package com.floydxiu.hceproject.CardAndUserInfo.CardAndUserInfoFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.floydxiu.hceproject.R;

/**
 * Created by Floyd on 2016/9/10.
 */

public class UserInfoFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_userinfo, container, false);
    }
}
