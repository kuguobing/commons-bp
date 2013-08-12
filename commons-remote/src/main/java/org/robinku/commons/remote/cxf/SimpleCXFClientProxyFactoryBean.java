package org.robinku.commons.remote.cxf;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.BusWiringBeanFactoryPostProcessor;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientFactoryBean;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 可配置HTTP连接参数的CXF代理
 * deprecated - using cxf-custom.xml自定义配置

 * 功能描述：
 * @author kuguobing
 * time : 2012-12-17 下午1:29:34
 */
@NoJSR250Annotations
public class SimpleCXFClientProxyFactoryBean extends ClientProxyFactoryBean implements FactoryBean<Object>,
        InitializingBean, ApplicationContextAware {

    public SimpleCXFClientProxyFactoryBean() {
        super();
    }

    public SimpleCXFClientProxyFactoryBean(ClientFactoryBean fact) {
        super(fact);
    }

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
    private Object serviceProxy;

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
    public void afterPropertiesSet() throws Exception {
        this.serviceProxy = create();
    }

    @Override
    public synchronized Object getObject() throws Exception {
        if (serviceProxy == null) {
            serviceProxy = create();
        }
        return serviceProxy;
    }

    @Override
    public Class<?> getObjectType() {
        return getServiceClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        if (getBus() == null) {
            Bus bus = BusFactory.getThreadDefaultBus();
            BusWiringBeanFactoryPostProcessor.updateBusReferencesInContext(bus, ctx);
            setBus(bus);
        }
    }

    /**
     * @return the connTimeout
     */
    public long getConnTimeout() {
        return connTimeout;
    }

    /**
     * @param connTimeout
     *            the connTimeout to set
     */
    public void setConnTimeout(long connTimeout) {
        this.connTimeout = connTimeout;
    }

    /**
     * @return the receiveTimeout
     */
    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    /**
     * @param receiveTimeout
     *            the receiveTimeout to set
     */
    public void setReceiveTimeout(long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }
}
