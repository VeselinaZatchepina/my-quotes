package com.github.veselinazatchepina.myquotes.quotecategoriestests

import android.arch.persistence.room.Room
import android.content.Context
import com.github.veselinazatchepina.myquotes.BuildConfig
import com.github.veselinazatchepina.myquotes.data.local.AppDatabase
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.local.model.AllQuoteData
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Field
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class QuoteCategoriesDataSourceTest {

    lateinit var context: Context
    lateinit var roomDatabase: AppDatabase
    lateinit var dataSource: QuoteLocalDataSource
    lateinit var categoriesTestSubscriber: TestSubscriber<List<String>>
    lateinit var quotesTestSubscriber: TestSubscriber<List<AllQuoteData>>

    @Before
    fun init() {
        context = Mockito.mock(Context::class.java)
        roomDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dataSource = QuoteLocalDataSource.getInstance(roomDatabase)
        categoriesTestSubscriber = TestSubscriber<List<String>>()
        quotesTestSubscriber = TestSubscriber<List<AllQuoteData>>()
    }

    private fun fillDb() {
        dataSource.saveQuoteData(createBookQuotePropertiesMap(
                "text1",
                "book1",
                "category1",
                "1",
                "1",
                "office1",
                "Book's quote",
                "comments1"),
                createAuthorsList())
        dataSource.saveQuoteData(createBookQuotePropertiesMap(
                "text2",
                "book2",
                "category1",
                "2",
                "2",
                "office2",
                "Book's quote",
                "comments2"),
                createAuthorsList())
        dataSource.saveQuoteData(createBookQuotePropertiesMap(
                "text3",
                "book3",
                "category2",
                "3",
                "3",
                "office3",
                "Book's quote",
                "comments3"),
                createAuthorsList())
        dataSource.saveQuoteData(createMyQuotePropertiesMap(
                "text4",
                "category1",
                "My quote",
                "comments4"),
                createAuthorsList())
        dataSource.saveQuoteData(createMyQuotePropertiesMap(
                "text5",
                "category3",
                "My quote",
                "comments5"),
                createAuthorsList())
    }

    private fun createBookQuotePropertiesMap(quoteText: String,
                                             bookName: String,
                                             quoteCategory: String,
                                             pageNumber: String,
                                             yearNumber: String,
                                             office: String,
                                             type: String,
                                             comments: String): HashMap<QuoteProperties, String> {
        val mapOfQuoteProperties = HashMap<QuoteProperties, String>()
        mapOfQuoteProperties[QuoteProperties.QUOTE_TEXT] = quoteText
        mapOfQuoteProperties[QuoteProperties.BOOK_NAME] = bookName
        mapOfQuoteProperties[QuoteProperties.QUOTE_CATEGORY_NAME] = quoteCategory
        mapOfQuoteProperties[QuoteProperties.PAGE_NUMBER] = pageNumber
        mapOfQuoteProperties[QuoteProperties.YEAR_NUMBER] = yearNumber
        mapOfQuoteProperties[QuoteProperties.PUBLISHING_OFFICE_NAME] = office
        val currentCreateDate = Calendar.getInstance()
        val currentDate = String.format("%1\$td %1\$tb %1\$tY", currentCreateDate)
        mapOfQuoteProperties[QuoteProperties.QUOTE_CREATION_DATE] = currentDate
        mapOfQuoteProperties[QuoteProperties.QUOTE_TYPE] = type
        mapOfQuoteProperties[QuoteProperties.QUOTE_COMMENTS] = comments
        return mapOfQuoteProperties
    }

    private fun createMyQuotePropertiesMap(quoteText: String,
                                           quoteCategory: String,
                                           type: String,
                                           comments: String): HashMap<QuoteProperties, String> {
        val mapOfQuoteProperties = HashMap<QuoteProperties, String>()
        mapOfQuoteProperties[QuoteProperties.QUOTE_TEXT] = quoteText
        mapOfQuoteProperties[QuoteProperties.QUOTE_CATEGORY_NAME] = quoteCategory
        val currentCreateDate = Calendar.getInstance()
        val currentDate = String.format("%1\$td %1\$tb %1\$tY", currentCreateDate)
        mapOfQuoteProperties[QuoteProperties.QUOTE_CREATION_DATE] = currentDate
        mapOfQuoteProperties[QuoteProperties.QUOTE_TYPE] = type
        mapOfQuoteProperties[QuoteProperties.QUOTE_COMMENTS] = comments
        return mapOfQuoteProperties
    }

    private fun createAuthorsList(): List<String> {
        val list = ArrayList<String>()
        list.add("FIRST NAME")
        list.add("SURNAME")
        list.add("MIDDLE NAME")
        return list
    }

    @Test
    fun testThatSelectedQuoteCategoriesIsBookQuoteType() {
        fillDb()
        dataSource.getQuoteCategories("Book's quote")
                .flatMap {
                    Flowable.just(it.map { it.quoteCategory?.categoryId })
                }
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertValue {
                    println(it)
                    it.contains(1) && it.contains(2) && it.size == 2
                }
    }

    @Test
    fun testThatSelectedQuoteCategoriesIsMyQuoteType() {
        fillDb()
        dataSource.getQuoteCategories("My quote")
                .flatMap {
                    Flowable.just(it.map { it.quoteCategory?.categoryId })
                }
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertValue {
                    println(it)
                    it.contains(1) && it.contains(3) && it.size == 2
                }
    }

    @Test
    fun testThatAllMyQuotesCurrentCategoryIsDeleted() {
        fillDb()
        val countOfDeletedQuote = dataSource.deleteQuoteCategory("My quote", "category1")
        dataSource.getAllQuoteDataByQuoteTypeAndQuoteCategory("My quote", "category1")
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertValue {
                    println("$it $countOfDeletedQuote")
                    it.isEmpty() && countOfDeletedQuote == 1
                }
    }

    @Test
    fun testThatAllBookQuotesCurrentCategoryIsDeleted() {
        fillDb()
        val countOfDeletedQuote = dataSource.deleteQuoteCategory("Book's quote", "category1")
        dataSource.getAllQuoteDataByQuoteTypeAndQuoteCategory("Book's quote", "category1")
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertValue {
                    println("$it $countOfDeletedQuote")
                    it.isEmpty() && countOfDeletedQuote == 2
                }
    }

    @After
    fun closeDb() {
        roomDatabase.close()
        resetSingleton(QuoteLocalDataSource::class.java, "INSTANCE")
    }

    private fun resetSingleton(clazz: Class<*>, fieldName: String) {
        val instance: Field
        try {
            instance = clazz.getDeclaredField(fieldName)
            instance.setAccessible(true)
            instance.set(null, null)
        } catch (e: Exception) {
            throw RuntimeException()
        }

    }

}