package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.BookAuthor


@Dao
interface BookAuthorDao {

    @Insert
    fun insertAuthor(author: BookAuthor): Long

    @Query("SELECT * FROM BookAuthor " +
            "WHERE BookAuthor.surname = :surname " +
            "AND BookAuthor.name = :name " +
            "AND BookAuthor.patronymic = :patronymic")
    fun getAuthor(surname: String, name: String, patronymic: String): BookAuthor?
}