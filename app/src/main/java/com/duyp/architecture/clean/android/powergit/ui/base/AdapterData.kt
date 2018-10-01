package com.duyp.architecture.clean.android.powergit.ui.base

interface AdapterData<out T> {

    fun getTotalCount(): Int

    fun getItemAtPosition(position: Int): T?
}