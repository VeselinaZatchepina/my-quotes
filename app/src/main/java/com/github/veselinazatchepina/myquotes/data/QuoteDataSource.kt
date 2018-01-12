package com.github.veselinazatchepina.myquotes.data

import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteCategory
import com.github.veselinazatchepina.myquotes.data.local.pojo.AllQuoteData
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import io.reactivex.Flowable


interface QuoteDataSource {

    fun getQuoteCategories(quoteType: String): Flowable<List<QuoteCategory>>

    fun saveQuoteData(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>)

    fun getAllQuotes(): Flowable<List<Quote>>

    fun getQuotesByQuoteType(quoteType: String): Flowable<List<Quote>>

    fun getQuotesByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String): Flowable<List<Quote>>

    fun getAllQuoteData(): Flowable<List<AllQuoteData>>

    fun getAllQuoteDataByQuoteType(quoteType: String): Flowable<List<AllQuoteData>>

    fun getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String): Flowable<List<AllQuoteData>>
}