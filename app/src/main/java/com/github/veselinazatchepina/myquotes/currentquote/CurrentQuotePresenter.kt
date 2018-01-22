package com.github.veselinazatchepina.myquotes.currentquote

import android.util.Log
import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.BookAuthor
import com.github.veselinazatchepina.myquotes.data.local.entity.BookReleaseYear
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class CurrentQuotePresenter(val quoteDataSource: QuoteDataSource,
                            val currentQuoteFragment: CurrentQuoteContract.View) : CurrentQuoteContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        currentQuoteFragment.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun getBookAuthors(bookAuthorId: List<Long>) {
        compositeDisposable.add(quoteDataSource.getBookAuthorsByIds(bookAuthorId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<BookAuthor>>() {
                    override fun onNext(list: List<BookAuthor>?) {
                        if (list != null) {
                            currentQuoteFragment.showBookAuthors(list)
                            Log.v("LIST_SIZE", list.size.toString() + "OK")
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getBookReleaseYear(yearIds: List<Long>) {
        compositeDisposable.add(quoteDataSource.getBookReleaseYearsByIds(yearIds).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<BookReleaseYear>>() {
                    override fun onNext(list: List<BookReleaseYear>?) {
                        if (list != null) {
                            currentQuoteFragment.showBookReleaseYears(list)
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