package com.developer.cookie.myquote.idea.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.QuoteDataRepository;
import com.developer.cookie.myquote.database.model.QuoteText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.RealmResults;

public class GetIdeaFragment extends Fragment {
    private static final String LOG_TAG = GetIdeaFragment.class.getSimpleName();

    QuoteDataRepository mQuoteDataRepository;
    RealmResults<QuoteText> mQuoteTextList;
    ArrayList<String> mListOfCoincideQuoteText;

    public GetIdeaFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuoteDataRepository = new QuoteDataRepository();
        mQuoteTextList = mQuoteDataRepository.getAllQuoteText();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get_idea, container, false);
        final EditText subjectEditText = (EditText) rootView.findViewById(R.id.subject_text);
        final Button generateButton = (Button) rootView.findViewById(R.id.generate_button);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListOfCoincideQuoteText(subjectEditText);
            }
        });

        return rootView;
    }

    //TODO сделать более эффективный запрос в бд (чтобы получать не все записи сразу)
    /**
     * Method creates list of quote text which coincide with user subject input.
     *
     * @param subjectEditText user input data
     */
    private void getListOfCoincideQuoteText(EditText subjectEditText) {
        List<Integer> listOfRandomNumber = new ArrayList<>();
        mListOfCoincideQuoteText = new ArrayList<String>();
        final String currentSubject = subjectEditText.getText().toString().trim();
        if (mQuoteTextList.load()) {
            int countOfCoincideQuoteText = 0;
            for (int i = 0; i < mQuoteTextList.size(); i++) {
                if (countOfCoincideQuoteText == 5) {
                    return;
                }
                int randomVariable = new Random().nextInt(mQuoteTextList.size());
                if (!listOfRandomNumber.contains(randomVariable)) {
                    String currentQuoteText = mQuoteTextList.get(randomVariable).getQuoteText();
                    if (currentQuoteText.contains(currentSubject) && !mListOfCoincideQuoteText.contains(currentQuoteText)) {
                        mListOfCoincideQuoteText.add(currentQuoteText);
                        countOfCoincideQuoteText++;
                    }
                    listOfRandomNumber.add(randomVariable);
                } else {
                    i--;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mQuoteDataRepository.closeDbConnect();
    }
}
