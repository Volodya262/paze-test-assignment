CREATE TABLE payment_event
(
    payment_id   BIGSERIAL NOT NULL,
    event_number INTEGER   NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE,
    data         JSONB,

    CONSTRAINT pk_payment_events_event_number PRIMARY KEY (payment_id, event_number)
);

CREATE SEQUENCE payment_seq START WITH 1;