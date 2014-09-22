DROP TABLE IF EXISTS person;

CREATE TABLE person (
  id character varying(256) NOT NULL,
  state integer NOT NULL DEFAULT 0,
  CONSTRAINT person_pkey PRIMARY KEY (id)
)
