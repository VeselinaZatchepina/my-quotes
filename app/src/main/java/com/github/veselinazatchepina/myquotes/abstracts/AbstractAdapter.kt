package com.github.veselinazatchepina.myquotes.abstracts

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.github.veselinazatchepina.myquotes.inflate


abstract class AbstractAdapter<ITEM> constructor(
        protected var itemList: List<ITEM>,
        private val layoutResId: Int) : RecyclerView.Adapter<AbstractAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = parent inflate layoutResId
        val viewHolder = Holder(view)
        val itemView = viewHolder.itemView
        itemView.setOnClickListener {
            val adapterPosition = viewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClick(itemView, adapterPosition)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    protected open fun onItemClick(itemView: View, position: Int) {

    }

    fun update(items: List<ITEM>) {
        itemList = items
        notifyDataSetChanged()
    }
}
