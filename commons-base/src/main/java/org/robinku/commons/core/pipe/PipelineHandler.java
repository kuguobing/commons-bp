/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2012-10-26 上午11:47:51
 */
package org.robinku.commons.core.pipe;

/**
 * 功能描述：pipeline Hanlder 处理器，取得链句柄：支持链接继续或跳出处理链接
 * @author kuguobing
 * time : 2012-10-26 上午11:47:51
 */
public interface PipelineHandler<I, O> {

    /**
     * 交易请求处理 
     * @param request - 请求
     * @param response - 响应
     * @param handlerChain - 处理链
     * @throws HandlerException
     */
    void handle(I request, O response, PipelineHandlerChain<I, O> handlerChain) throws HandlerException;
}
