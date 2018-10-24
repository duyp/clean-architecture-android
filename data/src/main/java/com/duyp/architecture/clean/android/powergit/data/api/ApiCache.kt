package com.duyp.architecture.clean.android.powergit.data.api

import okhttp3.Cache

class ApiCache(
        private val cache: Cache?
) {

    fun get() = cache

    /**
     * Evict all cache, see [Cache.evictAll]
     */
    fun evictAll() {
        cache?.evictAll()
    }

}