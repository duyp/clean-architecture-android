package com.duyp.architecture.clean.android.powergit.ui.base.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

abstract class BaseAdapter<T>(
        protected val mData: AdapterData<T>
): LoadMoreAdapter() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getTotalItem() = mData.getTotalCount()

    override fun getItemViewTypeAtPosition(position: Int) = mData.getItemTypeAtPosition(position)

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseViewHolder<*>) {
            mData.getItemAtPosition(position)?.let { (holder as BaseViewHolder<T>).bindData(it) }
        }
    }

    fun update(diffResult: DiffUtil.DiffResult?) {
        diffResult?.dispatchUpdatesTo(this) ?: notifyDataSetChanged()
    }
}