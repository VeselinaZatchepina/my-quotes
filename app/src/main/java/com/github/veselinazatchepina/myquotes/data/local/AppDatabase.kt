package com.github.veselinazatchepina.myquotes.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.util.Log
import com.github.veselinazatchepina.myquotes.data.local.dao.*
import com.github.veselinazatchepina.myquotes.data.local.entity.*


@Database(entities = arrayOf(
        Book::class,
        BookAndBookAuthor::class,
        BookAuthor::class,
        BookCategory::class,
        BookReleaseYear::class,
        BookAndBookReleaseYear::class,
        PublishingOffice::class,
        Quote::class,
        QuoteType::class),
        version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "my-quotes-database"
        private var INSTANCE: AppDatabase? = null

        fun getAppDatabaseInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME)
                        .build()
                Log.v("DATABASE_INSTANCE", "It was null")
            }
            Log.v("DATABASE_INSTANCE", "Is not null now")
            return INSTANCE!!
        }
    }

    abstract fun quoteDao(): QuoteDao

    abstract fun authorDao(): BookAuthorDao

    abstract fun bookCategoryDao(): BookCategoryDao

    abstract fun bookDao(): BookDao

    abstract fun bookReleaseYearDao(): BookReleaseYearDao

    abstract fun publishingOfficeDao(): PublishingOfficeDao

    abstract fun quoteTypeDao(): QuoteTypeDao

    abstract fun allQuoteDataDao(): AllQuoteDataDao

    abstract fun bookAndBookAuthorDao(): BookAndBookAuthorDao

    abstract fun bookAndBookReleaseYearDao(): BookAndBookReleaseYearDao

}