package com.developer.cookie.myquote.quote.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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
import com.developer.cookie.myquote.quote.Types;
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
    private static final String QUOTE_TYPE_BUNDLE = "add_quote_fragment_quote_type_bundle";
    private static final String QUOTE_ID_BUNDLE = "add_quote_fragment_quote_id_bundle";
    private static final String QUOTE_ID_NEW_INSTANCE = "add_quote_fragment_quote_id_new_instance";
    private static final String QUOTE_TYPE_NEW_INSTANCE = "add_quote_fragment_quote_type_new_instance";
    private static final String QUOTE_CATEGORY_NEW_INSTANCE = "add_quote_fragment_quote_category_new_instance";
    private static final String QUOTE_TEXT_SAVE_INSTANCE = "add_quote_fragment_quote_text_save_instance";
    private static final String QUOTE_BOOK_NAME_SAVE_INSTANCE = "add_quote_fragment_quote_book_name_save_instance";
    private static final String QUOTE_BOOK_AUTHOR_SAVE_INSTANCE = "add_quote_fragment_quote_book_author_save_instance";
    private static final String QUOTE_PUBLISHER_NAME_SAVE_INSTANCE = "add_quote_fragment_quote_publisher_name_save_instance";
    private static final String QUOTE_YEAR_SAVE_INSTANCE = "add_quote_fragment_quote_year_save_instance";
    private static final String QUOTE_PAGE_SAVE_INSTANCE = "add_quote_fragment_quote_page_save_instance";
    private static final String QUOTE_CATEGORY_VALUE_SAVE_INSTANCE = "add_quote_fragment_quote_category_value_save_instance";

    private QuoteDataRepository mQuoteDataRepository;
    private List<String> mAllCategories;
    private ArrayAdapter<String> mSpinnerAdapter;
    private String mSelectedValueOfCategory;
    private Spinner mSpinner;
    private String mCurrentQuoteText;
    private String mCurrentBookName;
    private String mCurrentAuthorName;
    private String mCurrentCategory;

    private EditText mQuoteText;
    private EditText mBookName;
    private EditText mAuthorName;
    private EditText mPageNumber;
    private EditText mYearNumber;
    private EditText mPublishName;

    private RealmResults<QuoteCategory> mQuoteCategories;
    private RealmResults<QuoteText> mQuoteTexts;
    public AlertDialog mAlertDialog;
    private Long mQuoteIdForEdit;
    private String mQuoteType;

    private TextInputLayout mQuoteTextInputLayout;
    private TextInputLayout mAuthorNameInputLayout;
    private TextInputLayout mBookNameInputLayout;
    private TextInputLayout mPageNumberInputLayout;
    private TextInputLayout mYearNumberInputLayout;
    private TextInputLayout mPublisherNameInputLayout;


    public AddQuoteFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        mQuoteDataRepository = new QuoteDataRepository();
        getQuoteForEditById();
        getAllCategories();
    }

    private void defineInputData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mQuoteType = savedInstanceState.getString(QUOTE_TYPE_BUNDLE);
            mQuoteIdForEdit = savedInstanceState.getLong(QUOTE_ID_BUNDLE);
            mCurrentCategory = savedInstanceState.getString(QUOTE_CATEGORY_NEW_INSTANCE);
            mSelectedValueOfCategory = savedInstanceState.getString(QUOTE_CATEGORY_VALUE_SAVE_INSTANCE);
        } else if (getArguments() != null) {
            mQuoteIdForEdit = getArguments().getLong(QUOTE_ID_NEW_INSTANCE, -1);
            mQuoteType = getArguments().getString(QUOTE_TYPE_NEW_INSTANCE);
            mCurrentCategory = getArguments().getString(QUOTE_CATEGORY_NEW_INSTANCE);
        }
    }

    private void getQuoteForEditById() {
        if (mQuoteIdForEdit != -1) {
            mQuoteTexts = mQuoteDataRepository.getQuoteByQuoteId(mQuoteIdForEdit);
        }
    }

    private void getAllCategories() {
        mQuoteCategories = mQuoteDataRepository.getListOfQuoteCategories(mQuoteType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_quote, container, false);
        initViews(rootView);
        hideFieldForMyQuotes(rootView);
        mQuoteCategories.addChangeListener(new RealmChangeListener<RealmResults<QuoteCategory>>() {
            @Override
            public void onChange(RealmResults<QuoteCategory> element) {
                createQuoteCategoryListForSpinner(element);
                setSpinnerOnCurrentPosition();
            }
        });
        fillFieldsWithQuoteEditData(savedInstanceState);
        setListenerToSpinner();
        return rootView;
    }

    private void initViews(View rootView) {
        mQuoteTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.quote_text_input_layout);
        mAuthorNameInputLayout = (TextInputLayout) rootView.findViewById(R.id.author_name_input_layout);
        mPageNumberInputLayout = (TextInputLayout) rootView.findViewById(R.id.page_number_input_layout);
        mYearNumberInputLayout = (TextInputLayout) rootView.findViewById(R.id.year_number_input_layout);
        mBookNameInputLayout = (TextInputLayout) rootView.findViewById(R.id.book_name_input_layout);
        mPublisherNameInputLayout = (TextInputLayout) rootView.findViewById(R.id.publisher_name_input_layout);
        mSpinner = (Spinner) rootView.findViewById(R.id.category_spinner);
        mQuoteText = mQuoteTextInputLayout.getEditText();
        mBookName = mBookNameInputLayout.getEditText();
        mAuthorName = mAuthorNameInputLayout.getEditText();
        mPageNumber = mPageNumberInputLayout.getEditText();
        mYearNumber = mYearNumberInputLayout.getEditText();
        mPublishName = mPublisherNameInputLayout.getEditText();
    }

    private void hideFieldForMyQuotes(View rootView) {
        if (isAdded()) {
            if (mQuoteType.equals(Types.MY_QUOTE)) {
                mBookNameInputLayout.setVisibility(View.GONE);
                mAuthorNameInputLayout.setVisibility(View.GONE);
                mPageNumberInputLayout.setVisibility(View.GONE);
                mYearNumberInputLayout.setVisibility(View.GONE);
                mPublisherNameInputLayout.setVisibility(View.GONE);
            }
        }
    }

    private void createQuoteCategoryListForSpinner(List<QuoteCategory> quoteCategoryList) {
        mAllCategories = new ArrayList<>();
        if (quoteCategoryList != null && !quoteCategoryList.isEmpty()) {
            for (QuoteCategory currentCategory : quoteCategoryList) {
                if (currentCategory != null) {
                    String category = currentCategory.getCategoryName();
                    mAllCategories.add(category.toUpperCase());
                }
            }
            if (isAdded()) {
                mAllCategories.add(getString(R.string.spinner_add_category));
                mAllCategories.add(getString(R.string.spinner_hint));
            }
        } else {
            if (isAdded()) {
                mAllCategories.add(getString(R.string.spinner_add_category));
                mAllCategories.add(getString(R.string.spinner_hint));
            }
        }
    }

    private void setSpinnerOnCurrentPosition() {
        if (mSelectedValueOfCategory == null) {
            createSpinnerAdapter();
            if (mCurrentCategory != null) {
                mSpinner.setSelection(mSpinnerAdapter.getPosition(mCurrentCategory.toUpperCase()));
            }
        } else {
            if (!mAllCategories.contains(mSelectedValueOfCategory)) {
                mAllCategories.add(0, mSelectedValueOfCategory);
            }
            createSpinnerAdapter();
            createSpinnerAdapter();
            if (isAdded()) {
                if (!mSelectedValueOfCategory.equals(getString(R.string.spinner_hint))) {
                    mSpinner.setSelection(mSpinnerAdapter.getPosition(mSelectedValueOfCategory));
                }
            }
        }
    }

    private void fillFieldsWithQuoteEditData(final Bundle savedInstanceState) {
        if (mQuoteIdForEdit != -1) {
            mQuoteTexts.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
                @Override
                public void onChange(RealmResults<QuoteText> element) {
                    if (element.size() > 0) {
                        FillViewsWithCurrentQuoteDataHelper.fillViewsWithCurrentQuoteData(element,
                                mQuoteText, mBookName, mAuthorName, mPageNumber, mPublishName, mYearNumber, mQuoteType);
                        setSpinnerSelectionOnCurrentCategory(element);
                    }
                    updateFragmentFieldsWhenRotate(savedInstanceState);
                }
            });
        }
    }

    private void setSpinnerSelectionOnCurrentCategory(RealmResults<QuoteText> element) {
        QuoteText quote = element.first();
        String currentQuoteCategory = quote.getCategory().getCategoryName();
        if (mAllCategories != null && !mAllCategories.isEmpty()) {
            mSpinner.setSelection(mSpinnerAdapter.getPosition(currentQuoteCategory.toUpperCase()));
        }
    }

    private void updateFragmentFieldsWhenRotate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mQuoteText.setText(savedInstanceState.getString(QUOTE_TEXT_SAVE_INSTANCE));
            mBookName.setText(savedInstanceState.getString(QUOTE_BOOK_NAME_SAVE_INSTANCE));
            mAuthorName.setText(savedInstanceState.getString(QUOTE_BOOK_AUTHOR_SAVE_INSTANCE));
            mPageNumber.setText(savedInstanceState.getString(QUOTE_PAGE_SAVE_INSTANCE));
            mYearNumber.setText(savedInstanceState.getString(QUOTE_YEAR_SAVE_INSTANCE));
            mPublishName.setText(savedInstanceState.getString(QUOTE_PUBLISHER_NAME_SAVE_INSTANCE));
            updateSpinnerSelection();
        }
    }

    private void updateSpinnerSelection() {
        if (mSelectedValueOfCategory != null) {
            if (!mAllCategories.contains(mSelectedValueOfCategory)) {
                mAllCategories.add(0, mSelectedValueOfCategory);
            }
            createSpinnerAdapter();
            if (isAdded()) {
                if (!mSelectedValueOfCategory.equals(getString(R.string.spinner_hint))) {
                    mSpinner.setSelection(mSpinnerAdapter.getPosition(mSelectedValueOfCategory));
                }
            }
        }
        if (mCurrentCategory != null) {
            mSpinner.setSelection(mSpinnerAdapter.getPosition(mCurrentCategory.toUpperCase()));
        }
    }

    private void setListenerToSpinner() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals(getString(R.string.spinner_add_category))) {
                    createAddCategoryDialog();
                } else {
                    mSelectedValueOfCategory = selectedItem;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createAddCategoryDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
        mDialogBuilder.setView(dialogView);
        final EditText userInput = (EditText) dialogView.findViewById(R.id.input_text);
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_add_category_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String currentUserInput = userInput.getText().toString();
                                mAllCategories.add(0, currentUserInput);
                                mSpinnerAdapter.clear();
                                mSpinnerAdapter.addAll(mAllCategories);
                                mSpinner.setSelection(0);
                                mSelectedValueOfCategory = currentUserInput;
                            }
                        })
                .setNegativeButton(getString(R.string.dialog_add_category_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                setSpinnerOnCurrentPosition();
                            }
                        });
        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }

    private void createSpinnerAdapter() {
        if (isAdded()) {
            // Set hint for mSpinner
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
        mSpinnerAdapter.addAll(mAllCategories);
        mSpinner.setAdapter(mSpinnerAdapter);
        mSpinner.setSelection(mSpinnerAdapter.getCount());
    }

    /**
     * Method checks if user choose hint in mSpinner.
     *
     * @return true if user choose hint and false else if.
     */
    public boolean isSpinnerSelectedItemHint() {
        return mSelectedValueOfCategory.equals(getString(R.string.spinner_hint));
    }

    /**
     * Method checks if main EditText is empty or not.
     *
     * @return false if EditText not empty and true if else.
     */
    public boolean isEditTextEmpty() {
        mCurrentQuoteText = mQuoteText.getText().toString();
        if (TextUtils.isEmpty(mCurrentQuoteText)) {
            mQuoteTextInputLayout.setError(getString(R.string.quote_text_empty_error));
            return true;
        }
        if (isAdded()) {
            if (!mQuoteType.equals(Types.MY_QUOTE)) {
                mCurrentAuthorName = mAuthorName.getText().toString();
                if (TextUtils.isEmpty(mCurrentAuthorName)) {
                    mAuthorNameInputLayout.setError(getString(R.string.author_name_empty_error));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isNumbersPositive() {
        return isCurrentNumberPositive(mPageNumber, mPageNumberInputLayout) &&
                isCurrentNumberPositive(mYearNumber, mYearNumberInputLayout);
    }

    private boolean isCurrentNumberPositive(EditText editText, TextInputLayout textInputLayout) {
        String currentValue = editText.getText().toString();
        if (!currentValue.isEmpty() && !currentValue.equals(getString(R.string.default_value)) && Integer.valueOf(currentValue) < 0) {
            textInputLayout.setError(getString(R.string.page_number_error));
            return false;
        }
        return true;
    }

    /**
     * Method creates quote properties map for QuoteCreator class and pass map to it.
     */
    public void createMapOfQuoteProperties() {
        Calendar currentCreateDate = Calendar.getInstance();
        String currentDate = String.format("%1$td %1$tb %1$tY", currentCreateDate);
        HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties = new HashMap<>();
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_TEXT, mCurrentQuoteText);
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_CATEGORY, mSelectedValueOfCategory);
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_CREATION_DATE, currentDate);
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_TYPE, mQuoteType);
        final String currentPageNumber;
        final String currentYearNumber;
        final String currentPublishName;
        if (!mQuoteType.equals(Types.MY_QUOTE)) {
            currentPageNumber = mPageNumber.getText().toString();
            currentYearNumber = mYearNumber.getText().toString();
            currentPublishName = mPublishName.getText().toString();
            mCurrentBookName = mBookName.getText().toString();
            mapOfQuoteProperties.put(QuotePropertiesEnum.BOOK_NAME, emptyTextCheck(mCurrentBookName));
            mapOfQuoteProperties.put(QuotePropertiesEnum.BOOK_AUTHOR, mCurrentAuthorName);
            mapOfQuoteProperties.put(QuotePropertiesEnum.PAGE_NUMBER, emptyTextCheck(currentPageNumber));
            mapOfQuoteProperties.put(QuotePropertiesEnum.YEAR_NUMBER, emptyTextCheck(currentYearNumber));
            mapOfQuoteProperties.put(QuotePropertiesEnum.PUBLISHER_NAME, emptyTextCheck(currentPublishName));
        }
        if (mQuoteIdForEdit != -1) {
            mQuoteDataRepository.saveChangedQuote(mQuoteIdForEdit, mapOfQuoteProperties);
        } else {
            mQuoteDataRepository.saveQuote(mapOfQuoteProperties);
        }
    }

    /**
     * Method checks is current value empty or equals "" and set "-" if it is.
     *
     * @param currentValue
     * @return current value
     */
    private String emptyTextCheck(String currentValue) {
        if (currentValue.isEmpty() || currentValue.equals("")) {
            currentValue = getString(R.string.default_value);
        }
        return currentValue;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUOTE_TYPE_BUNDLE, mQuoteType);
        outState.putLong(QUOTE_ID_BUNDLE, mQuoteIdForEdit);
        outState.putString(QUOTE_CATEGORY_NEW_INSTANCE, mCurrentCategory);
        outState.putString(QUOTE_TEXT_SAVE_INSTANCE, mQuoteText.getText().toString());
        outState.putString(QUOTE_BOOK_NAME_SAVE_INSTANCE, mBookName.getText().toString());
        outState.putString(QUOTE_BOOK_AUTHOR_SAVE_INSTANCE, mAuthorName.getText().toString());
        outState.putString(QUOTE_PUBLISHER_NAME_SAVE_INSTANCE, mPublishName.getText().toString());
        outState.putString(QUOTE_YEAR_SAVE_INSTANCE, mYearNumber.getText().toString());
        outState.putString(QUOTE_PAGE_SAVE_INSTANCE, mPageNumber.getText().toString());
        outState.putString(QUOTE_CATEGORY_VALUE_SAVE_INSTANCE, mSelectedValueOfCategory);
    }

    public static AddQuoteFragment newInstance(Long currentQuoteTextId, String quoteType, String currentCategory) {
        Bundle args = new Bundle();
        args.putSerializable(QUOTE_ID_NEW_INSTANCE, currentQuoteTextId);
        args.putSerializable(QUOTE_TYPE_NEW_INSTANCE, quoteType);
        args.putSerializable(QUOTE_CATEGORY_NEW_INSTANCE, currentCategory);
        AddQuoteFragment fragment = new AddQuoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mQuoteDataRepository.closeDbConnect();
    }
}
