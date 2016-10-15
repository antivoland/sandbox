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
