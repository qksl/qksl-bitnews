package io.bitnews.admin;

import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.ConnectionSource;
import org.beetl.sql.core.ConnectionSourceHelper;
import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.SQLLoader;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.DebugInterceptor;

/**
 * 
 * @author apple
 *
 */
public class ModelTest {
	static String driver = "com.mysql.jdbc.Driver";
	static String url = "jdbc:mysql://172.20.1.91:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	static String username = "root";
	static String password = "root";

	public static void main(String[] args) throws Exception {
		ConnectionSource source = ConnectionSourceHelper.getSimple(driver, url, username, password);
		DBStyle mysql = new MySqlStyle();
		// sql语句放在classpagth的/sql 目录下
		SQLLoader loader = new ClasspathLoader("/sql");
		// 数据库命名跟java命名一样，所以采用DefaultNameConversion，还有一个是UnderlinedNameConversion，下划线风格的，
		UnderlinedNameConversion nc = new UnderlinedNameConversion();
		// 最后，创建一个SQLManager,DebugInterceptor 不是必须的，但可以通过它查看sql执行情况
		SQLManager sqlManager = new SQLManager(mysql, loader, source, nc, new Interceptor[] { new DebugInterceptor() });

		// sqlManager.genPojoCodeToConsole("t_banner");

		// sqlManager.genSQLTemplateToConsole("t_team_news");
		sqlManager.select("select * from new_table", NewTable.class, new Object());
		
	}
	
	
	class NewTable {
		private Integer id;
		
		private String enc;
	}
}
