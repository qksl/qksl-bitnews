package io.bitnews.admin.config;

import io.bitnews.admin.jdbc.MultipleDataSource;
import io.bitnews.admin.jdbc.MultipleDataSourceScannerRegister;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@MultipleDataSource
@Import({MultipleDataSourceScannerRegister.class})
public class MultipleDataSourceConfig {

}