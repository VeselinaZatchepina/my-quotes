package com.developer.cookie.myquote.quote.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.QuoteDataRepository;
import com.developer.cookie.myquote.database.model.QuoteText;

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

    private static final String QUOTE_CURRENT_CATEGORY_NEW_INSTANCE = "com.developer.cookie.myquote.quote_current_id_new_instance_aqccf";
    private static final String QUOTE_TYPE_NEW_INSTANCE = "com.developer.cookie.myquote.quote_type_new_instance_aqccf";
    private static final String QUOTE_SEARCH_BUNDLE_AQCCF = "com.developer.cookie.myquote.quote_search_bundle_aqccf";

    View mRootView;
    String mCategoryName;
    QuoteDataRepository mQuoteDataRepository;
    ArrayList<Long> mListOfQuotesId;
    List<String> mCurrentCategoryQuoteTextList;

    AllQuoteCurrentCategoryRecyclerViewAdapter mRecyclerViewAdapter;

    RealmResults<QuoteText> mQuoteTexts;

    String mQuoteType;
    String mSortedBy;
    long mCurrentId;

    RecyclerView mRecyclerView;

    private AllQuoteCurrentCategoryCallbacks mCallbacks;

    SearchView mSearchView;
    String mSearchText;

    public AllQuoteCurrentCategoryFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            setDataFromSavedInstanceState(savedInstanceState);
        } else if (getArguments() != null) {
            mCategoryName = getArguments().getString(QUOTE_CURRENT_CATEGORY_NEW_INSTANCE);
            mQuoteType = getArguments().getString(QUOTE_TYPE_NEW_INSTANCE);
        }
        mQuoteDataRepository = new QuoteDataRepository();
        mQuoteTexts = mQuoteDataRepository.getListOfQuoteTextByCategory(mCategoryName, mQuoteType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        TextView currentCategory = (TextView) mRootView.findViewById(R.id.current_category);
        currentCategory.setText(mCategoryName);
        currentCategory.setAllCaps(true);

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
                for (int i = 0; i < element.size(); i++) {
                    mListOfQuotesId.add(element.get(i).getId());
                }
                if (isAdded()) {
                    if (getActivity().findViewById(R.id.detail_fragment_container) != null) {
                        mCallbacks.onQuoteSelected(mListOfQuotesId, element.first().getId());
                    }
                }
                if (mSortedBy != null) {
                    sortAndUpdateRecyclerView(mSortedBy);
                }
            }
        });

        return mRootView;
    }

    private void setDataFromSavedInstanceState(Bundle savedInstanceState) {
        mQuoteType = savedInstanceState.getString(QUOTE_TYPE_BUNDLE_AQCCF);
        mCategoryName = savedInstanceState.getString(QUOTE_CATEGORY_BUNDLE_AQCCF);
        mSortedBy = savedInstanceState.getString(QUOTE_SORTED_BUNDLE_AQCCF);
        mSearchText = savedInstanceState.getString(QUOTE_SEARCH_BUNDLE_AQCCF);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_current_all_quotes, menu);
        if (mQuoteType.equals("My quote")) {
            menu.findItem(R.id.filter_quote).setVisible(false);
        }
        MenuItem search = menu.findItem(R.id.search_quote);
        mSearchView = (SearchView) MenuItemCompat.getActionView(search);
        search(mSearchView);
        //focus the SearchView
        if (mSearchText != null && !mSearchText.isEmpty()) {
            search.expandActionView();
            mSearchView.setQuery(mSearchText, true);
            mSearchView.clearFocus();
        }
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

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mRecyclerViewAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
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
        inflater.inflate(R.menu.menu_popup_all_quotes, popup.getMenu());
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

    public static AllQuoteCurrentCategoryFragment newInstance(String quoteCategory, String quoteType) {
        Bundle args = new Bundle();
        args.putString(QUOTE_CURRENT_CATEGORY_NEW_INSTANCE, quoteCategory);
        args.putString(QUOTE_TYPE_NEW_INSTANCE, quoteType);
        AllQuoteCurrentCategoryFragment fragment = new AllQuoteCurrentCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUOTE_TYPE_BUNDLE_AQCCF, mQuoteType);
        outState.putString(QUOTE_CATEGORY_BUNDLE_AQCCF, mCategoryName);
        outState.putString(QUOTE_SORTED_BUNDLE_AQCCF, mSortedBy);
        mSearchText = mSearchView.getQuery().toString();
        outState.putString(QUOTE_SEARCH_BUNDLE_AQCCF, mSearchText);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (AllQuoteCurrentCategoryCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
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
            extends RecyclerView.Adapter<AllQuoteCurrentCategoryRecyclerViewAdapter.MyViewHolder>
            implements Filterable {
        private List<String> currentCategoryQuoteList;
        private List<String> filteredQuoteTextList;

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
                mCallbacks.onQuoteSelected(mListOfQuotesId, mCurrentId);
            }

            @Override
            public boolean onLongClick(View view) {
                mCurrentId = mListOfQuotesId.get(mCurrentCategoryQuoteTextList
                        .indexOf(currentQuote.getText().toString()));
                openDeleteQuoteDialog();
                return false;
            }
        }

        //For searchView
        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        filteredQuoteTextList = currentCategoryQuoteList;
                    } else {
                        List<String> filteredList = new ArrayList<>();
                        for (String currentQuoteText : currentCategoryQuoteList) {
                            if (currentQuoteText.toLowerCase().contains(charString)) {
                                filteredList.add(currentQuoteText);
                            }
                        }
                        filteredQuoteTextList = filteredList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredQuoteTextList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    filteredQuoteTextList = (ArrayList<String>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
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
                                    mQuoteDataRepository.deleteQuoteTextObjectById(mCurrentId, mQuoteType);
                                    final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
                                    Snackbar snackbarIsDeleted = Snackbar.make(coordinatorLayout, "Quote is deleted!", Snackbar.LENGTH_LONG);
                                    snackbarIsDeleted.show();
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

        /**
         * Method create custom adapter.
         *
         * @param listOfQuoteText
         */
        public AllQuoteCurrentCategoryRecyclerViewAdapter(List<String> listOfQuoteText) {
            currentCategoryQuoteList = listOfQuoteText;
            filteredQuoteTextList = listOfQuoteText;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.all_quote_current_category_recycler_view_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.currentQuote.setText(filteredQuoteTextList.get(position));
        }

        @Override
        public int getItemCount() {
            return filteredQuoteTextList.size();
        }

        /**
         * When adapter data is changed this method helps set new data for adapter.
         *
         * @param listOfQuoteText
         */
        public void changeDate(List<String> listOfQuoteText) {
            filteredQuoteTextList = listOfQuoteText;
            notifyDataSetChanged();
        }
    }

    public interface AllQuoteCurrentCategoryCallbacks {
        /**
         * Method add CurrentQuoteFragment as detail fragment
         *
         * @param listOfQuotesId
         * @param currentId
         */
        void onQuoteSelected(ArrayList<Long> listOfQuotesId, Long currentId);
    }
}