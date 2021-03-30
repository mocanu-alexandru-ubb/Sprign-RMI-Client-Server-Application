package Config;

import Networking.ServerInformation;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

@Configuration
public class JdbcConfig
{
    @Bean
    JdbcOperations jdbcOperations(DataSource dataSource)
    {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Bean
    DataSource dataSource()
    {
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setUrl(ServerInformation.databaseConnectionString);
        basicDataSource.setUsername(ServerInformation.databaseUser);
        basicDataSource.setPassword(ServerInformation.databasePassword);
        basicDataSource.setInitialSize(2);

        return basicDataSource;
    }
}