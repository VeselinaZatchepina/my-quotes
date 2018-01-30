package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.model.QuoteCategoryModel
import io.reactivex.Flowable

@Dao
interface QuoteCategoryModelDao {

    @Query("SELECT *, count(*) AS quoteCountOfCurrentCategory " +
            "FROM QuoteCategory INNER JOIN Quote " +
            "ON Quote.category_Id = QuoteCategory.categoryId " +
            "INNER JOIN (SELECT * FROM QuoteType WHERE QuoteType.type = :quoteType) c ON c.typeId = Quote.type_Id " +
            "GROUP BY QuoteCategory.categoryId")
    fun getQuoteCategoriesByQuoteType(quoteType: String): Flowable<List<QuoteCategoryModel>>
}