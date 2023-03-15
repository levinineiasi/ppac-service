CREATE TABLE IF NOT EXISTS OPENINGS (
    ID UUID PRIMARY KEY,
    HAS_TECHNICAL_INTERVIEW BOOLEAN NOT NULL,
    HAS_TECHNICAL_TEST BOOLEAN NOT NULL,
    PERIOD_COUNT INTEGER NOT NULL,
    PERIOD_TYPE VARCHAR(12) CHECK (PERIOD_TYPE IN ('WEEKS','MONTHS')),
    OPEN_POSITIONS INTEGER NOT NULL,
    ACCEPT_ON_CLOSING_OPPORTUNITY BOOLEAN NOT NULL,
    SIGN_AGREEMENT BOOLEAN NOT NULL,
    AVAILABLE BOOLEAN NOT NULL,
    TITLE VARCHAR(50),
    DESCRIPTION VARCHAR(255),
    REQUIREMENTS VARCHAR(255),
    RESTRICTIONS VARCHAR(255),
    RECRUITMENT_PROCESS VARCHAR(255),
    START_DATE VARCHAR(255)
);

CREATE TABLE  IF NOT EXISTS TRAINERS (
                          ID UUID PRIMARY KEY,
                          NAME VARCHAR(50) NOT NULL,
                          DESCRIPTION VARCHAR(255) NOT NULL,
                          LINKEDIN_URL VARCHAR(255),
                          AVATAR VARCHAR(255)
);

CREATE TABLE  IF NOT EXISTS OPENINGS_TRAINERS (
    ID UUID PRIMARY KEY,
    OPENINGS_ID  UUID  NOT NULL UNIQUE,
    TRAINERS_ID UUID NOT NULL UNIQUE,
    FOREIGN KEY (OPENINGS_ID) REFERENCES OPENINGS(ID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (TRAINERS_ID) REFERENCES TRAINERS(ID) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS KEYWORDS (
    ID UUID PRIMARY KEY,
    KEYWORDS varchar(255) DEFAULT NULL,
    FOREIGN KEY (ID) REFERENCES OPENINGS(ID)
);

CREATE TABLE IF NOT EXISTS CUSTOM_KEYWORDS (
    ID UUID PRIMARY KEY,
    CUSTOM_KEYWORDS varchar(255) DEFAULT NULL,
    FOREIGN KEY (ID) REFERENCES OPENINGS(ID)
    );