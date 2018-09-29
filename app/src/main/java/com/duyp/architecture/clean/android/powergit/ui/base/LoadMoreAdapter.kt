package com.duyp.architecture.clean.android.powergit.ui.base

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class LoadMoreAdapter<VH: RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    companion object {
        private const val TYPE_FOOTER = 0
        private const val TYPE_ITEM = 10
    }

    private var mIsProgressAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return when(viewType) {
            TYPE_FOOTER -> onCreateFooterViewHolder()
            else -> onCreateItemViewHolder()
        }
    }

    override fun getItemCount(): Int {
        return getTotalItem() + if (mIsProgressAdded) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (mIsProgressAdded && position == itemCount - 1) TYPE_FOOTER else TYPE_ITEM
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        when (holder.itemViewType) {
            TYPE_FOOTER -> onBindHeaderViewHolder(holder, position)
            else -> onBindItemViewHolder(holder, position)
        }
    }

    abstract fun getTotalItem(): Int

    abstract fun onCreateItemViewHolder(): VH

    abstract fun onCreateFooterViewHolder(): VH

    abstract fun onBindHeaderViewHolder(holder: VH, position: Int)

    abstract fun onBindItemViewHolder(holder: VH, position: Int)

    fun addProgress() {
        mIsProgressAdded = true
        notifyItemInserted(itemCount)
    }

    fun removeProgress() {
        mIsProgressAdded = false
        notifyItemRemoved(itemCount - 1)
    }

    fun isProgressAdded() = mIsProgressAdded
}
