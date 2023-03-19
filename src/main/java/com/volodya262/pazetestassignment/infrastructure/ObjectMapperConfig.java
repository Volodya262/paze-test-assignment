package com.volodya262.pazetestassignment.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonConfig() {
        return builder -> {
            builder.featuresToEnable(
                    MapperFeature.DEFAULT_VIEW_INCLUSION,
                    MapperFeature.AUTO_DETECT_IS_GETTERS,
                    MapperFeature.AUTO_DETECT_FIELDS,
                    MapperFeature.AUTO_DETECT_GETTERS,
                    DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE
            );
            builder.featuresToDisable(
                    SerializationFeature.INDENT_OUTPUT,
                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE
            );
            builder.modules(
                    new JodaMoneyModule(),
                    new JavaTimeModule(),
                    new Jdk8Module()
            );
        };
    }
}
