CREATE TABLE "file"
(
    id   SERIAL PRIMARY KEY,
    path VARCHAR(255),
    url  VARCHAR(255)
);

CREATE TABLE "role"
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE "account"
(
    id          SERIAL PRIMARY KEY,
    first_name  VARCHAR(25),
    last_name   VARCHAR(50),
    second_name VARCHAR(25),
    email       VARCHAR(10),
    username    VARCHAR(20),
    password    VARCHAR(15),
    rating      INT,
    created_at  TIMESTAMP,
    enabled     BOOLEAN,
    role_id     INT REFERENCES "role" (id),
    avatar_id   INT REFERENCES "file" (id)
);

CREATE TABLE "account_address"
(
    id             SERIAL PRIMARY KEY,
    id_account     INT REFERENCES "account" (id),
    addr_index     VARCHAR(6),
    addr_city      VARCHAR(15),
    addr_street    VARCHAR(25),
    addr_house     VARCHAR(5),
    addr_structure VARCHAR(10),
    addr_apart     VARCHAR(3),
    is_default     BOOLEAN
);

CREATE TABLE "confirm_token"
(
    id         SERIAL PRIMARY KEY,
    id_account INT REFERENCES "account" (id),
    expire_at  TIMESTAMP,
    token      UUID
);
