package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 * 用于配置认证和授权规则
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护（对于API接口通常不需要）
                .csrf(AbstractHttpConfigurer::disable)
                // 允许跨域请求
                .cors(AbstractHttpConfigurer::disable)
                // 配置授权规则
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // 允许访问的端点
                                .requestMatchers("/api/auth/**").permitAll() // 登录接口
                                .requestMatchers("/api/user-bind/**").permitAll() // 用户绑定接口
                                .requestMatchers("/api/sys-notify/**").permitAll() // 系统通知接口
                                .requestMatchers("/v3/api-docs/**").permitAll() // Swagger API文档
                                .requestMatchers("/swagger-ui/**").permitAll() // Swagger UI
                                .requestMatchers("/login.html").permitAll() // 登录页面
                                .requestMatchers("/todo.html").permitAll() // 待办页面
                                .requestMatchers("/bind.html").permitAll() // 绑定页面
                                .requestMatchers("/*.css", "/*.js", "/*.png", "/*.jpg", "/*.ico").permitAll() // 静态资源
                                .requestMatchers("/favicon.ico").permitAll() // favicon 图标
                                // 其他所有端点需要认证
                                .anyRequest().authenticated()
                )
                // 添加JWT认证过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
