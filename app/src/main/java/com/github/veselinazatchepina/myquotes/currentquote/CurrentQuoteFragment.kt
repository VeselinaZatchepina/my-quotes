package com.github.veselinazatchepina.myquotes.currentquote

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import kotlinx.android.synthetic.main.fragment_current_quote.view.*


class CurrentQuoteFragment : Fragment(), CurrentQuoteContract.View {

    lateinit var currentQuotePresenter: CurrentQuoteContract.Presenter
    private var quoteId: Long = 0L
    lateinit var rootView: View

    companion object {
        private const val QUOTE_ID_BUNDLE = "quote_id_bundle"

        fun createInstance(quoteId: Long): CurrentQuoteFragment {
            val bundle = Bundle()
            bundle.putLong(QUOTE_ID_BUNDLE, quoteId)
            val fragment = CurrentQuoteFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun setPresenter(presenter: CurrentQuoteContract.Presenter) {
        currentQuotePresenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quoteId = arguments?.getLong(QUOTE_ID_BUNDLE) ?: 0L
        currentQuotePresenter.getQuoteById(quoteId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_current_quote, container, false)
        return rootView
    }

    override fun showQuote(quote: Quote) {
        rootView.current_quote_text.text = quote.quoteText
    }


}