package com.github.veselinazatchepina.myquotes.addquote

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.github.veselinazatchepina.myquotes.R
import kotlinx.android.synthetic.main.fragment_add_quote.view.*
import org.jetbrains.anko.margin


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
        rootView.addAuthorFieldsBtn.setOnClickListener {

            val newFieldAuthorNameInputLayout = TextInputLayout(activity)
            val newFieldAuthorSurnameInputLayout = TextInputLayout(activity)
            val newFieldAuthorPatronymicInputLayout = TextInputLayout(activity)

            val newFieldAuthorNameEditText = EditText(activity)
            val newFieldAuthorSurnameEditText = EditText(activity)
            val newFieldAuthorPatronymicEditText = EditText(activity)

            val layoutParamsInputLayout = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParamsInputLayout.margin = resources.getDimension(R.dimen.add_author_fields_margin).toInt()

            val layoutParamsEditText = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)

            newFieldAuthorNameInputLayout.layoutParams = layoutParamsInputLayout
            newFieldAuthorSurnameInputLayout.layoutParams = layoutParamsInputLayout
            newFieldAuthorPatronymicInputLayout.layoutParams = layoutParamsInputLayout

            newFieldAuthorNameEditText.layoutParams = layoutParamsEditText
            newFieldAuthorSurnameEditText.layoutParams = layoutParamsEditText
            newFieldAuthorPatronymicEditText.layoutParams = layoutParamsEditText

            newFieldAuthorNameEditText.hint = "Book author name"
            newFieldAuthorSurnameEditText.hint = "Book author surname"
            newFieldAuthorPatronymicEditText.hint = "Book author second name"

            newFieldAuthorNameInputLayout.addView(newFieldAuthorNameEditText)
            newFieldAuthorSurnameInputLayout.addView(newFieldAuthorSurnameEditText)
            newFieldAuthorPatronymicInputLayout.addView(newFieldAuthorPatronymicEditText)

            rootView.addAuthorFieldsLinearLayout.addView(newFieldAuthorNameInputLayout)
            rootView.addAuthorFieldsLinearLayout.addView(newFieldAuthorSurnameInputLayout)
            rootView.addAuthorFieldsLinearLayout.addView(newFieldAuthorPatronymicInputLayout)

        }
    }
}