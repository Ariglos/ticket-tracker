CREATE TABLE ticket
(
    id                 BIGINT       NOT NULL IDENTITY,
    signature          VARCHAR(255) NOT NULL UNIQUE,
    fine_amount        VARCHAR(255) NOT NULL,
    administration_fee VARCHAR(255) NOT NULL,
    currency           VARCHAR(255) NOT NULL,
    status             VARCHAR(255) NOT NULL,
    offence_date       DATE         NOT NULL,
    due_date           DATE         NOT NULL,
    custom_offence     VARCHAR(255),
    offence_id         BIGINT       NOT NULL,
    employee_id        BIGINT       NOT NULL,
    CONSTRAINT pk_ticket PRIMARY KEY (id)
);

CREATE TABLE offence
(
    id          BIGINT       NOT NULL IDENTITY,
    description VARCHAR(255) NOT NULL,
    CONSTRAINT pk_offence PRIMARY KEY (id)
)

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_on_offence FOREIGN KEY (offence_id) REFERENCES offence (id);

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_on_employee FOREIGN KEY (employee_id) REFERENCES employee (id);