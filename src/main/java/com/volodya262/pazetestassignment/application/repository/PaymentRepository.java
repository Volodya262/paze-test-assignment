package com.volodya262.pazetestassignment.application.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volodya262.pazetestassignment.domain.Payment;
import com.volodya262.pazetestassignment.domain.paymentevent.PaymentEvent;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class PaymentRepository {

    public PaymentRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            ObjectMapper objectMapper,
            TransactionTemplate transactionTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.transactionTemplate = transactionTemplate;
    }

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final TransactionTemplate transactionTemplate;

    public Optional<Payment> getPaymentById(long paymentId) {
        //language=PostgreSQL
        var query = """
                    SELECT
                        payment_id,
                        event_number,
                        data
                    FROM
                        payment_event
                    WHERE
                        payment_id = :payment_id
                """;

        var params = new MapSqlParameterSource()
                .addValue("payment_id", paymentId);

        var events = jdbcTemplate.query(query, params, (rs, __) -> {
            var json = rs.getString("data");
            return readValue(json, PaymentEvent.class);
        });

        return Optional.of(Payment.fromEvents(paymentId, events));
    }

    public void save(Payment payment) {
        final var query = """
                    INSERT INTO payment_event (payment_id, event_number, created_at, data)
                    VALUES (:payment_id, :event_number, :created_at, :data::JSONB)
                """;

        final MapSqlParameterSource[] params = payment.getEventsToStore()
                .stream()
                .map(event -> new MapSqlParameterSource()
                        .addValue("payment_id", payment.getPaymentId())
                        .addValue("event_number", event.getEventNumber())
                        .addValue("created_at", event.getCreatedAt())
                        .addValue("data", writeValueAsString(event))
                )
                .toArray(MapSqlParameterSource[]::new);

        transactionTemplate.execute((__) -> jdbcTemplate.batchUpdate(query, params));
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM payment_event", new MapSqlParameterSource());
    }

    private <T> T readValue(String json, Class<T> klass) {
        try {
            return objectMapper.readValue(json, klass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> String writeValueAsString(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
