package com.duyp.architecture.clean.android.powergit.ui.utils

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import com.duyp.androidutils.glide.loader.SimpleGlideLoader
import com.duyp.architecture.clean.android.powergit.R

class AvatarLoader : SimpleGlideLoader {

    constructor(activity: Activity) : super(activity)

    constructor(fragment: Fragment) : super(fragment)

    constructor(context: Context) : super(context)

    override fun init() {
        super.init()
        setUseFixedSizeThumbnail(false)
        setPlaceHolderRes(R.drawable.ic_github)
    }
}
