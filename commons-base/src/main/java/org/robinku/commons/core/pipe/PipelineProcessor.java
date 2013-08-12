/*
 * Copyright 2013 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-8-2 上午8:48:37
 */
package org.robinku.commons.core.pipe;

/**
 * 功能描述：管道过滤执行器接口定义
 * @author kuguobing
 * time : 2013-8-2 上午8:48:37
 */
public interface PipelineProcessor<I, O> {

    /**
     * 执行请求
     * @param request - 传入的请求对象
     * @return - 执行响应对象
     */
    void process(I request, O response);
}
