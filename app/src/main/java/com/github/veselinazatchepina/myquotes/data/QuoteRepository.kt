package com.github.veselinazatchepina.myquotes.data

import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteCategory
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
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

    override fun getQuoteCategories(quoteType: String): Flowable<List<QuoteCategory>> {
        return quoteLocalDataSource.getQuoteCategories(quoteType)
    }

    override fun saveQuoteData(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        quoteLocalDataSource.saveQuoteData(mapOfQuoteProperties, authors)
    }

    override fun getAllQuotes(): Flowable<List<Quote>> {
        return quoteLocalDataSource.getAllQuotes()
    }

    override fun getQuotesByQuoteType(quoteType: String): Flowable<List<Quote>> {
        return quoteLocalDataSource.getQuotesByQuoteType(quoteType)
    }

    override fun getQuotesByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String): Flowable<List<Quote>> {
        return quoteLocalDataSource.getQuotesByQuoteTypeAndQuoteCategory(quoteType, quoteCategory)
    }

    override fun getQuoteById(quoteId: Long): Flowable<Quote> {
        return quoteLocalDataSource.getQuoteById(quoteId)
    }
}