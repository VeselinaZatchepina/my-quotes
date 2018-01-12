package com.github.veselinazatchepina.myquotes.currentquote

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.data.local.pojo.AllQuoteData
import kotlinx.android.synthetic.main.fragment_current_quote.view.*
import java.io.Serializable


class CurrentQuoteFragment : Fragment(){

    lateinit var allQuoteData: AllQuoteData
    lateinit var rootView: View

    companion object {
        private const val ALL_QUOTE_DATA_BUNDLE = "all_quote_data_bundle"

        fun createInstance(allQuoteData: AllQuoteData): CurrentQuoteFragment {
            val bundle = Bundle()
            bundle.putSerializable(ALL_QUOTE_DATA_BUNDLE, allQuoteData as Serializable)
            val fragment = CurrentQuoteFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allQuoteData = arguments?.getSerializable(ALL_QUOTE_DATA_BUNDLE) as AllQuoteData
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_current_quote, container, false)
        showQuote(allQuoteData)
        return rootView
    }

    fun showQuote(allQuoteData: AllQuoteData) {
        rootView.current_quote_text.text = allQuoteData.quote?.quoteText ?: ""
    }


}