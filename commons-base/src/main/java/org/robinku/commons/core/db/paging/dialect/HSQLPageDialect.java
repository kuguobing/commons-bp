package org.robinku.commons.core.db.paging.dialect;

/**
 * 
 * @Title: HSQLPageDialect.java
 * @Description: An SQL dialect compatible with HSQLDB (Hypersonic SQL).
 * @author kuguobing<kuguobing@snda.com>
 * @date 2010-12-8 下午05:52:00
 * @version V1.0
 */
public class HSQLPageDialect extends AbstractDBPageDialect {

	@Override
	public String getPageQuerySql(String querySelect, long offset, long pagesize) {
		StringBuffer sb = new StringBuffer(querySelect.length() + 20);
		sb.append(querySelect.trim());
		if (offset > 0) {
			sb.insert(6, " limit " + offset + " " + pagesize);
		} else {
			sb.insert(6, " top " + pagesize);
		}
		return sb.toString();

	}

	@Override
	public boolean supportPage() {
		return true;
	}
}
