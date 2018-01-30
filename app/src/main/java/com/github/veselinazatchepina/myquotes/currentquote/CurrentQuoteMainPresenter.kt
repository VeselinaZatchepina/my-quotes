package com.github.veselinazatchepina.myquotes.currentquote

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.model.AllQuoteData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class CurrentQuoteMainPresenter(val quoteDataSource: QuoteDataSource,
                                val currentQuoteMainFragment: CurrentQuoteMainContract.View) : CurrentQuoteMainContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        currentQuoteMainFragment.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun getAllQuoteData() {
        compositeDisposable.add(quoteDataSource.getAllQuoteData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<AllQuoteData>>() {
                    override fun onNext(list: List<AllQuoteData>?) {
                        if (list != null) {
                            currentQuoteMainFragment.createViewPager(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getAllQuoteDataByQuoteType(quoteType: String) {
        compositeDisposable.add(quoteDataSource.getAllQuoteDataByQuoteType(quoteType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<AllQuoteData>>() {
                    override fun onNext(list: List<AllQuoteData>?) {
                        if (list != null) {
                            currentQuoteMainFragment.createViewPager(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))

    }

    override fun getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String) {
        compositeDisposable.add(quoteDataSource.getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType, quoteCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<AllQuoteData>>() {
                    override fun onNext(list: List<AllQuoteData>?) {
                        if (list != null) {
                            currentQuoteMainFragment.createViewPager(list)
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