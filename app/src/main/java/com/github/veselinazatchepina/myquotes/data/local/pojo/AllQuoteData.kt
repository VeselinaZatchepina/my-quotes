package com.github.veselinazatchepina.myquotes.data.local.pojo

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.github.veselinazatchepina.myquotes.data.local.entity.*
import java.io.Serializable


class AllQuoteData(

        @Embedded
        var quote: Quote? = null,

        @Embedded
        var book: Book? = null,

        @Embedded
        var bookAndBookAuthor: BookAndBookAuthor? = null,

        @Embedded
        var bookAndBookReleaseYear: BookAndBookReleaseYear? = null,

        @Relation(
                parentColumn = "office_Id",
                entityColumn = "officeId",
                entity = PublishingOffice::class
        )
        var publishingOffice: List<PublishingOffice>? = null,

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
        var author: List<BookAuthor>? = null,

        @Relation(
                parentColumn = "yearIdJoin",
                entityColumn = "yearId",
                entity = BookReleaseYear::class
        )
        var year: List<BookReleaseYear>? = null
) : Serializable