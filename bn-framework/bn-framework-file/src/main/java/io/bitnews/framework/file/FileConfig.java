package io.bitnews.framework.file;

import io.bitnews.framework.file.properties.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ywd on 2019/11/21.
 */
@Configuration
@EnableConfigurationProperties(FileProperties.class)
public class FileConfig {
}
