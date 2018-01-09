package com.github.veselinazatchepina.myquotes.data.remote

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteCategory
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

    override fun getQuoteCategories(quoteType: String): Flowable<List<QuoteCategory>> {
        TODO("we don't need remote now")
    }

    override fun saveQuoteData(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        TODO("we don't need remote now")
    }

    override fun getAllQuotes(): Flowable<List<Quote>> {
        TODO("we don't need remote now")
    }

    override fun getQuotesByQuoteType(quoteType: String): Flowable<List<Quote>> {
        TODO("we don't need remote now")
    }

    override fun getQuotesByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String): Flowable<List<Quote>> {
        TODO("we don't need remote now")
    }

    override fun getQuoteById(quoteId: Long): Flowable<Quote> {
        TODO("we don't need remote now")
    }

}