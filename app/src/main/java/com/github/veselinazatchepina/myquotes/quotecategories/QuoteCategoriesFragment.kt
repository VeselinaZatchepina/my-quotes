package com.github.veselinazatchepina.myquotes.quotecategories

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.R
import kotlinx.android.synthetic.main.fragment_recycler_view.*


class QuoteCategoriesFragment : Fragment(), QuoteCategoriesContract.View {

    var mQuoteCategoriesPresenter: QuoteCategoriesContract.Presenter? = null

    companion object {
        fun createInstance(): QuoteCategoriesFragment {
            return QuoteCategoriesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_recycler_view, container,false)
        current_category.visibility = View.GONE




        return rootView
    }

    override fun setPresenter(presenter: QuoteCategoriesContract.Presenter) {
        mQuoteCategoriesPresenter = presenter
    }
}