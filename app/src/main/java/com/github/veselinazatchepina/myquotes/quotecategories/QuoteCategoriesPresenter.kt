package com.github.veselinazatchepina.myquotes.quotecategories

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import com.github.veselinazatchepina.myquotes.data.local.model.QuoteCategoryModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class QuoteCategoriesPresenter(val quoteDataSource: QuoteDataSource,
                               val bookCategoriesView: QuoteCategoriesContract.View) : QuoteCategoriesContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        bookCategoriesView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun getQuoteCategoriesList(quoteType: String) {
        compositeDisposable.add(quoteDataSource.getQuoteCategories(quoteType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<QuoteCategoryModel>>() {
                    override fun onNext(list: List<QuoteCategoryModel>?) {
                        if (list != null) {
                            bookCategoriesView.showQuoteCategoriesList(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getQuotesByCategory(categoryName: String): List<Quote> {
        return arrayListOf()
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}