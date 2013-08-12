package org.robinku.commons.aop.tx;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.robinku.commons.aop.utils.AopClassUtils;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.dao.ConcurrencyFailureException;

/**
 * 数据库事务异常重试拦截器
 * @description
 */
public class TransactionRetryAOP implements MethodInterceptor {

    /**
     * 系统日志输出句柄
     */
    private final Logger log = Logger.getLogger(TransactionRetryAOP.class);

    /**
     * 最大重试次数(默认:3次)
     */
    private int maxRetryCount = 3;

    /**
     * 充值前休眠的时长(单位:毫秒 默认值:1000)
     */
    private long sleepMillisecond = 1000;

    /**
     * 当事务更新过程中发生数据访问异常时,按照指定的次数进行重试
     * 
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation mi) throws Throwable {
        int retryCount = 0;
        while (true) {
            try {
                ReflectiveMethodInvocation inv = (ReflectiveMethodInvocation) mi;
                MethodInvocation anotherInvocation = inv.invocableClone();
                return anotherInvocation.proceed();
            } catch (ConcurrencyFailureException e) {
                String callInfo = AopClassUtils.getMethodCallInfo(mi);
                if (++retryCount <= maxRetryCount) {
                    long thisTimeSleepMillsecond = sleepMillisecond * retryCount;
                    log.info("调用【" + callInfo + "】发生数据库访问冲突异常,休眠【" + (thisTimeSleepMillsecond / 1000) + "】秒后尝试重试【"
                            + retryCount + "/" + maxRetryCount + "】");
                    Thread.sleep(thisTimeSleepMillsecond);
                    continue;
                } else {
                    log.info("调用【" + callInfo + "】发生数据库访问冲突异常,重试【" + maxRetryCount + "】次依然未能取得成功,以此抛出【" + e
                            + "】异常信息!!!!");
                    throw e;
                }

            }
        }

    }

    /**
     * 获取“maxRetryCount”(类型：int)
     */
    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    /**
     * 设置“maxRetryCount”（类型：int）
     */
    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    /**
     * 获取“sleepMillisecond”(类型：long)
     */
    public long getSleepMillisecond() {
        return sleepMillisecond;
    }

    /**
     * 设置“sleepMillisecond”（类型：long）
     */
    public void setSleepMillisecond(long sleepMillisecond) {
        this.sleepMillisecond = sleepMillisecond;
    }

}
