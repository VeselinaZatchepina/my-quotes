package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class BookCategory(@PrimaryKey(autoGenerate = true) val categoryId: Long,
                        val categoryName: String,
                        val quoteCount: Int)