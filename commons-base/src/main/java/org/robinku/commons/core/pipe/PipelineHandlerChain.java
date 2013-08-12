/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2012-11-7 下午04:52:53
 */
package org.robinku.commons.core.pipe;

/**
 * 功能描述：交易请求处理链
 * @author kuguobing
 * time : 2012-11-7 下午04:52:53
 */
public interface PipelineHandlerChain<I, O> {
    /**
     * 交易请求处理
     * @param model
     * @throws HandlerException
     */
    void doHandle(I request, O response) throws HandlerException;
}
