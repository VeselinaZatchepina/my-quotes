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
import com.developer.cookie.myquote.database.model.BookAuthor;
import com.developer.cookie.myquote.database.model.BookName;
import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.quote.QuotePropertiesEnum;
import com.developer.cookie.myquote.quote.activities.AddQuoteActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;


/**
 * AddQuoteFragment is used for input properties of the quote.
 */
public class AddQuoteFragment extends Fragment {

    private static final String LOG_TAG = AddQuoteFragment.class.getSimpleName();

    private View rootView;
    QuoteDataRepository quoteDataRepository;
    private List<String> listOfAllCategories;
    ArrayAdapter<String> spinnerAdapter;
    private String valueOfCategory;
    Spinner spinner;

    String currentQuoteText;
    String currentBookName;
    String currentAuthorName;

    Long quoteIdFromIntent;

    EditText quoteText;
    EditText bookName;
    EditText authorName;
    EditText pageNumber;
    EditText yearNumber;
    EditText publishName;

    QuoteText quoteTextObject;

    public AddQuoteFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Serializable quoteTextId = getActivity()
                .getIntent()
                .getSerializableExtra(AddQuoteActivity.QUOTE_DATA_FOR_EDIT);
        if (quoteTextId!= null) {
            quoteIdFromIntent = Long.valueOf(quoteTextId.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_quote, container, false);
        spinner = (Spinner) rootView.findViewById(R.id.category_spinner);

        quoteText = (EditText) rootView.findViewById(R.id.quote_text);
        bookName = (EditText) rootView.findViewById(R.id.book_name);
        authorName = (EditText) rootView.findViewById(R.id.author_name);
        pageNumber = (EditText) rootView.findViewById(R.id.page_number);
        yearNumber = (EditText) rootView.findViewById(R.id.year_number);
        publishName = (EditText) rootView.findViewById(R.id.publish_name);

        quoteDataRepository = new QuoteDataRepository();

        RealmResults<QuoteCategory> quoteCategoryList = quoteDataRepository.getListOfQuoteCategories();
        quoteCategoryList.addChangeListener(new RealmChangeListener<RealmResults<QuoteCategory>>() {
            @Override
            public void onChange(RealmResults<QuoteCategory> element) {
                createQuoteCategoryListForSpinner(element);
                // Set hint for spinner
                spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        if (position == getCount()) {
                            ((TextView)v.findViewById(android.R.id.text1)).setText("");
                            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
                        }
                        return v;
                    }

                    @Override
                    public int getCount() {
                        return super.getCount()-1;
                    }
                };
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) ;
                spinnerAdapter.addAll(listOfAllCategories);
                spinner.setAdapter(spinnerAdapter);
                spinner.setSelection(spinnerAdapter.getCount());
            }
        });

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

        if (quoteIdFromIntent != null) {
            RealmResults<QuoteText> quoteTexts = quoteDataRepository.getQuoteTextObjectsByQuoteId(quoteIdFromIntent);
            quoteTexts.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
                @Override
                public void onChange(RealmResults<QuoteText> element) {
                    quoteTextObject = element.first();
                    quoteText.setText(quoteTextObject.getQuoteText());
                    BookName bookNameObject = quoteTextObject.getBookName();
                    bookName.setText(bookNameObject.getBookName());
                    RealmList<BookAuthor> bookAuthors = bookNameObject.getBookAuthors();
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < bookAuthors.size(); i++) {
                        if (i != bookAuthors.size() - 1) {
                            builder.append(bookAuthors.get(i).getBookAuthor()).append(", ");
                        } else {
                            builder.append(bookAuthors.get(i).getBookAuthor());
                        }
                    }
                    String currentAuthorName = builder.toString();
                    authorName.setText(currentAuthorName);
                    pageNumber.setText(quoteTextObject.getPage().getPageNumber());
                    publishName.setText(bookNameObject.getPublisher().getPublisherName());
                    yearNumber.setText(bookNameObject.getYear().getYearNumber());
                    spinner.setSelection(spinnerAdapter.getPosition(quoteTextObject.getCategory().getCategory()));
                }
            });
        }

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
            listOfAllCategories.add(getString(R.string.spinner_add_category));
            listOfAllCategories.add(getString(R.string.spinner_hint));
        } else {
            listOfAllCategories.add(getString(R.string.spinner_add_category));
            listOfAllCategories.add(getString(R.string.spinner_hint));
        }
    }

    /**
     * Method checks if main EditText is empty or not.
     * @return false if EditText not empty and true if else.
     */
    public boolean isEditTextEmpty() {
        currentQuoteText = quoteText.getText().toString();
        currentBookName = bookName.getText().toString();
        currentAuthorName = authorName.getText().toString();

        if(TextUtils.isEmpty(currentQuoteText)) {
            quoteText.setError("Quote text cannot be empty");
            return true;
        }
        if (TextUtils.isEmpty(currentBookName)) {
            bookName.setError("Book name cannot be empty");
            return true;
        }
        if (TextUtils.isEmpty(currentAuthorName)) {
            authorName.setError("Author name cannot be empty");
            return true;
        }
        return false;
    }

    /**
     * Method creates quote properties map for QuoteCreator class and pass map to it.
     */
    public void createMapOfQuoteProperties() {
        final String currentPageNumber = pageNumber.getText().toString();
        final String currentYearNumber = yearNumber.getText().toString();
        final String currentPublishName = publishName.getText().toString();

        Calendar currentCreateDate = Calendar.getInstance();
        long currentMillis = currentCreateDate.getTimeInMillis();

        HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties = new HashMap<>();
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_TEXT, currentQuoteText);
        mapOfQuoteProperties.put(QuotePropertiesEnum.BOOK_NAME, currentBookName);
        mapOfQuoteProperties.put(QuotePropertiesEnum.BOOK_AUTHOR, currentAuthorName);
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_CATEGORY, valueOfCategory);
        mapOfQuoteProperties.put(QuotePropertiesEnum.PAGE_NUMBER, currentPageNumber);
        mapOfQuoteProperties.put(QuotePropertiesEnum.YEAR_NUMBER, currentYearNumber);
        mapOfQuoteProperties.put(QuotePropertiesEnum.PUBLISHER_NAME, currentPublishName);
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_CREATE_DATE, String.valueOf(currentMillis));
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_TYPE, "MyQuote");

        quoteDataRepository.saveQuote(mapOfQuoteProperties);

//        if (quoteIdFromIntent != null) {
//            quoteDataRepository.saveChangedQuoteObject(quoteTextObject, mapOfQuoteProperties);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quoteDataRepository.closeDbConnect();
    }
}
