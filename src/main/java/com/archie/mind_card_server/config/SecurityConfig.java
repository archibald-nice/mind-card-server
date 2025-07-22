package com.archie.mind_card_server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("配置安全过滤链");
        
        http
            // 禁用CSRF保护
            .csrf(AbstractHttpConfigurer::disable)
            
            // 配置会话管理
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 配置请求授权
            .authorizeHttpRequests(authz -> authz
                // 允许访问健康检查端点
                .requestMatchers("/actuator/**").permitAll()
                
                // 允许访问API文档相关端点
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // 允许访问静态资源
                .requestMatchers("/static/**", "/uploads/**").permitAll()
                
                // 允许OPTIONS请求
                .requestMatchers("OPTIONS", "/**").permitAll()
                
                // 其他请求需要认证（暂时允许所有请求，可根据需要调整）
                .anyRequest().permitAll()
            )
            
            // 配置HTTP基本认证
            .httpBasic(httpBasic -> httpBasic
                .realmName("Mind Card Server"))
            
            // 配置CORS
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                corsConfiguration.setAllowedOriginPatterns(java.util.List.of("*"));
                corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                corsConfiguration.setAllowCredentials(true);
                return corsConfiguration;
            }));
        
        log.info("安全配置完成");
        return http.build();
    }
}
