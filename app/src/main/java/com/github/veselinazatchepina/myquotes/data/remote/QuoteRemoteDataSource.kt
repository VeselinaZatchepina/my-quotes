package com.github.veselinazatchepina.myquotes.data.remote

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
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

    override fun saveQuoteData(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        TODO("we don't need remote now")
    }
}