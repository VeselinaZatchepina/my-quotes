package com.developer.cookie.myquote.quote.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.model.Category;
import com.developer.cookie.myquote.quote.QuoteCreator;
import com.developer.cookie.myquote.quote.QuotePropertiesEnum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddQuoteFragment extends Fragment {

    private static final String LOG_TAG = AddQuoteFragment.class.getSimpleName();

    private Realm realm;

    private View rootView;

    private FloatingActionButton fab;

    private String valueOfCategory;

    private List<String> listOfAllCategory;


    public AddQuoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_quote, container, false);
        // Create the Realm instance
        realm = Realm.getDefaultInstance();

        //Work with Spinner
        listOfAllCategory = new ArrayList<>();
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.category_spinner);
        RealmResults<Category> listOfCategory = realm.where(Category.class).findAll();
        if (listOfCategory != null || !listOfCategory.isEmpty()) {
            for (int i = 0; i < listOfCategory.size(); i++) {
                Category currentCategory = listOfCategory.get(i);
                if (currentCategory != null) {
                    String category = currentCategory.getCategory();
                    listOfAllCategory.add(category);
                }
            }
            listOfAllCategory.add("+ Add new category");
            listOfAllCategory.add("Select category");
        } else {
            listOfAllCategory.add("+ Add new category");
            listOfAllCategory.add("Select category");
        }
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) ;
        spinnerAdapter.addAll(listOfAllCategory);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(spinnerAdapter.getCount()); //set the hint the default selection so it appears on launch.

        //Add listener to spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("+ Add new category")) {
                    // create dialog for add category
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
                                            listOfAllCategory.add(0, currentUserInput);
                                            spinnerAdapter.clear();
                                            spinnerAdapter.addAll(listOfAllCategory);
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

        //Create quote data
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMapOfQuoteProperties();
                replaceFragment();
            }
        });

        return rootView;
    }

    private void createMapOfQuoteProperties() {
        EditText quoteText = (EditText) rootView.findViewById(R.id.quote_text);
        final EditText bookName = (EditText) rootView.findViewById(R.id.book_name);
        EditText authorName = (EditText) rootView.findViewById(R.id.author_name);
        EditText pageNumber = (EditText) rootView.findViewById(R.id.page_number);
        EditText yearNumber = (EditText) rootView.findViewById(R.id.year_number);
        EditText publishName = (EditText) rootView.findViewById(R.id.publish_name);

        final String currentQuoteText = quoteText.getText().toString();
        final String currentBookName = bookName.getText().toString();
        final String currentAuthorName = authorName.getText().toString();
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
        mapOfQuoteProperties.put(QuotePropertiesEnum.PUBLISH_NAME, currentPublishName);
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_CREATE_DATE, String.valueOf(currentMillis));
        mapOfQuoteProperties.put(QuotePropertiesEnum.QUOTE_TYPE, "MyQuote");

        QuoteCreator quoteCreator = new QuoteCreator();
        quoteCreator.createAndSaveQuote(mapOfQuoteProperties);
    }

    private void replaceFragment() {

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.remove(this)
                           .add(R.id.container, new QuoteCategoryFragment()).commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp, getActivity().getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
