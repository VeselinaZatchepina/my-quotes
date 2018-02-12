package com.github.veselinazatchepina.myquotes

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.github.veselinazatchepina.myquotes.allquote.AllQuotesActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AllQuotesInstTest {

    @get: Rule
    var rule: IntentsTestRule<AllQuotesActivity> =
            IntentsTestRule(AllQuotesActivity::class.java)

    @Test
    fun isDeleteQuoteDialogDisplayed() {
        onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.longClick()))
        Espresso.onView(ViewMatchers.withText("Delete quote?"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}