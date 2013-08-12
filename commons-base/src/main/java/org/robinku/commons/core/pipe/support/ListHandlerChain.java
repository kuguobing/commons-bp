package org.robinku.commons.core.pipe.support;

import java.util.Collections;
import java.util.List;

import org.robinku.commons.core.pipe.HandlerException;
import org.robinku.commons.core.pipe.PipelineHandler;
import org.robinku.commons.core.pipe.PipelineHandlerChain;

/**
 * 
 * 功能描述：交易处理链接口服务实现：非线程安全，必须创建实例Prototype
 * @author kuguobing
 * time : 2012-11-7 下午05:42:30
 */
public class ListHandlerChain<I, O> implements PipelineHandlerChain<I, O> {

    /**
     * 用于维护当前处理器所在的链中位置
     */
    private int pos = 0;

    /**
     * 处理器列表
     */
    private List<PipelineHandler<I, O>> handlers;

    private void setHandlers(List<PipelineHandler<I, O>> handlers) {
        if (handlers != null)
            this.handlers = Collections.unmodifiableList(handlers); //ImmutableList.copyOf(handlers);
    }

    public ListHandlerChain(List<PipelineHandler<I, O>> handlers) {
        this.setHandlers(handlers);
    }

    @Override
    public void doHandle(final I request, final O response) throws HandlerException {
        if (handlers == null)
            return;

        //处理对应的Handler
        if (pos < handlers.size()) {
            PipelineHandler<I, O> handler = this.handlers.get(pos++);

            handler.handle(request, response, this);
        }

    }

}
