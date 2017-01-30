package com.developer.cookie.myquote.quote;


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
import com.developer.cookie.myquote.database.model.BookAuthor;
import com.developer.cookie.myquote.database.model.BookName;
import com.developer.cookie.myquote.database.model.Category;
import com.developer.cookie.myquote.database.model.Page;
import com.developer.cookie.myquote.database.model.Publisher;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.database.model.Type;
import com.developer.cookie.myquote.database.model.Year;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
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
                Category typification = listOfCategory.get(i);
                if (typification != null) {
                    String currentCategory = typification.getCategory();
                    if (!listOfAllCategory.contains(currentCategory)) {
                        listOfAllCategory.add(currentCategory);
                    }
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
        //spinner.setOnItemSelectedListener(getActivity());
        //Log.v("SIZE", "size " + listOfAllCategory.size());


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
                                            listOfAllCategory.add(0, userInput.getText().toString());
                                            spinner.setSelection(0);
                                            spinnerAdapter.clear();
                                            spinnerAdapter.addAll(listOfAllCategory);
                                            //spinnerAdapter.notifyDataSetChanged();
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
            // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //Create and save quote
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndSaveRealmObject();
                replaceFragment();
            }
        });

        return rootView;
    }

    private void createAndSaveRealmObject() {

        EditText quoteText = (EditText) rootView.findViewById(R.id.quote_text);
        final EditText bookName = (EditText) rootView.findViewById(R.id.book_name);
        EditText authorName = (EditText) rootView.findViewById(R.id.author_name);
        EditText pageNumber = (EditText) rootView.findViewById(R.id.page_number);
        EditText yearNumber = (EditText) rootView.findViewById(R.id.year_number);
        EditText publishName = (EditText) rootView.findViewById(R.id.publish_name);

        final String currentQuoteText = quoteText.getText().toString();
        final String currentBookName = bookName.getText().toString();
        final String currentAuthorName = authorName.getText().toString();
        final int currentPageNumber = Integer.valueOf(pageNumber.getText().toString());
        final int currentYearNumber = Integer.valueOf(yearNumber.getText().toString());
        final String currentPublishName = publishName.getText().toString();

        //Write data to DB
        realm.executeTransactionAsync(new Realm.Transaction() {
                                     @Override
                                     public void execute(Realm realm) {
                                         Publisher publisherRealmObject = realm.createObject(Publisher.class);
                                         publisherRealmObject.setId(getNextKey(publisherRealmObject, realm));
                                         publisherRealmObject.setPublisherName(currentPublishName);

                                         Year yearRealmObject = realm.createObject(Year.class);
                                         yearRealmObject.setId(getNextKey(yearRealmObject, realm));
                                         yearRealmObject.setYearNumber(currentYearNumber);

                                         BookAuthor bookAuthorRealmObject = realm.createObject(BookAuthor.class);
                                         bookAuthorRealmObject.setId(getNextKey(bookAuthorRealmObject, realm));
                                         bookAuthorRealmObject.setBookAuthor(currentAuthorName);

                                         BookName bookNameRealmObject = realm.createObject(BookName.class);
                                         bookNameRealmObject.setId(getNextKey(bookNameRealmObject, realm));
                                         bookNameRealmObject.setBookName(currentBookName);
                                         bookNameRealmObject.setPublisher(publisherRealmObject);
                                         bookNameRealmObject.setYear(yearRealmObject);
                                         bookNameRealmObject.getBookAuthors().add(bookAuthorRealmObject);

                                         Page pageRealmObject = realm.createObject(Page.class);
                                         pageRealmObject.setId(getNextKey(pageRealmObject, realm));
                                         pageRealmObject.setPageNumber(currentPageNumber);

                                         RealmResults<Category> results = realm.where(Category.class)
                                                 .contains("category", valueOfCategory)
                                                 .findAll();
                                         Category typificationRealmObject;
                                         if (results == null || results.isEmpty()) {
                                             typificationRealmObject = realm.createObject(Category.class);
                                             typificationRealmObject.setId(getNextKey(typificationRealmObject, realm));
                                             typificationRealmObject.setCategory(valueOfCategory);
                                         } else {
                                             typificationRealmObject = results.get(0);
                                         }

                                         Type type = realm.createObject(Type.class);
                                         type.setId((getNextKey(type, realm)));
                                         type.setType("MyQuote");

                                         QuoteText quoteTextRealmObject = realm.createObject(QuoteText.class);
                                         quoteTextRealmObject.setId(getNextKey(quoteTextRealmObject, realm));
                                         quoteTextRealmObject.setQuoteText(currentQuoteText);
                                         quoteTextRealmObject.setBookName(bookNameRealmObject);
                                         quoteTextRealmObject.setPage(pageRealmObject);
                                         quoteTextRealmObject.setCategory(typificationRealmObject);
                                         quoteTextRealmObject.setType(type);

                                     }
                                 }
                , new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {

                    }
                });
    }

    private int getNextKey(RealmObject currentClass, Realm realm) {
        int id;

        try {
            id = realm.where(currentClass.getClass()).max("id").intValue() + 1;
        } catch(ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }

    private void replaceFragment() {

            Fragment quoteCategoryFragment = new QuoteCategoryFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, quoteCategoryFragment);
            ft.commit();

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
