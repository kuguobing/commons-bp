/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2012-12-4 下午04:29:42
 */
package org.robinku.commons.remote.zk;

import com.netflix.curator.framework.CuratorFramework;

/**
 * 功能描述：ZK客户端调用回调处理
 * @author kuguobing
 * time : 2012-12-4 下午04:29:42
 */
public interface ZkCallBack<V> {

    /**
     * 回调执行分布锁内方法
     * @param zk
     * @param lockerPath
     * @return
     * @throws Exception
     */
    V doInZk(CuratorFramework zk, String lockerPath) throws Exception;
}
