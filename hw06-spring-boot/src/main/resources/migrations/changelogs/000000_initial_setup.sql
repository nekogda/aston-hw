-- liquibase formatted sql

-- changeset @nekogda:1677516812042-1
create table if not exists USERS(
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  login TEXT not null,
  password TEXT not null,
  PRIMARY KEY ( id ),
  UNIQUE (login)
);