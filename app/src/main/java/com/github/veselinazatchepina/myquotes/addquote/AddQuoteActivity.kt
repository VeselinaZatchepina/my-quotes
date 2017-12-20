package com.github.veselinazatchepina.myquotes.addquote

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.SingleFragmentAbstractActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource


class AddQuoteActivity : SingleFragmentAbstractActivity() {

    lateinit var addQuoteView : AddQuoteFragment
    lateinit var addQuotePresenter : AddQuotePresenter

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, AddQuoteActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment {
        addQuoteView = AddQuoteFragment.createInstance()
        return addQuoteView
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(applicationContext, SingleFragmentAbstractActivity.provideSchedulerProvider()),
                QuoteRemoteDataSource.getInstance())
        addQuotePresenter = AddQuotePresenter(quoteRepository, addQuoteView)
    }

    override fun setFabImageResId(): Int = R.drawable.ic_check_white_24dp

    override fun defineActionWhenFabIsPressed(view: View) {
        addQuoteView.createMapOfQuoteProperties()
    }

}