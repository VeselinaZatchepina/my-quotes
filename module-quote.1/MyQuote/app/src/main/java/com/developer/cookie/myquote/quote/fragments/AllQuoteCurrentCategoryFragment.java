package com.developer.cookie.myquote.quote.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.developer.cookie.myquote.quote.Types;

import io.realm.OrderedRealmCollection;
import io.realm.RealmChangeListener;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * AllQuoteCurrentCategoryFragment is used for presentation list of all quotes of current category.
 */
public class AllQuoteCurrentCategoryFragment extends Fragment {
    private static final String LOG_TAG = AllQuoteCurrentCategoryFragment.class.getSimpleName();
    private static final String QUOTE_TYPE_BUNDLE = "all_quote_current_category_fragment_quote_type_bundle";
    private static final String QUOTE_CATEGORY_BUNDLE = "all_quote_current_category_fragment_quote_category_bundle";
    private static final String QUOTE_SORTED_BUNDLE = "all_quote_current_category_fragment_quote_sorted_bundle";
    private static final String QUOTE_CURRENT_CATEGORY_NEW_INSTANCE = "all_quote_current_category_fragment_quote_current_category_new_instance";
    private static final String QUOTE_TYPE_NEW_INSTANCE = "all_quote_current_category_fragment_quote_type_new_instance";
    private AllQuoteCurrentCategoryCallbacks mCallbacks;
    private String mCategoryName;
    private String mQuoteType;
    private QuoteDataRepository mQuoteDataRepository;
    private RealmResults<QuoteText> mQuoteTexts;
    private AllQuoteRecyclerViewAdapter mRecyclerViewAdapter;
    private String mSortedBy;
    private long mCurrentId;

    public AllQuoteCurrentCategoryFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        defineInputData(savedInstanceState);
        mQuoteDataRepository = new QuoteDataRepository();
        mQuoteTexts = mQuoteDataRepository.getListOfQuotesByCategory(mCategoryName, mQuoteType);
    }

    private void defineInputData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            setDataFromSavedInstanceState(savedInstanceState);
        } else if (getArguments() != null) {
            mCategoryName = getArguments().getString(QUOTE_CURRENT_CATEGORY_NEW_INSTANCE);
            mQuoteType = getArguments().getString(QUOTE_TYPE_NEW_INSTANCE);
        }
    }

    private void setDataFromSavedInstanceState(Bundle savedInstanceState) {
        mQuoteType = savedInstanceState.getString(QUOTE_TYPE_BUNDLE);
        mCategoryName = savedInstanceState.getString(QUOTE_CATEGORY_BUNDLE);
        mSortedBy = savedInstanceState.getString(QUOTE_SORTED_BUNDLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        setCurrentCategoryTitle(rootView);
        defineRecyclerView(rootView);
        if (mSortedBy != null) {
            sortAndUpdateRecyclerView(mSortedBy);
        }
        mQuoteTexts.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
            @Override
            public void onChange(RealmResults<QuoteText> element) {
                if (element.size() == 0 && getActivity() != null) {
                    getActivity().finish();
                }
                setFirstCurrentQuoteForTablet(mQuoteTexts);
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    private void setCurrentCategoryTitle(View rootView) {
        TextView currentCategoryTitle = (TextView) rootView.findViewById(R.id.current_category);
        currentCategoryTitle.setText(mCategoryName);
        currentCategoryTitle.setAllCaps(true);
    }

    private void defineRecyclerView(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewAdapter = new AllQuoteRecyclerViewAdapter(getActivity(), mQuoteTexts, true);
        recyclerView.setAdapter(mRecyclerViewAdapter);
    }

    private void setFirstCurrentQuoteForTablet(RealmResults<QuoteText> realmResults) {
        if (isAdded()) {
            if (getActivity().findViewById(R.id.detail_fragment_container) != null && !realmResults.isEmpty()) {
                mCallbacks.onQuoteSelected(realmResults.first().getId());
            }
        }
    }

    private void sortAndUpdateRecyclerView(String mSortedBy) {
        mQuoteTexts = mQuoteTexts.sort(mSortedBy);
        mRecyclerViewAdapter.changeDate();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_current_all_quotes, menu);
        defineVisibilityFilterMenu(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void defineVisibilityFilterMenu(Menu menu) {
        if (mQuoteType.equals(Types.MY_QUOTE)) {
            menu.findItem(R.id.filter_quote).setVisible(false);
        }
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
                    case R.id.filter_by_book_name:
                        mSortedBy = "bookName.bookName";
                        break;
                    case R.id.filter_by_year:
                        mSortedBy = "bookName.year.yearNumber";
                        break;
                    case R.id.filter_by_publisher:
                        mSortedBy = "bookName.publisher.publisherName";
                        break;
                }
                sortAndUpdateRecyclerView(mSortedBy);
                return true;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_all_quotes, popup.getMenu());
        popup.show();
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
        outState.putString(QUOTE_TYPE_BUNDLE, mQuoteType);
        outState.putString(QUOTE_CATEGORY_BUNDLE, mCategoryName);
        outState.putString(QUOTE_SORTED_BUNDLE, mSortedBy);
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
    private class AllQuoteRecyclerViewAdapter extends RealmRecyclerViewAdapter<QuoteText, AllQuoteRecyclerViewAdapter.MyViewHolder> {
        private OrderedRealmCollection<QuoteText> data;

        public AllQuoteRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<QuoteText> data, boolean autoUpdate) {
            super(context, data, autoUpdate);
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.all_quote_current_category_recycler_view_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.currentQuoteText.setText(data.get(position).getQuoteText());
            holder.quoteText = data.get(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void changeDate() {
            data = mQuoteTexts;
            notifyDataSetChanged();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView currentQuoteText;
            QuoteText quoteText;

            MyViewHolder(View container) {
                super(container);
                currentQuoteText = (TextView) container.findViewById(R.id.current_quote);
                container.setOnClickListener(this);
                container.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                mCurrentId = quoteText.getId();
                mCallbacks.onQuoteSelected(mCurrentId);
            }

            @Override
            public boolean onLongClick(View view) {
                mCurrentId = quoteText.getId();
                openDeleteQuoteCategoryDialog();
                return false;
            }

            private void openDeleteQuoteCategoryDialog() {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View dialogView = layoutInflater.inflate(R.layout.dialog_delete, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
                mDialogBuilder.setView(dialogView);
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.dialog_ok_button),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mQuoteDataRepository.deleteQuoteByIdFromDb(mCurrentId, mQuoteType);
                                        showSnackbar();
                                    }
                                })
                        .setNegativeButton(getString(R.string.dialog_cancel_button),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();
            }

            private void showSnackbar() {
                final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
                Snackbar snackbarIsDeleted = Snackbar.make(coordinatorLayout, getString(R.string.quote_is_deleted), Snackbar.LENGTH_LONG);
                snackbarIsDeleted.show();
            }
        }
    }

    public interface AllQuoteCurrentCategoryCallbacks {
        /**
         * Method add CurrentQuoteFragment as detail fragment
         *
         * @param currentId
         */
        void onQuoteSelected(long currentId);
    }
}