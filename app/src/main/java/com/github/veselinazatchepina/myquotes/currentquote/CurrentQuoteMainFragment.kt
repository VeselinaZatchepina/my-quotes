package com.github.veselinazatchepina.myquotes.currentquote

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.data.local.pojo.AllQuoteData
import kotlinx.android.synthetic.main.fragment_current_quote_pager.view.*


class CurrentQuoteMainFragment : Fragment(), CurrentQuoteMainContract.View {

    lateinit var currentQuoteMainPresenter: CurrentQuoteMainContract.Presenter
    lateinit var rootView: View
    lateinit var quoteType: String
    lateinit var quoteCategory: String

    companion object {
        private const val QUOTE_TYPE_BUNDLE = "quote_type_bundle"
        private const val QUOTE_CATEGORY_BUNDLE = "quote_category_bundle"

        fun createInstance(quoteType: String, quoteCategory: String): CurrentQuoteMainFragment {
            val bundle = Bundle()
            bundle.putString(QUOTE_TYPE_BUNDLE, quoteType)
            bundle.putString(QUOTE_CATEGORY_BUNDLE, quoteCategory)
            val fragment = CurrentQuoteMainFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quoteType = arguments?.getString(QUOTE_TYPE_BUNDLE) ?: ""
        quoteCategory = arguments?.getString(QUOTE_CATEGORY_BUNDLE) ?: ""

        if (quoteType == "" && quoteCategory == "") {
            currentQuoteMainPresenter.getAllQuoteData()
        }
        if (quoteType != "" && quoteCategory == "") {
            currentQuoteMainPresenter.getAllQuoteDataByQuoteType(quoteType)
        }
        if (quoteType != "" && quoteCategory != "") {
            currentQuoteMainPresenter.getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType, quoteCategory)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_current_quote_pager, container, false)
        return rootView
    }

    override fun setPresenter(presenter: CurrentQuoteMainContract.Presenter) {
        currentQuoteMainPresenter = presenter
    }

    override fun createViewPager(quotes: List<AllQuoteData>) {
        rootView.quote_pager.adapter = object : FragmentStatePagerAdapter(this.fragmentManager) {

            override fun getItem(position: Int): Fragment {
                val fragment = CurrentQuoteFragment.createInstance(quotes[position])
                return fragment
            }

            override fun getCount(): Int {
                return quotes.size
            }
        }
    }
}