package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import com.github.veselinazatchepina.myquotes.data.local.entity.BookAuthor


@Dao
interface BookAuthorDao {

    @Insert
    fun insertAuthor(author: BookAuthor)
}