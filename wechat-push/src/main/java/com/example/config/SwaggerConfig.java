package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 * 用于配置Swagger文档的基本信息
 */
@Configuration
public class SwaggerConfig {
    
    /**
     * 配置OpenAPI信息
     * @return OpenAPI实例
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("微信推送服务API")
                        .description("微信推送服务的API文档，用于调试和测试接口")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("contact@example.com")
                                .url("http://example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}