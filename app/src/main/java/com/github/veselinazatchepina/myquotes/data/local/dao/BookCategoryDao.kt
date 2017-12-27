package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory


@Dao
interface BookCategoryDao {

    @Insert
    fun insertBookCategory(bookCategory: BookCategory): Long

    @Query("SELECT * FROM BookCategory WHERE BookCategory.categoryName = :name")
    fun getBookCategoryByName(name: String): BookCategory?

    @Query("UPDATE BookCategory SET quoteCount = :quoteCount WHERE BookCategory.categoryId = :bookCategoryId")
    fun updateQuoteCount(quoteCount: Int, bookCategoryId: Long)
}