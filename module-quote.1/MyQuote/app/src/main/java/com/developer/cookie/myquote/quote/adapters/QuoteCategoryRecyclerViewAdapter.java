package com.developer.cookie.myquote.quote.adapters;


import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.cookie.myquote.R;

import java.util.List;

/**
 * This class is custom adapter for QuoteCategoryFragment.
 * It helps to show list of all quote categories and how quote counts belongs to every this category.
 */
public class QuoteCategoryRecyclerViewAdapter
        extends RecyclerView.Adapter<QuoteCategoryRecyclerViewAdapter.MyViewHolder> {
    private List<String> listOfCategory;
    private List<Integer> quoteCountList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemQuoteCategory;
        TextView itemQuoteCount;

        MyViewHolder(View container) {
            super(container);
            itemQuoteCategory = (TextView) container.findViewById(R.id.item_quote_category);
            itemQuoteCount = (TextView) container.findViewById(R.id.item_quote_count);
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