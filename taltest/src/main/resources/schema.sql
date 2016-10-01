CREATE TABLE employee
(
  id   VARCHAR(128) PRIMARY KEY   NOT NULL,
  name VARCHAR(128)               NOT NULL
);

CREATE TABLE relation
(
  manager_id  VARCHAR(128) NOT NULL,
  employee_id VARCHAR(128) NOT NULL,
  PRIMARY KEY (manager_id, employee_id),
  FOREIGN KEY (manager_id) REFERENCES employee (id),
  FOREIGN KEY (employee_id) REFERENCES employee (id)
);

INSERT INTO employee (id, name) VALUES
  ('fry', 'Fry'),
  ('leela', 'Leela'),
  ('bender', 'Bender'),
  ('amy', 'Amy'),
  ('hermes', 'Hermes'),
  ('professor', 'Professor'),
  ('zoidberg', 'Zoidberg'),
  ('scruffy', 'Scruffy');

INSERT INTO relation (manager_id, employee_id) VALUES
  ('professor', 'fry'),
  ('professor', 'leela'),
  ('professor', 'bender'),
  ('professor', 'amy'),
  ('professor', 'hermes'),
  ('professor', 'zoidberg'),
  ('professor', 'scruffy'),
  ('leela', 'fry'),
  ('leela', 'bender');
