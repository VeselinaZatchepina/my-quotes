package com.github.veselinazatchepina.myquotes.data

import com.github.veselinazatchepina.myquotes.data.local.entity.BookAuthor
import com.github.veselinazatchepina.myquotes.data.local.entity.BookReleaseYear
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import com.github.veselinazatchepina.myquotes.data.local.model.AllQuoteData
import com.github.veselinazatchepina.myquotes.data.local.model.QuoteCategoryModel
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

    override fun getQuoteCategories(quoteType: String): Flowable<List<QuoteCategoryModel>> {
        return quoteLocalDataSource.getQuoteCategories(quoteType)
    }

    override fun saveQuoteData(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        quoteLocalDataSource.saveQuoteData(mapOfQuoteProperties, authors)
    }

    override fun getAllQuotes(): Flowable<List<Quote>> {
        return quoteLocalDataSource.getAllQuotes()
    }

    override fun getQuotesByType(quoteType: String): Flowable<List<Quote>> {
        return quoteLocalDataSource.getQuotesByType(quoteType)
    }

    override fun getQuotesByTypeAndCategory(quoteType: String, quoteCategory: String): Flowable<List<Quote>> {
        return quoteLocalDataSource.getQuotesByTypeAndCategory(quoteType, quoteCategory)
    }

    override fun getAllQuoteData(): Flowable<List<AllQuoteData>> {
        return quoteLocalDataSource.getAllQuoteData()
    }

    override fun getAllQuoteDataByQuoteType(quoteType: String): Flowable<List<AllQuoteData>> {
        return quoteLocalDataSource.getAllQuoteDataByQuoteType(quoteType)
    }

    override fun getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String): Flowable<List<AllQuoteData>> {
        return quoteLocalDataSource.getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType, quoteCategory)
    }

    override fun getQuotesByQuoteTextIfContains(quoteText: String): Flowable<List<Quote>> {
        return quoteLocalDataSource.getQuotesByQuoteTextIfContains(quoteText)
    }

    override fun getQuotesByTypeAndTextIfContains(quoteType: String, text: String): Flowable<List<Quote>> {
        return quoteLocalDataSource.getQuotesByTypeAndTextIfContains(quoteType, text)
    }

    override fun getQuotesByTypeAndCategoryAndTextIfContains(quoteType: String, quoteCategory: String, text: String): Flowable<List<Quote>> {
        return quoteLocalDataSource.getQuotesByTypeAndCategoryAndTextIfContains(quoteType, quoteCategory, text)
    }

    override fun getBookAuthorsByIds(ids: List<Long>): Flowable<List<BookAuthor>> {
        return quoteLocalDataSource.getBookAuthorsByIds(ids)
    }

    override fun getBookReleaseYearsByIds(ids: List<Long>): Flowable<List<BookReleaseYear>> {
        return quoteLocalDataSource.getBookReleaseYearsByIds(ids)
    }

    override fun deleteQuote(qId: Long) {
        quoteLocalDataSource.deleteQuote(qId)
    }

    override fun deleteQuoteCategory(quoteType: String, quoteCategory: String) {
        quoteLocalDataSource.deleteQuoteCategory(quoteType, quoteCategory)
    }

    override fun updateQuote(quoteId: Long,
                             mapOfQuoteProperties: HashMap<QuoteProperties, String>,
                             authors: List<String>) {
        quoteLocalDataSource.updateQuote(quoteId, mapOfQuoteProperties, authors)
    }
}