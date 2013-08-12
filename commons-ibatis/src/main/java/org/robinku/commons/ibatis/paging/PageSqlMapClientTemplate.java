/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-3-13 下午4:01:35
 */
package org.robinku.commons.ibatis.paging;

import javax.sql.DataSource;

import org.robinku.commons.ibatis.base.ReflectUtil;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.util.Assert;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;

/**
 * 功能描述：
 * @author kuguobing
 * time : 2013-3-13 下午4:01:35
 */
@SuppressWarnings("deprecation")
public class PageSqlMapClientTemplate extends SqlMapClientTemplate {

    /**
     * 注入Paging Sql Executor覆盖Ibatis中的SqlExecutor
     */
    private SqlExecutor sqlExecutor;

    public SqlExecutor getSqlExecutor() {
        return sqlExecutor;
    }

    public void setSqlExecutor(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    public PageSqlMapClientTemplate() {
        super();
    }

    public PageSqlMapClientTemplate(DataSource dataSource, SqlMapClient sqlMapClient) {
        super(dataSource, sqlMapClient);
    }

    public PageSqlMapClientTemplate(SqlMapClient sqlMapClient) {
        super(sqlMapClient);
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        // Initialization时
        // 反射注入设置所有查询SqlMapClientTemplate'SqlMapClient的sqlExecutor属性
        Assert.notNull(sqlExecutor, "需要指定分页的Ibatis SqlExecutor！");
        // 由于sqlExecutor是com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient::getDelegate()的私有保护成员变量，
        // 且没有公开的set方法，所以此处通过反射绕过java的访问控制
        // Hack setting & replace sqlMapClient --> deltegate --> sqlExecutor
        SqlMapClient sqlMapClient = this.getSqlMapClient();
        if (sqlMapClient instanceof ExtendedSqlMapClient) {
            ReflectUtil.setFieldValue(((ExtendedSqlMapClient) sqlMapClient).getDelegate(), "sqlExecutor",
                    SqlExecutor.class, sqlExecutor);
        }

    }
}
