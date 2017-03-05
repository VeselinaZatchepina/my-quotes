package com.developer.cookie.myquote.database;


import com.developer.cookie.myquote.database.model.BookAuthor;
import com.developer.cookie.myquote.database.model.BookName;
import com.developer.cookie.myquote.database.model.BookPage;
import com.developer.cookie.myquote.database.model.BookPublicationYear;
import com.developer.cookie.myquote.database.model.BookPublisher;
import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.database.model.QuoteCreationDate;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.database.model.QuoteType;
import com.developer.cookie.myquote.quote.QuotePropertiesEnum;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * QuoteDataRepository helps to abstract realm layer.
 */
public class QuoteDataRepository implements QuoteRepository {

    private static final String LOG_TAG = QuoteDataRepository.class.getSimpleName();
    private final Realm realm;

    public QuoteDataRepository() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public RealmResults<QuoteCategory> getListOfQuoteCategories() {
        return realm.where(QuoteCategory.class).findAllSortedAsync("id");
    }

    @Override
    public void saveQuote(final HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm realm) {
                                              // Create book publisher
                                              BookPublisher publisherRealmObject = realm.createObject(BookPublisher.class);
                                              publisherRealmObject.setId(getNextKey(publisherRealmObject, realm));
                                              publisherRealmObject.setPublisherName(mapOfQuoteProperties.get(QuotePropertiesEnum.PUBLISHER_NAME));
                                              // Create book publication year
                                              BookPublicationYear yearRealmObject = realm.createObject(BookPublicationYear.class);
                                              yearRealmObject.setId(getNextKey(yearRealmObject, realm));
                                              yearRealmObject.setYearNumber(mapOfQuoteProperties.get(QuotePropertiesEnum.YEAR_NUMBER));
                                              // Create book authors
                                              BookAuthor bookAuthorRealmObject = realm.createObject(BookAuthor.class);
                                              bookAuthorRealmObject.setId(getNextKey(bookAuthorRealmObject, realm));
                                              bookAuthorRealmObject.setBookAuthor(mapOfQuoteProperties.get(QuotePropertiesEnum.BOOK_AUTHOR));
                                              // Create book name
                                              BookName bookNameRealmObject = realm.createObject(BookName.class);
                                              bookNameRealmObject.setId(getNextKey(bookNameRealmObject, realm));
                                              bookNameRealmObject.setBookName(mapOfQuoteProperties.get(QuotePropertiesEnum.BOOK_NAME));
                                              bookNameRealmObject.setPublisher(publisherRealmObject);
                                              bookNameRealmObject.setYear(yearRealmObject);
                                              bookNameRealmObject.getBookAuthors().add(bookAuthorRealmObject);
                                              // Create book page
                                              BookPage pageRealmObject = realm.createObject(BookPage.class);
                                              pageRealmObject.setId(getNextKey(pageRealmObject, realm));
                                              pageRealmObject.setPageNumber(mapOfQuoteProperties.get(QuotePropertiesEnum.PAGE_NUMBER));
                                              // Create quote category
                                              String valueOfCategory = mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_CATEGORY);
                                              QuoteCategory categoryRealmObject = checkAndGetCurrentCategory(realm, valueOfCategory, 1);
                                              QuoteType type = realm.createObject(QuoteType.class);
                                              type.setId((getNextKey(type, realm)));
                                              type.setType(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_TYPE));
                                              // Create quote creation date
                                              QuoteCreationDate quoteDate = realm.createObject(QuoteCreationDate.class);
                                              quoteDate.setId(getNextKey(quoteDate, realm));
                                              quoteDate.setQuoteDate(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_CREATE_DATE));
                                              // Create quote text
                                              QuoteText quoteTextRealmObject = realm.createObject(QuoteText.class);
                                              quoteTextRealmObject.setId(getNextKey(quoteTextRealmObject, realm));
                                              quoteTextRealmObject.setQuoteText(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_TEXT));
                                              quoteTextRealmObject.setBookName(bookNameRealmObject);
                                              quoteTextRealmObject.setPage(pageRealmObject);
                                              quoteTextRealmObject.setCategory(categoryRealmObject);
                                              quoteTextRealmObject.setType(type);
                                              quoteTextRealmObject.setDate(quoteDate);
                                          }
                                      });
    }

    /**
     * Method is used for increment "id".
     * @param currentClass
     * @param realm
     * @return id
     */
    private int getNextKey(RealmObject currentClass, Realm realm) {
        int id;
        try {
            id = realm.where(currentClass.getClass()).max("id").intValue() + 1;
        } catch(ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }

    @Override
    public RealmResults<QuoteText> getListOfQuoteText() {
        return realm.where(QuoteText.class).findAllAsync();
    }

    @Override
    public RealmResults<QuoteText> getListOfQuoteTextByCategory(String category) {
        return realm.where(QuoteText.class)
                .equalTo("category.category", category).findAllAsync();
    }

    @Override
    public RealmResults<QuoteText> getQuoteTextObjectsByQuoteText(String quoteText) {
        return realm.where(QuoteText.class).equalTo("quoteText", quoteText).findAllAsync();
    }

    @Override
    public RealmResults<QuoteText> getQuoteTextObjectsByQuoteId(Long quoteTextId) {
        return realm.where(QuoteText.class).equalTo("id", quoteTextId).findAllAsync();
    }

    @Override
    public void saveChangedQuoteObject(final long quoteTextId, final HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<QuoteText> quoteTextRealmList = realm.where(QuoteText.class).equalTo("id", quoteTextId).findAll();
                QuoteText quoteTextObject = quoteTextRealmList.first();
                // Set quote text
                quoteTextObject.setQuoteText(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_TEXT));
                // Set book name
                BookName bookName = quoteTextObject.getBookName();
                bookName.setBookName(mapOfQuoteProperties.get(QuotePropertiesEnum.BOOK_NAME));
                // Set book publisher
                BookPublisher bookPublisher = bookName.getPublisher();
                bookPublisher.setPublisherName(mapOfQuoteProperties.get(QuotePropertiesEnum.PUBLISHER_NAME));
                // Set publication year
                BookPublicationYear year = bookName.getYear();
                year.setYearNumber(mapOfQuoteProperties.get(QuotePropertiesEnum.YEAR_NUMBER));
                // Set book authors
                BookAuthor bookAuthorRealmObject = realm.createObject(BookAuthor.class);
                bookAuthorRealmObject.setId(getNextKey(bookAuthorRealmObject, realm));
                bookAuthorRealmObject.setBookAuthor(mapOfQuoteProperties.get(QuotePropertiesEnum.BOOK_AUTHOR));
                RealmList<BookAuthor> bookAuthorRealmList = bookName.getBookAuthors();
                bookAuthorRealmList.deleteAllFromRealm();
                bookAuthorRealmList.add(bookAuthorRealmObject);
                //Set book page
                BookPage page = quoteTextObject.getPage();
                page.setPageNumber(mapOfQuoteProperties.get(QuotePropertiesEnum.PAGE_NUMBER));
                // Set category
                String valueOfCategory = mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_CATEGORY); // было: qw стало: qwerty
                int flag;
                if (valueOfCategory.equals(quoteTextObject.getCategory().getCategory())) {
                    flag = 0;
                } else {
                    flag = 1;
                }
                QuoteCategory categoryRealmObject = checkAndGetCurrentCategory(realm, valueOfCategory, flag); // qwerty
                if (flag == 1) {
                    QuoteCategory quoteCategory = quoteTextObject.getCategory();
                    quoteCategory.setQuoteCountCurrentCategory(quoteCategory.getQuoteCountCurrentCategory() - 1);
                    if (quoteCategory.getQuoteCountCurrentCategory() == 0) {
                        quoteCategory.deleteFromRealm();
                    }
                }
                quoteTextObject.setCategory(categoryRealmObject);
                // Set date
                QuoteCreationDate date = quoteTextObject.getDate();
                date.setQuoteDate(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_CREATE_DATE));
                quoteTextObject.setDate(date);
            }
        });
    }

    @Override
    public void deleteQuoteTextObjectById(final long currentQuoteTextId) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<QuoteText> quoteTexts = realm.where(QuoteText.class).equalTo("id", currentQuoteTextId).findAll();
                QuoteText quoteText = quoteTexts.first();
                deleteQuoteTextObject(quoteText);
            }
        });
    }

    @Override
    public void deleteAllQuotesWithCurrentCategory(final String currentCategory) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<QuoteText> quoteTexts = realm.where(QuoteText.class)
                        .equalTo("category.category", currentCategory).findAll();
                for (QuoteText quoteText : quoteTexts) {
                    deleteQuoteTextObject(quoteText);
                }
            }
        });
    }

    @Override
    public void closeDbConnect() {
        realm.close();
    }

    /**
     * Method checks if current category existent and returns it or creates new category.
     * @param realm
     * @param valueOfCategory user value of category.
     * @param flag is used for edit quote: flag = 1 if user choose other category, flag = 0 if quote stay the same category.
     * @return
     */
    private QuoteCategory checkAndGetCurrentCategory(Realm realm, String valueOfCategory, int flag) {
        RealmResults<QuoteCategory> results = realm.where(QuoteCategory.class)
                .contains("category", valueOfCategory)
                .findAll();
        QuoteCategory categoryRealmObject;
        if (results == null || results.isEmpty()) {
            categoryRealmObject = realm.createObject(QuoteCategory.class);
            categoryRealmObject.setId(getNextKey(categoryRealmObject, realm));
            categoryRealmObject.setCategory(valueOfCategory);
            categoryRealmObject.setQuoteCountCurrentCategory(
                    categoryRealmObject.getQuoteCountCurrentCategory() + 1);
        } else {
            categoryRealmObject = results.get(0);
            if (flag == 1) {
                categoryRealmObject.setQuoteCountCurrentCategory(
                        categoryRealmObject.getQuoteCountCurrentCategory() + 1);
            }
        }
        return categoryRealmObject;
    }

    private void deleteQuoteTextObject(QuoteText quoteText) {
        quoteText.getBookName().getBookAuthors().deleteAllFromRealm();
        quoteText.getBookName().getYear().deleteFromRealm();
        quoteText.getBookName().getPublisher().deleteFromRealm();
        quoteText.getBookName().deleteFromRealm();
        quoteText.getDate().deleteFromRealm();
        quoteText.getPage().deleteFromRealm();
        QuoteCategory quoteCategory = quoteText.getCategory();
        quoteCategory.setQuoteCountCurrentCategory(quoteCategory.getQuoteCountCurrentCategory() - 1);
        if (quoteCategory.getQuoteCountCurrentCategory() == 0) {
            quoteText.getCategory().deleteFromRealm();
        }
        quoteText.deleteFromRealm();
    }
}
