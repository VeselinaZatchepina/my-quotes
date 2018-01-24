package com.github.veselinazatchepina.myquotes.addquote

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.SingleFragmentAbstractActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource
import kotlinx.android.synthetic.main.fab_popup_menu.*


class AddQuoteActivity : SingleFragmentAbstractActivity() {

    private var addQuoteView: AddQuoteFragment? = null
    private var addQuotePresenter: AddQuotePresenter? = null
    lateinit var quoteType: String

    companion object {
        private const val QUOTE_TYPE_INTENT = "quote_type_intent"
        private const val QUOTE_ID_INTENT = "quote_id_intent"

        fun newIntent(context: Context, quoteType: String): Intent {
            val intent = Intent(context, AddQuoteActivity::class.java)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            return intent
        }

        fun newIntent(context: Context, quoteType: String, quoteId: Long): Intent {
            val intent = Intent(context, AddQuoteActivity::class.java)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            intent.putExtra(QUOTE_ID_INTENT, quoteId)
            return intent
        }
    }

    override fun defineInputData() {
        quoteType = intent.getStringExtra(QUOTE_TYPE_INTENT)
        //TODO if quoteType = ""
        title = quoteType
    }

    override fun createFragment(): Fragment {
        addQuoteView = AddQuoteFragment.createInstance(quoteType)
        return addQuoteView!!
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(applicationContext, SingleFragmentAbstractActivity.provideSchedulerProvider()),
                QuoteRemoteDataSource.getInstance())
        addQuotePresenter = AddQuotePresenter(quoteRepository,
                addQuoteView ?: supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as AddQuoteFragment)
    }

    override fun setFabImageResId(): Int = R.drawable.ic_check_white_24dp

    override fun defineActionWhenFabIsPressed() {
        add_icon_fab.setOnClickListener {
            (addQuoteView ?:
                    supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as AddQuoteFragment)
                    .createMapOfQuoteProperties()
        }
    }
}