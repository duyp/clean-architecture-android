package com.duyp.architecture.clean.android.powergit.data.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation indicating no cache request
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface NoCache {}
