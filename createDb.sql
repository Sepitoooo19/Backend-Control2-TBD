CREATE EXTENSION IF NOT EXISTS postgis;
-- Creación de la tabla de usuarios
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,  -- Debe almacenarse hasheada (ej: BCrypt)
    role VARCHAR(10) NOT NULL CHECK (role IN ('ADMIN', 'USER')),
    name VARCHAR(100),
    location GEOGRAPHY(POINT, 4326)  -- Ubicación geográfica (lat/long)
);

-- Creación de la tabla de sectores
CREATE TABLE sectors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,  -- Ej: "Construcción", "Reparación de semáforos"
    location GEOGRAPHY(POINT, 4326)  -- Punto central del sector
);

-- Creación de la tabla de tareas
CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    due_date TIMESTAMP,  -- Fecha de vencimiento
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'COMPLETED')),
    user_id INTEGER REFERENCES users(id) ON DELETE SET NULL ON UPDATE CASCADE, -- Usuario asignado, considerar políticas ON DELETE/UPDATE
    sector_id INTEGER REFERENCES sectors(id) ON DELETE SET NULL ON UPDATE CASCADE, -- Sector asociado, considerar políticas ON DELETE/UPDATE
    location GEOGRAPHY(POINT, 4326),  -- Ubicación específica de la tarea
    created_at TIMESTAMP DEFAULT NOW()
);

