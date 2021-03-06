package com.github.veselinazatchepina.myquotes.addquote

import android.content.Context
import android.content.Intent
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.SingleFragmentAbstractActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.AppDatabase
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.local.model.AllQuoteData
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource
import kotlinx.android.synthetic.main.fab_popup_menu.*


class AddQuoteActivity : SingleFragmentAbstractActivity() {

    private var addQuoteView: AddQuoteFragment? = null
    private var addQuotePresenter: AddQuotePresenter? = null
    private val quoteType: String by lazy {
        intent.getStringExtra(QUOTE_TYPE_INTENT)
    }
    private val quoteCategory: String by lazy {
        intent.getStringExtra(QUOTE_CATEGORY_INTENT) ?: getString(R.string.spinner_hint)
    }
    private val editQuoteData: AllQuoteData? by lazy {
        intent.getSerializableExtra(QUOTE_DATA_INTENT) as? AllQuoteData
    }

    companion object {
        private const val QUOTE_TYPE_INTENT = "quote_type_intent"
        private const val QUOTE_CATEGORY_INTENT = "quote_category_intent"
        private const val QUOTE_DATA_INTENT = "quote_data_intent"

        fun newIntent(context: Context, quoteType: String): Intent {
            val intent = Intent(context, AddQuoteActivity::class.java)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            return intent
        }

        fun newIntent(context: Context,
                      quoteType: String,
                      allQuoteData: AllQuoteData?): Intent {
            val intent = Intent(context, AddQuoteActivity::class.java)
            intent.putExtra(QUOTE_DATA_INTENT, allQuoteData)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            return intent
        }

        fun newIntent(context: Context,
                      quoteType: String,
                      quoteCategory: String): Intent {
            val intent = Intent(context, AddQuoteActivity::class.java)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            intent.putExtra(QUOTE_CATEGORY_INTENT, quoteCategory)
            return intent
        }
    }

    override fun defineInputData() {
        title = if (editQuoteData != null) {
            editQuoteData!!.types?.first()?.type
        } else {
            quoteType
        }
    }

    override fun createFragment(): Fragment {
        if (editQuoteData != null) {
            addQuoteView = AddQuoteFragment.createInstanceForEdit(editQuoteData!!, quoteType)
        } else {
            addQuoteView = AddQuoteFragment.createInstance(quoteType, quoteCategory)
        }
        return addQuoteView!!
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun setFragment(fragment: AddQuoteFragment) {
        addQuoteView = fragment
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(AppDatabase.getAppDatabaseInstance(applicationContext)),
                QuoteRemoteDataSource.getInstance())
        addQuotePresenter = AddQuotePresenter(quoteRepository,
                addQuoteView
                        ?: supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as AddQuoteFragment)
    }

    override fun setFabImageResId(): Int = R.drawable.ic_check_white_24dp

    override fun defineActionWhenFabIsPressed() {
        addFab.setOnClickListener {
            (addQuoteView
                    ?: supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as AddQuoteFragment)
                    .createMapOfQuoteProperties()
        }
    }
}