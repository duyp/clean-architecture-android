package com.duyp.architecture.clean.android.powergit.data.api.interceptors

import android.text.TextUtils
import com.duyp.architecture.clean.android.powergit.data.api.ApiConstants.Headers.KEY_ACCEPT
import com.duyp.architecture.clean.android.powergit.data.api.ApiConstants.Headers.KEY_AUTHORIZATION
import com.duyp.architecture.clean.android.powergit.data.api.annotations.RequestAnnotations
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthorizationInterceptor(
        private val mRequestAnnotations: RequestAnnotations,
        private val mTokenProducer: (Int) -> String?
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        if (TextUtils.isEmpty(request.header(KEY_ACCEPT))) {
            // github accept header
            // https://developer.github.com/v3/#current-version
            requestBuilder.addHeader(KEY_ACCEPT, "application/vnd.github.v3+json")
        }

        // get the authentication type (set by annotation Authenticated)
        val authType = mRequestAnnotations.getAuthenticationOwnerType(request)

        // add authorization header if not existed and has token
        val header = request.header(KEY_AUTHORIZATION)
        if (TextUtils.isEmpty(header)) {
            authType?.let {
                mTokenProducer.invoke(authType)?.let { token -> requestBuilder.addHeader(KEY_AUTHORIZATION, token) }
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}