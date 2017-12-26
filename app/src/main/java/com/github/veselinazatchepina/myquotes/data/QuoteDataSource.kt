package com.github.veselinazatchepina.myquotes.data

import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import io.reactivex.Flowable


interface QuoteDataSource {

    fun getBookCategories() : Flowable<List<BookCategory>>

    fun saveQuoteData(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>)
}