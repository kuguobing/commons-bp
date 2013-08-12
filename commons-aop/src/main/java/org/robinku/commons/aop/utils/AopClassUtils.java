/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-3-13 下午6:16:29
 */
package org.robinku.commons.aop.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.aopalliance.intercept.MethodInvocation;

/**
 * 功能描述：
 * @author kuguobing
 * time : 2013-3-13 下午6:16:29
 */
public class AopClassUtils {

    /**
     * 取得指定对象的指定域的值
     * @param jp
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getDeclaredFieldValue(Object jp, String fieldName) throws NoSuchFieldException,
            IllegalAccessException {
        Class<?> class1 = jp.getClass();
        Field aField = class1.getDeclaredField(fieldName);
        aField.setAccessible(true);
        Object fieldValue = aField.get(jp);
        return fieldValue;
    }

    /**
     * 获取Throwable信息
     * 
     * @param t
     * @return
     */
    public static String getThrowableInfo(Throwable t) {
        if (t == null) {
            return null;
        }

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);

        return stringWriter.toString();
    }

    /**
     * 获取调用信息;
     * 
     * @param mi
     * @return
     */
    public static String getMethodCallInfo(MethodInvocation mi) {
        return getMethodCallInfo(mi.getThis(), mi.getMethod(), mi.getArguments());
    }

    /**
     * 
     * @param targetObj
     * @param method
     * @param args
     * @return
     */
    public static String getMethodCallInfo(Object targetObj, Method method, Object[] args) {
        String getMethodName = getMethodSimpleName(targetObj, method);
        return getMethodCallInfo(getMethodName, args);
    }

    /**
     * 获取方法调用信息
     * @param methodName 方法名称 
     * @param args       参数数组
     * @return
     */
    public static String getMethodCallInfo(String methodName, Object[] args) {
        StringBuffer callInfo = new StringBuffer();
        callInfo.append(methodName + "(");
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                callInfo.append(i > 0 ? "," : "");
                callInfo.append(args[i]);
            }
        }
        callInfo.append(")");
        return callInfo.toString();
    }

    /**
     * @param mi
     * @param result
     * @return
     */
    public static String getReturnInfo(MethodInvocation mi, Object result) {
        return mi.getMethod().getReturnType().equals(void.class) ? "void" : result.toString();
    }

    /**
     * @param targetObj
     * @return
     */
    private static String getObjectFullClassName(Object targetObj) {
        String simpleName = targetObj.getClass().getSimpleName();
        String objStr = targetObj.toString();
        String regex_proxy = "\\$Proxy\\d+";
        int indexOf = objStr.indexOf("@");
        if (Pattern.matches(regex_proxy, simpleName) && indexOf != -1) {
            return objStr.substring(0, indexOf);
        } else {
            return simpleName;
        }

    }

    /**
     * 取得方法简称(所属类无包名)
     * 
     * @param targetObj
     * @param method
     * @return
     */
    public static String getMethodSimpleName(Object targetObj, Method method) {
        if (targetObj != null) {
            String objectFullClassName = getObjectFullClassName(targetObj);
            return objectFullClassName.substring(objectFullClassName.lastIndexOf(".") + 1) + "." + method.getName();
        } else {
            return method.getDeclaringClass().getSimpleName() + "." + method.getName();// 所属类名
        }
    }
}
