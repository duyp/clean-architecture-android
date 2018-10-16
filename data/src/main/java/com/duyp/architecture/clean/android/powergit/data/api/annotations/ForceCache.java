package com.duyp.architecture.clean.android.powergit.data.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation indicating force cache request
 *
 * There are some api haven't been supported cache yet, so to prevent sending too much request in short time, we can
 * force the request to use cached response instead of actual network call
 *
 * see [{@link com.duyp.architecture.clean.android.powergit.data.api.interceptors.CacheInterceptor}]
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface ForceCache {}
