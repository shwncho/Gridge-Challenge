package com.server.insta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public Docket SwaggerApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(swaggerInfo()) // API Docu 및 작성자 정보 매핑
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.server.play.controller"))
                .paths(PathSelectors.any()) // controller package 전부
                .build()
                .useDefaultResponseMessages(false); // 기본 세팅값인 200, 401, 402, 403, 404를 사용하지 않는다.
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("SpringBoot API Documentation")
                .description("APi Docs for Server")
                .version("v0.0.1")
                .build();
    }
}
