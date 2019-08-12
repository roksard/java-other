--DROP TABLE IF EXISTS organisation_child, organisation_employee, employee_child, organisations, 
--employees ;

--CREATE DATABASE postgres; 
--EXEC SQL CONNECT TO "postgres" USER postgres;

\connect postgres postgres;
 
CREATE TABLE IF NOT EXISTS public.organisations (
  id SERIAL,
  name VARCHAR(250) NULL,
  parentId INT NULL,
  PRIMARY KEY (id)
);
  
CREATE TABLE IF NOT EXISTS public.employees (
  id SERIAL,
  name VARCHAR(250) NULL,
  organisationId INT NULL,
  parentId INT NULL,
  PRIMARY KEY (id)
);
  
CREATE TABLE IF NOT EXISTS public.organisation_child (
  parent_id INT NOT NULL,
  child_id INT NOT NULL,
  PRIMARY KEY (parent_id, child_id),
  CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES organisations (id)  
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_child FOREIGN KEY (child_id) REFERENCES organisations (id)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.organisation_employee (
  organisation_id INT NOT NULL,
  employee_id INT NOT NULL,
  PRIMARY KEY (organisation_id, employee_id),
  CONSTRAINT fk_organisation FOREIGN KEY (organisation_id) REFERENCES organisations (id)  
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES employees (id)
  	ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.employee_child (
  parent_id INT NOT NULL,
  child_id INT NOT NULL,
  PRIMARY KEY (parent_id, child_id),
  CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES employees (id)  
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_child FOREIGN KEY (child_id) REFERENCES employees (id)
    ON UPDATE CASCADE ON DELETE CASCADE
);