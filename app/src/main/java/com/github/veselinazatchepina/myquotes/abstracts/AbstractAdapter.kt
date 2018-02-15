package com.github.veselinazatchepina.myquotes.abstracts

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.inflate


abstract class AbstractAdapter<ITEM> constructor(
        protected var itemList: List<ITEM>,
        private val layoutResId: Int,
        private val emptyLayoutResId: Int) : RecyclerView.Adapter<AbstractAdapter.Holder>() {

    private val EMPTY_LIST = 0
    private val NOT_EMPTY_LIST = 1
    private var IS_EMPTY = NOT_EMPTY_LIST

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int {
        if (itemList.isEmpty()) {
            return 1
        } else {
            return itemList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList.isEmpty()) {
            IS_EMPTY = EMPTY_LIST
            IS_EMPTY
        } else IS_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view: View
        var viewHolder: Holder? = null
        when (IS_EMPTY) {
            NOT_EMPTY_LIST -> {
                view = parent inflate layoutResId
                viewHolder = Holder(view)
                val itemView = viewHolder.itemView
                itemView.setOnClickListener {
                    val adapterPosition = viewHolder!!.adapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClick(itemView, adapterPosition)
                    }
                }
                itemView.setOnLongClickListener {
                    val adapterPosition = viewHolder!!.adapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onLongItemClick(itemView, adapterPosition)
                    }
                    true
                }
            }
            EMPTY_LIST -> {
                view = parent inflate emptyLayoutResId
                viewHolder = Holder(view)
            }
        }

        return viewHolder!!
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    protected open fun onItemClick(itemView: View, position: Int) {

    }

    protected open fun onLongItemClick(itemView: View, position: Int) {

    }

    fun update(items: List<ITEM>) {
        itemList = items
        notifyDataSetChanged()
    }
}
