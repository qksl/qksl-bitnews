package io.bitnews.admin.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		List<Parameter> operationParameters = new ArrayList<Parameter>();
		operationParameters.add(new ParameterBuilder()
				.name("Authorization")
				.description("令牌")
				.modelRef(new ModelRef("string"))
				.parameterType("header")
				.required(false).build());
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("io.bitnews.admin.controller")).paths(PathSelectors.any())
				.build().apiInfo(apiInfo()).globalOperationParameters(operationParameters);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("管理员后台模块").build();
	}

}
