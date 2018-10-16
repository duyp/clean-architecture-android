package com.duyp.architecture.clean.android.powergit.data.api.interceptors

import com.duyp.architecture.clean.android.powergit.data.api.ApiConstants
import com.duyp.architecture.clean.android.powergit.data.api.ApiConstants.Headers.KEY_CACHE_CONTROL
import com.duyp.architecture.clean.android.powergit.data.api.annotations.RequestAnnotations
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * handles the caching behaviour
 *
 * MUST BE ADDED AS NetworkInterceptor to force using cache for network responses
 */
class CacheInterceptor(private val mRequestAnnotations: RequestAnnotations) : Interceptor {

    companion object {
        val DEFAULT_CACHE_CONTROL = CacheControl.Builder()
                .maxAge(30, TimeUnit.SECONDS)
                .build()
                .toString()
        val NO_CACHE_CONTROL = CacheControl.FORCE_NETWORK.toString()
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        // force network request in case request is annotated with @NoCache
        val noCache = mRequestAnnotations.isNoCache(request)

        // request
        if (noCache) {
            // remove if-modified-since to prevent 304 response from api
            builder.removeHeader(ApiConstants.Headers.KEY_IF_MODIFIED_SINCE)
        }

        // response
        val response = chain.proceed(builder.build())
        if (noCache) {
            return response.newBuilder()
                    .header(KEY_CACHE_CONTROL, NO_CACHE_CONTROL)
                    .build()
        } else {
            // remove unnecessary headers
            return response.newBuilder()
                    .removeHeader("keep-alive")
                    .removeHeader("connection")
                    .removeHeader("rtss")
                    .removeHeader("sess-id")
                    .removeHeader("vary")
                    .removeHeader("x-robots-tag")
                    .removeHeader("Pragma")
                    .removeHeader("ETag")
                    .removeHeader("Expires")
                    // rewrite cache control from network response (MUST ADD TO NETWORK INTERCEPTORS)
                    .header(ApiConstants.Headers.KEY_CACHE_CONTROL, DEFAULT_CACHE_CONTROL)
                    .build()
        }
    }
}
