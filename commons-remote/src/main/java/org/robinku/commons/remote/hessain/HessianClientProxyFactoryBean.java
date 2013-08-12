/*
 * Copyright 2013 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-8-1 上午9:19:16
 */
package org.robinku.commons.remote.hessain;

import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * 功能描述：扩展HessainProxyFactoryBean支持连接超时处理 - 父类只支持处理响应超时
 * @author kuguobing
 * time : 2013-8-1 上午9:19:16
 */
public class HessianClientProxyFactoryBean extends HessianProxyFactoryBean {

    // 父类由于是保护的
    private HessianProxyFactory proxyFactory = null;

    public HessianClientProxyFactoryBean() {
        super();

        //创建-设置ProxyFactory
        proxyFactory = new HessianProxyFactory();
        setProxyFactory(proxyFactory);
    }

    /**
     * 返回ProxyFactory供子类继承扩展
     * @return
     */
    protected HessianProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public void setConnectTimeout(long timeout) {
        this.proxyFactory.setConnectTimeout(timeout);
    }

    @Override
    public void setReadTimeout(long timeout) {
        super.setReadTimeout(timeout);
    }

}
