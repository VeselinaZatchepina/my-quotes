package com.github.veselinazatchepina.myquotes.abstracts

import android.view.View


class AdapterImpl<ITEM>(items: List<ITEM>,
                        layoutResId: Int,
                        private val bindHolder: View.(ITEM) -> Unit) : AbstractAdapter<ITEM>(items, layoutResId) {

    private var itemClick: ITEM.() -> Unit = {}

    constructor(items: List<ITEM>,
                layoutResId: Int,
                bindHolder: View.(ITEM) -> Unit,
                itemClick: ITEM.() -> Unit = {}) : this(items, layoutResId, bindHolder) {
        this.itemClick = itemClick
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.bindHolder(itemList[position])
    }

    override fun onItemClick(itemView: View, position: Int) {
        itemList[position].itemClick()
    }
}