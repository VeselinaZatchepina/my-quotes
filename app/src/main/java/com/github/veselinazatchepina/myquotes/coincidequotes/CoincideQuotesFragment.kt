package com.github.veselinazatchepina.myquotes.coincidequotes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.AdapterImpl
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import kotlinx.android.synthetic.main.quote_recycler_view_item.view.*


class CoincideQuotesFragment : Fragment(), CoincideQuotesContract.View {

    private var getIdeaPresenter: CoincideQuotesContract.Presenter? = null
    private lateinit var rootView: View
    private var quotesAdapter: AdapterImpl<Quote>? = null
    private val inputText: String by lazy {
        arguments?.getString(INPUT_TEXT_BUNDLE) ?: ""
    }

    companion object {
        private const val INPUT_TEXT_BUNDLE = "input_text_bundle"

        fun createInstance(inputText: String): CoincideQuotesFragment {
            val bundle = Bundle()
            bundle.putString(INPUT_TEXT_BUNDLE, inputText)
            val fragment = CoincideQuotesFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun setPresenter(presenter: CoincideQuotesContract.Presenter) {
        getIdeaPresenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        getIdeaPresenter?.getCoincideQuotesByInputText(inputText)
        return rootView
    }

    override fun showQuotes(quotes: List<Quote>) {
        quotesAdapter = AdapterImpl(quotes, R.layout.quote_recycler_view_item,
                R.layout.fragment_empty_coincide_recycler_view, {
            rootView.item_quote_text.text = getString(R.string.quote_text_format, it.quoteText)
        }, {

        }, {

        })
        rootView.recyclerView.adapter = quotesAdapter
        rootView.recyclerView.layoutManager = LinearLayoutManager(activity)
    }
}