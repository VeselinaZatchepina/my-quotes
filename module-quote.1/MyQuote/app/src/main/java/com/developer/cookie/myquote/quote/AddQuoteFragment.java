package com.developer.cookie.myquote.quote;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.developer.cookie.myquote.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddQuoteFragment extends Fragment {

    private static final String LOG_TAG = AddQuoteFragment.class.getName();


    public AddQuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_add_quote, container, false);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save quote
                EditText quoteText = (EditText) rootView.findViewById(R.id.quote_text);
                EditText bookName = (EditText) rootView.findViewById(R.id.book_name);
                EditText authorName = (EditText) rootView.findViewById(R.id.author_name);
                EditText pageNumber = (EditText) rootView.findViewById(R.id.page_number);
                EditText yearNumber = (EditText) rootView.findViewById(R.id.year_number);
                EditText publishName = (EditText) rootView.findViewById(R.id.publish_name);

                String currentQuoteText = quoteText.getText().toString();
                String currentBookName = bookName.getText().toString();
                String currentAuthorName = authorName.getText().toString();
                String currentPageNumber = pageNumber.getText().toString();
                String currentYearNumber = yearNumber.getText().toString();
                String currentPublishName = publishName.getText().toString();

                Log.v(LOG_TAG, currentQuoteText);

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
