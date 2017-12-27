package com.github.veselinazatchepina.myquotes.data.local.pojo

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.github.veselinazatchepina.myquotes.data.local.entity.Book
import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteType


class BookCategoriesAndQuoteType(

        @Embedded
        var quote: Quote? = null,

        @Embedded
        var book: Book? = null,

        @Embedded
        var quoteType: QuoteType? = null,

        @Relation(
                parentColumn = "category_Id",
                entityColumn = "categoryId",
                entity = BookCategory::class
        )
        var bookCategories: List<BookCategory>? = null
)