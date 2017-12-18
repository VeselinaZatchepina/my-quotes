package com.github.veselinazatchepina.myquotes.data

import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory
import io.reactivex.Flowable


class QuoteRepository private constructor(val quoteLocalDataSource: QuoteDataSource,
                                          val quoteRemoteDataSource: QuoteDataSource) : QuoteDataSource {

    companion object {
        private var INSTANCE: QuoteRepository? = null

        fun getInstance(quoteLocalDataSource: QuoteDataSource,
                        quoteRemoteDataSource: QuoteDataSource): QuoteRepository {
            if (INSTANCE == null) {
                INSTANCE = QuoteRepository(quoteLocalDataSource, quoteRemoteDataSource)
            }
            return INSTANCE!!
        }
    }

    override fun getBookCategories(): Flowable<List<BookCategory>> {
        return quoteLocalDataSource.getBookCategories()
    }
}