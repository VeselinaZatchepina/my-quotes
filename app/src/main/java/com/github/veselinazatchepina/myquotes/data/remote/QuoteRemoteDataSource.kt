package com.github.veselinazatchepina.myquotes.data.remote

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory
import io.reactivex.Flowable


class QuoteRemoteDataSource private constructor() : QuoteDataSource {

    companion object {
        private var INSTANCE: QuoteRemoteDataSource? = null

        fun getInstance(): QuoteRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = QuoteRemoteDataSource()
            }
            return INSTANCE!!
        }
    }

    override fun getBookCategories(): Flowable<List<BookCategory>> {
        TODO("we don't need remote now")
    }
}