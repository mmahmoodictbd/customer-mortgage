CREATE TABLE mortgage
(
    id                  UUID PRIMARY KEY,
    mortgage_sum        DECIMAL      NOT NULL,
    start_date          DATE         NOT NULL,
    end_date            DATE         NOT NULL,
    interest_percentage DECIMAL      NOT NULL,
    mortgage_type       VARCHAR(255) NOT NULL,
    created_at          TIMESTAMP    NOT NULL,
    updated_at          TIMESTAMP    NOT NULL
);

CREATE TABLE customer
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(255)        NOT NULL,
    address    VARCHAR(255)        NOT NULL,
    account_id VARCHAR(255) UNIQUE NOT NULL,
    age        INTEGER             NOT NULL,
    created_at TIMESTAMP           NOT NULL,
    updated_at TIMESTAMP           NOT NULL
);

CREATE TABLE mortgage_customer
(
    mortgage_id UUID,
    customer_id UUID,
    PRIMARY KEY (mortgage_id, customer_id),
    FOREIGN KEY (mortgage_id) REFERENCES mortgage (id),
    FOREIGN KEY (customer_id) REFERENCES customer (id)
);
