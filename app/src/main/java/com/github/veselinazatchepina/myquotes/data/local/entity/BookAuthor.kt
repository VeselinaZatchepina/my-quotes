package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class BookAuthor(val surname: String,
                      val name: String,
                      val patronymic: String) {
    @PrimaryKey(autoGenerate = true)
    var authorId: Long = 0
}