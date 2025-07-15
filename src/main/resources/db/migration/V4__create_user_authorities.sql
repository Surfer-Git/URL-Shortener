CREATE TABLE user_authorities (
    user_id     BIGINT NOT NULL,
    authority_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (authority_id) REFERENCES authorities(id) ON DELETE CASCADE
);

INSERT INTO user_authorities (user_id, authority_id) VALUES (2, 1);