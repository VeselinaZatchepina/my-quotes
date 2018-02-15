package com.github.veselinazatchepina.myquotes.coincidequotes

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import com.github.veselinazatchepina.myquotes.abstracts.SingleFragmentAbstractActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.AppDatabase
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource
import kotlinx.android.synthetic.main.fab_popup_menu.*


class CoincideQuotesActivity : SingleFragmentAbstractActivity() {

    private var coincideQuotesView: CoincideQuotesFragment? = null
    private var coincideQuotesPresenter: CoincideQuotesPresenter? = null
    private val inputText: String by lazy {
        intent.getStringExtra(INPUT_TEXT_INTENT)
    }

    companion object {
        private const val INPUT_TEXT_INTENT = "input_text_intent"

        fun newIntent(context: Context, inputText: String): Intent {
            val intent = Intent(context, CoincideQuotesActivity::class.java)
            intent.putExtra(INPUT_TEXT_INTENT, inputText)
            return intent
        }
    }

    override fun defineInputData() {
        add_icon_fab.visibility = View.GONE
        title = "Coincide idea"
    }

    override fun createFragment(): Fragment {
        coincideQuotesView = CoincideQuotesFragment.createInstance(inputText)
        return coincideQuotesView!!
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(AppDatabase.getAppDatabaseInstance(applicationContext)),
                QuoteRemoteDataSource.getInstance())
        coincideQuotesPresenter = CoincideQuotesPresenter(quoteRepository,
                coincideQuotesView
                        ?: supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as CoincideQuotesFragment)
    }
}