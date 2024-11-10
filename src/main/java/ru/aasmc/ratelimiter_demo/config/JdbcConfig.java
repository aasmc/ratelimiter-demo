package ru.aasmc.ratelimiter_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import ru.aasmc.ratelimiter_demo.storage.converter.StringToTimestampRangeConverter;
import ru.aasmc.ratelimiter_demo.storage.converter.TimestampRangeToStringConverter;

import java.util.Arrays;

@Configuration
public class JdbcConfig {

    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(
                Arrays.asList(
                        new StringToTimestampRangeConverter(),
                        new TimestampRangeToStringConverter()
                )
        );
    }

}
