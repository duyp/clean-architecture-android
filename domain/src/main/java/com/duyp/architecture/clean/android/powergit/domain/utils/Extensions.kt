package com.duyp.architecture.clean.android.powergit.domain.utils

import java.util.regex.Matcher


fun Matcher.totalMatches(): Int {
    var count = 0
    while (find()) count++
    return count
}