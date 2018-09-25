package com.duyp.architecture.clean.android.powergit.data.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation indicating the request should be added authorization token in the request header for specific
 * {@link OwnerType}
 * Normally used for Retrofit api requests
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface Authenticated {

    int ownerType() default OwnerType.USER_NORMAL;
}
