package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Author(@PrimaryKey(autoGenerate = true) val authorId: Long,
                  val surname: String,
                  val name: String,
                  val patronymic: String)