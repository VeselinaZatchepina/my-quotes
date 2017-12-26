package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class QuoteType(val type: String) {
    @PrimaryKey(autoGenerate = true)
    var typeId: Long = 0
}