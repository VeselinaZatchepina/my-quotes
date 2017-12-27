package com.github.veselinazatchepina.myquotes.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.*
import com.github.veselinazatchepina.myquotes.data.local.pojo.BookCategoriesAndQuoteType
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import com.github.veselinazatchepina.myquotes.utils.BaseSchedulerProvider
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


class QuoteLocalDataSource private constructor(val context: Context,
                                               val schedulerProvider: BaseSchedulerProvider) : QuoteDataSource {

    var databaseInstance: AppDatabase = AppDatabase.getAppDatabaseInstance(context)

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: QuoteLocalDataSource? = null

        fun getInstance(context: Context,
                        schedulerProvider: BaseSchedulerProvider): QuoteLocalDataSource {
            if (INSTANCE == null) {
                INSTANCE = QuoteLocalDataSource(context.applicationContext, schedulerProvider)
            }
            return INSTANCE!!
        }
    }

    override fun getBookCategories(quoteType: String): Flowable<List<BookCategoriesAndQuoteType>> {
        return databaseInstance.bookCategoriesAndQuoteTypeDao().getBookCategoriesByQuoteType(quoteType)
    }

    override fun saveQuoteData(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {

        Single.fromCallable {
            databaseInstance.runInTransaction {
                val publishingOfficeId = savePublishingOffice(mapOfQuoteProperties[QuoteProperties.PUBLISHING_OFFICE_NAME] ?: "")
                Log.v("SAVE_QUOTE", "OK OFFICE")
                val bookCategoryId = saveBookCategory(mapOfQuoteProperties[QuoteProperties.BOOK_CATEGORY_NAME] ?: "")
                Log.v("SAVE_QUOTE", "OK CATEGORY")
                val yearId = saveYear(mapOfQuoteProperties[QuoteProperties.YEAR_NUMBER]?.toLong() ?: 0L)
                Log.v("SAVE_QUOTE", "OK YEAR")
                val bookId = saveBook(mapOfQuoteProperties[QuoteProperties.BOOK_NAME] ?: "",
                        bookCategoryId, publishingOfficeId, yearId)
                Log.v("SAVE_QUOTE", "OK BOOK")
                saveBookAndBookReleaseYear(yearId, bookId)
                Log.v("SAVE_QUOTE", "OK BOOK_AND_BOOK_YEAR")
                val quoteTypeId = saveQuoteType(mapOfQuoteProperties[QuoteProperties.QUOTE_TYPE] ?: "")
                val quoteId = saveQuote(mapOfQuoteProperties[QuoteProperties.QUOTE_TEXT] ?: "",
                        mapOfQuoteProperties[QuoteProperties.QUOTE_CREATION_DATE] ?: "",
                        mapOfQuoteProperties[QuoteProperties.QUOTE_COMMENTS] ?: "",
                        mapOfQuoteProperties[QuoteProperties.PAGE_NUMBER]?.toLong() ?: 0L,
                        bookId,
                        quoteTypeId)
                Log.v("SAVE_QUOTE", "OK QUOTE")

                for (i in 0 until authors.count() step 3) {
                    val authorId = saveAuthor(authors[i], authors[i + 1], authors[i + 2])
                    Log.v("SAVE_QUOTE", "OK AUTHOR $authorId")
                    saveBookAndBookAuthor(bookId, authorId)
                    Log.v("SAVE_QUOTE", "OK BOOK_AND_BOOK_AUTHOR")
                }

                Log.v("SAVE_QUOTE",
                        mapOfQuoteProperties[QuoteProperties.BOOK_NAME] +
                                bookCategoryId.toString() +
                                publishingOfficeId.toString() +
                                yearId.toString() +
                                bookId.toString())
            }
        }.subscribeOn(Schedulers.io())
                .subscribe({
                }, {
                    Log.v("SAVE_QUOTE", "Shit")
                })
    }

    private fun saveQuote(quoteText: String,
                          creationDate: String,
                          comments: String,
                          pageNumber: Long,
                          bookId: Long,
                          typeId: Long): Long {
        val quote = Quote(quoteText, creationDate, comments, pageNumber, bookId, typeId)
        return databaseInstance.quoteDao().insertQuote(quote)
    }


    private fun savePublishingOffice(publishingOfficeName: String): Long {
        val existedPublishingOffice = databaseInstance.publishingOfficeDao()
                .getPublishingOfficeByName(publishingOfficeName)
        val existedPublishingOfficeId = existedPublishingOffice?.officeId ?: 0L
        return if (existedPublishingOfficeId == 0L) {
            val publishingOffice = PublishingOffice(publishingOfficeName)
            databaseInstance.publishingOfficeDao().insertPublishingOffice(publishingOffice)
        } else {
            existedPublishingOfficeId
        }
    }

    private fun saveBookCategory(bookCategoryName: String): Long {
        val existedBookCategory = databaseInstance.bookCategoryDao()
                .getBookCategoryByName(bookCategoryName)
        val existedBookCategoryId = existedBookCategory?.categoryId ?: 0L
        return if (existedBookCategoryId == 0L) {
            val bookCategory = BookCategory(bookCategoryName, 1)
            databaseInstance.bookCategoryDao().insertBookCategory(bookCategory)
        } else {
            databaseInstance.bookCategoryDao()
                    .updateQuoteCount(existedBookCategory!!.quoteCount + 1, existedBookCategoryId)
            existedBookCategoryId
        }
    }

    private fun saveYear(yearValue: Long): Long {
        val existedYear = databaseInstance.bookReleaseYearDao().getYearByValue(yearValue)
        val existedYearId = existedYear?.yearId ?: 0L
        return if (existedYearId == 0L) {
            val bookReleaseYear = BookReleaseYear(yearValue)
            databaseInstance.bookReleaseYearDao().insertBookYear(bookReleaseYear)
        } else {
            existedYearId
        }
    }

    private fun saveBook(bookName: String, categoryId: Long, publishingOfficeId: Long, yearId: Long): Long {
        val existedBook = databaseInstance.bookDao().getBookByNamePublishingYear(bookName, publishingOfficeId, yearId)
        val existedBookId = existedBook?.bookId ?: 0L
        return if (existedBookId == 0L) {
            val book = Book(bookName, publishingOfficeId, categoryId)
            databaseInstance.bookDao().insertBook(book)
        } else {
            existedBookId
        }
    }

    private fun saveBookAndBookReleaseYear(yearId: Long, bookId: Long) {
        val bookAndBookReleaseYear = BookAndBookReleaseYear(yearId, bookId)
        databaseInstance.bookAndBookReleaseYearDao().insertBookAndBookReleaseYear(bookAndBookReleaseYear)
    }

    private fun saveQuoteType(quoteTypeName: String): Long {
        val existedQuoteType = databaseInstance.quoteTypeDao().getQuoteTypeByName(quoteTypeName)
        val existedQuoteTypeId = existedQuoteType?.typeId ?: 0L
        return if (existedQuoteTypeId == 0L) {
            val quoteType = QuoteType(quoteTypeName)
            databaseInstance.quoteTypeDao().insertQuoteType(quoteType)
        } else {
            existedQuoteTypeId
        }
    }

    private fun saveAuthor(surname: String, name: String, patronymic: String): Long {
        val existedAuthor = databaseInstance.authorDao().getAuthor(surname, name, patronymic)
        val existedAuthorId = existedAuthor?.authorId ?: 0L
        return if (existedAuthorId == 0L) {
            val author = BookAuthor(surname, name, patronymic)
            databaseInstance.authorDao().insertAuthor(author)
        } else {
            existedAuthorId
        }
    }

    private fun saveBookAndBookAuthor(bookId: Long, authorId: Long) {
        val bookAndBookAuthor = BookAndBookAuthor(bookId, authorId)
        databaseInstance.bookAndBookAuthorDao().insertBookAndBookAuthor(bookAndBookAuthor)
    }
}