-- a)
CREATE TABLESPACE aebd_tables DATAFILE 'aebd_tables_01.dbf' SIZE 100m;

-- b)
CREATE TEMPORARY TABLESPACE aebd_temp TEMPFILE 'aebd_temp_01.dbf' SIZE 50m AUTOEXTEND ON;

-- c)
SELECT * FROM dba_tablespaces;

-- d)
CREATE USER aebd_user IDENTIFIED BY "aebd2020" DEFAULT TABLESPACE aebd_tables QUOTA UNLIMITED ON aebd_tables;

-- e)
GRANT CONNECT, RESOURCE, CREATE VIEW, CREATE SEQUENCE TO aebd_user;

-- f)
SELECT * FROM dba_users;