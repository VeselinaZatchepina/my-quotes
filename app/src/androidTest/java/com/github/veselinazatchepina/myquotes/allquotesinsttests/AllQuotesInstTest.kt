package com.github.veselinazatchepina.myquotes.allquotesinsttests

import android.support.design.widget.FloatingActionButton
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.BundleMatchers
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.allquote.AllQuotesActivity
import com.github.veselinazatchepina.myquotes.allquote.AllQuotesPresenter
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.*


@RunWith(AndroidJUnit4::class)
class AllQuotesInstTest {

    lateinit var allQuotesPresenter: AllQuotesPresenter

    @Captor
    lateinit var typeCaptor: ArgumentCaptor<String>

    @get: Rule
    var rule: IntentsTestRule<AllQuotesActivity> =
            IntentsTestRule(AllQuotesActivity::class.java)

    @Before
    fun init() {
        typeCaptor = ArgumentCaptor.forClass<String, String>(String::class.java)
        allQuotesPresenter = mock(AllQuotesPresenter::class.java)
    }

    @Test
    fun isDeleteQuoteDialogDisplayed() {
        onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.longClick()))
        Espresso.onView(ViewMatchers.withText("Delete quote?"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isAddBookQuoteIntentCorrect() {
        rule.activity.runOnUiThread {
            rule.activity.findViewById<FloatingActionButton>(R.id.book_quote_fab).visibility = View.VISIBLE

        }
        Espresso.onView(ViewMatchers.withId(R.id.add_icon_fab)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.book_quote_fab)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasExtras(
                BundleMatchers.hasEntry(CoreMatchers.equalTo("quote_type_intent"), CoreMatchers.equalTo("Book's quote"))
        ))
    }

    @Test
    fun isAddMyQuoteIntentCorrect() {
        rule.activity.runOnUiThread {
            rule.activity.findViewById<FloatingActionButton>(R.id.my_quote_fab).visibility = View.VISIBLE

        }
        Espresso.onView(ViewMatchers.withId(R.id.add_icon_fab)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.my_quote_fab)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasExtras(
                BundleMatchers.hasEntry(CoreMatchers.equalTo("quote_type_intent"), CoreMatchers.equalTo("My quote"))
        ))
    }

    @Test
    fun isFilterPopupMenuDisplayed() {
        onView(withId(R.id.filter_quote)).perform(click())
        onView(withText("Book quotes")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isClickedOnFilterByBooksQuotesCallCorrectFun() {
        rule.activity.getFragment()!!.setPresenter(allQuotesPresenter)
        onView(withId(R.id.filter_quote)).perform(click())
        onView(withText("Book quotes")).perform(click())
        verify(allQuotesPresenter).getQuotesByType("Book's quote")
        verifyNoMoreInteractions(allQuotesPresenter)
    }

    @Test
    fun isClickedOnFilterByMyQuotesCallCorrectFun() {
        rule.activity.getFragment()!!.setPresenter(allQuotesPresenter)
        onView(withId(R.id.filter_quote)).perform(click())
        onView(withText("My quotes")).perform(click())
        verify(allQuotesPresenter).getQuotesByType("My quote")
        verifyNoMoreInteractions(allQuotesPresenter)
    }

    @Test
    fun isClickedOnFilterByAllQuotesCallCorrectFun() {
        rule.activity.getFragment()!!.setPresenter(allQuotesPresenter)
        onView(withId(R.id.filter_quote)).perform(click())
        onView(withText("All quotes")).perform(click())
        verify(allQuotesPresenter).getAllQuotes()
        verifyNoMoreInteractions(allQuotesPresenter)
    }


}