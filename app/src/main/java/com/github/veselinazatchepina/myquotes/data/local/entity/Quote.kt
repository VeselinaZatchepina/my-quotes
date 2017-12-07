package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Quote(@PrimaryKey val quoteId: String,
                 val quoteText: String,
                 val creationDate: String,
                 val comments: String,
                 val pageNumber: String,
                 val bookNameId: String,
                 val categoryId: String,
                 val typeId: String)