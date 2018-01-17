package com.github.veselinazatchepina.myquotes.allquote

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.NavigationDrawerAbstractActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource


class AllQuotesActivity : NavigationDrawerAbstractActivity() {

    private var allQuotesView: AllQuotesFragment? = null
    private var allQuotesPresenter: AllQuotesPresenter? = null

    private lateinit var quoteType: String
    private lateinit var quoteCategory: String

    companion object {
        private const val QUOTE_CATEGORY_INTENT = "quote_category_intent"
        private const val QUOTE_TYPE_INTENT = "quote_type_intent"

        fun newIntent(context: Context): Intent {
            return Intent(context, AllQuotesActivity::class.java)
        }

        fun newIntent(context: Context, quoteCategory: String, quoteType: String): Intent {
            val intent = Intent(context, AllQuotesActivity::class.java)
            intent.putExtra(QUOTE_CATEGORY_INTENT, quoteCategory)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            return intent
        }
    }

    override fun defineNavigationDrawer() {
        if (quoteCategory == "") {
            super.defineNavigationDrawer()
        }
    }

    override fun defineAppBarLayoutExpandableValue() {
        setAppBarNotExpandable()
    }

    override fun defineInputData() {
        quoteType = intent.getStringExtra(QUOTE_TYPE_INTENT) ?: ""
        quoteCategory = intent.getStringExtra(QUOTE_CATEGORY_INTENT) ?: ""
        title = if (quoteType == "") {
            getString(R.string.title_all_quotes)
        } else {
            quoteType
        }
    }

    override fun createFragment(): Fragment {
        allQuotesView = AllQuotesFragment.createInstance(quoteType, quoteCategory)
        return allQuotesView!!
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(applicationContext, provideSchedulerProvider()),
                QuoteRemoteDataSource.getInstance())
        allQuotesPresenter = AllQuotesPresenter(quoteRepository,
                allQuotesView ?: supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as AllQuotesFragment)
    }
}