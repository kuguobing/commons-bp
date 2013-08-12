/*
 * Copyright 2013 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-8-1 下午2:44:29
 */
package org.robinku.commons.core.pipe.support;

import org.robinku.commons.core.pipe.HandlerException;
import org.robinku.commons.core.pipe.PipelineHandlerChain;


/**
 * 功能描述：
 * @author kuguobing
 * time : 2013-8-1 下午2:44:29
 */
public class NoopHandlerChain<I, O> implements PipelineHandlerChain<I, O> {

    @Override
    public void doHandle(I request, O response) throws HandlerException {
        //DO nothing
    }
}
