package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class QuoteType(@PrimaryKey(autoGenerate = true) val typeId: Long,
                     val type: String)