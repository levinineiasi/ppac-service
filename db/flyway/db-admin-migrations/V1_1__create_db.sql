CREATE TABLE IF NOT EXISTS CODES
(
    ID        UUID PRIMARY KEY,
    VALUE     INTEGER UNIQUE CHECK (VALUE BETWEEN 100000 AND 999999),
    CODE_TYPE VARCHAR(12) CHECK (CODE_TYPE IN ('ADMIN_CODE', 'COMPANY_CODE'))
);

CREATE TABLE IF NOT EXISTS COMPANIES
(
    ID          UUID PRIMARY KEY,
    NAME        VARCHAR(150) NOT NULL UNIQUE CHECK (LENGTH(NAME) >= 2),
    DESCRIPTION VARCHAR(3000) DEFAULT NULL UNIQUE CHECK (LENGTH(DESCRIPTION) >=  20),
    EMAIL       VARCHAR(50)  DEFAULT NULL UNIQUE CHECK (LENGTH(EMAIL) >= 5),
    LOGO        BYTEA        DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS COMPANIES_CODES
(
    ID         UUID PRIMARY KEY,
    CODE_ID    UUID NOT NULL UNIQUE,
    COMPANY_ID UUID NOT NULL UNIQUE,
    FOREIGN KEY (CODE_ID) REFERENCES CODES (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (COMPANY_ID) REFERENCES COMPANIES (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS OPENINGS
(
    ID                            UUID PRIMARY KEY,
    HAS_TECHNICAL_INTERVIEW       BOOLEAN NOT NULL,
    HAS_TECHNICAL_TEST            BOOLEAN NOT NULL,
    PERIOD_COUNT                  INTEGER NOT NULL CHECK (PERIOD_COUNT >= 1 AND PERIOD_COUNT <=50),
    PERIOD_TYPE                   VARCHAR(12) CHECK (PERIOD_TYPE IN ('WEEKS', 'MONTHS')),
    OPEN_POSITIONS                INTEGER NOT NULL CHECK (OPEN_POSITIONS >= 1 AND OPEN_POSITIONS <=50),
    ACCEPT_ON_CLOSING_OPPORTUNITY BOOLEAN NOT NULL,
    SIGN_AGREEMENT                BOOLEAN NOT NULL,
    AVAILABLE                     BOOLEAN NOT NULL,
    TITLE                         VARCHAR(150) CHECK (LENGTH(TITLE) >= 5),
    DESCRIPTION                   VARCHAR(3000) CHECK (LENGTH(DESCRIPTION) >= 20),
    REQUIREMENTS                  VARCHAR(3000),
    RESTRICTIONS                  VARCHAR(3000),
    RECRUITMENT_PROCESS           VARCHAR(3000),
    START_DATE                    DATE,
    VIEWS                         INTEGER NOT NULL CHECK (VIEWS >= 0)
);

CREATE TABLE IF NOT EXISTS TRAINERS
(
    ID           UUID PRIMARY KEY,
    NAME         VARCHAR(50)  NOT NULL CHECK (LENGTH(NAME) >= 2),
    ROLE         VARCHAR(150) NOT NULL,
    LINKEDIN_URL VARCHAR(100) DEFAULT NULL  CHECK (LENGTH(LINKEDIN_URL) >= 15),
    AVATAR       BYTEA DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS OPENINGS_TRAINERS
(
    OPENING_ENTITY_ID UUID NOT NULL,
    TRAINERS_ID       UUID NOT NULL,
    FOREIGN KEY (OPENING_ENTITY_ID) REFERENCES OPENINGS (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (TRAINERS_ID) REFERENCES TRAINERS (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (OPENING_ENTITY_ID, TRAINERS_ID)
);

CREATE TABLE IF NOT EXISTS OPENING_ENTITY_KEY_WORDS
(
    OPENING_ENTITY_ID UUID NOT NULL,
    KEY_WORDS         VARCHAR(255) DEFAULT NULL,
    FOREIGN KEY (OPENING_ENTITY_ID) REFERENCES OPENINGS (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (OPENING_ENTITY_ID, KEY_WORDS)
);

CREATE TABLE IF NOT EXISTS OPENING_ENTITY_CUSTOM_KEY_WORDS
(
    OPENING_ENTITY_ID UUID NOT NULL,
    CUSTOM_KEY_WORDS  varchar(255) DEFAULT NULL,
    FOREIGN KEY (OPENING_ENTITY_ID) REFERENCES OPENINGS (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (OPENING_ENTITY_ID, CUSTOM_KEY_WORDS)
);

CREATE TABLE IF NOT EXISTS COMPANIES_OPENINGS
(
    COMPANY_ENTITY_ID UUID NOT NULL,
    OPENINGS_ID       UUID NOT NULL,
    FOREIGN KEY (COMPANY_ENTITY_ID) REFERENCES COMPANIES (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (OPENINGS_ID) REFERENCES OPENINGS (ID) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (COMPANY_ENTITY_ID, OPENINGS_ID)
);
