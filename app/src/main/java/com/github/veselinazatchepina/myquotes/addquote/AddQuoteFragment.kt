package com.github.veselinazatchepina.myquotes.addquote

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import com.github.veselinazatchepina.myquotes.enums.QuoteType
import icepick.Icepick
import icepick.State
import kotlinx.android.synthetic.main.add_quote_book_part.*
import kotlinx.android.synthetic.main.add_quote_book_part.view.*
import kotlinx.android.synthetic.main.add_quote_main_part.*
import kotlinx.android.synthetic.main.add_quote_other.*
import kotlinx.android.synthetic.main.add_quote_other.view.*
import kotlinx.android.synthetic.main.dialog_add_category.view.*
import org.jetbrains.anko.margin
import org.jetbrains.anko.support.v4.toast
import java.util.*


class AddQuoteFragment : Fragment(), AddQuoteContract.View {

    private var addQuotePresenter: AddQuoteContract.Presenter? = null
    private lateinit var rootView: View
    private lateinit var quoteType: String
    @JvmField
    @State
    internal var authorFieldText: ArrayList<String>? = null
    private var authorFieldIds: ArrayList<Int> = ArrayList<Int>()
    private var quoteCategoriesList: List<String>? = null
    private lateinit var categorySpinnerAdapter: ArrayAdapter<String>
    private lateinit var quoteCategory: String
    @JvmField
    @State
    internal var chosenQuoteCategoryPosition: Int = -1
    @JvmField
    @State
    internal var chosenNewCategory: String? = null

    companion object {
        private const val QUOTE_TYPE_BUNDLE = "quote_type_bundle"
        private const val QUOTE_CATEGORY_BUNDLE = "quote_category_bundle"

        fun createInstance(quoteType: String, quoteCategory: String): AddQuoteFragment {
            val bundle = Bundle()
            bundle.putString(QUOTE_TYPE_BUNDLE, quoteType)
            bundle.putString(QUOTE_CATEGORY_BUNDLE, quoteCategory)
            val fragment = AddQuoteFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quoteType = arguments?.getString(QUOTE_TYPE_BUNDLE) ?: resources.getString(QuoteType.BOOK_QUOTE.resource)
        quoteCategory = arguments?.getString(QUOTE_CATEGORY_BUNDLE) ?: getString(R.string.spinner_hint)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Icepick.restoreInstanceState(this, savedInstanceState)
        rootView = inflater.inflate(R.layout.fragment_add_quote, container, false)
        addQuotePresenter?.getQuoteCategoriesList(quoteType)
        defineLayoutFields()
        defineAddFieldsForAuthorDataBtn()
        if (savedInstanceState != null) {
            defineAuthorFieldLayoutWhenConfigChanged()
        }
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        chosenQuoteCategoryPosition = addCategorySpinner.selectedItemPosition
        authorFieldText = ArrayList(createAuthorsList())
        Icepick.saveInstanceState(this, outState)
    }

    private fun defineAuthorFieldLayoutWhenConfigChanged() {
        val hints = createAuthorFieldsHintList()
        if (authorFieldText != null && !authorFieldText!!.isEmpty()) {
            for (j in 3 until (authorFieldText!!.size) step 3) {
                for (i in 0 until hints.count()) {
                    val currentInputLayout = createTextInputLayout(hints[i])
                    rootView.addAuthorFieldsLinearLayout.addView(currentInputLayout)
                    currentInputLayout.editText!!.setText(authorFieldText!![j + i])
                }
            }
        }
    }

    private fun defineLayoutFields() {
        if (quoteType == resources.getString(QuoteType.MY_QUOTE.resource)) {
            hideBookQuoteFields()
        }
    }

    private fun defineAddFieldsForAuthorDataBtn() {
        rootView.addAuthorFieldsBtn.setOnClickListener {
            val hints = createAuthorFieldsHintList()
            for (i in 0 until hints.count()) {
                rootView.addAuthorFieldsLinearLayout.addView(createTextInputLayout(hints[i]))
            }
        }
    }

    private fun createAuthorFieldsHintList(): List<String> {
        return listOf(getString(R.string.hint_author_name),
                getString(R.string.hint_author_surname),
                getString(R.string.hint_author_second_name))
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

    private fun hideBookQuoteFields() {
        rootView.addBookPartCard.visibility = View.GONE
        rootView.addPublishingOfficeInputLayout.visibility = View.GONE
        rootView.addYearInputLayout.visibility = View.GONE
        rootView.addPageNumberInputLayout.visibility = View.GONE
    }

    override fun setPresenter(presenter: AddQuoteContract.Presenter) {
        addQuotePresenter = presenter
    }

    override fun defineCategorySpinner(quoteCategories: List<String>) {
        if (isAdded) {
            quoteCategoriesList = quoteCategories
            defineSpinnerAdapter()
            setListenerToSpinner()
        }
    }

    override fun updateCategorySpinner(quoteCategories: List<String>) {
        if (isAdded) {
            categorySpinnerAdapter.clear()
            categorySpinnerAdapter.addAll(quoteCategories)
            chosenNewCategory = quoteCategories[0]
            setSpinnerToCorrectPositionWhenUpdate()
        }
    }

    private fun setSpinnerToCorrectPositionWhenUpdate() {
        if (chosenNewCategory != null) {
            addCategorySpinner.setSelection(categorySpinnerAdapter.getPosition(chosenNewCategory))
        } else {
            if (chosenQuoteCategoryPosition != -1) {
                addCategorySpinner.setSelection(chosenQuoteCategoryPosition)
            } else {
                addCategorySpinner.setSelection(0)
            }
        }
    }

    private fun defineSpinnerAdapter() {
        categorySpinnerAdapter = object : ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val v = super.getView(position, convertView, parent)
                if (position == count) {
                    v.findViewById<TextView>(android.R.id.text1).text = ""
                    v.findViewById<TextView>(android.R.id.text1).hint = getItem(count)
                }
                return v
            }

            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }
        defineSpinnerAdapterWhenCategoryAdded()
        addCategorySpinner.adapter = categorySpinnerAdapter
        setSpinnerToCorrectPositionWhenCreate()
    }

    private fun defineSpinnerAdapterWhenCategoryAdded() {
        if (chosenNewCategory != null) {
            val quoteCategoriesListWithNewCategory = ArrayList(quoteCategoriesList)
            quoteCategoriesListWithNewCategory.add(0, chosenNewCategory)
            categorySpinnerAdapter.addAll(quoteCategoriesListWithNewCategory)
        } else {
            categorySpinnerAdapter.addAll(quoteCategoriesList)
        }
    }

    private fun setSpinnerToCorrectPositionWhenCreate() {
        if (chosenQuoteCategoryPosition == -1 && quoteCategory == getString(R.string.spinner_hint)) {
            addCategorySpinner.setSelection(categorySpinnerAdapter.count)
        } else if (chosenQuoteCategoryPosition == -1 && quoteCategory != getString(R.string.spinner_hint)) {
            addCategorySpinner.setSelection(quoteCategoriesList!!.indexOf(quoteCategory.toUpperCase()))
        } else {
            addCategorySpinner.setSelection(chosenQuoteCategoryPosition)
        }
    }

    private fun setListenerToSpinner() {
        addCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem == getString(R.string.spinner_add_category)) {
                    createAddCategoryDialog()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun createAddCategoryDialog() {
        AlertDialog.Builder(activity).apply {
            val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_add_category, null)
            this.setView(dialogView)
            this.setCancelable(false)
                    .setPositiveButton(getString(R.string.dialog_add_category_ok)) { dialogInterface: DialogInterface?, id: Int ->
                        val currentUserInput = dialogView.input_text.text.toString()
                        addQuotePresenter?.addQuoteCategory(currentUserInput)
                    }
                    .setNegativeButton(getString(R.string.dialog_add_category_cancel)) { dialogInterface: DialogInterface?, id: Int ->
                        dialogInterface?.cancel()
                        addCategorySpinner.setSelection(0)
                    }
        }.create().show()
    }


    fun createMapOfQuoteProperties() {
        if (!isFieldEmpty(addQuoteText, addQuoteTextInputLayout) &&
                isBookCategorySelected()) {
            val mapOfQuoteProperties = HashMap<QuoteProperties, String>()
            mapOfQuoteProperties[QuoteProperties.QUOTE_TEXT] = addQuoteText.text.toString()
            mapOfQuoteProperties[QuoteProperties.BOOK_NAME] = addBookName.text.toString()
            mapOfQuoteProperties[QuoteProperties.BOOK_CATEGORY_NAME] = addCategorySpinner.selectedItem.toString()
            mapOfQuoteProperties[QuoteProperties.PAGE_NUMBER] = addPageNumber.text.toString()
            mapOfQuoteProperties[QuoteProperties.YEAR_NUMBER] = addYear.text.toString()
            mapOfQuoteProperties[QuoteProperties.PUBLISHING_OFFICE_NAME] = addPublishingOfficeName.text.toString()
            val currentCreateDate = Calendar.getInstance()
            val currentDate = String.format("%1\$td %1\$tb %1\$tY", currentCreateDate)
            mapOfQuoteProperties[QuoteProperties.QUOTE_CREATION_DATE] = currentDate
            mapOfQuoteProperties[QuoteProperties.QUOTE_TYPE] = quoteType
            mapOfQuoteProperties[QuoteProperties.QUOTE_COMMENTS] = addComments.text.toString()

            addQuotePresenter?.saveQuote(mapOfQuoteProperties, createAuthorsList())
            activity?.finish()
        }
        if (!isBookCategorySelected()) {
            toast("Choose allQuoteData category")
        }
    }

    private fun createAuthorsList(): List<String> {
        val list = ArrayList<String>()
        list.add(addAuthorFirstName.text.toString())
        list.add(addAuthorSurname.text.toString())
        list.add(addAuthorMiddleName.text.toString())
        authorFieldIds
                .map { rootView.findViewById<EditText>(it) }
                .mapTo(list) { it.text.toString() }
        return list
    }

    private fun isFieldEmpty(editText: EditText, textInputLayout: TextInputLayout): Boolean =
            if (TextUtils.isEmpty(editText.text)) {
                textInputLayout.error = "This field couldn't be empty"
                true
            } else {
                false
            }


    private fun isBookCategorySelected(): Boolean = addCategorySpinner.selectedItem.toString() != getString(R.string.spinner_hint)
}