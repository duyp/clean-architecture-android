package com.duyp.architecture.clean.android.powergit.data.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation indicating the quest is plain request (without adding any headers)
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface PlainRequest {}
