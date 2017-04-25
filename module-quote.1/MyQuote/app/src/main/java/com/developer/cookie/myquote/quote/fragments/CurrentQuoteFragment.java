package com.developer.cookie.myquote.quote.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    public static final String QUOTE_ID_BUNDLE_CQF = "com.developer.cookie.myquote.quote_id_bundle_cqf";

    Long mCurrentQuoteTextId;
    QuoteDataRepository mQuoteDataRepository;
    RealmResults<QuoteText> mCurrentQuoteObjectList;
    String mCurrentCategory;
    String mQuoteType;
    ShareActionProvider mShareActionProvider;

    Intent sharingIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mQuoteType = savedInstanceState.getString(QUOTE_TYPE_BUNDLE_CQF);
            mCurrentQuoteTextId = savedInstanceState.getLong(QUOTE_ID_BUNDLE_CQF);
        } else {
            mQuoteType = getActivity().getTitle().toString();
            mCurrentQuoteTextId = getArguments().getLong(CURRENT_QUOTE_ID_ARG_CQF);
        }
        mQuoteDataRepository = new QuoteDataRepository();
        mCurrentQuoteObjectList = mQuoteDataRepository.getQuoteTextObjectsByQuoteId(mCurrentQuoteTextId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_current_quote, container, false);
        final TextView quoteTextView = (TextView) rootView.findViewById(R.id.current_quote_text);
        final TextView bookNameView = (TextView) rootView.findViewById(R.id.current_book_name);
        final TextView authorNameView = (TextView) rootView.findViewById(R.id.current_author_name);
        final TextView pageNumberView = (TextView) rootView.findViewById(R.id.current_page_number);
        final TextView publisherNameTextView = (TextView) rootView.findViewById(R.id.current_publisher_name);
        final TextView yearNumberView = (TextView) rootView.findViewById(R.id.current_year_number);
        final TextView quoteCreationDate = (TextView) rootView.findViewById(R.id.quote_creation_date);

        mCurrentQuoteObjectList.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
            @Override
            public void onChange(RealmResults<QuoteText> element) {
                if (element.size() > 0) {
                    mCurrentCategory = element.first().getCategory().getCategory();
                    FillViewsWithCurrentQuoteDataHelper.fillViewsWithCurrentQuoteData(element, quoteTextView,
                            bookNameView, authorNameView, pageNumberView, publisherNameTextView, yearNumberView, mQuoteType);
                    quoteCreationDate.setText(element.first().getDate().getQuoteDate());
                    if (mQuoteType.equals("My quote")) {
                        LinearLayout quoteAuthorTitle = (LinearLayout) rootView.findViewById(R.id.linear_layout_quote_author_title);
                        LinearLayout bookNameTitle = (LinearLayout) rootView.findViewById(R.id.linear_layout_book_name_title);
                        LinearLayout publisherNameTitle = (LinearLayout) rootView.findViewById(R.id.linear_layout_publisher_name_title);
                        LinearLayout yearNumberTitle = (LinearLayout) rootView.findViewById(R.id.linear_layout_year_number_title);
                        LinearLayout pageNumberTitle = (LinearLayout) rootView.findViewById(R.id.linear_layout_page_number_title);
                        quoteAuthorTitle.setVisibility(View.GONE);
                        bookNameTitle.setVisibility(View.GONE);
                        publisherNameTitle.setVisibility(View.GONE);
                        yearNumberTitle.setVisibility(View.GONE);
                        pageNumberTitle.setVisibility(View.GONE);
                    }
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
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_current_quote, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String quoteTextForShareBody = mCurrentQuoteObjectList.first().getQuoteText();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "It is great quote! Listen!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, quoteTextForShareBody);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(sharingIntent);
        }
        if (getActivity().findViewById(R.id.detail_fragment_container) != null) {
            Log.v(LOG_TAG, "DELETE");
            MenuItem itemDeleteQuote = menu.findItem(R.id.delete_quote);
            itemDeleteQuote.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_quote:
                openDeleteQuoteDialog();
                break;
            case R.id.menu_item_share:
                startActivity(Intent.createChooser(sharingIntent, "Select conversation"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDeleteQuoteDialog() {
        // Create dialog for delete current quote
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.dialog_delete, null);
        TextView deleteDialogTitle = (TextView) dialogView.findViewById(R.id.dialog_delete_title);
        deleteDialogTitle.setText(getResources().getString(R.string.dialog_delete_current_quote_title));
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
        mDialogBuilder.setView(dialogView);
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_ok_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mQuoteDataRepository.deleteQuoteTextObjectById(mCurrentQuoteTextId, mQuoteType);
                                getActivity().finish();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.dialog_cancel_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUOTE_TYPE_BUNDLE_CQF, mQuoteType);
        outState.putLong(QUOTE_ID_BUNDLE_CQF, mCurrentQuoteTextId);
    }
}
