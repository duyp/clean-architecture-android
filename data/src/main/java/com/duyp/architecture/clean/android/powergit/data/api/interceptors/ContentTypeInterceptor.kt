package com.duyp.architecture.clean.android.powergit.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by duypham on 10/24/17.
 *
 */

class ContentTypeInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request.newBuilder()
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("Content-type", "application/vnd.github.v3+json")
                .method(request.method(), request.body())
                .build())
    }
}
