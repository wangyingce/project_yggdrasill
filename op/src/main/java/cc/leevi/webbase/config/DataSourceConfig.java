package cc.leevi.webbase.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "neo4jDataSourceProperties")
    @ConfigurationProperties("neo4j.datasource")
    public DataSourceProperties neo4jDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean("neo4jDataSource")
    public HikariDataSource neo4jDataSource(@Qualifier("neo4jDataSourceProperties") DataSourceProperties dataSourceProperties){
        return createDataSource(dataSourceProperties);
    }

    @Primary
    @Bean(name = "neo4jJdbcTemplate")
    public JdbcTemplate neo4jJdbcTemplate(@Qualifier("neo4jDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return new JdbcTemplate(createDataSource(dataSourceProperties));
    }

    @Primary
    @Bean("neo4jTransactionManager")
    public DataSourceTransactionManager neo4jTransactionManager(@Qualifier("neo4jDataSource") HikariDataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "springDataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties jdbcDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("springDataSource")
    public HikariDataSource springDataSource(@Qualifier("springDataSourceProperties") DataSourceProperties dataSourceProperties){
        return createDataSource(dataSourceProperties);
    }

    @Bean(name = "springJdbcTemplate")
    public JdbcTemplate springJdbcTemplate(@Qualifier("springDataSource") HikariDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("springTransactionManager")
    public DataSourceTransactionManager springTransactionManager(@Qualifier("springDataSource") HikariDataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }



    private HikariDataSource createDataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setConnectionTestQuery(properties.getConnectionTestQuery());
        dataSource.setMaxLifetime(properties.getMaxLifeTime());
        dataSource.setIdleTimeout(properties.getIdleTimeout());
        dataSource.setMaximumPoolSize(properties.getMaximumPoolSize());
        dataSource.setMinimumIdle(properties.getMinimumIdle());
        return dataSource;
    }
}