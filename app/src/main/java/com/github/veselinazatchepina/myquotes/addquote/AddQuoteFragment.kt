package com.github.veselinazatchepina.myquotes.addquote

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.R


class AddQuoteFragment : Fragment(), AddQuoteContract.View {

    lateinit var addQuotePresenter: AddQuoteContract.Presenter
    lateinit var rootView: View

    companion object {
        fun createInstance(): AddQuoteFragment {
            return AddQuoteFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_add_quote, container, false)
        return rootView
    }

    override fun setPresenter(presenter: AddQuoteContract.Presenter) {
        addQuotePresenter = presenter
    }

}