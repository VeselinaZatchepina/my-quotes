package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity
data class BookReleaseYear(val year: Long) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var yearId: Long = 0
}