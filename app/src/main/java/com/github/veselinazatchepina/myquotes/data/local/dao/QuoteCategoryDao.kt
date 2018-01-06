package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteCategory
import io.reactivex.Flowable


@Dao
interface QuoteCategoryDao {

    @Insert
    fun insertQuoteCategory(quoteCategory: QuoteCategory): Long

    @Query("SELECT * FROM QuoteCategory WHERE QuoteCategory.categoryName = :name")
    fun getQuoteCategoryByName(name: String): QuoteCategory?

    @Query("SELECT * FROM QuoteCategory INNER JOIN Quote ON QuoteCategory.categoryId = Quote.category_Id INNER JOIN (SELECT * FROM QuoteType WHERE QuoteType.type = :quoteType) c ON c.typeId = Quote.type_Id")
    fun getQuoteCategoryByQuoteType(quoteType: String): Flowable<List<QuoteCategory>>

    @Query("UPDATE QuoteCategory SET quoteCount = :quoteCount WHERE QuoteCategory.categoryId = :quoteCategoryId")
    fun updateQuoteCount(quoteCount: Int, quoteCategoryId: Long)
}