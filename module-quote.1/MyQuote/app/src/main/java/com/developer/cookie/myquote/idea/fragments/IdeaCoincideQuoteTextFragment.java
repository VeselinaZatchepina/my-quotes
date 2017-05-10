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

import java.util.ArrayList;
import java.util.List;


public class IdeaCoincideQuoteTextFragment extends Fragment {
    private static final String QUOTE_TEXT_LIST_NEW_INSTANCE = "idea_coincide_quote_text_fragment_quote_text_list_new_instance";
    private List<String> mCoincideQuoteTexts;

    public IdeaCoincideQuoteTextFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCoincideQuoteTexts = getArguments().getStringArrayList(QUOTE_TEXT_LIST_NEW_INSTANCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        setCurrentCategoryTitleVisibility(rootView);
        defineRecyclerView(rootView);
        return rootView;
    }

    private void setCurrentCategoryTitleVisibility(View rootView) {
        TextView currentCategoryTextView = (TextView) rootView.findViewById(R.id.current_category);
        currentCategoryTextView.setVisibility(View.GONE);
    }

    private void defineRecyclerView(View rootView) {
        IdeaCoincideRecyclerViewAdapter recyclerViewAdapter = new IdeaCoincideRecyclerViewAdapter(mCoincideQuoteTexts);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
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
        private static final int EMPTY_LIST = 0;
        private static final int NOT_EMPTY_LIST = 1;
        private List<String> currentQuoteList;

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView currentQuote;

            MyViewHolder(View container) {
                super(container);
                currentQuote = (TextView) container.findViewById(R.id.current_quote);
            }
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
            switch (viewType) {
                case EMPTY_LIST:
                    View itemViewEmpty;
                    if (getActivity().findViewById(R.id.detail_fragment_container) == null) {
                        itemViewEmpty = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.fragment_empty_recycler_view, parent, false);
                    } else {
                        itemViewEmpty = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.fragment_empty_recycler_view_get_idea_tablet, parent, false);
                    }
                    return new MyViewHolder(itemViewEmpty);
                case NOT_EMPTY_LIST:
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.all_quote_current_category_recycler_view_item, parent, false);
                    return new MyViewHolder(itemView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (!currentQuoteList.isEmpty()) {
                holder.currentQuote.setText(currentQuoteList.get(position));
            }
        }

        @Override
        public int getItemCount() {
            if (currentQuoteList.isEmpty()) {
                return 1;
            }
            return currentQuoteList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return currentQuoteList.isEmpty() ? EMPTY_LIST : NOT_EMPTY_LIST;
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
