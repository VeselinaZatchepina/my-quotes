package com.github.veselinazatchepina.myquotes.currentquote

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.SingleFragmentAbstractActivity
import com.github.veselinazatchepina.myquotes.addquote.AddQuoteActivity
import com.github.veselinazatchepina.myquotes.allquote.AllQuotesActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource


class CurrentQuoteActivity : SingleFragmentAbstractActivity() {

    private lateinit var currentQuoteMainView: CurrentQuoteMainFragment
    private lateinit var currentQuoteMainPresenter: CurrentQuoteMainPresenter
    private lateinit var quoteType: String
    private lateinit var quoteCategory: String
    private var quoteId: Long = -1

    companion object {
        private const val QUOTE_CATEGORY_INTENT = "quote_category_intent"
        private const val QUOTE_TYPE_INTENT = "quote_type_intent"
        private const val QUOTE_ID_INTENT = "quote_id_intent"

        fun newIntent(context: Context): Intent {
            return Intent(context, AllQuotesActivity::class.java)
        }

        fun newIntent(context: Context, quoteCategory: String, quoteType: String): Intent {
            val intent = Intent(context, CurrentQuoteActivity::class.java)
            intent.putExtra(QUOTE_CATEGORY_INTENT, quoteCategory)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            return intent
        }

        fun newIntent(context: Context, quoteCategory: String, quoteType: String, quoteId: Long): Intent {
            val intent = Intent(context, CurrentQuoteActivity::class.java)
            intent.putExtra(QUOTE_CATEGORY_INTENT, quoteCategory)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            intent.putExtra(QUOTE_ID_INTENT, quoteId)
            return intent
        }
    }

    override fun defineInputData() {
        quoteType = intent.getStringExtra(QUOTE_TYPE_INTENT) ?: ""
        quoteCategory = intent.getStringExtra(QUOTE_CATEGORY_INTENT) ?: ""
        quoteId = intent.getLongExtra(QUOTE_ID_INTENT, -1)
        title = if (quoteType != "") {
            quoteType
        } else {
            getString(R.string.current_quote_title)
        }
    }

    override fun createFragment(): Fragment {
        currentQuoteMainView = CurrentQuoteMainFragment.createInstance(quoteType, quoteCategory, quoteId)
        return currentQuoteMainView
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(applicationContext, provideSchedulerProvider()),
                QuoteRemoteDataSource.getInstance())
        currentQuoteMainPresenter = CurrentQuoteMainPresenter(quoteRepository, currentQuoteMainView)
    }

    override fun setFabImageResId(): Int {
        return R.drawable.ic_mode_edit_white_24dp
    }

    override fun defineActionWhenFabIsPressed(view: View) {
        startActivity(AddQuoteActivity.newIntent(this, quoteType, currentQuoteMainView.selectedQuoteId!!))
    }
}