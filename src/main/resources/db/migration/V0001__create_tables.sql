CREATE TABLE tb_event (
    event_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    local VARCHAR(255) NOT NULL,
    total_seats INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_ticket (
    ticket_id UUID PRIMARY KEY,
    event_id UUID NOT NULL,
    seat_row VARCHAR(50) NOT NULL,
    seat_number VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ticket_event FOREIGN KEY (event_id) REFERENCES tb_event(event_id),
    CONSTRAINT uk_ticket_seat UNIQUE (event_id, seat_row, seat_number)
);

CREATE TABLE tb_reservation (
    reservation_id UUID PRIMARY KEY,
    ticket_id UUID NOT NULL,
    user_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reservation_ticket FOREIGN KEY (ticket_id) REFERENCES tb_ticket(ticket_id)
);

-- Índices para performance
CREATE INDEX idx_ticket_event_status ON tb_ticket(event_id, status);
CREATE INDEX idx_reservation_status_expires ON tb_reservation(status, expires_at);
CREATE INDEX idx_reservation_ticket ON tb_reservation(ticket_id);