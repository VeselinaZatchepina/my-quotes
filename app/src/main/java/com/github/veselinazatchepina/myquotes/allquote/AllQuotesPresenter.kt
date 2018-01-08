package com.github.veselinazatchepina.myquotes.allquote

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class AllQuotesPresenter(val quoteDataSource: QuoteDataSource,
                         val allQuotesView: AllQuotesContract.View) : AllQuotesContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        allQuotesView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun getAllQuotes() {
        compositeDisposable.add(quoteDataSource.getAllQuotes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<Quote>>() {
                    override fun onNext(list: List<Quote>?) {
                        if (list != null) {
                            allQuotesView.showQuotes(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getQuotesByQuoteType(quoteType: String) {
        compositeDisposable.add(quoteDataSource.getQuotesByQuoteType(quoteType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<Quote>>() {
                    override fun onNext(list: List<Quote>?) {
                        if (list != null) {
                            allQuotesView.showQuotes(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getQuotesByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String) {
        compositeDisposable.add(quoteDataSource.getQuotesByQuoteTypeAndQuoteCategory(quoteType, quoteCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<Quote>>() {
                    override fun onNext(list: List<Quote>?) {
                        if (list != null) {
                            allQuotesView.showQuotes(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }


    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}