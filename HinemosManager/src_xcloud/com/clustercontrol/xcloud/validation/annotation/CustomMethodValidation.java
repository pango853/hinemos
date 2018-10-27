/*
 * Copyright (c) 2018 NTT DATA INTELLILINK Corporation. All rights reserved.
 *
 * Hinemos (http://www.hinemos.info/)
 *
 * See the LICENSE file for licensing information.
 */
package com.clustercontrol.xcloud.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.clustercontrol.xcloud.validation.CustomMethodValidator;

@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)
public @interface CustomMethodValidation {
	Class<? extends CustomMethodValidator>[] value();
}
