CREATE TABLE CODES
(
    ID        UUID     PRIMARY KEY,
    VALUE     INTEGER  UNIQUE,
    CODE_TYPE VARCHAR(12)
);

CREATE TABLE COMPANIES
(
    ID           UUID  PRIMARY KEY,
    DISPLAY_NAME VARCHAR(30) NOT NULL,
    FULL_NAME    VARCHAR(50) DEFAULT NULL,
    LOGO         BYTEA DEFAULT NULL
);

CREATE TABLE COMPANIES_CODES
(
    ID           UUID  PRIMARY KEY,
    CODE_ID  UUID  NOT NULL UNIQUE,
    COMPANY_ID UUID NOT NULL UNIQUE
);