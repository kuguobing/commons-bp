/*
 * Copyright 2012 shengpay.com, Inc. All rights reserved.
 * shengpay.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * creator : kuguobing
 * create time : 2013-3-13 下午4:44:36
 */
package org.robinku.commons.ibatis.batch;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.robinku.commons.ibatis.paging.PageSqlExecutor;

import com.ibatis.sqlmap.engine.execution.BatchException;
import com.ibatis.sqlmap.engine.execution.BatchResult;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.scope.StatementScope;

/**
 * 修复Bug:
    Bug描述：由于Ibatis开源框架中对于Batch批量数据插入处理的Bug，导致分润导入原始待计算分润的交易订单时，报“ORA-00604: error occurred at recursive SQL level 1 ORA-01000: maximum open cursors exceeded”异常，导致无法进行每天的结算分润任务。 

        缺陷等级：A 

        改正方案：由于不同的业务数据，将原分润判断不同非空字段动态生成不同的批量insert语句，改成永远只生成一个固定的Insert语句，这样避免触碰Ibatis对应的BUG . 

    Ibatis对应的BUG代码部分备注摘要： 
    com.ibatis.sqlmap.engine.execution.SqlExecutor.addBatch 
    if (currentSql != null && currentSql.equals(sql)) { 
    int last = statementList.size() - 1; 
    ps = (PreparedStatement) statementList.get(last); 
    } else { 
    ps = prepareStatement(statementScope.getSession(), conn, sql); 
    setStatementTimeout(statementScope.getStatement(), ps); 
    currentSql = sql; 
    statementList.add(ps); 
    batchResultList.add(new BatchResult(statementScope.getStatement().getId(), sql)); 
    }
 * 
 * 功能描述：Batch批处理Bug增强修复处理
 * @author kuguobing
 * time : 2013-3-13 下午4:44:36
 * 
 * @see SqlExecutor.Batch
 */
public class BatchPageSqlExecutor extends PageSqlExecutor {

    @Override
    public void addBatch(StatementScope statementScope, Connection conn, String sql, Object[] parameters)
            throws SQLException {
        Batch batch = (Batch) statementScope.getSession().getBatch();
        if (batch == null) {
            batch = new Batch();
            statementScope.getSession().setBatch(batch);
        }
        batch.addBatch(statementScope, conn, sql, parameters);
    }

    @Override
    public void cleanup(SessionScope sessionScope) {
        Batch batch = (Batch) sessionScope.getBatch();
        if (batch != null) {
            batch.cleanupBatch(sessionScope);
            sessionScope.setBatch(null);
        }
    }

    @Override
    public int executeBatch(SessionScope sessionScope) throws SQLException {
        int rows = 0;
        Batch batch = (Batch) sessionScope.getBatch();
        if (batch != null) {
            try {
                rows = batch.executeBatch();
            } finally {
                batch.cleanupBatch(sessionScope);
            }
        }
        return rows;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List executeBatchDetailed(SessionScope sessionScope) throws SQLException, BatchException {
        List answer = null;
        Batch batch = (Batch) sessionScope.getBatch();
        if (batch != null) {
            try {
                answer = batch.executeBatchDetailed();
            } finally {
                batch.cleanupBatch(sessionScope);
            }
        }
        return answer;
    }

    private static void setStatementTimeout(MappedStatement mappedStatement, Statement statement) throws SQLException {
        if (mappedStatement.getTimeout() != null) {
            statement.setQueryTimeout(mappedStatement.getTimeout().intValue());
        }
    }

    private static PreparedStatement prepareStatement(SessionScope sessionScope, Connection conn, String sql)
            throws SQLException {
        SqlMapExecutorDelegate delegate = ((SqlMapClientImpl) sessionScope.getSqlMapExecutor()).getDelegate();
        if (sessionScope.hasPreparedStatementFor(sql)) {
            return sessionScope.getPreparedStatement((sql));
        } else {
            PreparedStatement ps = conn.prepareStatement(sql);
            sessionScope.putPreparedStatement(delegate, sql, ps);
            return ps;
        }
    }

    private static void closeStatement(SessionScope sessionScope, PreparedStatement ps) {
        if (ps != null) {
            if (!sessionScope.hasPreparedStatement(ps)) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    //
    // Inner Classes
    //

    private static class Batch {
        private Map<String, PreparedStatement> statementMap = new HashMap<String, PreparedStatement>();
        private Map<String, BatchResult> batchResultMap = new HashMap<String, BatchResult>();
        private int size;

        /**
         * Create a new batch
         */
        public Batch() {
            this.size = 0;
        }

        /**
         * Getter for the batch size
         *
         * @return - the batch size
         */
        public int getSize() {
            return size;
        }

        /**
         * Add a prepared statement to the batch
         *
         * @param statementScope    - the request scope
         * @param conn       - the database connection
         * @param sql        - the SQL to add
         * @param parameters - the parameters for the SQL
         * @throws SQLException - if the prepare for the SQL fails
         */
        public void addBatch(StatementScope statementScope, Connection conn, String sql, Object[] parameters)
                throws SQLException {
            PreparedStatement ps = null;
            if (sql != null && statementMap.containsKey(sql)) {
                ps = statementMap.get(sql);
            } else {
                ps = prepareStatement(statementScope.getSession(), conn, sql);
                setStatementTimeout(statementScope.getStatement(), ps);
                statementMap.put(sql, ps);
                batchResultMap.put(sql, new BatchResult(statementScope.getStatement().getId(), sql));
            }
            statementScope.getParameterMap().setParameters(statementScope, ps, parameters);
            ps.addBatch();
            size++;
        }

        /**
         * TODO (Jeff Butler) - maybe this method should be deprecated in some release,
         * and then removed in some even later release.  executeBatchDetailed gives
         * much more complete information.
         * <p/>
         * Execute the current session's batch
         *
         * @return - the number of rows updated
         * @throws SQLException - if the batch fails
         */
        public int executeBatch() throws SQLException {
            int totalRowCount = 0;
            for (PreparedStatement ps : statementMap.values()) {
                int[] rowCounts = ps.executeBatch();
                for (int j = 0; j < rowCounts.length; j++) {
                    if (rowCounts[j] == Statement.SUCCESS_NO_INFO) {
                        // do nothing
                    } else if (rowCounts[j] == Statement.EXECUTE_FAILED) {
                        throw new SQLException("The batched statement at index " + j + " failed to execute.");
                    } else {
                        totalRowCount += rowCounts[j];
                    }
                }
            }
            return totalRowCount;
        }

        /**
         * Batch execution method that returns all the information
         * the driver has to offer.
         *
         * @return a List of BatchResult objects
         * @throws BatchException (an SQLException sub class) if any nested
         *                        batch fails
         * @throws SQLException   if a database access error occurs, or the drive
         *                        does not support batch statements
         * @throws BatchException if the driver throws BatchUpdateException
         */
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public List executeBatchDetailed() throws SQLException, BatchException {
            List answer = new ArrayList();
            int i = 0;
            for (Entry<String, PreparedStatement> entryStmt : statementMap.entrySet()) {
                BatchResult br = batchResultMap.get(entryStmt.getKey());
                PreparedStatement ps = entryStmt.getValue();
                try {
                    br.setUpdateCounts(ps.executeBatch());
                } catch (BatchUpdateException e) {
                    StringBuffer message = new StringBuffer();
                    message.append("Sub batch number ");
                    message.append(i + 1);
                    message.append(" failed.");
                    if (i > 0) {
                        message.append(" ");
                        message.append(i);
                        message.append(" prior sub batch(s) completed successfully, but will be rolled back.");
                    }
                    throw new BatchException(message.toString(), e, answer, br.getStatementId(), br.getSql());
                }
                answer.add(br);
                i++;
            }
            return answer;
        }

        /**
         * Close all the statements in the batch and clear all the statements
         *
         * @param sessionScope
         */
        public void cleanupBatch(SessionScope sessionScope) {
            for (PreparedStatement ps : statementMap.values()) {
                closeStatement(sessionScope, ps);
            }
            statementMap.clear();
            batchResultMap.clear();
            size = 0;
        }
    }
}
