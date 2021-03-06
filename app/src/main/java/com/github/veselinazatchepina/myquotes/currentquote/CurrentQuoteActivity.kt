package com.github.veselinazatchepina.myquotes.currentquote

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.SingleFragmentAbstractActivity
import com.github.veselinazatchepina.myquotes.addquote.AddQuoteActivity
import com.github.veselinazatchepina.myquotes.allquote.AllQuotesActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.AppDatabase
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource
import kotlinx.android.synthetic.main.fab_popup_menu.*


class CurrentQuoteActivity : SingleFragmentAbstractActivity() {

    private var currentQuoteMainView: CurrentQuoteMainFragment? = null
    private var currentQuoteMainPresenter: CurrentQuoteMainPresenter? = null
    private val quoteType: String by lazy {
        intent.getStringExtra(QUOTE_TYPE_INTENT) ?: ""
    }
    private val quoteCategory: String by lazy {
        intent.getStringExtra(QUOTE_CATEGORY_INTENT) ?: ""
    }
    private val quoteId: Long by lazy {
        intent.getLongExtra(QUOTE_ID_INTENT, -1)
    }

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
        title = if (quoteType != "") {
            quoteType
        } else {
            getString(R.string.current_quote_title)
        }
    }

    override fun createFragment(): Fragment {
        currentQuoteMainView = CurrentQuoteMainFragment.createInstance(quoteType, quoteCategory, quoteId)
        return currentQuoteMainView!!
    }

    fun setMainFragment(fragment: CurrentQuoteMainFragment) {
        currentQuoteMainView = fragment
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(AppDatabase.getAppDatabaseInstance(applicationContext)),
                QuoteRemoteDataSource.getInstance())
        currentQuoteMainPresenter = CurrentQuoteMainPresenter(quoteRepository,
                currentQuoteMainView
                        ?: supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as CurrentQuoteMainFragment)
    }

    override fun setFabImageResId(): Int {
        return R.drawable.ic_mode_edit_white_24dp
    }

    override fun defineActionWhenFabIsPressed() {
        addFab.setOnClickListener {
            val currentMainFragment = currentQuoteMainView
                    ?: supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as CurrentQuoteMainFragment
            startActivity(AddQuoteActivity.newIntent(this,
                    currentMainFragment.chosenQuoteData!!.types!!.first().type,
                    currentMainFragment.chosenQuoteData))
        }
    }
}