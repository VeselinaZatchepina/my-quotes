package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import com.github.veselinazatchepina.myquotes.data.local.entity.BookAndBookReleaseYear


@Dao
interface BookAndBookReleaseYearDao {

    @Insert
    fun insertBookAndBookReleaseYear(bookAndBookReleaseYear: BookAndBookReleaseYear)


}