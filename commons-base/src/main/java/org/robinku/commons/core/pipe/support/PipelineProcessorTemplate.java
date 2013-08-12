/*
 * Copyright 2013 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-8-2 上午8:53:40
 */
package org.robinku.commons.core.pipe.support;

import java.util.ArrayList;
import java.util.List;

import org.robinku.commons.core.pipe.PipelineEventListener;
import org.robinku.commons.core.pipe.PipelineHandler;
import org.robinku.commons.core.pipe.PipelineHandlerChain;
import org.robinku.commons.core.pipe.PipelineProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 功能描述：管道过滤处理器模板支持类
 * @author kuguobing
 * time : 2013-8-2 上午8:53:40
 */
public class PipelineProcessorTemplate<I, O> implements PipelineProcessor<I, O> {
    private static final Logger logger = LoggerFactory.getLogger(PipelineProcessorTemplate.class);

    /**
     * Handler列表
     */
    private List<PipelineHandler<I, O>> processHandlers = new ArrayList<PipelineHandler<I, O>>();

    /**
     * Listener列表
     */
    private List<PipelineEventListener<I, O>> processListeners = new ArrayList<PipelineEventListener<I, O>>();

    public void setProcessHandlers(List<PipelineHandler<I, O>> processHandlers) {
        this.processHandlers.addAll(processHandlers);
    }

    public void setProcessListener(PipelineEventListener<I, O> processListener) {
        this.processListeners.add(processListener);
    }

    public void setProcessListeners(List<PipelineEventListener<I, O>> processListeners) {
        this.processListeners.addAll(processListeners);
    }

    public List<PipelineHandler<I, O>> findProcessHandlers() {
        return processHandlers;
    }

    public List<PipelineEventListener<I, O>> findProcessListeners() {
        return processListeners;
    }

    public void addProcessHandler(PipelineHandler<I, O> handler) {
        this.processHandlers.add(handler);
    }

    public void removeProcessHandler(PipelineHandler<I, O> handler) {
        this.processHandlers.remove(handler);
    }

    public void addProcessListener(PipelineEventListener<I, O> listener) {
        this.processListeners.add(listener);
    }

    public void removeProcessListener(PipelineEventListener<I, O> listener) {
        this.processListeners.remove(listener);
    }

    @Override
    public void process(I request, O response) {
        // 执行前处理Action
        try {
            if (this.processListeners != null)
                for (PipelineEventListener<I, O> listener : processListeners)
                    listener.beforeProcess(request);
        } catch (Exception e1) {
            logger.warn("警告可忽略，【beforeProcess】事件处理过程发生异常：", e1);
        }

        // 执行处理链
        PipelineHandlerChain<I, O> handlerChain = new ListHandlerChain<I, O>(processHandlers);
        try {
            handlerChain.doHandle(request, response);
        } catch (Throwable e) {

            // 执行错误处理Action
            try {
                if (this.processListeners != null)
                    for (PipelineEventListener<I, O> listener : processListeners)
                        listener.onProcessError(request, e);
            } catch (Exception e1) {
                logger.warn("警告可忽略，【onProcessError】事件处理过程发生异常：", e1);
            }
        }

        //执行后处理Action
        try {
            if (this.processListeners != null)
                for (PipelineEventListener<I, O> listener : processListeners)
                    listener.afterProcess(request, response);
        } catch (Exception e) {
            logger.warn("警告可忽略，【afterProcess】事件处理过程发生异常：", e);
        }

    }

}
