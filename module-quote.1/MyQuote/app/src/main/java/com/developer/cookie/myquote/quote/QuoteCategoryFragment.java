package com.developer.cookie.myquote.quote;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.cookie.myquote.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuoteCategoryFragment extends Fragment {


    public QuoteCategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_quote_category, container, false);

        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container, new AddQuoteFragment()).commit();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp, getActivity().getTheme()));
                } else {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
                }
            }
        });

        return rootView;
    }

}
