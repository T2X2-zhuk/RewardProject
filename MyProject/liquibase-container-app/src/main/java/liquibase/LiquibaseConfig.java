package liquibase;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {


    // ===========================
    // Calculation DB
    // ===========================
    @Bean
    @ConfigurationProperties(prefix = "liquibase.calculation")
    public LiquibaseProperties calculationLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public DataSource calculationDataSource(@Qualifier("calculationLiquibaseProperties") LiquibaseProperties props) {
        return DataSourceBuilder.create()
                .url(props.getUrl())
                .username(props.getUser())
                .password(props.getPassword())
                .driverClassName(props.getDriverClassName())
                .build();
    }

    @Bean
    public SpringLiquibase calculationLiquibase(@Qualifier("calculationLiquibaseProperties") LiquibaseProperties props) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(props.getChangeLog());
        liquibase.setDataSource(calculationDataSource(props));
        liquibase.setShouldRun(true);
        return liquibase;
    }

    // ===========================
    // Payment DB
    // ===========================
    @Bean
    @ConfigurationProperties(prefix = "liquibase.payment")
    public LiquibaseProperties paymentLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public DataSource paymentDataSource(@Qualifier("paymentLiquibaseProperties") LiquibaseProperties props) {
        return DataSourceBuilder.create()
                .url(props.getUrl())
                .username(props.getUser())
                .password(props.getPassword())
                .driverClassName(props.getDriverClassName())
                .build();
    }

    @Bean
    public SpringLiquibase paymentLiquibase(@Qualifier("paymentLiquibaseProperties") LiquibaseProperties props) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(props.getChangeLog());
        liquibase.setDataSource(paymentDataSource(props));
        liquibase.setShouldRun(true);
        return liquibase;
    }
}
