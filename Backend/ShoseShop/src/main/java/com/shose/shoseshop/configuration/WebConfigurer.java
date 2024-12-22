package com.shose.shoseshop.configuration;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfigurer implements ServletContextInitializer, WebMvcConfigurer {

    private final Environment env;

    // Bean cho ModelMapper
    @Bean
    public ModelMapper initModelMapper(){
        return new ModelMapper();
    }

    // Phương thức cấu hình cho ServletContext
    @Override
    public void onStartup(ServletContext servletContext) {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }
        log.info("Web application fully configured");
    }

    // Cấu hình CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Cấu hình các giá trị CORS cho phép
        config.setAllowCredentials(true); // Cho phép gửi thông tin đăng nhập (cookies)
        config.addAllowedOrigin("http://localhost:3000, http://localhost:3001"); // Địa chỉ frontend được phép truy cập
        config.addAllowedMethod("*"); // Cho phép tất cả các phương thức HTTP (GET, POST, PUT, DELETE, OPTIONS)
        config.addAllowedHeader("*"); // Cho phép tất cả các headers

        // Đảm bảo rằng các giá trị CORS không trống trước khi đăng ký chúng
        if (!CollectionUtils.isEmpty(config.getAllowedOrigins()) || !CollectionUtils.isEmpty(config.getAllowedOriginPatterns())) {
            log.debug("Registering CORS filter");
            source.registerCorsConfiguration("/api/**", config); // Đăng ký CORS cho các API
            source.registerCorsConfiguration("/management/**", config); // Đăng ký CORS cho các API quản lý
            source.registerCorsConfiguration("/v3/api-docs", config); // Đăng ký CORS cho API docs (Swagger)
            source.registerCorsConfiguration("/swagger-ui/**", config); // Đăng ký CORS cho Swagger UI
        }

        return source;
    }

    // Cấu hình nội dung cho việc đàm phán kiểu dữ liệu (Content Negotiation)
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(false)
                .ignoreAcceptHeader(true)
                .defaultContentType(org.springframework.http.MediaType.APPLICATION_JSON) // Kiểu dữ liệu mặc định là JSON
                .mediaType("json", org.springframework.http.MediaType.APPLICATION_JSON)
                .mediaType("xml", org.springframework.http.MediaType.APPLICATION_XML);
    }
}
