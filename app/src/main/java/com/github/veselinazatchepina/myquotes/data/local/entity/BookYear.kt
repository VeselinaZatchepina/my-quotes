package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
data class BookYear(@PrimaryKey(autoGenerate = true) val bookYearId: Long,
                    val yearId: Long,
                    val bookNameId: Long)