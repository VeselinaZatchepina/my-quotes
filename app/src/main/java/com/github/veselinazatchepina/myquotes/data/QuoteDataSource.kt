package com.github.veselinazatchepina.myquotes.data

import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteCategory
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import io.reactivex.Flowable


interface QuoteDataSource {

    fun getQuoteCategories(quoteType: String) : Flowable<List<QuoteCategory>>

    fun saveQuoteData(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>)
}