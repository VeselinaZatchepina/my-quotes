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
import com.developer.cookie.myquote.quote.Types;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * QuoteDataRepository helps to group queries to Realm.
 */
public class QuoteDataRepository implements QuoteRepository {

    private static final String LOG_TAG = QuoteDataRepository.class.getSimpleName();
    private final Realm mRealm;

    public QuoteDataRepository() {
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public RealmResults<QuoteCategory> getListOfQuoteCategories(String quoteType) {
        return mRealm.where(QuoteCategory.class).equalTo("type.type", quoteType).findAllSortedAsync("id");
    }

    @Override
    public void saveQuote(final HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                QuoteType quoteType = saveQuoteType(realm, mapOfQuoteProperties);
                QuoteCategory quoteCategory = saveQuoteCategory(realm, mapOfQuoteProperties, quoteType);
                QuoteCreationDate quoteCreationDate = saveQuoteCreationDate(realm, mapOfQuoteProperties);
                QuoteText quoteText = saveQuoteText(realm, mapOfQuoteProperties, quoteCategory, quoteType, quoteCreationDate);
                if (quoteType.getTypeValue().equals(Types.BOOK_QUOTE)) {
                    BookPublisher bookPublisher = saveBookPublisher(realm, mapOfQuoteProperties);
                    BookPublicationYear bookPublicationYear = saveBookPublicationYear(realm, mapOfQuoteProperties);
                    BookAuthor bookAuthor = saveBookAuthor(realm, mapOfQuoteProperties);
                    BookName bookName = saveBookName(realm, mapOfQuoteProperties, bookPublisher, bookPublicationYear, bookAuthor);
                    BookPage bookPage = saveBookPage(realm, mapOfQuoteProperties);
                    addFieldsToQuoteText(quoteText, bookName, bookPage);
                }
            }
        });
    }

    private QuoteCreationDate saveQuoteCreationDate(Realm realm, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        QuoteCreationDate quoteCreationDate = realm.createObject(QuoteCreationDate.class);
        quoteCreationDate.setId(getNextKey(quoteCreationDate, realm));
        quoteCreationDate.setQuoteDateValue(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_CREATION_DATE));
        return quoteCreationDate;
    }

    private BookPublisher saveBookPublisher(Realm realm, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        BookPublisher bookPublisher = realm.createObject(BookPublisher.class);
        bookPublisher.setId(getNextKey(bookPublisher, realm));
        bookPublisher.setPublisherName(mapOfQuoteProperties.get(QuotePropertiesEnum.PUBLISHER_NAME));
        return bookPublisher;
    }

    private BookPublicationYear saveBookPublicationYear(Realm realm, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        BookPublicationYear bookPublicationYear = realm.createObject(BookPublicationYear.class);
        bookPublicationYear.setId(getNextKey(bookPublicationYear, realm));
        bookPublicationYear.setYearNumber(mapOfQuoteProperties.get(QuotePropertiesEnum.YEAR_NUMBER));
        return bookPublicationYear;
    }

    private BookAuthor saveBookAuthor(Realm realm, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        BookAuthor bookAuthor = realm.createObject(BookAuthor.class);
        bookAuthor.setId(getNextKey(bookAuthor, realm));
        bookAuthor.setBookAuthorName(mapOfQuoteProperties.get(QuotePropertiesEnum.BOOK_AUTHOR));
        return bookAuthor;
    }

    private QuoteType saveQuoteType(Realm realm, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        return checkAndGetCurrentType(realm, mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_TYPE));
    }

    private QuoteCategory saveQuoteCategory(Realm realm, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties, QuoteType quoteType) {
        String valueOfCategory = mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_CATEGORY).toLowerCase();
        QuoteCategory quoteCategory = checkAndGetCurrentCategory(realm, valueOfCategory, quoteType.getTypeValue());
        quoteCategory.setType(quoteType);
        return quoteCategory;
    }

    private BookName saveBookName(Realm realm, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties,
                                  BookPublisher bookPublisher, BookPublicationYear bookPublicationYear, BookAuthor bookAuthor) {
        BookName bookName = realm.createObject(BookName.class);
        bookName.setId(getNextKey(bookName, realm));
        bookName.setBookNameValue(mapOfQuoteProperties.get(QuotePropertiesEnum.BOOK_NAME));
        bookName.setPublisher(bookPublisher);
        bookName.setYear(bookPublicationYear);
        bookName.getBookAuthors().add(bookAuthor);
        return bookName;
    }

    private BookPage saveBookPage(Realm realm, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        BookPage bookPage = realm.createObject(BookPage.class);
        bookPage.setId(getNextKey(bookPage, realm));
        bookPage.setPageNumber(mapOfQuoteProperties.get(QuotePropertiesEnum.PAGE_NUMBER));
        return bookPage;
    }

    private QuoteText saveQuoteText(Realm realm, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties,
                                    QuoteCategory quoteCategory, QuoteType quoteType, QuoteCreationDate quoteCreationDate) {
        QuoteText quoteText = realm.createObject(QuoteText.class);
        quoteText.setId(getNextKey(quoteText, realm));
        quoteText.setQuoteText(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_TEXT));
        quoteText.setCategory(quoteCategory);
        quoteText.setType(quoteType);
        quoteText.setDate(quoteCreationDate);
        return quoteText;
    }

    private QuoteText addFieldsToQuoteText(QuoteText quoteText, BookName bookName, BookPage bookPage) {
        quoteText.setBookName(bookName);
        quoteText.setPage(bookPage);
        return quoteText;
    }

    /**
     * Method is used for increment "id".
     *
     * @param currentClass current class
     * @param realm current db
     * @return id
     */
    private int getNextKey(RealmObject currentClass, Realm realm) {
        int id;
        try {
            id = realm.where(currentClass.getClass()).max("id").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }

    /**
     * Method checks if current category existent and returns it or creates new category.
     *
     * @param realm current db
     * @param valueOfCategory user value of category.
     * @param quoteType current quote type
     * @return quote category
     */
    private QuoteCategory checkAndGetCurrentCategory(Realm realm, String valueOfCategory, String quoteType) {
        RealmResults<QuoteCategory> quoteCategoryResults = realm.where(QuoteCategory.class)
                .equalTo("type.type", quoteType)
                .contains("category", valueOfCategory)
                .findAll();
        QuoteCategory quoteCategory;
        if (quoteCategoryResults == null || quoteCategoryResults.isEmpty()) {
            quoteCategory = createQuoteCategory(realm, valueOfCategory, quoteType);
        } else {
            quoteCategory = quoteCategoryResults.first();
            changeQuoteCountCurrentCategory(quoteCategory);
        }
        return quoteCategory;
    }

    private QuoteCategory createQuoteCategory(Realm realm, String valueOfCategory, String quoteType) {
        QuoteCategory quoteCategory = realm.createObject(QuoteCategory.class);
        quoteCategory.setId(getNextKey(quoteCategory, realm));
        quoteCategory.setCategoryName(valueOfCategory);
        quoteCategory.setQuoteCountCurrentCategory(quoteCategory.getQuoteCountCurrentCategory() + 1);
        quoteCategory.setType(checkAndGetCurrentType(realm, quoteType));
        return quoteCategory;
    }

    private void changeQuoteCountCurrentCategory(QuoteCategory quoteCategory) {
        quoteCategory.setQuoteCountCurrentCategory(
                quoteCategory.getQuoteCountCurrentCategory() + 1);
    }

    /**
     * Method checks if current quote type existent and returns it or creates new type.
     *
     * @param realm current db
     * @param quoteTypeValue current quote type
     * @return QuoteType object
     */
    private QuoteType checkAndGetCurrentType(Realm realm, String quoteTypeValue) {
        RealmResults<QuoteType> quoteTypes = realm.where(QuoteType.class)
                .equalTo("type", quoteTypeValue)
                .findAll();
        QuoteType quoteType;
        if (quoteTypes == null || quoteTypes.isEmpty()) {
            quoteType = createQuoteType(realm, quoteTypeValue);
        } else {
            quoteType = quoteTypes.first();
        }
        return quoteType;
    }

    private QuoteType createQuoteType(Realm realm, String quoteTypeValue) {
        QuoteType quoteType = realm.createObject(QuoteType.class);
        quoteType.setId(getNextKey(quoteType, realm));
        quoteType.setTypeValue(quoteTypeValue);
        return quoteType;
    }

    @Override
    public RealmResults<QuoteText> getListOfQuotesByCategory(String category, String type) {
        return mRealm.where(QuoteText.class)
                .equalTo("type.type", type)
                .equalTo("category.category", category)
                .findAllAsync();
    }

    @Override
    public RealmResults<QuoteText> getQuoteByQuoteId(Long quoteTextId) {
        return mRealm.where(QuoteText.class).equalTo("id", quoteTextId).findAllAsync();
    }

    @Override
    public void saveChangedQuote(final long quoteTextId, final HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                String quoteType = mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_TYPE);
                RealmResults<QuoteText> quoteTexts = realm.where(QuoteText.class).equalTo("id", quoteTextId).findAll();
                QuoteText currentQuoteText = quoteTexts.first();
                updateQuoteText(currentQuoteText, mapOfQuoteProperties);
                updateCategory(realm, currentQuoteText, mapOfQuoteProperties, quoteType);
                updateDate(currentQuoteText, mapOfQuoteProperties);
                if (quoteType.equals(Types.BOOK_QUOTE)) {
                    updateBookName(currentQuoteText, mapOfQuoteProperties);
                    updateBookPublisher(currentQuoteText, mapOfQuoteProperties);
                    updateBookPublicationYear(currentQuoteText, mapOfQuoteProperties);
                    updateBookAuthor(realm, currentQuoteText, mapOfQuoteProperties);
                    updateBookPage(currentQuoteText, mapOfQuoteProperties);
                }
            }
        });
    }

    private void updateQuoteText(QuoteText quoteText, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        quoteText.setQuoteText(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_TEXT));
    }

    private void updateBookName(QuoteText quoteText, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        BookName bookName = quoteText.getBookName();
        bookName.setBookNameValue(mapOfQuoteProperties.get(QuotePropertiesEnum.BOOK_NAME));
    }

    private void updateBookPublisher(QuoteText quoteText, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        BookPublisher bookPublisher = quoteText.getBookName().getPublisher();
        bookPublisher.setPublisherName(mapOfQuoteProperties.get(QuotePropertiesEnum.PUBLISHER_NAME));
    }

    private void updateBookPublicationYear(QuoteText quoteText, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        BookPublicationYear bookPublicationYear = quoteText.getBookName().getYear();
        bookPublicationYear.setYearNumber(mapOfQuoteProperties.get(QuotePropertiesEnum.YEAR_NUMBER));
    }

    private void updateBookAuthor(Realm realm, QuoteText quoteText, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        BookAuthor bookAuthor = realm.createObject(BookAuthor.class);
        bookAuthor.setId(getNextKey(bookAuthor, realm));
        bookAuthor.setBookAuthorName(mapOfQuoteProperties.get(QuotePropertiesEnum.BOOK_AUTHOR));
        RealmList<BookAuthor> currentBookAuthors = quoteText.getBookName().getBookAuthors();
        currentBookAuthors.deleteAllFromRealm();
        currentBookAuthors.add(bookAuthor);
    }

    private void updateBookPage(QuoteText quoteText, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        BookPage bookPage = quoteText.getPage();
        bookPage.setPageNumber(mapOfQuoteProperties.get(QuotePropertiesEnum.PAGE_NUMBER));
    }

    private void updateDate(QuoteText quoteText, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        QuoteCreationDate quoteCreationDate = quoteText.getDate();
        quoteCreationDate.setQuoteDateValue(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_CREATION_DATE));
        quoteText.setDate(quoteCreationDate);
    }

    private void updateCategory(Realm realm, QuoteText quoteText, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties, String quoteType) {
        String valueOfCategory = mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_CATEGORY).toLowerCase();
        if (!valueOfCategory.equals(quoteText.getCategory().getCategoryName())) {
            QuoteCategory newQuoteCategory = checkAndGetCurrentCategory(realm, valueOfCategory, quoteType);
            updateQuoteCountLastCategory(quoteText);
            quoteText.setCategory(newQuoteCategory);
        }
    }

    private void updateQuoteCountLastCategory(QuoteText quoteText) {
        QuoteCategory quoteCategory = quoteText.getCategory();
        quoteCategory.setQuoteCountCurrentCategory(quoteCategory.getQuoteCountCurrentCategory() - 1);
        if (quoteCategory.getQuoteCountCurrentCategory() == 0) {
            quoteCategory.deleteFromRealm();
        }
    }

    @Override
    public void deleteQuoteByIdFromDb(final long currentQuoteTextId, final String quoteType) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<QuoteText> quoteTexts = realm.where(QuoteText.class)
                        .equalTo("id", currentQuoteTextId)
                        .findAll();
                QuoteText quoteText = quoteTexts.first();
                deleteQuoteFromDb(quoteText, quoteType);
            }
        });
    }

    private void deleteQuoteFromDb(QuoteText quoteText, String quoteType) {
        if (quoteType.equals("Book quote")) {
            quoteText.getBookName().getBookAuthors().deleteAllFromRealm();
            quoteText.getBookName().getYear().deleteFromRealm();
            quoteText.getBookName().getPublisher().deleteFromRealm();
            quoteText.getBookName().deleteFromRealm();
            quoteText.getPage().deleteFromRealm();
        }
        quoteText.getDate().deleteFromRealm();
        updateQuoteCountLastCategory(quoteText);
        quoteText.deleteFromRealm();
    }

    @Override
    public void deleteAllQuotesWithCurrentCategory(final String currentCategory, final String quoteType) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<QuoteText> quoteTexts = realm.where(QuoteText.class)
                        .equalTo("category.category", currentCategory)
                        .equalTo("type.type", quoteType)
                        .findAll();
                for (QuoteText quoteText : quoteTexts) {
                    deleteQuoteFromDb(quoteText, quoteType);
                }
            }
        });
    }

    @Override
    public RealmResults<QuoteText> getAllQuote() {
        return mRealm.where(QuoteText.class).findAllAsync();
    }

    @Override
    public void closeDbConnect() {
        mRealm.close();
    }
}
