package io.bitnews.passport.config;

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
