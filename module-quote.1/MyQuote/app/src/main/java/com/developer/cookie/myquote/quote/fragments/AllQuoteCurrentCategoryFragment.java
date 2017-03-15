package com.developer.cookie.myquote.quote.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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
    private static final String QUOTE_TYPE = "quote type";
    private static final String QUOTE_CATEGORY = "quote category";

    View mRootView;
    String mCategoryName;
    QuoteDataRepository mQuoteDataRepository;
    ArrayList<Long> mListOfQuotesId;
    List<String> mCurrentCategoryQuoteTextList;

    AllQuoteCurrentCategoryRecyclerViewAdapter mRecyclerViewAdapter;

    RealmResults<QuoteText> mQuoteTexts;

    String mQuoteType;

    long mCurrentId;

    AllQuoteCurrentCategoryActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            if (savedInstanceState != null) {
                mQuoteType = savedInstanceState.getString(QUOTE_TYPE);
                mCategoryName = savedInstanceState.getString(QUOTE_CATEGORY);
            } else {
                // Get current clicked category name
                mCategoryName = mActivity
                        .getIntent()
                        .getSerializableExtra(AllQuoteCurrentCategoryActivity.CATEGORY_NAME)
                        .toString();
                mQuoteType = mActivity.getTitle().toString();
            }
            mQuoteDataRepository = new QuoteDataRepository();
            mQuoteTexts = mQuoteDataRepository.getListOfQuoteTextByCategory(mCategoryName, mQuoteType);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUOTE_TYPE, mQuoteType);
        outState.putString(QUOTE_CATEGORY, mCategoryName);
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
                mCurrentCategoryQuoteTextList = new ArrayList<String>();
                for (int i = 0; i < element.size(); i++) {
                    mCurrentCategoryQuoteTextList.add(element.get(i).getQuoteText());
                }
                RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
                registerForContextMenu(recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerViewAdapter = new AllQuoteCurrentCategoryRecyclerViewAdapter(mCurrentCategoryQuoteTextList);
                recyclerView.setAdapter(mRecyclerViewAdapter);
                // Create list of quote's id for data transfer to another fragment
                mListOfQuotesId = new ArrayList<Long>();
                for(int i = 0; i < element.size(); i++) {
                    mListOfQuotesId.add(element.get(i).getId());
                }
            }
        });
        return mRootView;
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