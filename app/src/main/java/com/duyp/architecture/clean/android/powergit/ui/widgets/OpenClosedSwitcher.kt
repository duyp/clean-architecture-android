package com.duyp.architecture.clean.android.powergit.ui.widgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.duyp.architecture.clean.android.powergit.R

class OpenClosedSwitcher : LinearLayout {

    private lateinit var mTvOpen: TextView

    private lateinit var mTvClosed: TextView

    private var mOnSwitchedListener: ((isOpened: Boolean) -> Unit)? = null

    private var mIsOpen = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.widget_open_closed_switcher, this)
        mTvOpen = findViewById(R.id.tvOpen)
        mTvClosed = findViewById(R.id.tvClosed)
        invalidateSelection()

        mTvOpen.setOnClickListener {
            setState(true)
            mOnSwitchedListener?.invoke(true)
        }

        mTvClosed.setOnClickListener {
            setState(false)
            mOnSwitchedListener?.invoke(false)
        }
    }

    fun setOnSwitchListener(listener: (Boolean) -> Unit) {
        mOnSwitchedListener = listener
    }

    fun setState(isOpen: Boolean) {
        mIsOpen = isOpen
        invalidateSelection()
    }

    private fun invalidateSelection() {
        val new = if (mIsOpen) mTvOpen else mTvClosed
        val old = if (!mIsOpen) mTvOpen else mTvClosed
        new.setTypeface(new.typeface, Typeface.BOLD)
        new.setTextColor(resources.getColor(R.color.anthracite))
        old.setTypeface(old.typeface, Typeface.NORMAL)
        old.setTextColor(resources.getColor(R.color.grey))
    }
}
