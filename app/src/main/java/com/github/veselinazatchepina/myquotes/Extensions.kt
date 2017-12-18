package com.github.veselinazatchepina.myquotes

import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.abstracts.AdapterImpl

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