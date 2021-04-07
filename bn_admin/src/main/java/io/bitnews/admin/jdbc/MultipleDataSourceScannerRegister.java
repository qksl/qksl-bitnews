package io.bitnews.admin.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author lahne at 2020/4/21
 */
@Slf4j
public class MultipleDataSourceScannerRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final String DEFAULT_PREFIX = "app.datasource";

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        MergedAnnotation<MultipleDataSource> annotation = importingClassMetadata.getAnnotations().get(MultipleDataSource.class);
        String propertiesPrefix = DEFAULT_PREFIX;
        if (annotation.isPresent()){
            MultipleDataSource multipleDataSource = annotation.synthesize();
            propertiesPrefix = multipleDataSource.propertiesPrefix();
        }

        log.info("开始从{}加载数据源", propertiesPrefix);
        Binder binder = new Binder(ConfigurationPropertySources.get(environment));
        ResolvableType mapType = ResolvableType.forClassWithGenerics(Map.class, String.class, DataSourceProperties.class);
        Map<String, DataSourceProperties> propertiesMap = binder.bindOrCreate(propertiesPrefix, Bindable.of(mapType));
        if (!CollectionUtils.isEmpty(propertiesMap)){
            propertiesMap.entrySet().stream().forEach(e -> {
                String beanName = e.getKey();
                register(registry, beanName, e.getValue());
            });
        }
    }

    void register(BeanDefinitionRegistry registry, String dataSourceName, DataSourceProperties dataSourceProperties){
        if (!registry.containsBeanDefinition(dataSourceName)){
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DataSource.class,
                    () -> dataSourceProperties.initializeDataSourceBuilder().build());
            log.info("注册数据源Bean: {}", dataSourceName);
            registry.registerBeanDefinition(dataSourceName, builder.getBeanDefinition());
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
