create database neo4jdemo;
begin;
CREATE USER 'neo4jdemo' IDENTIFIED BY 'neo4jdemo';
GRANT ALL PRIVILEGES ON neo4jdemo.* TO 'neo4jdemo';
commit;