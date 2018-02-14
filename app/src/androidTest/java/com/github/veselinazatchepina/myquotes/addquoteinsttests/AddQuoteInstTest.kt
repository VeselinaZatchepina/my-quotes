package com.github.veselinazatchepina.myquotes.addquoteinsttests

import android.support.design.widget.FloatingActionButton
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.quotecategories.QuoteCategoriesActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddQuoteInstTest {

    @get: Rule
    var rule: IntentsTestRule<QuoteCategoriesActivity> =
            IntentsTestRule(QuoteCategoriesActivity::class.java)

    @Before
    fun init() {
        rule.activity.runOnUiThread {
            rule.activity.findViewById<FloatingActionButton>(R.id.book_quote_fab).visibility = View.VISIBLE

        }
        onView(withId(R.id.add_icon_fab)).perform(ViewActions.click())
        onView(withId(R.id.book_quote_fab)).perform(ViewActions.click())
        ViewActions.closeSoftKeyboard()
    }

    @Test
    fun testIsNewAuthorsFieldDisplayedWhenButtonClicked() {
        onView(withText("Add author")).perform(scrollTo()).perform(ViewActions.click())
        onView(withContentDescription("first name")).check(ViewAssertions.matches(isDisplayed()))
        onView(withContentDescription("surname")).check(ViewAssertions.matches(isDisplayed()))
        onView(withContentDescription("middle name")).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun isDialogAddQuoteCategoryDisplayedWhenSpinnerClicked() {
        onView(withId(R.id.addCategorySpinner)).perform(click())
        onView(withText("+ add new category")).perform(click())
        onView(withText("Enter the category name")).check(ViewAssertions.matches(isDisplayed()))
    }

}
