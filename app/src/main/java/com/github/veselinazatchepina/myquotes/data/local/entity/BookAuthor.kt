package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class BookAuthor(@PrimaryKey(autoGenerate = true) val bookAuthorId: Long,
                      val authorId: Long,
                      val bookNameId: Long)