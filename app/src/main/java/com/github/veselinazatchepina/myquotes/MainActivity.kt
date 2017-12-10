package com.github.veselinazatchepina.myquotes

import android.app.Activity
import android.os.Bundle
import com.github.veselinazatchepina.myquotes.data.local.AppDatabase
import com.github.veselinazatchepina.myquotes.data.local.entity.*
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val db = AppDatabase.getAppDatabaseInstance(applicationContext)
        val db2 = AppDatabase.getAppDatabaseInstance(applicationContext)

        //BookAuthor
        val surname = "Novikova"
        val name = "Veselina"
        val patronymic = "Alekseevna"

        //BookCategory
        val category = "mind"

        //Book
        val nameBook = "my book"

        //BookReleaseYear
        val year = 2017L

        //PublishingOffice
        val publishingOffice = "my office"

        //QuoteType
        val type = "my quote"

        //Quote
        val quoteText = "Hello, my dear"
        val quoteText2 = "Hello, my dear"
        val date = "2/01/17"
        val comments = "my comments"
        val pageNumber = 2L

        val author = createAuthor(surname, name, patronymic)
        val bookCategory = createCategory(category)
        val bookName = createBookName(nameBook)
        val bookYear = createYear(year)
        val office = createOffice(publishingOffice)
        val quoteType = createType(type)
        val quote = createQuote(quoteText, date, comments, pageNumber)
        val quote2 = createQuote(quoteText2, date, comments, pageNumber)

        Single.fromCallable {
            //            db.authorDao().insertAuthor(author)
//            db.bookCategoryDao().insertBookCategory(bookCategory)
//            db.publishingOfficeDao().insertPublishingOffice(office)
//            db.bookReleaseYearDao().insertBookYear(bookYear)
//            db.quoteTypeDao().insertQuoteType(quoteType)
//            db.bookDao().insertBook(bookName)
//            db.quoteDao().insertQuote(quote)
            //db.bookAndBookAuthorDao().insertBookAndBookAuthor(BookAndBookAuthor(2,2,2))


        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe()

        Flowable.fromCallable {
            //db.allQuoteDataDao().getAllQuoteDataByQuoteId(2)
            //db.allQuoteDataDao().getAllQuote()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
            //var bookList = it.count()
            //var data = it
            //for (data in it) {
             //   Log.v("OOOOOOOOOOOOOO", data.quote?.quoteId.toString() + data.book?.bookId.toString() +
            //    data.bookAndBookAuthor?.bookAndBookAuthorId.toString())
           // }
//bookList?.count().toString()

        }, {

        })

    }

    fun createAuthor(surname: String, name: String, patronymic: String): BookAuthor {
        return BookAuthor(2, surname, name, patronymic)
    }

    fun createCategory(category: String): BookCategory {
        return BookCategory(2, category)
    }

    fun createBookName(bookName: String): Book {
        return Book(2, bookName, 2, 2)
    }

    fun createYear(year: Long): BookReleaseYear {
        return BookReleaseYear(2, year)
    }

    fun createOffice(name: String): PublishingOffice {
        return PublishingOffice(2, name)
    }

    fun createType(type: String): QuoteType {
        return QuoteType(2, type)
    }

    fun createQuote(text: String, date: String, comments: String, page: Long): Quote {
        return Quote(2, text, date, comments, page, 2, 2)
    }


}
