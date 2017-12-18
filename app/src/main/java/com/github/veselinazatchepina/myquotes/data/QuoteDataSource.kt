package com.github.veselinazatchepina.myquotes.data

import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory
import io.reactivex.Flowable


interface QuoteDataSource {

    fun getBookCategories() : Flowable<List<BookCategory>>
}