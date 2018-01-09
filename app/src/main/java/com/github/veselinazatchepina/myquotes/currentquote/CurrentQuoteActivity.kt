package com.github.veselinazatchepina.myquotes.currentquote

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.SingleFragmentAbstractActivity
import com.github.veselinazatchepina.myquotes.bookcategories.QuoteCategoriesPresenter
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource


class CurrentQuoteActivity : SingleFragmentAbstractActivity() {

    private lateinit var currentQuoteView: CurrentQuoteFragment
    private lateinit var currentQuotePresenter: CurrentQuotePresenter
    private var quoteId: Long = 0L

    companion object {
        private const val QUOTE_ID_INTENT = "quote_id_intent"

        fun newIntent(context: Context, quoteId: Long): Intent {
            val intent = Intent(context, CurrentQuoteActivity::class.java)
            intent.putExtra(QUOTE_ID_INTENT, quoteId)
            return intent
        }
    }

    override fun defineInputData() {
        quoteId = intent.getLongExtra(QUOTE_ID_INTENT, 0L)
        title = getString(R.string.current_quote_title)
    }


    override fun createFragment(): Fragment {
        currentQuoteView = CurrentQuoteFragment.createInstance(quoteId)
        return currentQuoteView
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(applicationContext, provideSchedulerProvider()),
                QuoteRemoteDataSource.getInstance())
        currentQuotePresenter = CurrentQuotePresenter(quoteRepository, currentQuoteView)
    }
}