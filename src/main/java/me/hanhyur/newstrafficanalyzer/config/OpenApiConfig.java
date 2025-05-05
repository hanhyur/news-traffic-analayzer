package me.hanhyur.newstrafficanalyzer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("뉴스 트래픽 분석 시스템 API")
                .version("1.0.0")
                .description("kafka와 Redis 기반 실시간 뉴스 트래픽 수집 및 분석 API 명세서");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }

}
