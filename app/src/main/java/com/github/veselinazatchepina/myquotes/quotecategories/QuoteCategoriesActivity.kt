package com.github.veselinazatchepina.myquotes.quotecategories

import android.app.Fragment
import android.content.Context
import android.content.Intent
import com.github.veselinazatchepina.myquotes.abstracts.NavigationDrawerAbstractActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource


class QuoteCategoriesActivity : NavigationDrawerAbstractActivity() {

    private lateinit var mQuoteCategoriesView: QuoteCategoriesFragment
    private lateinit var mQuoteCategoriesPresenter: QuoteCategoriesPresenter

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, QuoteCategoriesActivity::class.java)
        }
    }

    override fun createFragment(): Fragment? {
        mQuoteCategoriesView = QuoteCategoriesFragment.createInstance()
        return mQuoteCategoriesView
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(QuoteLocalDataSource(), QuoteRemoteDataSource())
        mQuoteCategoriesPresenter = QuoteCategoriesPresenter(quoteRepository, mQuoteCategoriesView)
    }

}