package com.github.veselinazatchepina.myquotes.data.local.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.github.veselinazatchepina.myquotes.data.local.entity.*
import java.io.Serializable


class AllQuoteData(

        @Embedded
        var quote: Quote? = null,

        @Embedded
        var book: Book? = null,

        @Relation(
                parentColumn = "book_Id",
                entityColumn = "bookId",
                entity = Book::class
        )
        var bookList: List<Book>? = null,

        @Relation(
                parentColumn = "type_Id",
                entityColumn = "typeId",
                entity = QuoteType::class
        )
        var types: List<QuoteType>? = null,

        @Relation(
                parentColumn = "category_Id",
                entityColumn = "categoryId",
                entity = QuoteCategory::class
        )
        var category: List<QuoteCategory>? = null,

        @Relation(
                parentColumn = "office_Id",
                entityColumn = "officeId",
                entity = PublishingOffice::class
        )
        var publishingOffice: List<PublishingOffice>? = null,

        @Relation(
                parentColumn = "bookId",
                entityColumn = "bookIdJoin",
                entity = BookAndBookAuthor::class
        )
        var authorsId: List<BookAndBookAuthor>? = null,

        @Relation(
                parentColumn = "bookId",
                entityColumn = "byBookIdJoin",
                entity = BookAndBookReleaseYear::class
        )
        var yearsId: List<BookAndBookReleaseYear>? = null
) : Serializable