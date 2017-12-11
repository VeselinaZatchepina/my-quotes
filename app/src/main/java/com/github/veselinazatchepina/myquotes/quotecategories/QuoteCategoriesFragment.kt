package com.github.veselinazatchepina.myquotes.quotecategories

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class QuoteCategoriesFragment : Fragment(), QuoteCategoriesContract.View {

    companion object {
        fun createInstance(): QuoteCategoriesFragment {
            return QuoteCategoriesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setPresenter(presenter: QuoteCategoriesContract.Presenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}