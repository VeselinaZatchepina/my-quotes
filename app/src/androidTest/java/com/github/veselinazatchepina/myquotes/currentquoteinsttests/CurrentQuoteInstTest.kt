package com.github.veselinazatchepina.myquotes.currentquoteinsttests

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.currentquote.CurrentQuoteActivity
import com.github.veselinazatchepina.myquotes.currentquote.CurrentQuoteMainFragment
import com.github.veselinazatchepina.myquotes.currentquote.CurrentQuoteMainPresenter
import com.github.veselinazatchepina.myquotes.data.local.AppDatabase
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.lang.reflect.Field
import java.util.*


class CurrentQuoteInstTest {

    lateinit var mainFragment: CurrentQuoteMainFragment
    lateinit var mainPresenter: CurrentQuoteMainPresenter
    lateinit var dataSource: QuoteLocalDataSource
    lateinit var context: Context
    lateinit var roomDatabase: AppDatabase

    @get: Rule
    var activityRule: IntentsTestRule<CurrentQuoteActivity> =
            IntentsTestRule<CurrentQuoteActivity>(CurrentQuoteActivity::class.java)

    @Before
    fun init() {
        context = Mockito.mock(Context::class.java)
        roomDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dataSource = QuoteLocalDataSource.getInstance(roomDatabase)
        mainFragment = Mockito.mock(CurrentQuoteMainFragment::class.java)
        mainPresenter = Mockito.mock(CurrentQuoteMainPresenter::class.java)
        mainFragment.setPresenter(mainPresenter)
        activityRule.activity.setMainFragment(mainFragment)
        fillDb()
        mainPresenter.getAllQuoteData()
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
    fun isShareQuoteIntentCorrect() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.menu_item_share)).perform(ViewActions.click())
        Intents.intended(CoreMatchers.allOf(IntentMatchers.hasAction(Intent.ACTION_CHOOSER),
                IntentMatchers.hasExtra(CoreMatchers.`is`(Intent.EXTRA_INTENT),
                        CoreMatchers.allOf(IntentMatchers.hasAction(Intent.ACTION_SEND),
                                IntentMatchers.hasExtra(Intent.EXTRA_SUBJECT, "It is great quote! Listen!")))))
    }


    @Test
    fun isDeleteQuoteDialogDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.menu_item_delete_quote)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("Delete quote?")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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