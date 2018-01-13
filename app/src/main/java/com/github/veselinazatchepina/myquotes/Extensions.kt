package com.github.veselinazatchepina.myquotes

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.abstracts.AdapterImpl
import java.util.regex.Pattern

infix fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun <ITEM> RecyclerView.setUp(items: List<ITEM>,
                              layoutResId: Int,
                              bindHolder: View.(ITEM) -> Unit,
                              itemClick: ITEM.() -> Unit = {},
                              manager: RecyclerView.LayoutManager = LinearLayoutManager(this.context))
        : AdapterImpl<ITEM> {
    return AdapterImpl(items, layoutResId, {
        bindHolder(it)
    }, {
        itemClick()
    }).apply {
        layoutManager = manager
        adapter = this
    }
}

fun String.setFirstVowelColor(context: Context): Spannable {
    var newText: Spannable = SpannableString(this)
    if (!this.isEmpty() || this != "") {
        val index = getFirstVowelIndex(this)
        newText = SpannableString(this)
        newText.setSpan(ForegroundColorSpan(context.resources.getColor(R.color.card_background)),
                index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return newText
    }
    return newText
}

/**
 * Method returns index of first vowel in title (for english and russian languages)
 *
 * @param text     current text
 * @return index of first vowel
 */
private fun getFirstVowelIndex(text: String): Int {
    val patternString = "(?i:[aeiouy]).*"
    return getIndex(patternString, text)
}

private fun getIndex(patternString: String, text: String): Int {
    val p = Pattern.compile(patternString)
    val m = p.matcher(text)
    return if (m.find()) {
        m.start()
    } else {
        -1
    }
}