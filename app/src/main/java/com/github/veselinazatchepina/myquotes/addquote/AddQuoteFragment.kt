package com.github.veselinazatchepina.myquotes.addquote

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import com.github.veselinazatchepina.myquotes.enums.QuoteType
import kotlinx.android.synthetic.main.fragment_add_quote.*
import kotlinx.android.synthetic.main.fragment_add_quote.view.*
import org.jetbrains.anko.margin


class AddQuoteFragment : Fragment(), AddQuoteContract.View {

    lateinit var addQuotePresenter: AddQuoteContract.Presenter
    lateinit var rootView: View
    lateinit var quoteType: String
    val authorFieldIds: ArrayList<Int> = ArrayList<Int>()
    lateinit var bookCategory: String

    companion object {
        private const val QUOTE_TYPE_BUNDLE = "quote_type_bundle"

        fun createInstance(quoteType: String): AddQuoteFragment {
            val bundle = Bundle()
            bundle.putString(QUOTE_TYPE_BUNDLE, quoteType)
            val fragment = AddQuoteFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_add_quote, container, false)
        defineLayoutFields()
        defineAddFieldsForAuthorDataBtn()
        return rootView
    }

    private fun defineLayoutFields() {
        quoteType = arguments?.getString(QUOTE_TYPE_BUNDLE) ?: ""
        if (quoteType == resources.getString(QuoteType.MY_QUOTE.resource)) {
            hideBookQuoteFields()
        }
    }

    private fun hideBookQuoteFields() {
        rootView.addBookNameInputLayout.visibility = View.GONE
        rootView.addAuthorFieldsLinearLayout.visibility = View.GONE
        rootView.addAuthorFieldsBtn.visibility = View.GONE
        rootView.addPublishingOfficeInputLayout.visibility = View.GONE
        rootView.addYearInputLayout.visibility = View.GONE
        rootView.addPageNumberInputLayout.visibility = View.GONE
    }

    override fun setPresenter(presenter: AddQuoteContract.Presenter) {
        addQuotePresenter = presenter
    }

    fun createMapOfQuoteProperties() {
        // TODO check is book category selected
      //  if (isQuoteTextFieldNotEmpty()) {

            val mapOfQuoteProperties = HashMap<QuoteProperties, String>()
            mapOfQuoteProperties[QuoteProperties.QUOTE_TEXT] = addQuoteText.text.toString()
            mapOfQuoteProperties[QuoteProperties.BOOK_NAME] = addBookName.text.toString()
            mapOfQuoteProperties[QuoteProperties.BOOK_CATEGORY_NAME] = ""
            mapOfQuoteProperties[QuoteProperties.PAGE_NUMBER] = addPageNumber.text.toString()
            mapOfQuoteProperties[QuoteProperties.YEAR_NUMBER] = addYear.text.toString()
            mapOfQuoteProperties[QuoteProperties.PUBLISHING_OFFICE_NAME] = addPublishingOfficeName.text.toString()
            mapOfQuoteProperties[QuoteProperties.QUOTE_CREATION_DATE] = ""
            mapOfQuoteProperties[QuoteProperties.QUOTE_TYPE] = quoteType
            mapOfQuoteProperties[QuoteProperties.QUOTE_COMMENTS] = addComments.text.toString()

            addQuotePresenter.saveQuote(mapOfQuoteProperties, createAuthorsList())
      //  }
    }

    private fun createAuthorsList(): List<String> {
        val list = ArrayList<String>()
        list.add(addAuthorName.text.toString())
        list.add(addAuthorSurname.text.toString())
        list.add(addAuthorPatronymic.text.toString())
        authorFieldIds
                .map { rootView.findViewById<EditText>(it) }
                .mapTo(list) { it.text.toString() }
        return list
    }

    private fun isQuoteTextFieldNotEmpty(): Boolean {
        var isEmpty = true
        if (TextUtils.isEmpty(addQuoteText.text)) {
            addQuoteTextInputLayout.error = "Quote text mustn't be empty"
            isEmpty = false
        }
        return isEmpty
    }

    //private fun isBookCategorySelected(): Boolean = bookCategory != ("Select quote category")

    private fun defineAddFieldsForAuthorDataBtn() {
        rootView.addAuthorFieldsBtn.setOnClickListener {
            val hints = listOf(getString(R.string.hint_author_name),
                    getString(R.string.hint_author_surname),
                    getString(R.string.hint_author_second_name))

            for (i in 0 until hints.count()) {
                rootView.addAuthorFieldsLinearLayout.addView(createTextInputLayout(hints[i]))
            }
        }
    }

    private fun createTextInputLayout(hint: String): TextInputLayout {
        val newFieldInputLayout = TextInputLayout(activity)
        newFieldInputLayout.layoutParams = createLayoutParamsForInputLayout()
        newFieldInputLayout.addView(createEditText(hint))
        return newFieldInputLayout
    }

    private fun createLayoutParamsForInputLayout(): LinearLayout.LayoutParams {
        val layoutParamsInputLayout = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParamsInputLayout.margin = resources.getDimension(R.dimen.add_author_fields_margin).toInt()
        return layoutParamsInputLayout
    }

    private fun createEditText(hint: String): EditText {
        val newFieldEditText = EditText(activity)
        newFieldEditText.layoutParams = createLayoutParamsForEditText()
        newFieldEditText.hint = hint
        newFieldEditText.id = View.generateViewId()
        authorFieldIds.add(newFieldEditText.id)
        return newFieldEditText
    }

    private fun createLayoutParamsForEditText(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}