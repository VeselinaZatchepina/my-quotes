package com.github.veselinazatchepina.myquotes.allquote

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.SearchView
import android.view.*
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.AdapterImpl
import com.github.veselinazatchepina.myquotes.currentquote.CurrentQuoteActivity
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import kotlinx.android.synthetic.main.quote_recycler_view_item.view.*
import org.jetbrains.anko.textColor
import com.github.veselinazatchepina.myquotes.enums.QuoteType as QuoteTypeEnum


class AllQuotesFragment : Fragment(), AllQuotesContract.View {

    private var allQuotesPresenter: AllQuotesContract.Presenter? = null
    lateinit var quoteType: String
    lateinit var quoteCategory: String
    private lateinit var rootView: View
    private var quotesAdapter: AdapterImpl<Quote>? = null
    private var filterQuoteType: String = ""
    private var quoteForDelete: Quote? = null

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
        setHasOptionsMenu(true)
        quoteType = arguments?.getString(QUOTE_TYPE_BUNDLE) ?: ""
        quoteCategory = arguments?.getString(QUOTE_CATEGORY_BUNDLE) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        getQuotes()
        defineChosenQuoteCategoryTitleIfExist()
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_all_quotes, menu)
        super.onCreateOptionsMenu(menu, inflater)
        hideFilterMenu(menu)
        defineSearchViewListener(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.filter_quote -> {
                val view = activity!!.findViewById(R.id.filter_quote) as ActionMenuItemView
                showPopup(view)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(activity!!, view)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.show_book_quotes -> {
                    allQuotesPresenter?.getQuotesByType(getString(QuoteTypeEnum.BOOK_QUOTE.resource))
                    filterQuoteType = getString(QuoteTypeEnum.BOOK_QUOTE.resource)
                    true
                }
                R.id.show_my_quotes -> {
                    allQuotesPresenter?.getQuotesByType(getString(QuoteTypeEnum.MY_QUOTE.resource))
                    filterQuoteType = getString(QuoteTypeEnum.MY_QUOTE.resource)
                    true
                }
                R.id.show_all_quotes -> {
                    allQuotesPresenter?.getAllQuotes()
                    filterQuoteType = ""
                    true
                }
                else -> true
            }
        }
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_popup_all_quotes, popup.menu)
        popup.show()
    }


    private fun hideFilterMenu(menu: Menu?) {
        if (quoteType != "") {
            menu?.findItem(R.id.filter_quote)?.isVisible = false
        }
    }

    private fun defineSearchViewListener(menu: Menu?) {
        val searchView = menu?.findItem(R.id.search_quote)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getQuotesFromSearchView(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getQuotesFromSearchView(newText ?: "")
                return true
            }

        })
    }

    private fun getQuotes() {
        if (quoteType == "" && quoteCategory == "") {
            allQuotesPresenter?.getAllQuotes()
        }
        if (quoteType != "" && quoteCategory == "") {
            allQuotesPresenter?.getQuotesByType(quoteType)
        }
        if (quoteType != "" && quoteCategory != "") {
            allQuotesPresenter?.getQuotesByTypeAndCategory(quoteType, quoteCategory)
        }
    }

    private fun getQuotesFromSearchView(query: String) {
        if (quoteType == "" && quoteCategory == "") {
            if (filterQuoteType != "") {
                allQuotesPresenter?.getQuotesByTypeAndTextIfContains(filterQuoteType, query)
            } else {
                allQuotesPresenter?.getQuotesByTextIfContains(query ?: "")
            }
        }
        if (quoteType != "" && quoteCategory == "") {
            allQuotesPresenter?.getQuotesByTypeAndTextIfContains(quoteType, query)
        }
        if (quoteType != "" && quoteCategory != "") {
            allQuotesPresenter?.getQuotesByTypeAndCategoryAndTextIfContains(quoteType, quoteCategory, query)
        }
    }

    private fun defineChosenQuoteCategoryTitleIfExist() {
        if (quoteCategory != "") {
            rootView.currentCategory.text = quoteCategory.toUpperCase()
            rootView.currentCategory.textColor = resources.getColor(R.color.card_background)
        } else {
            rootView.currentCategory.visibility = View.GONE
        }
    }

    override fun showQuotes(quotes: List<Quote>) {
        if (quotesAdapter != null) {
            quotesAdapter!!.update(quotes)
        } else {
            quotesAdapter = AdapterImpl(quotes, R.layout.quote_recycler_view_item, R.layout.fragment_empty_recycler_view,  {
                rootView.item_quote_text.text = getString(R.string.quote_text_format, it.quoteText)
            }, {
                if (filterQuoteType == "") {
                    startActivity(CurrentQuoteActivity.newIntent(activity!!.applicationContext,
                            quoteCategory,
                            quoteType,
                            quoteId))
                } else {
                    startActivity(CurrentQuoteActivity.newIntent(activity!!.applicationContext,
                            quoteCategory,
                            filterQuoteType,
                            quoteId))
                }
            }, {
                quoteForDelete = this
                createDeleteQuoteDialog(this)
            })
            rootView.recyclerView.adapter = quotesAdapter
            rootView.recyclerView.layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun createDeleteQuoteDialog(quote: Quote) {
        AlertDialog.Builder(activity).apply {
            val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_delete_quote, null)
            this.setView(dialogView)
            this.setCancelable(false)
                    .setPositiveButton(getString(R.string.dialog_add_category_ok)) { dialogInterface: DialogInterface?, id: Int ->
                        allQuotesPresenter?.deleteQuote(quote.quoteId)
                    }
                    .setNegativeButton(getString(R.string.dialog_add_category_cancel)) { dialogInterface: DialogInterface?, id: Int ->
                        dialogInterface?.cancel()
                    }
        }.create().show()
    }

    override fun showQuotesFromSearchView(quotes: List<Quote>) {
        quotesAdapter!!.update(quotes)
    }
}