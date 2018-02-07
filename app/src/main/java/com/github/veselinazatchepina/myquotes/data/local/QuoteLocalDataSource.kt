package com.github.veselinazatchepina.myquotes.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.*
import com.github.veselinazatchepina.myquotes.data.local.model.AllQuoteData
import com.github.veselinazatchepina.myquotes.data.local.model.QuoteCategoryModel
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import com.github.veselinazatchepina.myquotes.utils.BaseSchedulerProvider
import io.reactivex.Flowable


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

    override fun getQuoteCategories(quoteType: String): Flowable<List<QuoteCategoryModel>> {
        return databaseInstance.quoteCategoryModel().getQuoteCategoriesByQuoteType(quoteType)
    }

    override fun saveQuoteData(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        databaseInstance.runInTransaction {
            val publishingOfficeId = savePublishingOffice(mapOfQuoteProperties[QuoteProperties.PUBLISHING_OFFICE_NAME]
                    ?: "")
            Log.v("SAVE_QUOTE", "OK OFFICE")
            val quoteCategoryId = saveQuoteCategory(mapOfQuoteProperties[QuoteProperties.BOOK_CATEGORY_NAME]
                    ?: "")
            Log.v("SAVE_QUOTE", "OK CATEGORY")
            val yearId = saveYear(if (mapOfQuoteProperties[QuoteProperties.YEAR_NUMBER] == null ||
                    mapOfQuoteProperties[QuoteProperties.YEAR_NUMBER] == "") {
                0L
            } else {
                mapOfQuoteProperties[QuoteProperties.YEAR_NUMBER]!!.toLong()
            })
            Log.v("SAVE_QUOTE", "OK YEAR")
            val bookId = saveBook(mapOfQuoteProperties[QuoteProperties.BOOK_NAME] ?: "",
                    publishingOfficeId, yearId)
            Log.v("SAVE_QUOTE", "OK BOOK")
            saveBookAndBookReleaseYear(yearId, bookId)
            Log.v("SAVE_QUOTE", "OK BOOK_AND_BOOK_YEAR")
            val quoteTypeId = saveQuoteType(mapOfQuoteProperties[QuoteProperties.QUOTE_TYPE] ?: "")
            Log.v("SAVE_QUOTE", "OK QUOTE_TYPE")
            val quoteId = saveQuote(mapOfQuoteProperties[QuoteProperties.QUOTE_TEXT] ?: "",
                    mapOfQuoteProperties[QuoteProperties.QUOTE_CREATION_DATE] ?: "",
                    mapOfQuoteProperties[QuoteProperties.QUOTE_COMMENTS] ?: "",
                    if (mapOfQuoteProperties[QuoteProperties.PAGE_NUMBER] == null ||
                            mapOfQuoteProperties[QuoteProperties.PAGE_NUMBER] == "") {
                        0L
                    } else {
                        mapOfQuoteProperties[QuoteProperties.PAGE_NUMBER]!!.toLong()
                    },
                    bookId,
                    quoteTypeId,
                    quoteCategoryId)
            Log.v("SAVE_QUOTE", "OK QUOTE")

            for (i in 0 until authors.count() step 3) {
                val authorId = saveAuthor(authors[i], authors[i + 1], authors[i + 2])
                Log.v("SAVE_QUOTE", "OK AUTHOR $authorId")
                saveBookAndBookAuthor(bookId, authorId)
                Log.v("SAVE_QUOTE", "OK BOOK_AND_BOOK_AUTHOR")
            }

            Log.v("SAVE_QUOTE",
                    mapOfQuoteProperties[QuoteProperties.BOOK_NAME] +
                            quoteCategoryId.toString() +
                            publishingOfficeId.toString() +
                            yearId.toString() +
                            bookId.toString())
        }
    }

    private fun saveQuote(quoteText: String,
                          creationDate: String,
                          comments: String,
                          pageNumber: Long,
                          bookId: Long,
                          typeId: Long,
                          categoryId: Long): Long {
        val quote = Quote(quoteText, creationDate, comments, pageNumber, bookId, typeId, categoryId)
        return databaseInstance.quoteDao().insertQuote(quote)
    }


    private fun savePublishingOffice(publishingOfficeName: String): Long {
        val existedPublishingOffice = databaseInstance.publishingOfficeDao()
                .getPublishingOfficeByName(publishingOfficeName)
        val existedPublishingOfficeId = existedPublishingOffice?.officeId ?: 0L
        return if (existedPublishingOfficeId == 0L) {
            val publishingOffice = PublishingOffice(publishingOfficeName)
            val id = databaseInstance.publishingOfficeDao().insertPublishingOffice(publishingOffice)
            if (publishingOfficeName == "") {
                databaseInstance.publishingOfficeDao().updatePublishingOffice(id, "NoPublishingOfficeName$id")
            }
            id
        } else {
            existedPublishingOfficeId
        }
    }

    private fun saveQuoteCategory(quoteCategoryName: String): Long {
        val existedQuoteCategory = databaseInstance.quoteCategoryDao()
                .getQuoteCategoryByName(quoteCategoryName.toLowerCase())
        val existedQuoteCategoryId = existedQuoteCategory?.categoryId ?: 0L
        return if (existedQuoteCategoryId == 0L) {
            val quoteCategory = QuoteCategory(quoteCategoryName.toLowerCase())
            databaseInstance.quoteCategoryDao().insertQuoteCategory(quoteCategory)
        } else {
            existedQuoteCategoryId
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

    private fun saveBook(bookName: String, publishingOfficeId: Long, yearId: Long): Long {
        val existedBook = databaseInstance.bookDao().getBookByNamePublishingYear(bookName, publishingOfficeId, yearId)
        val existedBookId = existedBook?.bookId ?: 0L
        return if (existedBookId == 0L) {
            val book = Book(bookName, publishingOfficeId)
            val id = databaseInstance.bookDao().insertBook(book)
            if (bookName == "") {
                databaseInstance.bookDao().updateBook(id, "NoBookName$id")
            }
            id
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
            val id = databaseInstance.authorDao().insertAuthor(author)
            if (surname == "" && name == "" && patronymic == "") {
                databaseInstance.authorDao().updateBookAuthor(id, "NoAuthorName$id")
            }
            id
        } else {
            existedAuthorId
        }
    }

    private fun saveBookAndBookAuthor(bookId: Long, authorId: Long) {
        val bookAndBookAuthor = BookAndBookAuthor(bookId, authorId)
        databaseInstance.bookAndBookAuthorDao().insertBookAndBookAuthor(bookAndBookAuthor)
    }

    override fun getAllQuotes(): Flowable<List<Quote>> {
        return databaseInstance.quoteDao().getAllQuotes()
    }

    override fun getQuotesByType(quoteType: String): Flowable<List<Quote>> {
        return databaseInstance.quoteDao().getQuotesByType(quoteType)
    }

    override fun getQuotesByTypeAndCategory(quoteType: String, quoteCategory: String): Flowable<List<Quote>> {
        return databaseInstance.quoteDao().getQuotesByTypeAndCategory(quoteType, quoteCategory)
    }

    override fun getAllQuoteData(): Flowable<List<AllQuoteData>> {
        return databaseInstance.allQuoteDataDao().getAllQuoteData()
    }

    override fun getAllQuoteDataByQuoteType(quoteType: String): Flowable<List<AllQuoteData>> {
        return databaseInstance.allQuoteDataDao().getAllQuoteDataByQuoteType(quoteType)
    }

    override fun getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String): Flowable<List<AllQuoteData>> {
        return databaseInstance.allQuoteDataDao().getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType, quoteCategory)
    }

    override fun getQuotesByQuoteTextIfContains(quoteText: String): Flowable<List<Quote>> {
        return databaseInstance.quoteDao().getAllQuotesByQuoteTextIfContains(quoteText)
    }

    override fun getQuotesByTypeAndTextIfContains(quoteType: String, text: String): Flowable<List<Quote>> {
        return databaseInstance.quoteDao().getQuotesByTypeAndTextIfContains(quoteType, text)
    }

    override fun getQuotesByTypeAndCategoryAndTextIfContains(quoteType: String, quoteCategory: String, text: String): Flowable<List<Quote>> {
        return databaseInstance.quoteDao().getQuotesByTypeAndCategoryAndTextIfContains(quoteType, quoteCategory, text)
    }

    override fun getBookAuthorsByIds(ids: List<Long>): Flowable<List<BookAuthor>> {
        return databaseInstance.authorDao().getBookAuthorsByIds(ids)
    }

    override fun getBookReleaseYearsByIds(ids: List<Long>): Flowable<List<BookReleaseYear>> {
        return databaseInstance.bookReleaseYearDao().getBookReleaseYearsByIds(ids)
    }

    override fun deleteQuote(qId: Long) {
        databaseInstance.quoteDao().deleteQuote(qId)
    }

    override fun deleteQuoteCategory(quoteType: String, quoteCategory: String) {
        databaseInstance.quoteCategoryDao().deleteQuoteCategory(quoteType, quoteCategory)
    }

    override fun updateQuote(quoteId: Long,
                             mapOfQuoteProperties: HashMap<QuoteProperties, String>,
                             authors: List<String>) {
        databaseInstance.runInTransaction {

            val editPublishingOfficeId = savePublishingOffice(mapOfQuoteProperties[QuoteProperties.PUBLISHING_OFFICE_NAME]!!)
            Log.v("EDIT_QUOTE", "OK OFFICE")

            val editQuoteCategoryId = saveQuoteCategory(mapOfQuoteProperties[QuoteProperties.BOOK_CATEGORY_NAME]!!)
            Log.v("EDIT_QUOTE", "OK CATEGORY")

            val yearId = saveYear(if (mapOfQuoteProperties[QuoteProperties.YEAR_NUMBER] == null ||
                    mapOfQuoteProperties[QuoteProperties.YEAR_NUMBER] == "") {
                0L
            } else {
                mapOfQuoteProperties[QuoteProperties.YEAR_NUMBER]!!.toLong()
            })
            Log.v("EDIT_QUOTE", "OK YEAR")

            val bookId = saveBook(mapOfQuoteProperties[QuoteProperties.BOOK_NAME] ?: "",
                    editPublishingOfficeId, yearId)
            Log.v("EDIT_QUOTE", "OK BOOK")

            saveBookAndBookReleaseYear(yearId, bookId)
            Log.v("EDIT_QUOTE", "OK BOOK_AND_BOOK_YEAR")

            val quoteTypeId = saveQuoteType(mapOfQuoteProperties[QuoteProperties.QUOTE_TYPE] ?: "")
            Log.v("EDIT_QUOTE", "OK QUOTE_TYPE")

            val quoteId = updateQuoteObject(quoteId, mapOfQuoteProperties[QuoteProperties.QUOTE_TEXT]
                    ?: "",
                    mapOfQuoteProperties[QuoteProperties.QUOTE_CREATION_DATE] ?: "",
                    mapOfQuoteProperties[QuoteProperties.QUOTE_COMMENTS] ?: "",
                    if (mapOfQuoteProperties[QuoteProperties.PAGE_NUMBER] == null ||
                            mapOfQuoteProperties[QuoteProperties.PAGE_NUMBER] == "") {
                        0L
                    } else {
                        mapOfQuoteProperties[QuoteProperties.PAGE_NUMBER]!!.toLong()
                    },
                    bookId,
                    quoteTypeId,
                    editQuoteCategoryId)
            Log.v("EDIT_QUOTE", "OK QUOTE")

            for (i in 0 until authors.count() step 3) {
                val authorId = saveAuthor(authors[i], authors[i + 1], authors[i + 2])
                Log.v("EDIT_QUOTE", "OK AUTHOR $authorId")
                saveBookAndBookAuthor(bookId, authorId)
                Log.v("EDIT_QUOTE", "OK BOOK_AND_BOOK_AUTHOR")
            }
        }
    }

    private fun updateQuoteObject(quoteId: Long, quoteText: String,
                                  creationDate: String,
                                  comments: String,
                                  pageNumber: Long,
                                  bookId: Long,
                                  typeId: Long,
                                  categoryId: Long): Long {
        val editQuote = databaseInstance.quoteDao().getSimpleQuoteById(quoteId).apply {
            this.quoteText = quoteText
            this.creationDate = creationDate
            this.comments = comments
            this.pageNumber = pageNumber
            this.book_Id = bookId
            this.category_Id = categoryId
            this.type_Id = typeId
        }
        databaseInstance.quoteDao().updateQuote(editQuote)
        return quoteId
    }
}