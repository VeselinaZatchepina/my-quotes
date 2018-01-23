package com.github.veselinazatchepina.myquotes.addquote

import android.util.Log
import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteCategory
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class AddQuotePresenter(val quoteDataSource: QuoteDataSource,
                        val addQuoteView: AddQuoteContract.View) : AddQuoteContract.Presenter {

    private var compositeDisposable: CompositeDisposable
    private var bookCategoriesForSpinner = ArrayList<String>()

    init {
        addQuoteView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun saveQuote(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        quoteDataSource.saveQuoteData(mapOfQuoteProperties, authors)
    }

    override fun getQuoteCategoriesList(quoteType: String) {
        compositeDisposable.add(quoteDataSource.getQuoteCategories(quoteType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<QuoteCategory>>() {
                    override fun onNext(list: List<QuoteCategory>?) {
                        if (list != null) {
                            if (bookCategoriesForSpinner.isEmpty()) {
                                bookCategoriesForSpinner.clear()
                                addQuoteView.defineCategorySpinner(defineBookCategoriesListForView(list))
                            } else {
                                bookCategoriesForSpinner.clear()
                                addQuoteView.updateCategorySpinner(defineBookCategoriesListForView(list))
                            }
                        } else {
                            Log.v("GET BOOK CATEGORIES", "NULL")
                        }
                    }

                    override fun onComplete() {
                        Log.v("GET BOOK CATEGORIES", "COMPLETE")
                    }

                    override fun onError(t: Throwable?) {
                        Log.v("GET BOOK CATEGORIES", "OOPS")
                    }
                }))
    }

    private fun defineBookCategoriesListForView(list: List<QuoteCategory>): List<String> {
        list.mapTo(bookCategoriesForSpinner) {
            Log.v("CATEGORIES", it.categoryName)
            it.categoryName.toUpperCase()
        }
        bookCategoriesForSpinner.add("+ add new category")
        bookCategoriesForSpinner.add("Select allQuoteData category")
        return bookCategoriesForSpinner
    }

    override fun addQuoteCategory(category: String) {
        bookCategoriesForSpinner.add(0, category)
        addQuoteView.updateCategorySpinner(bookCategoriesForSpinner)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }


}