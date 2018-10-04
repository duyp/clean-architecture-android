package com.duyp.architecture.clean.android.powergit.ui.base.adapter

import android.support.v7.widget.RecyclerView

abstract class BaseAdapter<T>(
        private val mData: AdapterData<T>
): LoadMoreAdapter() {

    override fun getTotalItem() = mData.getTotalCount()

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseViewHolder<*>) {
            mData.getItemAtPosition(position)?.let { (holder as BaseViewHolder<T>).bindData(it) }
        }
    }
}