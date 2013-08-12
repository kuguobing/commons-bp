/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2012-11-26 下午01:41:19
 */
package org.robinku.commons.aop.logger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能描述：日志输出Annotation
 * @author kuguobing
 * time : 2012-11-26 下午01:41:19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Documented
public @interface LoggerAnn {
    /**
     * 被修饰域是否关闭日志输出功能
     * @return
     */
    boolean disableLogOut() default false;
}
