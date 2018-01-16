package com.github.veselinazatchepina.myquotes.allquote

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.AdapterImpl
import com.github.veselinazatchepina.myquotes.currentquote.CurrentQuoteActivity
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import kotlinx.android.synthetic.main.quote_recycler_view_item.view.*


class AllQuotesFragment : Fragment(), AllQuotesContract.View {

    lateinit var allQuotesPresenter: AllQuotesContract.Presenter
    lateinit var quoteType: String
    lateinit var quoteCategory: String
    lateinit var rootView: View
    lateinit var quotesAdapter: AdapterImpl<Quote>

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quoteType = arguments?.getString(QUOTE_TYPE_BUNDLE) ?: ""
        quoteCategory = arguments?.getString(QUOTE_CATEGORY_BUNDLE) ?: ""

        if (quoteType == "" && quoteCategory == "") {
            allQuotesPresenter.getAllQuotes()
        }
        if (quoteType != "" && quoteCategory == "") {
            allQuotesPresenter.getQuotesByQuoteType(quoteType)
        }
        if (quoteType != "" && quoteCategory != "") {
            allQuotesPresenter.getQuotesByQuoteTypeAndQuoteCategory(quoteType, quoteCategory)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        return rootView
    }

    override fun showQuotes(quotes: List<Quote>) {
        quotesAdapter = AdapterImpl(quotes, R.layout.quote_recycler_view_item, {
            rootView.item_quote_text.text = it.quoteText
        }, {
            startActivity(CurrentQuoteActivity.newIntent(activity!!.applicationContext,
                    quoteCategory,
                    quoteType))
        })
        rootView.recyclerView.adapter = quotesAdapter
        rootView.recyclerView.layoutManager = LinearLayoutManager(activity)
    }
}