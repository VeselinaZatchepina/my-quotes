package com.developer.cookie.myquote.idea.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.QuoteDataRepository;
import com.developer.cookie.myquote.database.model.QuoteText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmResults;

public class GetIdeaFragment extends Fragment {
    private static final String LOG_TAG = GetIdeaFragment.class.getSimpleName();
    private QuoteDataRepository mQuoteDataRepository;
    private RealmResults<QuoteText> mQuoteTexts;
    private View mRootView;
    private List<Integer> mRandomList;
    private GetIdeaCallbacks mCallbacks;

    public GetIdeaFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuoteDataRepository = new QuoteDataRepository();
        mQuoteTexts = mQuoteDataRepository.getAllQuote();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_get_idea, container, false);
        final Button generateButton = (Button) mRootView.findViewById(R.id.generate_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListOfCoincideQuoteText();
            }
        });
        return mRootView;
    }

    /**
     * Method creates list of quote text which coincide with user subject input.
     */
    private void getListOfCoincideQuoteText() {
        final EditText subjectEditText = (EditText) mRootView.findViewById(R.id.subject_text);
        String currentSubject = subjectEditText.getText().toString().toLowerCase();
        ArrayList<String> coincideQuoteTexts = new ArrayList<String>();
        if (mQuoteTexts.load()) {
            createRandomNumbers(mQuoteTexts.size());
            int countOfCoincideQuoteText = 0;
            for (int i = 0; i < mQuoteTexts.size(); i++) {
                if (countOfCoincideQuoteText == 5) {
                    mCallbacks.generateIdea(coincideQuoteTexts);
                    return;
                }
                int randomVariable = mRandomList.get(i);
                String currentQuoteText = mQuoteTexts.get(randomVariable).getQuoteText();
                if (TextUtils.isEmpty(currentSubject)) {
                    if (!coincideQuoteTexts.contains(currentQuoteText)) {
                        coincideQuoteTexts.add(currentQuoteText);
                        countOfCoincideQuoteText++;
                    }
                } else {
                    if (currentQuoteText.toLowerCase().contains(currentSubject) && !coincideQuoteTexts.contains(currentQuoteText)) {
                        coincideQuoteTexts.add(currentQuoteText);
                        countOfCoincideQuoteText++;
                    }
                }
            }
            mCallbacks.generateIdea(coincideQuoteTexts);
        }
    }

    private void createRandomNumbers(int listSize) {
        mRandomList = new ArrayList<Integer>();
        for (int i = 0; i < listSize; i++) mRandomList.add(i);
        Collections.shuffle(mRandomList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (GetIdeaCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        mQuoteTexts.removeAllChangeListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mQuoteDataRepository.closeDbConnect();
    }

    public interface GetIdeaCallbacks {
        /**
         * Method add IdeaCoincideQuoteTextFragment as detail fragment
         *
         * @param listOfQuoteText
         */
        void generateIdea(ArrayList<String> listOfQuoteText);
    }
}
