ALTER TABLE students ADD CONSTRAINT age_constraint CHECK (age > 15);
ALTER TABLE students ADD CONSTRAINT name_unique UNIQUE (name);
ALTER TABLE students ALTER COLUMN name SET NOT NULL;
ALTER TABLE faculties ADD CONSTRAINt name_color_unique UNIQUE (name, color);
ALTER TABLE students ALTER COLUMN Age SET DEFAULT 20;