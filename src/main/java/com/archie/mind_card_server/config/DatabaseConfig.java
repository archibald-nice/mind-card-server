package com.archie.mind_card_server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Configuration
@EnableJpaRepositories(basePackages = "com.archie.mind_card_server.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    
    @Value("${spring.datasource.url}")
    private String jdbcUrl;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    
    @Value("${spring.datasource.hikari.maximum-pool-size:20}")
    private int maximumPoolSize;
    
    @Value("${spring.datasource.hikari.minimum-idle:5}")
    private int minimumIdle;
    
    @Value("${spring.datasource.hikari.connection-timeout:30000}")
    private long connectionTimeout;
    
    @Value("${spring.datasource.hikari.idle-timeout:600000}")
    private long idleTimeout;
    
    @Value("${spring.datasource.hikari.max-lifetime:1800000}")
    private long maxLifetime;
    
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        log.info("配置数据库连接池");
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        
        // 连接池配置
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        
        // 连接池名称
        config.setPoolName("MindCardHikariCP");
        
        // 连接测试查询
        config.setConnectionTestQuery("SELECT 1");
        
        // 自动提交
        config.setAutoCommit(true);
        
        // 连接泄漏检测
        config.setLeakDetectionThreshold(60000);
        
        log.info("数据库连接池配置完成 - URL: {}, 最大连接数: {}", jdbcUrl, maximumPoolSize);
        
        return new HikariDataSource(config);
    }
}
