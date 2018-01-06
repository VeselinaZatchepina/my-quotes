package com.github.veselinazatchepina.myquotes.data.local.pojo

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.github.veselinazatchepina.myquotes.data.local.entity.*


class AllQuoteData(

        @Embedded
        var quote: Quote? = null,

        @Embedded
        var book: Book? = null,

        @Embedded
        var bookAndBookAuthor: BookAndBookAuthor? = null,

        @Relation(
                parentColumn = "type_Id",
                entityColumn = "typeId",
                entity = QuoteType::class
        )
        var type: List<QuoteType>? = null,

        @Relation(
                parentColumn = "book_Id",
                entityColumn = "bookId",
                entity = Book::class
        )
        var bookList: List<Book>? = null,

        @Relation(
                parentColumn = "category_Id",
                entityColumn = "categoryId",
                entity = QuoteCategory::class
        )
        var category: List<QuoteCategory>? = null,

        @Relation(
                parentColumn = "authorIdJoin",
                entityColumn = "authorId",
                entity = BookAuthor::class
        )
        var author: List<BookAuthor>? = null
)