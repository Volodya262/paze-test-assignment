package com.volodya262.pazetestassignment.application.services;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentIdGenerator {

    public PaymentIdGenerator(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public long getNextId() {
        return jdbcTemplate.queryForObject("SELECT nextval('payment_seq'::REGCLASS)", new MapSqlParameterSource(), Long.class);
    }
}
