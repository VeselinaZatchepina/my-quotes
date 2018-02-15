package com.github.veselinazatchepina.myquotes.coincidequotes

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class CoincideQuotesPresenter(val quoteDataSource: QuoteDataSource,
                              val coincideQuotesView: CoincideQuotesContract.View) : CoincideQuotesContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        coincideQuotesView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun getCoincideQuotesByInputText(inputText: String) {
        if (inputText.isNotEmpty()) {
            compositeDisposable.add(quoteDataSource.getCoincideQuotesByInputText(inputText).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSubscriber<List<Quote>>() {
                        override fun onNext(list: List<Quote>?) {
                            if (list != null) {
                                coincideQuotesView.showQuotes(list)
                            }
                        }

                        override fun onComplete() {

                        }

                        override fun onError(t: Throwable?) {

                        }

                    }))
        } else {
            coincideQuotesView.showQuotes(emptyList())
        }
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}