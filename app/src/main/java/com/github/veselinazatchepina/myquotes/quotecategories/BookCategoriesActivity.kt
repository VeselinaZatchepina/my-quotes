package com.github.veselinazatchepina.myquotes.quotecategories


import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.NavigationDrawerAbstractActivity
import com.github.veselinazatchepina.myquotes.addquote.AddQuoteActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource
import com.github.veselinazatchepina.myquotes.enums.QuoteType


class BookCategoriesActivity : NavigationDrawerAbstractActivity() {

    private lateinit var bookCategoriesView: BookCategoriesFragment
    private lateinit var bookCategoriesPresenter: BookCategoriesPresenter

    private var quoteType: String? = null

    companion object {
        private const val QUOTE_TYPE_INTENT = "quote_type_intent"

        fun newIntent(context: Context, quoteType: String): Intent {
            val intent = Intent(context, BookCategoriesActivity::class.java)
            intent.putExtra(QUOTE_TYPE_INTENT, quoteType)
            return intent
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_nav_drawer
    }

    override fun defineInputData() {
        quoteType = intent.getStringExtra(QUOTE_TYPE_INTENT)
        title = quoteType ?: getString(QuoteType.BOOK_QUOTE.resource)
    }

    override fun createFragment(): Fragment {
        bookCategoriesView = BookCategoriesFragment.createInstance()
        return bookCategoriesView
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(applicationContext, provideSchedulerProvider()),
                QuoteRemoteDataSource.getInstance())
        bookCategoriesPresenter = BookCategoriesPresenter(quoteRepository, bookCategoriesView)
    }

    override fun defineActionWhenFabIsPressed(view: View) {
        startActivity(AddQuoteActivity.newIntent(this))
    }
}