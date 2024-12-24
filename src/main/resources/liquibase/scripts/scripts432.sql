-- liquibase formatted sql

-- changeset artems:1
CREATE INDEX name_index ON student(name);
-- changeset artems:2
CREATE INDEX color_index ON faculty(name, color);