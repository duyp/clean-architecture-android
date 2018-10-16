package com.duyp.architecture.clean.android.powergit.data.api.annotations;

import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import static com.duyp.architecture.clean.android.powergit.data.api.annotations.AnnotationUtils.isAuthenticated;
import static com.duyp.architecture.clean.android.powergit.data.api.annotations.AnnotationUtils.isForceCache;
import static com.duyp.architecture.clean.android.powergit.data.api.annotations.AnnotationUtils.isNoCache;
import static com.duyp.architecture.clean.android.powergit.data.api.annotations.AnnotationUtils.isPlainRequest;

public class AnnotationWrapCallAdapterFactory extends CallAdapter.Factory {

    private final CallAdapter.Factory mOriginal;

    private final RequestAnnotations mRequestAnnotations;

    private AnnotationWrapCallAdapterFactory(final CallAdapter.Factory wrapped, final RequestAnnotations requestAnnotations) {
        mRequestAnnotations = requestAnnotations;
        mOriginal = wrapped;
    }

    @SuppressWarnings("unchecked") @Override
    public CallAdapter<?, ?> get(@NonNull final Type returnType, @NonNull final Annotation[] annotations,
                                 @NonNull final Retrofit retrofit) {
        return new AnnotationCallAdapter(mOriginal.get(returnType, annotations, retrofit), mRequestAnnotations, annotations);
    }

    public static CallAdapter.Factory create(final CallAdapter.Factory wrapped, @NonNull final RequestAnnotations requestAnnotations) {
        return new AnnotationWrapCallAdapterFactory(wrapped, requestAnnotations);
    }

    private static class AnnotationCallAdapter<R> implements CallAdapter<R, Object> {

        private final CallAdapter<R, Object> mWrappedAdapter;

        private final RequestAnnotations mRequestAnnotations;

        private final Annotation[] mAnnotations;

        private AnnotationCallAdapter(final CallAdapter<R, Object> wrappedAdapter, final RequestAnnotations requestAnnotations,
                                      final Annotation[] annotations) {
            mWrappedAdapter = wrappedAdapter;
            mRequestAnnotations = requestAnnotations;
            mAnnotations = annotations;
        }

        @Override public Type responseType() {
            return mWrappedAdapter.responseType();
        }

        @Override public Object adapt(@NonNull Call<R> call) {
            onRequest(call.request());
            return mWrappedAdapter.adapt(call);
        }

        private void onRequest(final Request request) {
            if (isPlainRequest(mAnnotations)) {
                mRequestAnnotations.registerPlainRequest(request);
            }
            if (isNoCache(mAnnotations)) {
                mRequestAnnotations.registerNoCache(request);
            }
            if (isForceCache(mAnnotations)) {
                mRequestAnnotations.registerForceCache(request);
            }
            final Authenticated authenticated = isAuthenticated(mAnnotations);
            if (authenticated != null) {
                mRequestAnnotations.registerAuthentication(request, authenticated.ownerType());
            }
        }
    }
}