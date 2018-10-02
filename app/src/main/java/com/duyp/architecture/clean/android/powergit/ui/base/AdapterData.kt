package com.duyp.architecture.clean.android.powergit.ui.base

/**
 * Represents data to be rendered on RecyclerView's Adapter
 */
interface AdapterData<out T> {

    /**
     * Total number of items
     */
    fun getTotalCount(): Int

    /**
     * Item at specific position. The implementer shouldn't care about adapter's position such as headers, footers...
     * It has been already calculated as the real position in data list.
     */
    fun getItemAtPosition(position: Int): T?
}