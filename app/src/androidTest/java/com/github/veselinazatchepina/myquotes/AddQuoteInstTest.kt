package com.github.veselinazatchepina.myquotes

import android.support.design.widget.FloatingActionButton
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.github.veselinazatchepina.myquotes.quotecategories.QuoteCategoriesActivity
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddQuoteInstTest {

    @get: Rule
    var rule: IntentsTestRule<QuoteCategoriesActivity> =
            IntentsTestRule(QuoteCategoriesActivity::class.java)

    @Test
    fun isAddBookQuoteIntentCorrect() {
        rule.activity.runOnUiThread {
            rule.activity.findViewById<FloatingActionButton>(R.id.book_quote_fab).visibility = View.VISIBLE

        }
        onView(withId(R.id.add_icon_fab)).perform(click())
        onView(withId(R.id.book_quote_fab)).perform(click())

        intended(hasExtras(
                hasEntry(equalTo("quote_type_intent"), equalTo("Book's quote"))
        ))
    }

    @Test
    fun isAddMyQuoteIntentCorrect() {
        rule.activity.runOnUiThread {
            rule.activity.findViewById<FloatingActionButton>(R.id.my_quote_fab).visibility = View.VISIBLE

        }
        onView(withId(R.id.add_icon_fab)).perform(click())
        onView(withId(R.id.my_quote_fab)).perform(click())

        intended(hasExtras(
                hasEntry(equalTo("quote_type_intent"), equalTo("My quote"))
        ))
    }
}
