package com.duyp.architecture.clean.android.powergit.ui.base

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.inflate

abstract class LoadMoreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_PROGRESS = 0
        private const val TYPE_ITEM = 10
    }

    private var mIsProgressAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_PROGRESS -> onCreateProgressViewHolder(parent)
            else -> onCreateItemViewHolder(parent, viewType)
        }
    }

    override fun getItemCount(): Int {
        return getTotalItem() + if (mIsProgressAdded) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (mIsProgressAdded && position == itemCount - 1) TYPE_PROGRESS else TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_PROGRESS -> onBindHeaderViewHolder(holder, position)
            else -> onBindItemViewHolder(holder, position)
        }
    }

    abstract fun getTotalItem(): Int

    abstract fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    abstract fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    protected fun onCreateProgressViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return DefaultProgressViewHolder(parent.inflate(R.layout.progress_layout))
    }

    protected fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    fun addProgress() {
        mIsProgressAdded = true
        notifyItemInserted(itemCount)
    }

    fun removeProgress() {
        mIsProgressAdded = false
        notifyItemRemoved(itemCount - 1)
    }

    fun isProgressAdded() = mIsProgressAdded

    class DefaultProgressViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
