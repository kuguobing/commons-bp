/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2012-11-5 下午02:50:58
 */
package org.robinku.commons.aop.logger;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.robinku.commons.aop.utils.AopClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

/**
 * 功能描述：Logger AOP日志输出，统计调用执行时间和日志
 * @author kuguobing
 * time : 2012-11-5 下午02:50:58
 */
public class LoggerAOP implements MethodInterceptor {
    private static Logger logger = LoggerFactory.getLogger(LoggerAOP.class);

    private boolean logDetail = false;

    public void setLogDetail(boolean logDetail) {
        this.logDetail = logDetail;
    }

    /**
     * Aspect输出日志
     * @param mi
     * @return
     * @throws Throwable
     */
    public Object logout(ProceedingJoinPoint mi) throws Throwable {
        ProxyMethodInvocation method = (ProxyMethodInvocation) AopClassUtils.getDeclaredFieldValue(
                (MethodInvocationProceedingJoinPoint) mi, "methodInvocation");
        //调用AOP方法拦截输出
        return this.invoke(method);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        //整理调用方法的信息;
        String methodSimpleInfo = getMethodSimpleName(mi);
        long start = System.currentTimeMillis();
        try {
            Object result = mi.proceed();

            logger.info("Call {} method spend {} ms.", methodSimpleInfo, (System.currentTimeMillis() - start));

            //判断Annotation是否需要记录日志
            LoggerAnn logoutAnn = mi.getMethod().getAnnotation(LoggerAnn.class);
            if (logoutAnn != null && logoutAnn.disableLogOut()) {
                return result;
            }

            //判断是否需要打印Detail信息
            if (logDetail) {
                try {
                    printReq(mi);
                    printResp(mi, result);
                } catch (Throwable e) {
                    //do nothing
                }
            }

            return result;
        } catch (Throwable t) {
            String throwableInfo = AopClassUtils.getThrowableInfo(t);
            logger.error("Call {} method meet error 【{}】.", methodSimpleInfo, throwableInfo);

            throw t;
        }
    }

    /**
     * 打印请求日志
     */
    private static void printReq(MethodInvocation mi) {
        //判断是否需要记录日志
        //        LoggerAnn logoutAnn = mi.getMethod().getAnnotation(LoggerAnn.class);
        //        if (logoutAnn != null && logoutAnn.disableLogOut()) {
        //            return;
        //        }

        String methodInfo = AopClassUtils.getMethodCallInfo("", mi.getArguments());
        logger.info("Request Object is【{}】", methodInfo);
    }

    /**
     * 打印相应日志
     * @param mi
     * @param o
     * @param logPre
     */
    private static void printResp(MethodInvocation mi, Object o) {
        String returnInfo = AopClassUtils.getReturnInfo(mi, o);
        logger.info("Response Object is【{}】", returnInfo);
    }

    /**
     * 取得方法名称(例:ClassUtils.getMethodName)
     * 
     * @param mi
     * @return
     */
    private static String getMethodSimpleName(MethodInvocation mi) {
        Method method = mi.getMethod();
        return AopClassUtils.getMethodSimpleName(mi.getThis(), method);
    }

    /**
     * 打印日志
     * 
     * @param o
     * @param logPre
     */
    //@SuppressWarnings("unchecked")
    //    private static void printReq(Object o, String logPre) {
    //        if (o == null) {
    //            logger.info(logPre + "null");
    //        }
    //
    //        if (o instanceof Map) {
    //            logger.info(logPre + JsonUtils.toJson(o));
    //        } else if (o instanceof String) {
    //            logger.info(logPre + o);
    //        } else if (o instanceof BigDecimal) {
    //            logger.info(logPre + ((BigDecimal) o).toString());
    //        } else if (o instanceof BaseObject) {
    //            logger.info(logPre + ((BaseObject) o).toString());
    //        } else {
    //            logger.info(logPre + ToStringBuilder.reflectionToString(o));
    //        }
    //    }

}
