package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory


@Dao
interface BookCategoryDao {

    @Insert
    fun insertBookCategory(bookCategory: BookCategory)

    @Query("SELECT * FROM BookCategory")
    fun getAllBookCategories() : List<BookCategory>
}