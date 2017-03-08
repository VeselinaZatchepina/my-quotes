package com.developer.cookie.myquote.quote.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.QuoteDataRepository;
import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.quote.activities.AllQuoteCurrentCategoryActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * QuoteCategoryFragment is used for presentation list of all categories (quote category) and
 * count of the quotes for every this category.
 */
public class QuoteCategoryFragment extends Fragment {

    private static final String LOG_TAG = QuoteCategoryFragment.class.getSimpleName();
    View rootView;
    QuoteDataRepository quoteDataRepository;

    List<Integer> quoteCountListOfEveryCategory;
    List<String> listOfCategories;
    Pair<List<String>,List<Integer>> pair;

    QuoteCategoryRecyclerViewAdapter quoteCategoryRecyclerViewAdapter;
    RealmResults<QuoteCategory> quoteCategoryList;

    String quoteType;

    public QuoteCategoryFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quoteType = getActivity().getTitle().toString();
        quoteDataRepository = new QuoteDataRepository();
        quoteCategoryList = quoteDataRepository.getListOfQuoteCategories(quoteType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recyclerview_fragment, container, false);
        // Create callback for swipe to delete
        final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String currentCategory = quoteCategoryRecyclerViewAdapter.listOfCategory.get(viewHolder.getAdapterPosition());
                quoteDataRepository.deleteAllQuotesWithCurrentCategory(currentCategory, quoteType);
            }
        };

        quoteCategoryList.addChangeListener(new RealmChangeListener<RealmResults<QuoteCategory>>() {
            @Override
            public void onChange(RealmResults<QuoteCategory> element) {
                createPairObject(element);
                quoteCategoryRecyclerViewAdapter= new QuoteCategoryRecyclerViewAdapter(pair);
                // Create and set custom adapter for recyclerview
                RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(quoteCategoryRecyclerViewAdapter);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }
        });

        return rootView;
    }

    /**
     * Method create Pair object for custom adapter. First parameter is name list of category,
     * second is quote count list of this categories.
     * @param element list of quote category realm object.
     */
    private void createPairObject(List<QuoteCategory> element) {
                // Create parameters for pair object
                quoteCountListOfEveryCategory = new ArrayList<Integer>();
                for (int i = 0; i < element.size(); i++) {
                    quoteCountListOfEveryCategory.add(element.get(i).getQuoteCountCurrentCategory());
                }
                listOfCategories = new ArrayList<>();
                for (int i = 0; i<element.size(); i++) {
                    String currentCategory = element.get(i).getCategory();
                    listOfCategories.add(currentCategory);
                }
                pair = new Pair<>(listOfCategories, quoteCountListOfEveryCategory);
            }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quoteDataRepository.closeDbConnect();
    }

    /**
     * This class is custom adapter for QuoteCategoryFragment.
     * It helps to show list of all quote categories and how quote counts belongs to every this category.
     */
    private class QuoteCategoryRecyclerViewAdapter
            extends RecyclerView.Adapter<QuoteCategoryRecyclerViewAdapter.MyViewHolder> {
        private List<String> listOfCategory;
        private List<Integer> quoteCountList;

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView itemQuoteCategory;
            TextView itemQuoteCount;

            MyViewHolder(View container) {
                super(container);
                itemQuoteCategory = (TextView) container.findViewById(R.id.item_quote_category);
                itemQuoteCount = (TextView) container.findViewById(R.id.item_quote_count);
                container.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Intent intent = AllQuoteCurrentCategoryActivity.newIntent(getActivity(),
                        itemQuoteCategory.getText().toString(), quoteType);
                startActivity(intent);
            }
        }

        /**
         * Method create custom adapter.
         * @param pair object with two field List<String> and List<Integer>. First field is for list of category.
         *             Second field is for list of quote count.
         */
        public QuoteCategoryRecyclerViewAdapter (Pair<List<String>,List<Integer>> pair) {
            listOfCategory = pair.first;
            quoteCountList = pair.second;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.quote_category_recycler_view_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.itemQuoteCategory.setText(listOfCategory.get(position));
            holder.itemQuoteCount.setText(String.valueOf(quoteCountList.get(position)));
        }

        @Override
        public int getItemCount() {
            return listOfCategory.size();
        }

        /**
         * When adapter data is changed this method helps set new data for adapter.
         * @param pair
         */
        public void changeDate(Pair<List<String>,List<Integer>> pair) {
            listOfCategory = pair.first;
            quoteCountList = pair.second;
            notifyDataSetChanged();
        }
    }
}