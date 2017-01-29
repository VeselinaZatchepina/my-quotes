package com.developer.cookie.myquote.quote.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.cookie.myquote.R;

import java.util.List;


public class QuoteCategoryRecyclerViewAdapter
        extends RecyclerView.Adapter<QuoteCategoryRecyclerViewAdapter.MyViewHolder> {

    private List<String> listOfCategory;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemQuoteCategory;
        //public TextView itemQuoteCount;

        MyViewHolder(View container) {
            super(container);
            itemQuoteCategory = (TextView) container.findViewById(R.id.item_quote_category);
            //this.itemQuoteCount = (TextView) container.findViewById(R.id.item_quote_count);
        }
    }

    public QuoteCategoryRecyclerViewAdapter (List<String> realmResults) {
        listOfCategory = realmResults;
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
    }

    @Override
    public int getItemCount() {
        return listOfCategory.size();
    }

    public void changeDate(List<String> list) {
        listOfCategory = list;
        notifyDataSetChanged();
    }




//    private HashMap<String, Integer> getAllCategoryAndItsCount(RealmResults<QuoteText> realmResults) {
//        HashMap<String, Integer> mapOfAllCategory = new HashMap<>();
//        int count = 0;
//
//        for (int i = 0; i < realmResults.size(); i++) {
//            QuoteText currentQuoteText = realmResults.get(i);
//            String currentCategory = currentQuoteText.getTypification().getCategory();
//
//            if (!mapOfAllCategory.containsKey(currentCategory)) {
//                mapOfAllCategory.put(currentCategory, count++);
//            } else {
//                int currentValue = mapOfAllCategory.get(currentCategory).intValue();
//                mapOfAllCategory.put(currentCategory, currentValue++);
//            }
//        }
//
//        return mapOfAllCategory;
//
//    }
}