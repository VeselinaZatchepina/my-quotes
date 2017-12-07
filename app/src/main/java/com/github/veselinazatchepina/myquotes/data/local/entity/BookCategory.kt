package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class BookCategory(@PrimaryKey val categoryId: String,
                        val categoryName: String)