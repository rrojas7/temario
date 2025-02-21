CREATE TABLE IF NOT EXISTS employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    register_date DATE
);

INSERT INTO employee (name,age,register_date) VALUES ('Juan Pérez',34,'2025-01-15');
INSERT INTO employee (name,age,register_date) VALUES ('María García',42,'2025-02-19');
INSERT INTO employee (name,age,register_date) VALUES ('Pedro Romero',25,'2024-11-20');
INSERT INTO employee (name,age,register_date) VALUES ('Luis Diaz',38,'2024-06-04');