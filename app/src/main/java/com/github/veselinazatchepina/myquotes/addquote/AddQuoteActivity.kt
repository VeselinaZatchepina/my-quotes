package com.github.veselinazatchepina.myquotes.addquote

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.SingleFragmentAbstractActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.local.model.AllQuoteData
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource
import kotlinx.android.synthetic.main.fab_popup_menu.*


class AddQuoteActivity : SingleFragmentAbstractActivity() {

    private var addQuoteView: AddQuoteFragment? = null
    private var addQuotePresenter: AddQuotePresenter? = null
    var quoteType: String? = null
    lateinit var quoteCategory: String
    private var editQuoteData: AllQuoteData? = null

    companion object {
        private const val QUOTE_TYPE_INTENT = "quote_type_intent"
        private const val QUOTE_CATEGORY_INTENT = "quote_category_intent"
        private const val QUOTE_DATA_INTENT = "quote_data_intent"
        private const val BOOK_AUTHORS_INTENT = "book_authors_intent"
        private const val YEARS_INTENT = "years_intent"

        fun newIntent(context: Context, quoteType: String): Intent {
            val intent = Intent(context, AddQuoteActivity::class.java)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            return intent
        }

        fun newIntent(context: Context,
                      allQuoteData: AllQuoteData?): Intent {
            val intent = Intent(context, AddQuoteActivity::class.java)
            intent.putExtra(QUOTE_DATA_INTENT, allQuoteData)
            return intent
        }

        fun newIntent(context: Context, quoteType: String, quoteCategory: String): Intent {
            val intent = Intent(context, AddQuoteActivity::class.java)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            intent.putExtra(QUOTE_CATEGORY_INTENT, quoteCategory)
            return intent
        }
    }

    override fun defineInputData() {
        quoteType = intent.getStringExtra(QUOTE_TYPE_INTENT)
        quoteCategory = intent.getStringExtra(QUOTE_CATEGORY_INTENT) ?: getString(R.string.spinner_hint)
        editQuoteData = intent.getSerializableExtra(QUOTE_DATA_INTENT) as? AllQuoteData
        title = if (editQuoteData != null) {
            editQuoteData!!.types?.first()?.type
        } else {
            quoteType
        }
    }

    override fun createFragment(): Fragment {
        if (editQuoteData != null) {
            addQuoteView = AddQuoteFragment.createInstance(editQuoteData!!)
        } else {
            addQuoteView = AddQuoteFragment.createInstance(quoteType!!, quoteCategory)
        }
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