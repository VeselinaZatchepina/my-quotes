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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.QuoteDataRepository;
import com.developer.cookie.myquote.database.model.QuoteCategory;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * QuoteCategoryFragment is used for presentation list of all categories (quote category) and
 * count of the quotes for every this category.
 */
public class QuoteCategoryFragment extends Fragment {
    private static final String LOG_TAG = QuoteCategoryFragment.class.getSimpleName();
    private static final String QUOTE_TYPE_BUNDLE = "quote_category_fragment_quote_type_bundle";
    private static final String CURRENT_QUOTE_TYPE_NEW_INSTANCE = "quote_category_fragment_current_quote_type_new_instance";
    QuoteDataRepository mQuoteDataRepository;
    QuoteCategoryRecyclerViewAdapter mQuoteCategoryRecyclerViewAdapter;
    RealmResults<QuoteCategory> mQuoteCategoryResults;
    String mQuoteType;
    String mCategoryForDelete;
    private QuoteCategoryCallbacks mCallbacks;

    public QuoteCategoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        mQuoteDataRepository = new QuoteDataRepository();
        mQuoteCategoryResults = mQuoteDataRepository.getListOfQuoteCategories(mQuoteType);
    }

    private void defineInputData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mQuoteType = savedInstanceState.getString(QUOTE_TYPE_BUNDLE);
        } else if (getArguments() != null) {
            mQuoteType = getArguments().getString(CURRENT_QUOTE_TYPE_NEW_INSTANCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        setCurrentCategoryTitleIsGone(rootView);
        defineRecyclerView(rootView);
        return rootView;
    }

    private void setCurrentCategoryTitleIsGone(View rootView) {
        TextView currentCategoryTitle = (TextView) rootView.findViewById(R.id.current_category);
        currentCategoryTitle.setVisibility(View.GONE);
    }

    private void defineRecyclerView(View rootView) {
        mQuoteCategoryRecyclerViewAdapter = new QuoteCategoryRecyclerViewAdapter(getActivity(), mQuoteCategoryResults, true);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mQuoteCategoryRecyclerViewAdapter);
    }

    public static QuoteCategoryFragment newInstance(String quoteType) {
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_QUOTE_TYPE_NEW_INSTANCE, quoteType);
        QuoteCategoryFragment fragment = new QuoteCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(QUOTE_TYPE_BUNDLE, mQuoteType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (QuoteCategoryCallbacks) context;
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

    private class QuoteCategoryRecyclerViewAdapter extends RealmRecyclerViewAdapter<QuoteCategory, QuoteCategoryRecyclerViewAdapter.MyViewHolder> {
        private static final int EMPTY_LIST = 0;
        private static final int NOT_EMPTY_LIST = 1;

        private OrderedRealmCollection<QuoteCategory> mData;

        public QuoteCategoryRecyclerViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<QuoteCategory> data, boolean autoUpdate) {
            super(context, data, autoUpdate);
            mData = data;
        }

        @Override
        public QuoteCategoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case EMPTY_LIST:
                    View itemViewEmpty = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.fragment_empty_recycler_view, parent, false);
                    return new MyViewHolder(itemViewEmpty);
                case NOT_EMPTY_LIST:
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.quote_categories_recycler_view_item, parent, false);
                    return new MyViewHolder(itemView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(QuoteCategoryRecyclerViewAdapter.MyViewHolder holder, int position) {
            if (!mData.isEmpty()) {
                holder.itemQuoteCategory.setText(mData.get(position).getCategoryName());
                holder.itemQuoteCategory.setAllCaps(true);
                holder.itemQuoteCount.setText(String.valueOf(mData.get(position).getQuoteCountCurrentCategory()));
            }
        }

        @Override
        public int getItemCount() {
            if (mData.isEmpty()) {
                return 1;
            }
            return mData.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (mData.isEmpty()) {
                return EMPTY_LIST;
            } else {
                return NOT_EMPTY_LIST;
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView itemQuoteCategory;
            TextView itemQuoteCount;

            MyViewHolder(View container) {
                super(container);
                itemQuoteCategory = (TextView) container.findViewById(R.id.quote_category_name);
                itemQuoteCount = (TextView) container.findViewById(R.id.quote_count);
                container.setOnClickListener(this);
                container.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (!mData.isEmpty()) {
                    mCallbacks.onCategorySelected(itemQuoteCategory.getText().toString(), mQuoteType);
                }
            }

            @Override
            public boolean onLongClick(View view) {
                if (!mData.isEmpty()) {
                    mCategoryForDelete = itemQuoteCategory.getText().toString();
                    openDeleteQuoteCategoryDialog();
                }
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
                                        mQuoteDataRepository.deleteAllQuotesWithCurrentCategory(mCategoryForDelete, mQuoteType);
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
                Snackbar snackbarIsDeleted = Snackbar.make(coordinatorLayout, "Quote category " + mCategoryForDelete + " is deleted", Snackbar.LENGTH_LONG);
                snackbarIsDeleted.show();
            }
        }
    }

    public interface QuoteCategoryCallbacks {
        /**
         * Method starts activity with all quotes of current category
         *
         * @param currentCategory
         * @param quoteType
         */
        void onCategorySelected(String currentCategory, String quoteType);
    }
}