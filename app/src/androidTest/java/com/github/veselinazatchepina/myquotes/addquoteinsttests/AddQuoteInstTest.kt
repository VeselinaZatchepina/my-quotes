package com.github.veselinazatchepina.myquotes.addquoteinsttests

import android.support.design.widget.FloatingActionButton
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.BundleMatchers
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.addquote.AddQuoteActivity
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddQuoteInstTest {

    @get: Rule
    var rule: IntentsTestRule<AddQuoteActivity> =
            IntentsTestRule(AddQuoteActivity::class.java)




}
