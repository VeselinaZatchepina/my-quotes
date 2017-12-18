package com.github.veselinazatchepina.myquotes.quotecategories

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.AdapterImpl
import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory
import kotlinx.android.synthetic.main.book_categories_recycler_view_item.view.*
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import org.jetbrains.anko.toast


class BookCategoriesFragment : Fragment(), BookCategoriesContract.View {

    var mQuoteCategoriesPresenter: BookCategoriesContract.Presenter? = null
    lateinit var bookCategoriesAdapter: AdapterImpl<BookCategory>
    lateinit var bookCategories: List<BookCategory>

    companion object {
        fun createInstance(): BookCategoriesFragment {
            return BookCategoriesFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookCategories = arrayListOf(BookCategory(1, "first", 1),
                BookCategory(2, "second", 2),
                BookCategory(3, "third", 1))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_recycler_view, container, false)
        setCurrentCategoryTitleIsGone(rootView)
        defineRecyclerView(rootView)



        return rootView
    }

    override fun setPresenter(presenter: BookCategoriesContract.Presenter) {
        mQuoteCategoriesPresenter = presenter
    }

    private fun setCurrentCategoryTitleIsGone(rootView: View?) {
        rootView?.current_category?.visibility = View.GONE
    }

    private fun defineRecyclerView(rootView: View) {
        bookCategoriesAdapter = AdapterImpl(bookCategories, R.layout.book_categories_recycler_view_item, {
            item_category_name.text = it.categoryName
            item_quote_count.text = it.quoteCount.toString()
        }, {
            toast("Clicked ${this.categoryName}")
        })
        rootView.recycler_view.adapter = bookCategoriesAdapter
        rootView.recycler_view.layoutManager = LinearLayoutManager(activity)
    }
}