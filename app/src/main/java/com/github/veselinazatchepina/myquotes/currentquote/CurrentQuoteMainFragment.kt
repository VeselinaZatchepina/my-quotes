package com.github.veselinazatchepina.myquotes.currentquote

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.AppDatabase
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.local.model.AllQuoteData
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource
import icepick.Icepick
import icepick.State
import kotlinx.android.synthetic.main.fragment_current_quote_pager.view.*


class CurrentQuoteMainFragment : Fragment(), CurrentQuoteMainContract.View {

    private var currentQuoteMainPresenter: CurrentQuoteMainContract.Presenter? = null
    var currentQuoteFragment: CurrentQuoteFragment? = null
    lateinit var rootView: View
    private val quoteType: String by lazy {
        arguments?.getString(QUOTE_TYPE_BUNDLE) ?: ""
    }
    private val quoteCategory: String by lazy {
        arguments?.getString(QUOTE_CATEGORY_BUNDLE) ?: ""
    }
    var chosenQuoteData: AllQuoteData? = null
    @JvmField
    @State
    var selectedQuoteId: Long? = -1L

    companion object {
        private const val QUOTE_TYPE_BUNDLE = "quote_type_bundle"
        private const val QUOTE_CATEGORY_BUNDLE = "quote_category_bundle"
        private const val QUOTE_ID_BUNDLE = "quote_id_bundle"

        fun createInstance(quoteType: String, quoteCategory: String, quoteId: Long): CurrentQuoteMainFragment {
            val bundle = Bundle()
            bundle.putString(QUOTE_TYPE_BUNDLE, quoteType)
            bundle.putString(QUOTE_CATEGORY_BUNDLE, quoteCategory)
            bundle.putLong(QUOTE_ID_BUNDLE, quoteId)
            val fragment = CurrentQuoteMainFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedQuoteId = arguments?.getLong(QUOTE_ID_BUNDLE, -1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Icepick.restoreInstanceState(this, savedInstanceState)
        rootView = inflater.inflate(R.layout.fragment_current_quote_pager, container, false)
        getAllQuoteData()
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
    }

    private fun getAllQuoteData() {
        if (quoteType == "" && quoteCategory == "") {
            currentQuoteMainPresenter?.getAllQuoteData()
        }
        if (quoteType != "" && quoteCategory == "") {
            currentQuoteMainPresenter?.getAllQuoteDataByQuoteType(quoteType)
        }
        if (quoteType != "" && quoteCategory != "") {
            currentQuoteMainPresenter?.getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType, quoteCategory)
        }
    }

    override fun setPresenter(presenter: CurrentQuoteMainContract.Presenter) {
        currentQuoteMainPresenter = presenter
    }

    override fun createViewPager(quotes: List<AllQuoteData>) {
        rootView.quotePager.adapter = object : FragmentStatePagerAdapter(this.fragmentManager) {

            override fun getItem(position: Int): Fragment {
                currentQuoteFragment = CurrentQuoteFragment.createInstance(quotes[position])
                createPresenter(currentQuoteFragment!!)
                return currentQuoteFragment!!
            }

            override fun getCount(): Int {
                return quotes.size
            }
        }
        setViewPagerOnClickedQuotePosition(quotes)
        setViewPagerOnPageChangeListener(quotes)
    }

    private fun setViewPagerOnPageChangeListener(quotes: List<AllQuoteData>) {
        rootView.quotePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                selectedQuoteId = quotes[position].quote!!.quoteId
                chosenQuoteData = quotes[position]
            }

            override fun onPageSelected(position: Int) {

            }

        })
    }

    private fun setViewPagerOnClickedQuotePosition(quotes: List<AllQuoteData>) {
        (0 until quotes.size)
                .filter { quotes[it].quote!!.quoteId == selectedQuoteId }
                .forEach {
                    rootView.quotePager.currentItem = it
                }
    }

    private fun createPresenter(currentQuoteView: CurrentQuoteFragment) {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(AppDatabase.getAppDatabaseInstance(activity!!.applicationContext)),
                QuoteRemoteDataSource.getInstance())
        val currentQuotePresenter = CurrentQuotePresenter(quoteRepository,
                currentQuoteView)
    }
}