package com.github.veselinazatchepina.myquotes.addquote

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.github.veselinazatchepina.myquotes.R
import kotlinx.android.synthetic.main.fragment_add_quote.view.*


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
        defineAddFieldsForAuthorDataBtn()
        return rootView
    }

    override fun setPresenter(presenter: AddQuoteContract.Presenter) {
        addQuotePresenter = presenter
    }

    fun createMapOfQuoteProperties() {
        var mapOfQuoteProperties = HashMap<String, String>()
        addQuotePresenter.saveQuote(mapOfQuoteProperties)
    }

    private fun defineAddFieldsForAuthorDataBtn() {
        rootView.add_author_btn.setOnClickListener {
            val newAuthorNameField = EditText(activity)
            val newAuthorSurnameField = EditText(activity)
            val newAuthorPatronymicField = EditText(activity)

            var allLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)


            newAuthorNameField.layoutParams = allLayoutParams


            newAuthorSurnameField.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            newAuthorPatronymicField.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)

            newAuthorNameField.hint = "Book author name"
            newAuthorSurnameField.hint = "Book author surname"
            newAuthorPatronymicField.hint = "Book author second name"

            rootView.add_linear_layout.addView(newAuthorNameField)
            rootView.add_linear_layout.addView(newAuthorSurnameField)
            rootView.add_linear_layout.addView(newAuthorPatronymicField)

        }
    }




}