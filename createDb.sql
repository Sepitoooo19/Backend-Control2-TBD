CREATE EXTENSION IF NOT EXISTS postgis; -- Asegúrate de que PostGIS esté habilitado

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    name VARCHAR(100) NOT NULL,
    location GEOMETRY(POINT, 4326)      -- Ubicación geográfica del usuario.
);

---

CREATE TABLE sectors (
    id SERIAL PRIMARY KEY,          
    name VARCHAR(100) UNIQUE NOT NULL,
    location GEOMETRY(POINT, 4326) NOT NULL
);

---

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,               
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date TIMESTAMP,
    status INT NOT NULL DEFAULT 1,
    user_id INT NOT NULL,               
    sector_id INT NOT NULL,             
    location GEOMETRY(POINT, 4326),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Se establece automáticamente al crear la tarea.

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (sector_id) REFERENCES sectors(id) ON DELETE RESTRICT
);