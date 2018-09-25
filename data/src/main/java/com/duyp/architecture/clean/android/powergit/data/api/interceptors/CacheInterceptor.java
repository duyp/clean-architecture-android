package com.duyp.architecture.clean.android.powergit.data.api.interceptors;

import com.duyp.architecture.clean.android.powergit.data.api.ApiConstants;
import com.duyp.architecture.clean.android.powergit.data.api.annotations.RequestAnnotations;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.duyp.architecture.clean.android.powergit.data.api.ApiConstants.Headers.KEY_CACHE_CONTROL;

// handles the caching behaviour
public class CacheInterceptor implements Interceptor {

    private final RequestAnnotations mRequestAnnotations;

    public CacheInterceptor(final RequestAnnotations requestAnnotations) {
        mRequestAnnotations = requestAnnotations;
    }

    @Override public Response intercept(@NonNull final Chain chain) throws IOException {
        Request request = chain.request();
        final Request.Builder builder = request.newBuilder();

        // force network request in case request is annotated with @NoCache
        final boolean noCache = mRequestAnnotations.isNoCache(request);
        if(noCache) {
            final CacheControl noCacheControl = CacheControl.FORCE_NETWORK;
            builder.header(KEY_CACHE_CONTROL, noCacheControl.toString());
        }

        // use cache if request is NOT annotated as @NoCache
        if(!noCache) {
            final CacheControl cacheControl = new CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(7, TimeUnit.DAYS)
                    .build();
            builder.header(KEY_CACHE_CONTROL, cacheControl.toString());
        } else {
            // remove if-modified-since to prevent 304 response from api
            builder.removeHeader(ApiConstants.Headers.KEY_IF_MODIFIED_SINCE);
        }

        return chain.proceed(builder.build());

        // response
        //if (noCache) {
        //    return chain.proceed(builder.build());
        //} else {
        //
        //    final Response response = chain.proceed(builder.build());
        //
        //    // remove unnecessary headers and set cache response
        //    final CacheControl cacheControl = new CacheControl.Builder()
        //            .maxAge(60, TimeUnit.SECONDS)
        //            .build();
        //
        //    return response.newBuilder()
        //            .removeHeader("keep-alive")
        //            .removeHeader("connection")
        //            .removeHeader("rtss")
        //            .removeHeader("sess-id")
        //            .removeHeader("vary")
        //            .removeHeader("x-robots-tag")
        //            .removeHeader("Pragma")
        //            .removeHeader("ETag")
        //            .removeHeader("Expires")
        //            .header(ApiConstants.Headers.KEY_CACHE_CONTROL, cacheControl.toString())
        //            .build();
        //}
    }
}
