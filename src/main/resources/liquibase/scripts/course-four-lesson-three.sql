--liquibase formatted sql

--changeSet abcde:1
CREATE INDEX students_name_idx ON students(name);

--changeSet abcde:2
CREATE INDEX faculty_name_color_idx ON faculties(name, color);