package org.robinku.commons.core.db.paging.dialect;

/**
 * 
 * @Title: SQLServer2008PageDialect.java
 * @Description: A dialect for Microsoft SQL Server 2008 with JDBC Driver 3.0
 *               and above，支持物理分页
 * @author kuguobing<kuguobing@snda.com>
 * @date 2011-5-26 上午10:49:57
 * @version V1.0
 */
public class SQLServer2008PageDialect extends SQLServer2005PageDialect {

//	public static void main(String[] args) {
//		DBPageDialect pageDialect = new SQLServer2008PageDialect();
//		String pageSQL = pageDialect
//				.getPageQuerySql(
//						"SELECT * FROM ESALE_SHAREPROFIT_FT WHERE (TYPE1 = '01' OR TYPE1= '02') AND DATA_DESC = '2011-05-14' ",
//						0, 100);
//
//		System.out.println("分页语句：" + pageSQL);
//	}
}
