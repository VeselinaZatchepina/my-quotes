package com.github.veselinazatchepina.myquotes

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.github.veselinazatchepina.myquotes.quotecategories.QuoteCategoriesActivity
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
        onView(withText("Delete quote category?")).check(matches(isDisplayed()));
    }
}