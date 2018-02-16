package com.github.veselinazatchepina.myquotes.getidea

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.coincidequotes.CoincideQuotesActivity
import kotlinx.android.synthetic.main.fragment_get_idea.view.*


class GetIdeaFragment : Fragment(), GetIdeaContract.View {

    private var getIdeaPresenter: GetIdeaContract.Presenter? = null
    private lateinit var rootView: View

    companion object {

        fun createInstance(): GetIdeaFragment {
            return GetIdeaFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_get_idea, container, false)
        rootView.generateButton.setOnClickListener {
            val inputText = rootView.subjectText.text.toString()
            startActivity(CoincideQuotesActivity.newIntent(activity!!.applicationContext, inputText))
        }
        return rootView
    }

    override fun setPresenter(presenter: GetIdeaContract.Presenter) {
        getIdeaPresenter = presenter
    }
}