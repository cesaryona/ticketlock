CREATE INDEX idx_reservation_expiration
ON tb_reservation(status, expires_at)
WHERE status = 'PENDING';