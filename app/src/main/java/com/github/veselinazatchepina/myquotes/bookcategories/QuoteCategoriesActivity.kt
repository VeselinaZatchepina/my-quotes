package com.github.veselinazatchepina.myquotes.bookcategories


import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.github.veselinazatchepina.myquotes.abstracts.NavigationDrawerAbstractActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource
import com.github.veselinazatchepina.myquotes.enums.QuoteType


class QuoteCategoriesActivity : NavigationDrawerAbstractActivity() {

    private lateinit var quoteCategoriesView: QuoteCategoriesFragment
    private lateinit var quoteCategoriesPresenter: QuoteCategoriesPresenter

    private lateinit var quoteType: String

    companion object {
        private const val QUOTE_TYPE_INTENT = "quote_type_intent"

        fun newIntent(context: Context, quoteType: String): Intent {
            val intent = Intent(context, QuoteCategoriesActivity::class.java)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            return intent
        }
    }

    override fun defineInputData() {
        quoteType = intent.getStringExtra(QUOTE_TYPE_INTENT) ?: getString(QuoteType.BOOK_QUOTE.resource)
        title = quoteType
    }

    override fun createFragment(): Fragment {
        quoteCategoriesView = QuoteCategoriesFragment.createInstance(quoteType)
        return quoteCategoriesView
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(applicationContext, provideSchedulerProvider()),
                QuoteRemoteDataSource.getInstance())
        quoteCategoriesPresenter = QuoteCategoriesPresenter(quoteRepository, quoteCategoriesView)
    }
}