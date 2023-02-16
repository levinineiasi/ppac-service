create table companies
(
    id           uuid not null primary key,
    display_name varchar(255),
    full_name    varchar(255),
    logo         bytea
);

create table students
(
    id         uuid not null primary key,
    company_id uuid,
    first_name varchar(255),
    last_name  varchar(255)
);
