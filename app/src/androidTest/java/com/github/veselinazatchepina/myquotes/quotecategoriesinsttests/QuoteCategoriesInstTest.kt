package com.github.veselinazatchepina.myquotes.quotecategoriesinsttests

import android.support.design.widget.FloatingActionButton
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.BundleMatchers
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.quotecategories.QuoteCategoriesActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuoteCategoriesInstTest {

    @get: Rule
    var rule: IntentsTestRule<QuoteCategoriesActivity> =
            IntentsTestRule(QuoteCategoriesActivity::class.java)

    @Test
    fun isDeleteQuoteCategoryDialogDisplayed() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, longClick()))
        onView(withText("Delete quote category?")).check(matches(isDisplayed()))
    }

    @Test
    fun isAddBookQuoteIntentCorrect() {
        rule.activity.runOnUiThread {
            rule.activity.findViewById<FloatingActionButton>(R.id.book_quote_fab).visibility = View.VISIBLE

        }
        onView(withId(R.id.add_icon_fab)).perform(ViewActions.click())
        onView(withId(R.id.book_quote_fab)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasExtras(
                BundleMatchers.hasEntry(CoreMatchers.equalTo("quote_type_intent"), CoreMatchers.equalTo("Book's quote"))
        ))
    }

    @Test
    fun isAddMyQuoteIntentCorrect() {
        rule.activity.runOnUiThread {
            rule.activity.findViewById<FloatingActionButton>(R.id.my_quote_fab).visibility = View.VISIBLE

        }
        onView(withId(R.id.add_icon_fab)).perform(ViewActions.click())
        onView(withId(R.id.my_quote_fab)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.hasExtras(
                BundleMatchers.hasEntry(CoreMatchers.equalTo("quote_type_intent"), CoreMatchers.equalTo("My quote"))
        ))
    }
}