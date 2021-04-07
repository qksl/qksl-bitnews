package io.bitnews.oauth.config;

import com.zaxxer.hikari.HikariDataSource;
import org.beetl.sql.core.*;
import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.DebugInterceptor;
import org.beetl.sql.ext.spring4.BeetlSqlDataSource;
import org.beetl.sql.ext.spring4.SqlManagerFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * Created by ywd on 2019/7/16.
 */
@Configuration
public class SqlManagerConfig {

    @Bean(name="dataSource")
    @Primary
    public DataSource datasource(Environment env) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(env.getProperty("spring.datasource.url"));
        ds.setUsername(env.getProperty("spring.datasource.username"));
        ds.setPassword(env.getProperty("spring.datasource.password"));
        ds.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        return ds;
    }

    @Bean(name="adminSqlManager")
    public SQLManager sqlManager(Environment env) {
        String driver = env.getProperty("spring.datasource.admin.driver-class-name");
        String url = env.getProperty("spring.datasource.admin.url");
        String name = env.getProperty("spring.datasource.admin.username");
        String password = env.getProperty("spring.datasource.admin.password");
        ConnectionSource source = ConnectionSourceHelper.getSimple(driver, url, name, password);
        DBStyle mysql = new MySqlStyle();
        // sql语句放在classpagth的/sql 目录下
        SQLLoader loader = new ClasspathLoader("/sql");
        // 数据库命名跟java命名一样，所以采用DefaultNameConversion，还有一个是UnderlinedNameConversion，下划线风格的
        UnderlinedNameConversion nc = new  UnderlinedNameConversion();
        // 最后，创建一个SQLManager,DebugInterceptor 不是必须的，但可以通过它查看sql执行情况
        SQLManager sqlManager = new SQLManager(mysql,loader,source,nc,new Interceptor[]{new DebugInterceptor()});
        return sqlManager;
    }

    @Bean
    @Primary
    public SqlManagerFactoryBean sqlManagerFactoryBean(@Qualifier("dataSource") DataSource dataSource) {
        SqlManagerFactoryBean factoryBean = new SqlManagerFactoryBean();
        BeetlSqlDataSource source = new BeetlSqlDataSource();
        source.setMasterSource(dataSource);
        factoryBean.setCs(source);
        factoryBean.setDbStyle(new MySqlStyle());
        //控制台或者日志系统输出执行的sql语句
        factoryBean.setInterceptors(new Interceptor[]{new DebugInterceptor()});
        //开启驼峰
        factoryBean.setNc(new UnderlinedNameConversion());
        //sql文件路径
        factoryBean.setSqlLoader(new ClasspathLoader("/sql"));
        return factoryBean;
    }


}
