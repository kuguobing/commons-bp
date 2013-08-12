package org.robinku.commons.core.db.paging.dialect;

/**
 * 
 * @Title: MySQLPageDialect.java
 * @Description: An SQL dialect for MySQL.
 * @author kuguobing<kuguobing@snda.com>
 * @date 2010-12-8 下午05:49:23
 * @version V1.0
 */
public class MySQLPageDialect extends AbstractDBPageDialect implements
		DBPageDialect {

	/**
	 * LIMIT 0 quickly returns an empty set. This can be useful for checking the
	 * validity of a query.
	 * 
	 * [FROM pos ] [LIMIT [ offset ,] row_count ]
	 * 
	 */
	@Override
	public String getPageQuerySql(String querySelect, long offset, long pagesize) {
		StringBuffer sb = new StringBuffer(querySelect.length() + 20);

		sb.append(querySelect);

		if (offset > 0) {
			sb.append(" limit " + offset + "," + pagesize);
		} else {
			sb.append(" limit " + pagesize);
		}
		return sb.toString();

	}

	@Override
	public boolean supportPage() {
		return true;
	}

}