package com.developer.cookie.myquote.quote.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.QuoteDataRepository;
import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.quote.QuotePropertiesEnum;
import com.developer.cookie.myquote.quote.activities.AddQuoteActivity;
import com.developer.cookie.myquote.utils.FillViewsWithCurrentQuoteDataHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * AddQuoteFragment is used for input properties of the quote. It used for save and edit quotes.
 */
public class AddQuoteFragment extends Fragment {

    private static final String LOG_TAG = AddQuoteFragment.class.getSimpleName();
    public static final String QUOTE_TYPE_BUNDLE_AQF = "com.developer.cookie.myquote.quote.fragments.quote_type_bundle_aqf";
    public static final String QUOTE_ID_BUNDLE_AQF = "com.developer.cookie.myquote.quote.fragments.quote_id_bundle_aqf";

    QuoteDataRepository mQuoteDataRepository;
    private List<String> mListOfAllCategories;
    ArrayAdapter<String> mSpinnerAdapter;
    private String mValueOfCategory;
    Spinner mSpinner;

    String mCurrentQuoteText;
    String mCurrentBookName;
    String mCurrentAuthorName;

    EditText mQuoteText;
    EditText mBookName;
    EditText mAuthorName;
    EditText mPageNumber;
    EditText mYearNumber;
    EditText mPublishName;

    RealmResults<QuoteCategory> mQuoteCategoryList;
    RealmResults<QuoteText> mQuoteTexts;

    Long mQuoteTextId;
    String mCurrentQuoteTextObjectCategory;

    QuoteText mQuoteTextObject;

    String mQuoteType;

    public AddQuoteFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuoteDataRepository = new QuoteDataRepository();
        if (savedInstanceState != null) {
            mQuoteType = savedInstanceState.getString(QUOTE_TYPE_BUNDLE_AQF);
            mQuoteTextId = savedInstanceState.getLong(QUOTE_ID_BUNDLE_AQF);
        } else {
            // Get quote id for edit
            mQuoteTextId = getActivity().getIntent().getLongExtra(AddQuoteActivity.QUOTE_TEXT_ID_INTENT_AQA, -1);
            // Get quote type
            mQuoteType = getActivity().getTitle().toString();
        }
        if (mQuoteTextId != -1) {
            mQuoteTexts = mQuoteDataRepository.getQuoteTextObjectsByQuoteId(mQuoteTextId);
        }
        // Get all quote categories
        mQuoteCategoryList = mQuoteDataRepository.getListOfQuoteCategories(mQuoteType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_quote, container, false);
        mSpinner = (Spinner) rootView.findViewById(R.id.category_spinner);
        mQuoteText = (EditText) rootView.findViewById(R.id.quote_text);
        mBookName = (EditText) rootView.findViewById(R.id.book_name);
        mAuthorName = (EditText) rootView.findViewById(R.id.author_name);
        mPageNumber = (EditText) rootView.findViewById(R.id.page_number);
        mYearNumber = (EditText) rootView.findViewById(R.id.year_number);
        mPublishName = (EditText) rootView.findViewById(R.id.publish_name);
        // Create view for "My quote" type
        if (isAdded()) {
            if (mQuoteType.equals(getString(R.string.my_quote_type))) {
                mBookName.setVisibility(View.GONE);
                mAuthorName.setVisibility(View.GONE);
                mPageNumber.setVisibility(View.GONE);
                mYearNumber.setVisibility(View.GONE);
                mPublishName.setVisibility(View.GONE);
            }
        }
        // Work with mSpinner
        mQuoteCategoryList.addChangeListener(new RealmChangeListener<RealmResults<QuoteCategory>>() {
            @Override
            public void onChange(RealmResults<QuoteCategory> element) {
                createQuoteCategoryListForSpinner(element);
                // Set hint for mSpinner
                if (isAdded()) {
                    mSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            if (position == getCount()) {
                                ((TextView) v.findViewById(android.R.id.text1)).setText("");
                                ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
                            }
                            return v;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1;
                        }
                    };
                }
                mSpinnerAdapter.addAll(mListOfAllCategories);
                mSpinner.setAdapter(mSpinnerAdapter);
                mSpinner.setSelection(mSpinnerAdapter.getCount());
                // If we choose Quote for edit we set mSpinner to current quote category position.
                if (mQuoteTextId != -1) {
                    mSpinner.setSelection(mListOfAllCategories.indexOf(mCurrentQuoteTextObjectCategory));
                }
            }
        });
        // AddQuoteFragment for Quote edit. We fill all Views in fragment with current quote data.
        if (mQuoteTextId != -1) {
            mQuoteTexts.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
                @Override
                public void onChange(RealmResults<QuoteText> element) {
                    if (element.size() > 0) {
                        FillViewsWithCurrentQuoteDataHelper.fillViewsWithCurrentQuoteData(element,
                                mQuoteText, mBookName, mAuthorName, mPageNumber, mPublishName, mYearNumber, mQuoteType);
                        mQuoteTextObject = element.first();
                        mCurrentQuoteTextObjectCategory = mQuoteTextObject.getCategory().getCategory();
                        if (mListOfAllCategories != null && !mListOfAllCategories.isEmpty()) {
                            mSpinner.setSelection(mListOfAllCategories.indexOf(mCurrentQuoteTextObjectCategory));
                        }
                    }
                }
            });
        }
        //Add listener to mSpinner
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals(getString(R.string.spinner_add_category))) {
                    // Create dialog for add category
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View dialogView = layoutInflater.inflate(R.layout.add_category_dialog, null);
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
                    mDialogBuilder.setView(dialogView);
                    final EditText userInput = (EditText) dialogView.findViewById(R.id.input_text);
                    mDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            String currentUserInput = userInput.getText().toString();
                                            mListOfAllCategories.add(0, currentUserInput);
                                            mSpinnerAdapter.clear();
                                            mSpinnerAdapter.addAll(mListOfAllCategories);
                                            mSpinner.setSelection(0);
                                            mValueOfCategory = currentUserInput;
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = mDialogBuilder.create();
                    alertDialog.show();
                } else {
                    mValueOfCategory = selectedItem;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }

    /**
     * Method checks if user choose hint in mSpinner.
     *
     * @return true if user choose hint and false else if.
     */
    public boolean isSpinnerSelectedItemHint() {
        return mValueOfCategory.equals(getString(R.string.spinner_hint));
    }

    /**
     * Method creates list of categories for mSpinnerAdapter.
     *
     * @param quoteCategoryList list of all quotes category from db.
     */
    private void createQuoteCategoryListForSpinner(List<QuoteCategory> quoteCategoryList) {
        mListOfAllCategories = new ArrayList<>();
        // Create list of categories for mSpinnerAdapter
        if (quoteCategoryList != null || !quoteCategoryList.isEmpty()) {
            for (int i = 0; i < quoteCategoryList.size(); i++) {
                QuoteCategory currentCategory = quoteCategoryList.get(i);
                if (currentCategory != null) {
                    String category = currentCategory.getCategory();
                    mListOfAllCategories.add(category);
                }
            }
            if (isAdded()) {
                mListOfAllCategories.add(getString(R.string.spinner_add_category));
                mListOfAllCategories.add(getString(R.string.spinner_hint));
            }
        } else {
            if (isAdded()) {
                mListOfAllCategories.add(getString(R.string.spinner_add_category));
                mListOfAllCategories.add(getString(R.string.spinner_hint));
            }
        }
    }

    /**
     * Method checks if main EditText is empty or not.
     *
     * @return false if EditText not empty and true if else.
     */
    public boolean isEditTextEmpty() {
        mCurrentQuoteText = mQuoteText.getText().toString();
        if (TextUtils.isEmpty(mCurrentQuoteText)) {
            mQuoteText.setError("Quote text cannot be empty");
            return true;
        }
        if (isAdded()) {
            if (!mQuoteType.equals(getString(R.string.my_quote_type))) {
                mCurrentAuthorName = mAuthorName.getText().toString();
                if (TextUtils.isEmpty(mCurrentAuthorName)) {
                    mAuthorName.setError("Author name cannot be empty");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method creates quote properties map for QuoteCreator class and pass map to it.
     */
    public void createMapOfQuoteProperties() {
        Calendar currentCreateDate = Calendar.getInstance();
        String currentDate = String.format("%1$td %1$tb %1$tY", currentCreateDate);
        HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties = new HashMap<>();
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_TEXT, mCurrentQuoteText);
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_CATEGORY, mValueOfCategory);
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_CREATE_DATE, String.valueOf(currentDate));
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_TYPE, mQuoteType);
        final String currentPageNumber;
        final String currentYearNumber;
        final String currentPublishName;
        if (!mQuoteType.equals(getString(R.string.my_quote_type))) {
            currentPageNumber = mPageNumber.getText().toString();
            currentYearNumber = mYearNumber.getText().toString();
            currentPublishName = mPublishName.getText().toString();
            mCurrentBookName = mBookName.getText().toString();
            mapOfQuoteProperties.put(QuotePropertiesEnum.BOOK_NAME, mCurrentBookName);
            mapOfQuoteProperties.put(QuotePropertiesEnum.BOOK_AUTHOR, mCurrentAuthorName);
            mapOfQuoteProperties.put(QuotePropertiesEnum.PAGE_NUMBER, currentPageNumber);
            mapOfQuoteProperties.put(QuotePropertiesEnum.YEAR_NUMBER, currentYearNumber);
            mapOfQuoteProperties.put(QuotePropertiesEnum.PUBLISHER_NAME, currentPublishName);
        }
        if (mQuoteTextId != -1) {
            mQuoteDataRepository.saveChangedQuoteObject(mQuoteTextId, mapOfQuoteProperties);
        } else {
            mQuoteDataRepository.saveQuote(mapOfQuoteProperties);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUOTE_TYPE_BUNDLE_AQF, mQuoteType);
        outState.putLong(QUOTE_ID_BUNDLE_AQF, mQuoteTextId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mQuoteDataRepository.closeDbConnect();
    }
}
