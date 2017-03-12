package com.developer.cookie.myquote.quote.fragments;


import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

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
    View rootView;
    String categoryName;
    QuoteDataRepository quoteDataRepository;
    ArrayList<Long> listOfQuotesId;
    List<String> currentCategoryQuoteTextList;

    AllQuoteCurrentCategoryRecyclerViewAdapter recyclerViewAdapter;

    RealmResults<QuoteText> quoteTexts;

    String quoteType;

    long currentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quoteType = getActivity().getTitle().toString();
        // Get current clicked category name
        categoryName = getActivity()
                .getIntent()
                .getSerializableExtra(AllQuoteCurrentCategoryActivity.CATEGORY_NAME)
                .toString();
        quoteDataRepository = new QuoteDataRepository();
        quoteTexts = quoteDataRepository.getListOfQuoteTextByCategory(categoryName, quoteType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recyclerview_fragment, container, false);

        quoteTexts.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
            @Override
            public void onChange(RealmResults<QuoteText> element) {
                if (element.size() == 0 && getActivity() != null) {
                    getActivity().finish();
                }
                currentCategoryQuoteTextList = new ArrayList<String>();
                for (int i = 0; i < element.size(); i++) {
                    currentCategoryQuoteTextList.add(element.get(i).getQuoteText());
                }
                RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
                registerForContextMenu(recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewAdapter = new AllQuoteCurrentCategoryRecyclerViewAdapter(currentCategoryQuoteTextList);
                recyclerView.setAdapter(recyclerViewAdapter);
                // Create list of quote's id for data transfer to another fragment
                listOfQuotesId = new ArrayList<Long>();
                for(int i = 0; i < element.size(); i++) {
                    listOfQuotesId.add(element.get(i).getId());
                }
            }
        });
        return rootView;
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
                quoteDataRepository.deleteQuoteTextObjectById(currentId,quoteType);
                Toast.makeText(getActivity(), "Quote deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quoteDataRepository.closeDbConnect();
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
                currentId = listOfQuotesId.get(currentCategoryQuoteTextList
                        .indexOf(currentQuote.getText().toString()));
                Intent intent = CurrentQuotePagerActivity.newIntent(getActivity(),
                        listOfQuotesId, currentId, quoteType);
                startActivity(intent);
            }

            @Override
            public boolean onLongClick(View view) {
                currentId = listOfQuotesId.get(currentCategoryQuoteTextList
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