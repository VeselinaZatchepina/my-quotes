package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteType


@Dao
interface QuoteTypeDao {

    @Insert
    fun insertQuoteType(quoteType: QuoteType): Long

    @Query("SELECT * FROM QuoteType WHERE QuoteType.type = :quoteType")
    fun getQuoteTypeByName(quoteType: String): QuoteType?
}