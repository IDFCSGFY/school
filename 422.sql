CREATE TABLE owners (
id SERIAL PRIMARY KEY,
name TEXT,
age INT,
driver_license BOOLEAN,
car_number VARCHAR(6) REFERENCES cars (number)
);
CREATE TABLE cars (
number VARCHAR(6) PRIMARY KEY,
brand TEXT,
model TEXT,
cost MONEY
);