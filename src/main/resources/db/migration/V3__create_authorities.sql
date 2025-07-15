CREATE TABLE authorities (
    id  BIGSERIAL PRIMARY KEY,
    name  VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO authorities (name) VALUES ('read');