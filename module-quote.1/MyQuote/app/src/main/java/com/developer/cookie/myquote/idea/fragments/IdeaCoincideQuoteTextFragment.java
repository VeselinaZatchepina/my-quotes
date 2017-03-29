package com.developer.cookie.myquote.idea.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.idea.activities.IdeaCoincideQuoteTextActivity;

import java.util.ArrayList;
import java.util.List;


public class IdeaCoincideQuoteTextFragment extends Fragment {

    private static final String QUOTE_TEXT_LIST_NEW_INSTANCE = "com.developer.cookie.myquote.quote_text_list_new_instance_icqtf";

    List<String> mListOfCoicideQuoteText;
    RecyclerView mRecyclerView;
    IdeaCoincideRecyclerViewAdapter mRecyclerViewAdapter;

    public IdeaCoincideQuoteTextFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListOfCoicideQuoteText = getArguments().getStringArrayList(QUOTE_TEXT_LIST_NEW_INSTANCE);
        } else {
            mListOfCoicideQuoteText = getActivity().getIntent().getStringArrayListExtra(IdeaCoincideQuoteTextActivity.LIST_COINCIDE_QUOTE_TEXT_INTENT_ICQTA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recyclerview_fragment, container, false);

        mRecyclerViewAdapter = new IdeaCoincideRecyclerViewAdapter(mListOfCoicideQuoteText);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        return rootView;
    }

    public static IdeaCoincideQuoteTextFragment newInstance(ArrayList<String> quoteTextList) {
        Bundle args = new Bundle();
        args.putStringArrayList(QUOTE_TEXT_LIST_NEW_INSTANCE, quoteTextList);
        IdeaCoincideQuoteTextFragment fragment = new IdeaCoincideQuoteTextFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This class is custom adapter for IdeaCoincideQuoteTextFragment.
     * It helps to show list of all coincide quotes.
     */
    private class IdeaCoincideRecyclerViewAdapter
            extends RecyclerView.Adapter<IdeaCoincideQuoteTextFragment.IdeaCoincideRecyclerViewAdapter.MyViewHolder> {
        private List<String> currentQuoteList;

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView currentQuote;

            MyViewHolder(View container) {
                super(container);
                currentQuote = (TextView) container.findViewById(R.id.current_quote);
                container.setOnClickListener(this);
                container.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) { }

            @Override
            public boolean onLongClick(View view) { return true; }
        }

        /**
         * Method create custom adapter.
         *
         * @param listOfQuoteText
         */
        public IdeaCoincideRecyclerViewAdapter(List<String> listOfQuoteText) {
            currentQuoteList = listOfQuoteText;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.all_quote_current_category_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.currentQuote.setText(currentQuoteList.get(position));
        }

        @Override
        public int getItemCount() {
            return currentQuoteList.size();
        }

        /**
         * When adapter data is changed this method helps set new data for adapter.
         *
         * @param listOfQuoteText
         */
        public void changeDate(List<String> listOfQuoteText) {
            currentQuoteList = listOfQuoteText;
            notifyDataSetChanged();
        }
    }
}
