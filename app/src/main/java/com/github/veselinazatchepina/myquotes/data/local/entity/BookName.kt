package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class BookName(@PrimaryKey val bookNameId: String,
                    val bookName: String,
                    val publishingOfficeId: String)