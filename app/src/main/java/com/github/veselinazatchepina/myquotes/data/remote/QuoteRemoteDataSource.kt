package com.github.veselinazatchepina.myquotes.data.remote

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.BookAuthor
import com.github.veselinazatchepina.myquotes.data.local.entity.BookReleaseYear
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import com.github.veselinazatchepina.myquotes.data.local.model.AllQuoteData
import com.github.veselinazatchepina.myquotes.data.local.model.QuoteCategoryModel
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

    override fun getQuoteCategories(quoteType: String): Flowable<List<QuoteCategoryModel>> {
        TODO("we don't need remote now")
    }

    override fun saveQuoteData(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        TODO("we don't need remote now")
    }

    override fun getAllQuotes(): Flowable<List<Quote>> {
        TODO("we don't need remote now")
    }

    override fun getQuotesByType(quoteType: String): Flowable<List<Quote>> {
        TODO("we don't need remote now")
    }

    override fun getQuotesByTypeAndCategory(quoteType: String, quoteCategory: String): Flowable<List<Quote>> {
        TODO("we don't need remote now")
    }

    override fun getAllQuoteData(): Flowable<List<AllQuoteData>> {
        TODO("we don't need remote now")
    }

    override fun getAllQuoteDataByQuoteType(quoteType: String): Flowable<List<AllQuoteData>> {
        TODO("we don't need remote now")
    }

    override fun getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String): Flowable<List<AllQuoteData>> {
        TODO("we don't need remote now")
    }

    override fun getQuotesByQuoteTextIfContains(quoteText: String): Flowable<List<Quote>> {
        TODO("we don't need remote now")
    }

    override fun getQuotesByTypeAndTextIfContains(quoteType: String, text: String): Flowable<List<Quote>> {
        TODO("we don't need remote now")
    }

    override fun getQuotesByTypeAndCategoryAndTextIfContains(quoteType: String, quoteCategory: String, text: String): Flowable<List<Quote>> {
        TODO("we don't need remote now")
    }

    override fun getBookAuthorsByIds(ids: List<Long>): Flowable<List<BookAuthor>> {
        TODO("we don't need remote now")
    }

    override fun getBookReleaseYearsByIds(ids: List<Long>): Flowable<List<BookReleaseYear>> {
        TODO("we don't need remote now")
    }

    override fun deleteQuote(qId: Long) {
        TODO("we don't need remote now")
    }

    override fun deleteQuoteCategory(quoteType: String, quoteCategory: String) {
        TODO("we don't need remote now")
    }

    override fun updateQuote(quoteId: Long, mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        TODO("we don't need remote now")
    }
}