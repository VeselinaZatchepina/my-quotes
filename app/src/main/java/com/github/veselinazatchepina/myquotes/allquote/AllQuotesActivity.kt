package com.github.veselinazatchepina.myquotes.allquote

import android.content.Context
import android.content.Intent
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.NavigationDrawerAbstractActivity
import com.github.veselinazatchepina.myquotes.addquote.AddQuoteActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.AppDatabase
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource
import kotlinx.android.synthetic.main.fab_popup_menu.*


class AllQuotesActivity : NavigationDrawerAbstractActivity() {

    private var allQuotesView: AllQuotesFragment? = null
    private var allQuotesPresenter: AllQuotesPresenter? = null
    private val quoteType: String by lazy {
        intent.getStringExtra(QUOTE_TYPE_INTENT) ?: ""
    }
    private val quoteCategory: String by lazy {
        intent.getStringExtra(QUOTE_CATEGORY_INTENT) ?: ""
    }

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

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun getFragment(): AllQuotesFragment? {
        return allQuotesView
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(AppDatabase.getAppDatabaseInstance(applicationContext)),
                QuoteRemoteDataSource.getInstance())
        allQuotesPresenter = AllQuotesPresenter(quoteRepository,
                allQuotesView
                        ?: supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as AllQuotesFragment)
    }

    override fun defineActionWhenFabIsPressed() {
        if (quoteType == "") {
            super.defineActionWhenFabIsPressed()
        } else {
            addFab.setOnClickListener {
                startActivity(AddQuoteActivity.newIntent(this, quoteType, quoteCategory))
            }
        }
    }
}