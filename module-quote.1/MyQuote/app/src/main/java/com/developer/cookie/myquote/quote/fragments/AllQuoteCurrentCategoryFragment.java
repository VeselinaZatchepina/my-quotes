package com.developer.cookie.myquote.quote.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
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
import com.developer.cookie.myquote.quote.activities.AllQuoteCurrentCategoryActivity;
import com.developer.cookie.myquote.quote.activities.CurrentQuotePagerActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * AllQuoteCurrentCategoryFragment is used for presentation list of all quotes of current category.
 */
public class AllQuoteCurrentCategoryFragment extends Fragment {
    private static final String LOG_TAG = AllQuoteCurrentCategoryFragment.class.getSimpleName();
    private static final String QUOTE_TYPE_BUNDLE_AQCCF = "com.developer.cookie.myquote.quote_type_bundle_aqccf";
    private static final String QUOTE_CATEGORY_BUNDLE_AQCCF = "com.developer.cookie.myquote.quote_category_bundle_aqccf";
    private static final String QUOTE_SORTED_BUNDLE_AQCCF = "com.developer.cookie.myquote.quote_sorted_bundle_aqccf";

    View mRootView;
    String mCategoryName;
    QuoteDataRepository mQuoteDataRepository;
    ArrayList<Long> mListOfQuotesId;
    List<String> mCurrentCategoryQuoteTextList;

    AllQuoteCurrentCategoryRecyclerViewAdapter mRecyclerViewAdapter;

    RealmResults<QuoteText> mQuoteTexts;

    String mQuoteType;
    String mSortedBy = "date";
    long mCurrentId;

    AllQuoteCurrentCategoryActivity mActivity;

    Fragment currentFragment = this;

    boolean isReload = false;

    RecyclerView mRecyclerView;

    public AllQuoteCurrentCategoryFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
            if (savedInstanceState != null) {
                setDataFromSavedInstanceState(savedInstanceState);
            } else {
                // Get current clicked category name
                mCategoryName = mActivity
                        .getIntent()
                        .getSerializableExtra(AllQuoteCurrentCategoryActivity.QUOTE_CATEGORY_INTENT_AQCCA)
                        .toString();
                mQuoteType = mActivity.getTitle().toString();
            }
            mQuoteDataRepository = new QuoteDataRepository();
            mQuoteTexts = mQuoteDataRepository.getListOfQuoteTextByCategory(mCategoryName, mQuoteType, mSortedBy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.recyclerview_fragment, container, false);
        mQuoteTexts.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
            @Override
            public void onChange(RealmResults<QuoteText> element) {
                if (element.size() == 0 && getActivity() != null) {
                    getActivity().finish();
                }
                mCurrentCategoryQuoteTextList = getListOfQuoteText(element);
                mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
                registerForContextMenu(mRecyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerViewAdapter = new AllQuoteCurrentCategoryRecyclerViewAdapter(mCurrentCategoryQuoteTextList);
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
                // Create list of quote's id for data transfer to another fragment
                mListOfQuotesId = new ArrayList<Long>();
                for(int i = 0; i < element.size(); i++) {
                    mListOfQuotesId.add(element.get(i).getId());
                }
            }
        });
        return mRootView;
    }

    private void setDataFromSavedInstanceState(Bundle savedInstanceState) {
        mQuoteType = savedInstanceState.getString(QUOTE_TYPE_BUNDLE_AQCCF);
        mCategoryName = savedInstanceState.getString(QUOTE_CATEGORY_BUNDLE_AQCCF);
        mSortedBy = savedInstanceState.getString(QUOTE_SORTED_BUNDLE_AQCCF);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.recycler_view) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu_quote_category, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete:
                mQuoteDataRepository.deleteQuoteTextObjectById(mCurrentId, mQuoteType);
                final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
                Snackbar snackbarIsDeleted = Snackbar.make(coordinatorLayout, "Quote is deleted!", Snackbar.LENGTH_LONG);
                snackbarIsDeleted.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_quote:
                ActionMenuItemView view = (ActionMenuItemView) getActivity().findViewById(R.id.filter_quote);
                showPopup(view);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.filter_by_author:
                        mSortedBy = "author";
                        sortAndUpdateRecyclerView(mSortedBy);
                        return true;
                    case R.id.filter_by_book_name:
                        mSortedBy = "bookName.bookName";
                        sortAndUpdateRecyclerView(mSortedBy);
                        return true;
                    case R.id.filter_by_year:
                        mSortedBy = "bookName.year.yearNumber";
                        sortAndUpdateRecyclerView(mSortedBy);
                        return true;
                    case R.id.filter_by_publisher:
                        mSortedBy = "bookName.publisher.publisherName";
                        sortAndUpdateRecyclerView(mSortedBy);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.filter_popup_menu, popup.getMenu());
        popup.show();
    }

    private void sortAndUpdateRecyclerView(String mSortedBy) {
        mQuoteTexts = mQuoteTexts.sort(mSortedBy);
        List<String> quoteTextList = getListOfQuoteText(mQuoteTexts);
        mRecyclerViewAdapter.changeDate(quoteTextList);
    }

    private List<String> getListOfQuoteText(RealmResults<QuoteText> realmResults) {
        List<String> quoteTextList = new ArrayList<String>();
        for (int i = 0; i < realmResults.size(); i++) {
            quoteTextList.add(realmResults.get(i).getQuoteText());
        }
        return quoteTextList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUOTE_TYPE_BUNDLE_AQCCF, mQuoteType);
        outState.putString(QUOTE_CATEGORY_BUNDLE_AQCCF, mCategoryName);
        outState.putString(QUOTE_SORTED_BUNDLE_AQCCF, mSortedBy);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AllQuoteCurrentCategoryActivity) {
            mActivity = (AllQuoteCurrentCategoryActivity) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mQuoteDataRepository.closeDbConnect();
    }

    /**
     * This class is custom adapter for AllQuoteCurrentCategoryFragment.
     * It helps to show list of all quotes of current category.
     */
    private class AllQuoteCurrentCategoryRecyclerViewAdapter
            extends RecyclerView.Adapter<AllQuoteCurrentCategoryRecyclerViewAdapter.MyViewHolder> {
        private List<String> currentCategoryQuoteList;

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView currentQuote;

            MyViewHolder(View container) {
                super(container);
                currentQuote = (TextView) container.findViewById(R.id.current_quote);
                container.setOnClickListener(this);
                container.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                mCurrentId = mListOfQuotesId.get(mCurrentCategoryQuoteTextList
                        .indexOf(currentQuote.getText().toString()));
                Intent intent = CurrentQuotePagerActivity.newIntent(getActivity(),
                        mListOfQuotesId, mCurrentId, mQuoteType);
                startActivity(intent);
            }

            @Override
            public boolean onLongClick(View view) {
                mCurrentId = mListOfQuotesId.get(mCurrentCategoryQuoteTextList
                        .indexOf(currentQuote.getText().toString()));
                getActivity().openContextMenu(view);
                return false;
            }
        }

        /**
         * Method create custom adapter.
         * @param listOfQuoteText
         */
        public AllQuoteCurrentCategoryRecyclerViewAdapter (List<String> listOfQuoteText) {
            currentCategoryQuoteList = listOfQuoteText;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.all_quote_current_category_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.currentQuote.setText(currentCategoryQuoteList.get(position));
        }

        @Override
        public int getItemCount() {
            return currentCategoryQuoteList.size();
        }

        /**
         * When adapter data is changed this method helps set new data for adapter.
         * @param listOfQuoteText
         */
        public void changeDate(List<String> listOfQuoteText) {
            currentCategoryQuoteList = listOfQuoteText;
            notifyDataSetChanged();
        }
    }
}