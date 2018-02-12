package com.github.veselinazatchepina.myquotes

import android.arch.persistence.room.Room
import android.content.Context
import com.github.veselinazatchepina.myquotes.data.local.AppDatabase
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import io.reactivex.Observable
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.ArrayList
import org.mockito.BDDMockito.`when` as whenMockito


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class AddQuoteDataSourceTest {

    lateinit var context: Context
    lateinit var roomDatabase: AppDatabase
    lateinit var dataSource: QuoteLocalDataSource
    lateinit var testSubscriber: TestSubscriber<List<String>>

    @Before
    fun init() {
        context = mock(Context::class.java)
        roomDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dataSource = QuoteLocalDataSource.getInstance(roomDatabase)
        testSubscriber = TestSubscriber<List<String>>()
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
                "category2",
                "2",
                "2",
                "office2",
                "Book's quote",
                "comments2"),
                createAuthorsList())
        dataSource.saveQuoteData(createBookQuotePropertiesMap(
                "text3",
                "book3",
                "category3",
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
                "category4",
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
    fun testThatBookQuoteDataSavingIsCorrect() {
        var savedQuoteId: Long = -1
        Observable.fromCallable {
            dataSource.saveQuoteData(createBookQuotePropertiesMap(
                    "text7",
                    "book7",
                    "category7",
                    "7",
                    "7",
                    "office7",
                    "Book's quote",
                    "comments7"), createAuthorsList())
        }.subscribe({
                    savedQuoteId = it
                })
        assertNotEquals(-1, savedQuoteId)
    }

    @Test
    fun testThatMyQuoteDataSavingIsCorrect() {
        var savedQuoteId: Long = -1
        Observable.fromCallable {
            dataSource.saveQuoteData(createMyQuotePropertiesMap(
                    "text6",
                    "category6",
                    "My quote",
                    "comments6"), createAuthorsList())
        }.subscribe({
                    savedQuoteId = it
                })
        assertNotEquals(-1, savedQuoteId)
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