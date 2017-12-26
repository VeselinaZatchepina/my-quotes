package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class BookCategory(val categoryName: String,
                        val quoteCount: Int) {
    @PrimaryKey(autoGenerate = true)
    var categoryId: Long = 0
}