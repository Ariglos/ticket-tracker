CREATE TABLE attachment
(
    id        BIGINT        NOT NULL IDENTITY,
    file_name VARCHAR(255)  NOT NULL,
    data      NVARCHAR(max) NOT NULL,
    CONSTRAINT pk_attachment PRIMARY KEY (id)
);

ALTER TABLE ticket
    ADD attachment_id BIGINT;

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_on_attachment FOREIGN KEY (attachment_id) REFERENCES attachment (id);