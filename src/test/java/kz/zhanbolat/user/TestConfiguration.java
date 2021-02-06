package kz.zhanbolat.user;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.sql.DataSource;

@SpringBootConfiguration
@EntityScan(basePackages = "kz.zhanbolat.user.entity")
@ComponentScan(basePackages = "kz.zhanbolat.user")
public class TestConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer postgreSQLContainer() {
        PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:10")
                .withDatabaseName("test_db").withUsername("postgresql").withPassword("0");
        postgreSQLContainer.addExposedPort(5432);
        postgreSQLContainer.waitingFor(Wait.forListeningPort());
        return postgreSQLContainer;
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer postgreSQLContainer) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        hikariConfig.setUsername(postgreSQLContainer.getUsername());
        hikariConfig.setPassword(postgreSQLContainer.getPassword());
        hikariConfig.setDriverClassName("org.postgresql.Driver");

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        Flyway flyway = Flyway.configure().dataSource(hikariDataSource)
                .locations("classpath:db/migration", "classpath:data/migration").load();
        flyway.migrate();

        return hikariDataSource;
    }
}
