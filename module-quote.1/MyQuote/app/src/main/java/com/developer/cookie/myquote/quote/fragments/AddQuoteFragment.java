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

    QuoteDataRepository quoteDataRepository;
    private List<String> listOfAllCategories;
    ArrayAdapter<String> spinnerAdapter;
    private String valueOfCategory;
    Spinner spinner;

    String currentQuoteText;
    String currentBookName;
    String currentAuthorName;

    EditText quoteText;
    EditText bookName;
    EditText authorName;
    EditText pageNumber;
    EditText yearNumber;
    EditText publishName;

    RealmResults<QuoteCategory> quoteCategoryList;
    RealmResults<QuoteText> quoteTexts;

    Long quoteTextId;
    String currentQuoteTextObjectCategory;

    QuoteText quoteTextObject;

    String activityTitle;

    public AddQuoteFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quoteDataRepository = new QuoteDataRepository();
        // Get quote id for edit
        quoteTextId = getActivity().getIntent().getLongExtra(AddQuoteActivity.QUOTE_TEXT_ID, -1);
        if (quoteTextId!= -1) {
            quoteTexts = quoteDataRepository.getQuoteTextObjectsByQuoteId(quoteTextId);
        }
        activityTitle = getActivity().getTitle().toString();
        // Get all quote categories
        quoteCategoryList = quoteDataRepository.getListOfQuoteCategories(activityTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_quote, container, false);
            spinner = (Spinner) rootView.findViewById(R.id.category_spinner);

            quoteText = (EditText) rootView.findViewById(R.id.quote_text);
            bookName = (EditText) rootView.findViewById(R.id.book_name);
            authorName = (EditText) rootView.findViewById(R.id.author_name);
            pageNumber = (EditText) rootView.findViewById(R.id.page_number);
            yearNumber = (EditText) rootView.findViewById(R.id.year_number);
            publishName = (EditText) rootView.findViewById(R.id.publish_name);

        // Create view for "My quote" type
        if (isAdded()) {
            if (activityTitle.equals(getString(R.string.my_quote_type))) {
                bookName.setVisibility(View.GONE);
                authorName.setVisibility(View.GONE);
                pageNumber.setVisibility(View.GONE);
                yearNumber.setVisibility(View.GONE);
                publishName.setVisibility(View.GONE);
            }
        }

            // Work with spinner
            quoteCategoryList.addChangeListener(new RealmChangeListener<RealmResults<QuoteCategory>>() {
                @Override
                public void onChange(RealmResults<QuoteCategory> element) {
                    createQuoteCategoryListForSpinner(element);
                    // Set hint for spinner
                    if (isAdded()) {
                        spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {
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
                    spinnerAdapter.addAll(listOfAllCategories);
                    spinner.setAdapter(spinnerAdapter);
                    spinner.setSelection(spinnerAdapter.getCount());
                    // If we choose Quote for edit we set spinner to current quote category position.
                    if (quoteTextId != -1) {
                        spinner.setSelection(listOfAllCategories.indexOf(currentQuoteTextObjectCategory));
                    }
                }
            });

            // AddQuoteFragment for Quote edit. We fill all Views in fragment with current quote data.
        if (quoteTextId != -1) {
            quoteTexts.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
                @Override
                public void onChange(RealmResults<QuoteText> element) {
                    if (element.size() > 0) {
                        FillViewsWithCurrentQuoteDataHelper.fillViewsWithCurrentQuoteData(element,
                                quoteText, bookName, authorName, pageNumber, publishName, yearNumber);
                        quoteTextObject = element.first();
                        currentQuoteTextObjectCategory = quoteTextObject.getCategory().getCategory();
                        if (listOfAllCategories != null && !listOfAllCategories.isEmpty()) {
                            spinner.setSelection(listOfAllCategories.indexOf(currentQuoteTextObjectCategory));
                        }
                    }
                }
            });
        }

            //Add listener to spinner
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                                                listOfAllCategories.add(0, currentUserInput);
                                                spinnerAdapter.clear();
                                                spinnerAdapter.addAll(listOfAllCategories);
                                                spinner.setSelection(0);
                                                valueOfCategory = currentUserInput;
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
                        valueOfCategory = selectedItem;
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) { }
            });

        return rootView;
    }

    /**
     * Method checks if user choose hint in spinner.
     * @return true if user choose hint and false else if.
     */
    public boolean isSpinnerSelectedItemHint() {
        return valueOfCategory.equals(getString(R.string.spinner_hint));
    }

    /**
     * Method creates list of categories for spinnerAdapter.
     * @param quoteCategoryList list of all quotes category from db.
     */
    private void createQuoteCategoryListForSpinner(List<QuoteCategory> quoteCategoryList) {
        listOfAllCategories = new ArrayList<>();
        // Create list of categories for spinnerAdapter
        if (quoteCategoryList != null || !quoteCategoryList.isEmpty()) {
            for (int i = 0; i < quoteCategoryList.size(); i++) {
                QuoteCategory currentCategory = quoteCategoryList.get(i);
                if (currentCategory != null) {
                    String category = currentCategory.getCategory();
                    listOfAllCategories.add(category);
                }
            }
            if(isAdded()) {
                listOfAllCategories.add(getString(R.string.spinner_add_category));
                listOfAllCategories.add(getString(R.string.spinner_hint));
            }
        } else {
            if(isAdded()) {
                listOfAllCategories.add(getString(R.string.spinner_add_category));
                listOfAllCategories.add(getString(R.string.spinner_hint));
            }
        }
    }

    /**
     * Method checks if main EditText is empty or not.
     * @return false if EditText not empty and true if else.
     */
    public boolean isEditTextEmpty() {
        currentQuoteText = quoteText.getText().toString();
        if(TextUtils.isEmpty(currentQuoteText)) {
            quoteText.setError("Quote text cannot be empty");
            return true;
        }

        if (isAdded()) {
            if (!activityTitle.equals(getString(R.string.my_quote_type))) {
                currentBookName = bookName.getText().toString();
                currentAuthorName = authorName.getText().toString();
                if (TextUtils.isEmpty(currentBookName)) {
                    bookName.setError("Book name cannot be empty");
                    return true;
                }
                if (TextUtils.isEmpty(currentAuthorName)) {
                    authorName.setError("Author name cannot be empty");
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
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_TEXT, currentQuoteText);
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_CATEGORY, valueOfCategory);
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_CREATE_DATE, String.valueOf(currentDate));
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_TYPE, activityTitle);

        final String currentPageNumber;
        final String currentYearNumber;
        final String currentPublishName;
        if (!activityTitle.equals(getString(R.string.my_quote_type))) {
            currentPageNumber = pageNumber.getText().toString();
            currentYearNumber = yearNumber.getText().toString();
            currentPublishName = publishName.getText().toString();
            mapOfQuoteProperties.put(QuotePropertiesEnum.BOOK_NAME, currentBookName);
            mapOfQuoteProperties.put(QuotePropertiesEnum.BOOK_AUTHOR, currentAuthorName);
            mapOfQuoteProperties.put(QuotePropertiesEnum.PAGE_NUMBER, currentPageNumber);
            mapOfQuoteProperties.put(QuotePropertiesEnum.YEAR_NUMBER, currentYearNumber);
            mapOfQuoteProperties.put(QuotePropertiesEnum.PUBLISHER_NAME, currentPublishName);
        }
        if (quoteTextId != -1) {
            quoteDataRepository.saveChangedQuoteObject(quoteTextId, mapOfQuoteProperties);
        } else {
            quoteDataRepository.saveQuote(mapOfQuoteProperties);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quoteDataRepository.closeDbConnect();
    }
}
