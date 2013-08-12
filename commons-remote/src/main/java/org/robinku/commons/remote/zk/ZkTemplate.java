/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2012-12-4 下午04:06:59
 */
package org.robinku.commons.remote.zk;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.recipes.locks.InterProcessLock;
import com.netflix.curator.framework.recipes.locks.InterProcessMutex;
import com.netflix.curator.utils.ZKPaths;

/**
 * 功能描述：Zookeeper客户端执行模板
 * @author kuguobing
 * time : 2012-12-4 下午04:06:59
 */
public class ZkTemplate {
    private static final Logger logger = LoggerFactory.getLogger(ZkTemplate.class);

    /**
     * 自动IOC注入 Curator Zookeeper Client
     */
    private CuratorFramework curator;

    /**
     * 分布式互斥处理锁
     */
    private InterProcessLock locker;

    /**
     * 设置锁的路径
     */
    private String lockPath;

    public void setCurator(CuratorFramework curator) {
        this.curator = curator;
    }

    public void setLockPath(String lockPath) {
        this.lockPath = lockPath;
    }

    public void init() throws Exception {
        Assert.notNull(curator, "ZooKeeperClient can't be null.");
        Assert.notNull(lockPath, "Lock Path can't be null.");

        //启动Zookeeper客户端
        if (!this.curator.isStarted()) {
            this.curator.start();
        }

        //初始化锁
        locker = new InterProcessMutex(curator, this.lockPath);
    }

    public void destroy() throws Exception {
        if (this.curator != null && this.curator.isStarted())
            this.curator.close();
    }

    /**
     * ZK锁任务处理模板方法
     * @param <V>
     * @param callback
     * @return
     * @throws Exception
     */
    public <V> V doExecute(ZkCallBack<V> callback) throws Exception {
        if (callback == null)
            return null;

        logger.debug("Acquire the lock: {}", this.lockPath);

        if (!this.locker.acquire(30, TimeUnit.SECONDS)) {
            throw new IllegalStateException(" could not acquire the lock: " + this.lockPath);
        }

        try {

            return callback.doInZk(this.curator, this.lockPath);

        } finally {
            logger.debug("Releasing the lock: {}", this.lockPath);
            this.locker.release();
        }
    }

    /**
     * ZK锁任务处理模板方法 - For Each Child Path
     * @param childPath
     * @param callback
     * @return
     * @throws Exception
     */
    public <V> V doExecute(final String childPath, ZkCallBack<V> callback) throws Exception {
        if (callback == null || childPath == null || childPath.trim().length() == 0)
            return null;

        //创建子Child Path对应的路径
        String childLockPath = ZKPaths.makePath(lockPath, childPath);
        //初始化锁
        InterProcessMutex childLocker = new InterProcessMutex(curator, childLockPath);

        //锁定子Child Path，进行操作
        logger.debug("Acquire the lock: {}", childLockPath);

        if (!childLocker.acquire(30, TimeUnit.SECONDS)) {
            throw new IllegalStateException(" could not acquire the lock: " + childLockPath);
        }

        try {

            return callback.doInZk(this.curator, childLockPath);

        } finally {
            logger.debug("Releasing the lock: {}", childLockPath);
            childLocker.release();
        }
    }

}
