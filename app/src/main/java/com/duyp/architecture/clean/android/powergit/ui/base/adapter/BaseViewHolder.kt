package com.duyp.architecture.clean.android.powergit.ui.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {

    abstract fun bindData(data: T)
}