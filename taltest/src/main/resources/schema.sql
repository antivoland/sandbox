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
