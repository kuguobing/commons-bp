/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2012-12-17 下午1:03:35
 */
package org.robinku.commons.remote.cxf;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.BusWiringBeanFactoryPostProcessor;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientFactoryBean;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 该Class是创建WS客户端的工厂类，解决用1、jaxws:client方式和2、JaxWsProxyFactoryBean创建Client的方式，
 * 创建多次Proxy客户端实例或循环依赖创建。
 * 
 *  可配置HTTP连接参数的CXF代理 -超时配置
 * deprecated - using cxf-custom.xml自定义配置
 * 

 * 功能描述：Spring Style方式创建CXF WS的Client Proxy代理
 * @author kuguobing
 * time : 2012-12-17 下午1:03:35
 */
@NoJSR250Annotations
public class JaxWsSpringClientProxyFactoryBean extends JaxWsProxyFactoryBean implements ApplicationContextAware,
        FactoryBean<Object> {
    /**
     * 连接超时时间 - 缺省为5秒
     */
    private long connTimeout = 5000;

    /**
     * 接收超时时间 - 缺省为15秒
     */
    private long receiveTimeout = 15000;

    /**
     * CXF Proxy Service Object
     */
    private Object obj;

    public JaxWsSpringClientProxyFactoryBean() {
        super();
    }

    public JaxWsSpringClientProxyFactoryBean(ClientFactoryBean fact) {
        super(fact);
    }

    public void setConnTimeout(long connTimeout) {
        this.connTimeout = connTimeout;
    }

    public void setReceiveTimeout(long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        if (getBus() == null) {
            Bus bus = BusFactory.getThreadDefaultBus();
            BusWiringBeanFactoryPostProcessor.updateBusReferencesInContext(bus, ctx);
            setBus(bus);
        }
    }

    @Override
    protected ClientProxy clientClientProxy(Client c) {
        ClientProxy handler = super.clientClientProxy(c);

        // 设置HTTP连接参数        
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(connTimeout);
        httpClientPolicy.setAllowChunking(false);
        httpClientPolicy.setReceiveTimeout(receiveTimeout);

        //设置 Http Client Policy
        HTTPConduit http = (HTTPConduit) handler.getClient().getConduit();
        http.setClient(httpClientPolicy);

        return handler;
    }

    @Override
    public synchronized Object getObject() throws Exception {
        if (obj == null) {
            obj = create();
        }
        return obj;

    }

    @Override
    public Class<?> getObjectType() {
        return this.getServiceClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
