package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class BookReleaseYear(val year: Long) {
    @PrimaryKey(autoGenerate = true)
    var yearId: Long = 0
}