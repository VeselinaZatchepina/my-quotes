package com.developer.cookie.myquote.idea.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.cookie.myquote.R;

public class GetIdeaFragment extends Fragment {


    public GetIdeaFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_get_idea, container, false);
    }

}
