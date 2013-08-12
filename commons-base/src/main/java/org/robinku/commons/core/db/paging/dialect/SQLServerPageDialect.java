package org.robinku.commons.core.db.paging.dialect;

/**
 * 
 * @Title: SQLServerPageDialect.java
 * @Description: Microsoft Sql Server分页方言，目前只支持Sqlserver 2005 - 2008以上（SQL
 *               Server 2000不支持）
 * @author kuguobing<kuguobing@snda.com>
 * @date 2011-5-26 上午09:45:49
 * @version V1.0
 */
public class SQLServerPageDialect extends AbstractDBPageDialect {

	@Override
	public boolean supportPage() {
		return true;
	}

	@Override
	public boolean supportPageOffset() {
		return false;
	}

	/**
	 * One implements strategy:
	 * 
	 * "select top " + size +" * from tablename where id not in (select top " +
	 * (startIndex -1) + " id from tablename order by id desc) order by id desc"
	 * 
	 * select top 20 * from yourtable where id not in (select top 20 id from
	 * yourtable) < fetch records form 20 - 40>
	 * 
	 * 
	 * if (hasOffset) throw new UnsupportedOperationException("sql server has no
	 * offset"); return new StringBuffer( querySelect.length()+6 )
	 * .append(querySelect) .insert( getAfterSelectInsertPoint(querySelect), "
	 * top " + limit ) .toString();
	 */
	@Override
	public String getPageQuerySql(String querySelect, long offset, long pagesize) {
		if (offset > 0) {
			throw new UnsupportedOperationException(
					"query result offset is not supported");
		}

		return new StringBuffer(querySelect.length() + 15).append(querySelect)
				.insert(getAfterSelectInsertPoint(querySelect),
						" top " + pagesize).toString();
	}

	static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		final int selectDistinctIndex = sql.toLowerCase().indexOf(
				"select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}

}
