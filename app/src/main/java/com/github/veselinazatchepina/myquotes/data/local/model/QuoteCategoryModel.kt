package com.github.veselinazatchepina.myquotes.data.local.model

import android.arch.persistence.room.Embedded
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteCategory
import java.io.Serializable


class QuoteCategoryModel : Serializable {

    @Embedded
    var quoteCategory: QuoteCategory? = null

    var quoteCountOfCurrentCategory: Int = 0

}