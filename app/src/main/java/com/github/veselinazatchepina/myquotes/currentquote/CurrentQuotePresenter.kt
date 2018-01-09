package com.github.veselinazatchepina.myquotes.currentquote

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class CurrentQuotePresenter(val quoteDataSource: QuoteDataSource,
                            val currentQuoteView: CurrentQuoteContract.View) : CurrentQuoteContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        currentQuoteView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun getQuoteById(quoteId: Long) {
        compositeDisposable.add(quoteDataSource.getQuoteById(quoteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<Quote>() {
                    override fun onNext(quote: Quote?) {
                        if (quote != null) {
                            currentQuoteView.showQuote(quote)
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