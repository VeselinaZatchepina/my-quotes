package com.developer.cookie.myquote.quote.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.QuoteDataRepository;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.utils.FillViewsWithCurrentQuoteDataHelper;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * CurrentQuoteFragment is used for display information about quote.
 */
public class CurrentQuoteFragment extends Fragment {
    private static final String LOG_TAG = CurrentQuoteFragment.class.getSimpleName();
    private static final String CURRENT_QUOTE_ID_ARG_CQF = "com.developer.cookie.myquote.current_quote_id_arg_cqf";
    public static final String QUOTE_TYPE_BUNDLE_CQF = "com.developer.cookie.myquote.quote_type_bundle_cqf";
    public static final String QUOTE_ID_BUNDLE_CQF = "com.developer.cookie.myquote.quote_id_for_save_f";
    Long mCurrentQuoteTextId;
    QuoteDataRepository mQuoteDataRepository;
    RealmResults<QuoteText> mCurrentQuoteObjectList;
    String mCurrentCategory;
    String mQuoteType;
    ShareActionProvider mShareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mQuoteType = savedInstanceState.getString(QUOTE_TYPE_BUNDLE_CQF);
            mCurrentQuoteTextId = savedInstanceState.getLong(QUOTE_ID_BUNDLE_CQF);
            Log.v(LOG_TAG, mQuoteType);
        } else {
            mQuoteType = getActivity().getTitle().toString();
            mCurrentQuoteTextId = getArguments().getLong(CURRENT_QUOTE_ID_ARG_CQF);
        }
        mQuoteDataRepository = new QuoteDataRepository();
        mCurrentQuoteObjectList = mQuoteDataRepository.getQuoteTextObjectsByQuoteId(mCurrentQuoteTextId);
        Log.v(LOG_TAG, mQuoteType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.current_quote_fragment, container, false);
        final TextView quoteTextView = (TextView) rootView.findViewById(R.id.current_quote_text);
        final TextView bookNameView = (TextView) rootView.findViewById(R.id.current_book_name);
        final TextView authorNameView = (TextView) rootView.findViewById(R.id.current_author_name);
        final TextView pageNumberView = (TextView) rootView.findViewById(R.id.current_page_number);
        final TextView publisherNameTextView = (TextView) rootView.findViewById(R.id.current_publisher_name);
        final TextView yearNumberView = (TextView) rootView.findViewById(R.id.current_year_number);

        mCurrentQuoteObjectList.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
            @Override
            public void onChange(RealmResults<QuoteText> element) {
                if (element.size() > 0) {
                    mCurrentCategory = element.first().getCategory().getCategory();
                    FillViewsWithCurrentQuoteDataHelper.fillViewsWithCurrentQuoteData(element, quoteTextView,
                            bookNameView, authorNameView, pageNumberView, publisherNameTextView, yearNumberView, mQuoteType);
                }
            }
        });
        return rootView;
    }

    public static CurrentQuoteFragment newInstance(Long currentQuoteTextId) {
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_QUOTE_ID_ARG_CQF, currentQuoteTextId);
        CurrentQuoteFragment fragment = new CurrentQuoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.current_quote_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String quoteTextForShareBody = mCurrentQuoteObjectList.first().getQuoteText();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "It is great quote! Listen!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, quoteTextForShareBody);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(sharingIntent);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_quote:
                mQuoteDataRepository.deleteQuoteTextObjectById(mCurrentQuoteTextId, mQuoteType);
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUOTE_TYPE_BUNDLE_CQF, mQuoteType);
        outState.putLong(QUOTE_ID_BUNDLE_CQF, mCurrentQuoteTextId);
    }

}
