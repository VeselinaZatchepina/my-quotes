package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import com.github.veselinazatchepina.myquotes.data.local.entity.BookReleaseYear


@Dao
interface BookReleaseYearDao {

    @Insert
    fun insertBookYear(bookReleaseYear: BookReleaseYear)
}