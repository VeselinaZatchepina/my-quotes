package com.github.veselinazatchepina.myquotes.quotecategories

import android.util.Log
import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.model.QuoteCategoryModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class QuoteCategoriesPresenter(val quoteDataSource: QuoteDataSource,
                               val quoteCategoriesView: QuoteCategoriesContract.View) : QuoteCategoriesContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        quoteCategoriesView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun getQuoteCategoriesList(quoteType: String) {
        compositeDisposable.add(quoteDataSource.getQuoteCategories(quoteType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<QuoteCategoryModel>>() {
                    override fun onNext(list: List<QuoteCategoryModel>?) {
                        if (list != null) {
                            quoteCategoriesView.showQuoteCategoriesList(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun deleteQuoteCategory(quoteType: String, quoteCategory: String) {
        compositeDisposable.add(Observable.fromCallable {
            quoteDataSource.deleteQuoteCategory(quoteType, quoteCategory)
        }.subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("DELETE_QUOTE_CAT", "$quoteType $quoteCategory OK")
                }, {
                    Log.d("DELETE_QUOTE_CAT", "ERROR")
                }, {
                    Log.d("DELETE_QUOTE_CAT", "COMPLETE")
                }))
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}