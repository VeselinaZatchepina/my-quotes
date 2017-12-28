package com.github.veselinazatchepina.myquotes.addquote

import android.util.Log
import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.pojo.BookCategoriesAndQuoteType
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class AddQuotePresenter(val quoteDataSource: QuoteDataSource,
                        val addQuoteView: AddQuoteContract.View) : AddQuoteContract.Presenter {

    private var compositeDisposable: CompositeDisposable
    private val bookCategories = ArrayList<String>()

    init {
        addQuoteView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun saveQuote(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        quoteDataSource.saveQuoteData(mapOfQuoteProperties, authors)
    }

    override fun getBookCategoriesList(quoteType: String) {
        compositeDisposable.add(quoteDataSource.getBookCategories(quoteType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<BookCategoriesAndQuoteType>>() {
                    override fun onNext(list: List<BookCategoriesAndQuoteType>?) {
                        if (list != null) {
                            addQuoteView.defineCategorySpinner(defineBookCategoriesListForView(list))
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

    private fun defineBookCategoriesListForView(list: List<BookCategoriesAndQuoteType>): List<String> {
        list.map {
            it.bookCategories?.mapTo(bookCategories) {
                it.categoryName.toUpperCase()
            }
        }
        bookCategories.add("+ add new category")
        bookCategories.add("Select quote category")
        return bookCategories
    }

    override fun addBookCategory(category: String) {
        bookCategories.add(0, category)
        addQuoteView.updateCategorySpinner(bookCategories)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }


}