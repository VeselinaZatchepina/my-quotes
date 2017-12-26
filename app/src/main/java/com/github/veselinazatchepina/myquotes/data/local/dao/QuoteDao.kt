package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote


@Dao
interface QuoteDao {

    @Query("SELECT * FROM Quote WHERE Quote.quoteId = :quoteId")
    fun getQuoteById(quoteId: Long) : Quote

    @Insert
    fun insertQuote(quote: Quote): Long


}