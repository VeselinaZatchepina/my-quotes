package com.github.veselinazatchepina.myquotes.allquote

import android.os.Bundle
import android.support.v4.app.Fragment


class AllQuotesFragment: Fragment(), AllQuotesContract.View {

    lateinit var allQuotesPresenter: AllQuotesContract.Presenter

    companion object {
        private const val QUOTE_TYPE_BUNDLE = "quote_type_bundle"
        private const val QUOTE_CATEGORY_BUNDLE = "quote_category_bundle"

        fun createInstance(quoteType: String, quoteCategory: String): AllQuotesFragment {
            val bundle = Bundle()
            bundle.putString(QUOTE_TYPE_BUNDLE, quoteType)
            bundle.putString(QUOTE_CATEGORY_BUNDLE, quoteCategory)
            val fragment = AllQuotesFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun setPresenter(presenter: AllQuotesContract.Presenter) {
        allQuotesPresenter = presenter
    }
}