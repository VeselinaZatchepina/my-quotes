package com.github.veselinazatchepina.myquotes.bookcategories


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.AdapterImpl
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteCategory
import kotlinx.android.synthetic.main.book_categories_recycler_view_item.view.*
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*


class BookCategoriesFragment : Fragment(), BookCategoriesContract.View {

    lateinit var bookCategoriesPresenter: BookCategoriesContract.Presenter
    lateinit var bookCategoriesAdapter: AdapterImpl<QuoteCategory>
    lateinit var rootView: View

    companion object {
        fun createInstance(): BookCategoriesFragment {
            return BookCategoriesFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookCategoriesPresenter.getBookCategoriesList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        setCurrentCategoryTitleIsGone(rootView)
        return rootView
    }

    override fun showBookCategoriesList(bookCategories: List<QuoteCategory>) {
        bookCategoriesAdapter = AdapterImpl(bookCategories, R.layout.book_categories_recycler_view_item, {
            item_category_name.text = it.categoryName
            item_quote_count.text = it.quoteCount.toString()
        }, {
            bookCategoriesPresenter.getQuotesByCategory(this.categoryName)
        })
        rootView.recycler_view.adapter = bookCategoriesAdapter
        rootView.recycler_view.layoutManager = LinearLayoutManager(activity)
    }

    override fun setPresenter(presenter: BookCategoriesContract.Presenter) {
        bookCategoriesPresenter = presenter
    }

    private fun setCurrentCategoryTitleIsGone(rootView: View) {
        rootView.current_category.visibility = View.GONE
    }
}