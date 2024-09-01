CREATE TABLE employee (
                          id BIGINT NOT NULL IDENTITY,
                          name VARCHAR(255) NOT NULL,
                          surname VARCHAR(255) NOT NULL,
                          phone_no VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL,
                          status VARCHAR(255) NOT NULL,
                          company_id BIGINT NOT NULL,
                          CONSTRAINT pk_employee PRIMARY KEY (id)
);

CREATE TABLE company (
                         id BIGINT NOT NULL IDENTITY,
                         name VARCHAR(255) NOT NULL,
                         CONSTRAINT pk_company PRIMARY KEY (id)
);

ALTER TABLE employee
    ADD CONSTRAINT  fk_employee_on_company FOREIGN KEY (company_id) REFERENCES company (id);