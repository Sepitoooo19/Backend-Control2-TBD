-- Sector 1: Centro de Roma (área alrededor del Coliseo)
INSERT INTO sectors (name, location) VALUES (
                                                'Roma Centro',
                                                ST_GeomFromText('POLYGON((12.47831 41.89462, 12.48300 41.89462, 12.48300 41.89800, 12.47831 41.89800, 12.47831 41.89462))', 4326)
                                            );

-- Sector 2: Trastevere (Roma)
INSERT INTO sectors (name, location) VALUES (
                                                'Trastevere',
                                                ST_GeomFromText('POLYGON((12.46500 41.88500, 12.47500 41.88500, 12.47500 41.89200, 12.46500 41.89200, 12.46500 41.88500))', 4326)
                                            );

-- Sector 3: Zona Norte de Milán
INSERT INTO sectors (name, location) VALUES (
                                                'Milán Norte',
                                                ST_GeomFromText('POLYGON((9.15000 45.48000, 9.18000 45.48000, 9.18000 45.50000, 9.15000 45.50000, 9.15000 45.48000))', 4326)
                                            );

-- Sector 4: Barrio de Montmartre (París)
INSERT INTO sectors (name, location) VALUES (
                                                'Montmartre',
                                                ST_GeomFromText('POLYGON((2.33000 48.88000, 2.34000 48.88000, 2.34000 48.89000, 2.33000 48.89000, 2.33000 48.88000))', 4326)
                                            );

-- Sector 5: La Boca (Buenos Aires)
INSERT INTO sectors (name, location) VALUES (
                                                'La Boca',
                                                ST_GeomFromText('POLYGON((-58.36500 -34.64000, -58.35500 -34.64000, -58.35500 -34.63000, -58.36500 -34.63000, -58.36500 -34.64000))', 4326)
                                            );

-- Tareas en Roma Centro (Sector 1)
INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
                                                                                           ('Reparación farola', 'Farola rota cerca del Coliseo', '2025-06-15 09:00:00', 'PENDING', 2, 1, ST_GeomFromText('POINT(12.48000 41.89600)', 4326)),
                                                                                           ('Limpieza plaza', 'Recoger basura en plaza central', '2025-06-10 08:00:00', 'PENDING', 2, 1, ST_GeomFromText('POINT(12.48150 41.89520)', 4326)),
                                                                                           ('Poda de árboles', 'Podar árboles en via dei Fori Imperiali', '2025-06-20 10:00:00', 'COMPLETED', 2, 1, ST_GeomFromText('POINT(12.47980 41.89580)', 4326));

-- Tareas en Trastevere (Sector 2)
INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
                                                                                           ('Reparación adoquines', 'Adoquines sueltos en via della Lungaretta', '2025-06-12 14:00:00', 'PENDING', 2, 2, ST_GeomFromText('POINT(12.47000 41.88900)', 4326)),
                                                                                           ('Pintura mural', 'Renovar pintura mural en plaza Santa Maria', '2025-07-01 09:00:00', 'PENDING', 2, 2, ST_GeomFromText('POINT(12.46950 41.88950)', 4326)),
                                                                                           ('Instalación bancos', 'Colocar nuevos bancos en plaza Trilussa', '2025-06-25 11:00:00', 'PENDING', 2, 2, ST_GeomFromText('POINT(12.46700 41.89000)', 4326));

-- Tareas en Milán Norte (Sector 3)
INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
                                                                                           ('Mantenimiento semáforos', 'Revisión semáforos en via Padova', '2025-06-18 08:30:00', 'PENDING', 2, 3, ST_GeomFromText('POINT(9.16000 45.49000)', 4326)),
                                                                                           ('Limpieza parque', 'Limpieza profunda del parque Sempione norte', '2025-06-22 07:00:00', 'COMPLETED', 2, 3, ST_GeomFromText('POINT(9.17000 45.49500)', 4326)),
                                                                                           ('Reparación bicicletero', 'Arreglar estación de bicicletas en via MacMahon', '2025-07-05 10:00:00', 'PENDING', 2, 3, ST_GeomFromText('POINT(9.17500 45.48500)', 4326));

-- Tareas en Montmartre (Sector 4)
INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
                                                                                           ('Limpieza escaleras', 'Limpiar escaleras de Sacré-Coeur', '2025-06-14 06:00:00', 'PENDING', 2, 4, ST_GeomFromText('POINT(2.33500 48.88600)', 4326)),
                                                                                           ('Reparación iluminación', 'Cambiar lámparas en Place du Tertre', '2025-06-30 16:00:00', 'PENDING', 2, 4, ST_GeomFromText('POINT(2.33800 48.88400)', 4326)),
                                                                                           ('Poda de arbustos', 'Podar arbustos en jardines de Montmartre', '2025-06-28 09:30:00', 'PENDING', 2, 4, ST_GeomFromText('POINT(2.33200 48.88200)', 4326));

-- Tareas en La Boca (Sector 5)
INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
                                                                                           ('Reparación puente', 'Refuerzo estructural puente Av. Pedro de Mendoza', '2025-07-10 08:00:00', 'PENDING', 2, 5, ST_GeomFromText('POINT(-58.36000 -34.63500)', 4326)),
                                                                                           ('Pintura fachadas', 'Renovar colores en Caminito', '2025-06-17 10:00:00', 'COMPLETED', 2, 5, ST_GeomFromText('POINT(-58.36300 -34.63600)', 4326)),
                                                                                           ('Limpieza ribera', 'Recoger residuos en la ribera', '2025-06-24 07:30:00', 'PENDING', 2, 5, ST_GeomFromText('POINT(-58.35800 -34.63800)', 4326));


-- Tareas en Roma Centro (Sector 1)
INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
                                                                                           ('Reparación farola', 'Farola rota cerca del Coliseo', '2025-06-15 09:00:00', 'PENDING', 2, 1, ST_GeomFromText('POINT(12.48000 41.89600)', 4326)),
                                                                                           ('Limpieza plaza', 'Recoger basura en plaza central', '2025-06-10 08:00:00', 'PENDING', 2, 1, ST_GeomFromText('POINT(12.48150 41.89520)', 4326)),
                                                                                           ('Poda de árboles', 'Podar árboles en via dei Fori Imperiali', '2025-06-20 10:00:00', 'COMPLETED', 2, 1, ST_GeomFromText('POINT(12.47980 41.89580)', 4326));

-- Tareas en Trastevere (Sector 2)
INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
                                                                                           ('Reparación adoquines', 'Adoquines sueltos en via della Lungaretta', '2025-06-12 14:00:00', 'PENDING', 2, 2, ST_GeomFromText('POINT(12.47000 41.88900)', 4326)),
                                                                                           ('Pintura mural', 'Renovar pintura mural en plaza Santa Maria', '2025-07-01 09:00:00', 'PENDING', 2, 2, ST_GeomFromText('POINT(12.46950 41.88950)', 4326)),
                                                                                           ('Instalación bancos', 'Colocar nuevos bancos en plaza Trilussa', '2025-06-25 11:00:00', 'PENDING', 2, 2, ST_GeomFromText('POINT(12.46700 41.89000)', 4326));

-- Tareas en Milán Norte (Sector 3)
INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
                                                                                           ('Mantenimiento semáforos', 'Revisión semáforos en via Padova', '2025-06-18 08:30:00', 'PENDING', 2, 3, ST_GeomFromText('POINT(9.16000 45.49000)', 4326)),
                                                                                           ('Limpieza parque', 'Limpieza profunda del parque Sempione norte', '2025-06-22 07:00:00', 'COMPLETED', 2, 3, ST_GeomFromText('POINT(9.17000 45.49500)', 4326)),
                                                                                           ('Reparación bicicletero', 'Arreglar estación de bicicletas en via MacMahon', '2025-07-05 10:00:00', 'PENDING', 2, 3, ST_GeomFromText('POINT(9.17500 45.48500)', 4326));

-- Tareas en Montmartre (Sector 4)
INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
                                                                                           ('Limpieza escaleras', 'Limpiar escaleras de Sacré-Coeur', '2025-06-14 06:00:00', 'PENDING', 2, 4, ST_GeomFromText('POINT(2.33500 48.88600)', 4326)),
                                                                                           ('Reparación iluminación', 'Cambiar lámparas en Place du Tertre', '2025-06-30 16:00:00', 'PENDING', 2, 4, ST_GeomFromText('POINT(2.33800 48.88400)', 4326)),
                                                                                           ('Poda de arbustos', 'Podar arbustos en jardines de Montmartre', '2025-06-28 09:30:00', 'PENDING', 2, 4, ST_GeomFromText('POINT(2.33200 48.88200)', 4326));

-- Tareas en La Boca (Sector 5)
INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
                                                                                           ('Reparación puente', 'Refuerzo estructural puente Av. Pedro de Mendoza', '2025-07-10 08:00:00', 'PENDING', 2, 5, ST_GeomFromText('POINT(-58.36000 -34.63500)', 4326)),
                                                                                           ('Pintura fachadas', 'Renovar colores en Caminito', '2025-06-17 10:00:00', 'COMPLETED', 2, 5, ST_GeomFromText('POINT(-58.36300 -34.63600)', 4326)),
                                                                                           ('Limpieza ribera', 'Recoger residuos en la ribera', '2025-06-24 07:30:00', 'PENDING', 2, 5, ST_GeomFromText('POINT(-58.35800 -34.63800)', 4326));