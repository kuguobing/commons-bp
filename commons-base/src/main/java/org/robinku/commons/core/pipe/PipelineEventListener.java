/*
 * Copyright 2013 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-7-8 下午3:04:51
 */
package org.robinku.commons.core.pipe;

import java.util.EventListener;

/**
 * 功能描述：
 * @author kuguobing
 * time : 2013-7-8 下午3:04:51
 */
public interface PipelineEventListener<I, O> extends EventListener {
    /**
     * 执行处理前事件
     * @param request - 请求 对象
     */
    void beforeProcess(I request);

    /**
     * 执行完成后事件
     * @param request - 请求对象
     * @param response - 响应对象
     */
    void afterProcess(I request, O response);

    /**
     * 处理异常事件
     * @param request - 请求对象
     * @param e        - 异常类
     */
    void onProcessError(I request, Throwable e);
}
