-- Insertar usuarios (8 en total) PRIMERO ESTO, LUEGO CREAR 2 USUARIOS MANUALES, PRIMERO UNO CON ADMIN
-- Y LUEGO OTRO CON USER
INSERT INTO users (username, password, role, name, location) VALUES
-- Admin
('admin1', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MQDq5phQ5SoYZK2PlK/.9.2LQfW5KQO', 'ADMIN', 'Administrador Principal', ST_GeographyFromText('POINT(-70.64827 -33.45694)')),  -- Santiago centro
-- Users
('user1', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MQDq5phQ5SoYZK2PlK/.9.2LQfW5KQO', 'USER', 'Juan Pérez', ST_GeographyFromText('POINT(-70.669265 -33.448889)')),  -- Providencia
('user2', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MQDq5phQ5SoYZK2PlK/.9.2LQfW5KQO', 'USER', 'María González', ST_GeographyFromText('POINT(-70.575 -33.522)')),  -- La Florida
('user3', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MQDq5phQ5SoYZK2PlK/.9.2LQfW5KQO', 'USER', 'Carlos López', ST_GeographyFromText('POINT(-70.724 -33.383)')),  -- Quilicura
('user4', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MQDq5phQ5SoYZK2PlK/.9.2LQfW5KQO', 'USER', 'Ana Martínez', ST_GeographyFromText('POINT(-70.707 -33.492)')),  -- Ñuñoa
('user5', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MQDq5phQ5SoYZK2PlK/.9.2LQfW5KQO', 'USER', 'Pedro Sánchez', ST_GeographyFromText('POINT(-70.662 -33.537)')),  -- San Miguel
('user6', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MQDq5phQ5SoYZK2PlK/.9.2LQfW5KQO', 'USER', 'Laura Ramírez', ST_GeographyFromText('POINT(-70.678 -33.415)')),  -- Recoleta
('user7', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MQDq5phQ5SoYZK2PlK/.9.2LQfW5KQO', 'USER', 'Diego Silva', ST_GeographyFromText('POINT(-70.611 -33.601)'));  -- Puente Alto


-- Insertar sectores con polígonos que representan áreas en Santiago de Chile
INSERT INTO sectors (name, location) VALUES
-- Sector 1: Centro de Santiago (polígono aproximado)
('Centro', ST_GeographyFromText('POLYGON((-70.660 -33.430, -70.640 -33.430, -70.640 -33.450, -70.660 -33.450, -70.660 -33.430))')),
-- Sector 2: Providencia
('Providencia', ST_GeographyFromText('POLYGON((-70.620 -33.420, -70.600 -33.420, -70.600 -33.450, -70.620 -33.450, -70.620 -33.420))')),
-- Sector 3: Ñuñoa
('Ñuñoa', ST_GeographyFromText('POLYGON((-70.580 -33.440, -70.560 -33.440, -70.560 -33.470, -70.580 -33.470, -70.580 -33.440))')),
-- Sector 4: Las Condes
('Las Condes', ST_GeographyFromText('POLYGON((-70.540 -33.400, -70.520 -33.400, -70.520 -33.430, -70.540 -33.430, -70.540 -33.400))')),
-- Sector 5: Maipú
('Maipú', ST_GeographyFromText('POLYGON((-70.760 -33.500, -70.740 -33.500, -70.740 -33.520, -70.760 -33.520, -70.760 -33.500))')),
-- Sector 6: La Florida
('La Florida', ST_GeographyFromText('POLYGON((-70.580 -33.500, -70.560 -33.500, -70.560 -33.530, -70.580 -33.530, -70.580 -33.500))')),
-- Sector 7: Puente Alto
('Puente Alto', ST_GeographyFromText('POLYGON((-70.580 -33.600, -70.560 -33.600, -70.560 -33.620, -70.580 -33.620, -70.580 -33.600))')),
-- Sector 8: Quilicura
('Quilicura', ST_GeographyFromText('POLYGON((-70.740 -33.370, -70.720 -33.370, -70.720 -33.390, -70.740 -33.390, -70.740 -33.370))'));

-- Tareas para usuario creado manualmente:

INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
-- Sector Centro
('Revisión infraestructura', 'Revisar estado de edificios municipales en el centro', '2023-12-15 09:00:00', 'PENDING', 10, 1, ST_GeographyFromText('POINT(-70.650 -33.437)')),
('Coordinación limpieza', 'Organizar equipo de limpieza para plaza principal', '2023-11-30 14:00:00', 'PENDING', 10, 1, ST_GeographyFromText('POINT(-70.655 -33.440)')),
('Inspección semáforos', 'Verificar funcionamiento de semáforos en calle principal', '2023-12-05 10:30:00', 'COMPLETED', 10, 1, ST_GeographyFromText('POINT(-70.645 -33.442)')),
('Reunión con comerciantes', 'Coordinar horarios de atención con comerciantes locales', '2023-12-10 11:00:00', 'PENDING', 10, 1, ST_GeographyFromText('POINT(-70.652 -33.438)')),
('Plan seguridad', 'Elaborar plan de seguridad para fiestas patrias', '2023-12-20 16:00:00', 'PENDING', 10, 1, ST_GeographyFromText('POINT(-70.648 -33.436)')),
-- Sector Providencia
('Evaluación parques', 'Evaluar estado de áreas verdes en Providencia', '2023-12-03 09:30:00', 'COMPLETED', 10, 2, ST_GeographyFromText('POINT(-70.610 -33.432)')),
('Coordinación transporte', 'Organizar rutas de transporte público', '2023-12-18 15:00:00', 'PENDING', 10, 2, ST_GeographyFromText('POINT(-70.615 -33.428)')),
('Revisión alumbrado', 'Verificar funcionamiento de alumbrado público', '2023-12-22 19:00:00', 'PENDING', 10, 2, ST_GeographyFromText('POINT(-70.618 -33.435)')),
('Encuesta satisfacción', 'Realizar encuesta a vecinos sobre servicios municipales', '2023-12-08 10:00:00', 'PENDING', 10, 2, ST_GeographyFromText('POINT(-70.612 -33.440)')),
('Mantenimiento caminos', 'Programar mantenimiento de calles secundarias', '2023-12-25 08:00:00', 'PENDING', 10, 2, ST_GeographyFromText('POINT(-70.605 -33.425)')),
-- Sector Ñuñoa
('Evento cultural', 'Organizar evento cultural en plaza Ñuñoa', '2023-12-12 18:00:00', 'PENDING', 10, 3, ST_GeographyFromText('POINT(-70.570 -33.450)')),
('Reparación bancas', 'Coordinar reparación de bancas en parques', '2023-12-07 11:30:00', 'COMPLETED', 10, 3, ST_GeographyFromText('POINT(-70.575 -33.455)')),
('Control de ruido', 'Realizar mediciones de ruido en zona residencial', '2023-12-14 21:00:00', 'PENDING', 10, 3, ST_GeographyFromText('POINT(-70.565 -33.460)')),
('Taller reciclaje', 'Organizar taller de reciclaje para vecinos', '2023-12-09 16:30:00', 'PENDING', 10, 3, ST_GeographyFromText('POINT(-70.578 -33.448)')),
('Repavimentación', 'Supervisar trabajos de repavimentación', '2023-12-28 07:00:00', 'PENDING', 10, 3, ST_GeographyFromText('POINT(-70.572 -33.452)')),
-- Sector Las Condes
('Inspección construcción', 'Inspeccionar obra nueva en sector alto', '2023-12-16 10:00:00', 'PENDING', 10, 4, ST_GeographyFromText('POINT(-70.530 -33.410)')),
('Seguridad vial', 'Implementar medidas de seguridad vial cerca de colegios', '2023-12-04 08:30:00', 'COMPLETED', 10, 4, ST_GeographyFromText('POINT(-70.535 -33.415)')),
('Mantenimiento áreas verdes', 'Programar poda de árboles en calles principales', '2023-12-19 09:00:00', 'PENDING', 10, 4, ST_GeographyFromText('POINT(-70.525 -33.420)')),
('Fiscalización comercio', 'Realizar fiscalización a locales comerciales', '2023-12-11 12:00:00', 'PENDING', 10, 4, ST_GeographyFromText('POINT(-70.538 -33.412)')),
('Plan emergencia', 'Actualizar plan de emergencia para el sector', '2023-12-29 15:30:00', 'PENDING', 10, 4, ST_GeographyFromText('POINT(-70.532 -33.418)'));

-- Tareas para user1 (USER)
INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
-- Sector Centro
('Reparación bache', 'Reparar bache en calle Merced', '2023-12-02 08:00:00', 'COMPLETED', 10, 1, ST_GeographyFromText('POINT(-70.653 -33.439)')),
('Limpieza alcantarilla', 'Limpiar alcantarilla en esquina Monjitas', '2023-12-06 09:30:00', 'PENDING', 10, 1, ST_GeographyFromText('POINT(-70.647 -33.441)')),
('Pintura señalética', 'Repintar señalética peatonal', '2023-12-13 14:00:00', 'PENDING', 10, 1, ST_GeographyFromText('POINT(-70.649 -33.438)')),
('Reposición luminaria', 'Cambiar luminaria dañada en plaza', '2023-12-17 16:30:00', 'PENDING',10, 1, ST_GeographyFromText('POINT(-70.651 -33.436)')),
('Recolección escombros', 'Retirar escombros de obra en calle lateral', '2023-12-21 07:30:00', 'PENDING', 10, 1, ST_GeographyFromText('POINT(-70.655 -33.435)')),
-- Sector Providencia
('Mantenimiento parque', 'Podar pasto y regar áreas verdes en parque', '2023-12-01 07:00:00', 'COMPLETED', 10, 2, ST_GeographyFromText('POINT(-70.614 -33.430)')),
('Reparación vereda', 'Arreglar vereda dañada por raíces', '2023-12-08 08:30:00', 'PENDING', 10, 2, ST_GeographyFromText('POINT(-70.616 -33.432)')),
('Control plagas', 'Aplicar tratamiento contra plagas en árboles', '2023-12-15 10:00:00', 'PENDING', 10, 2, ST_GeographyFromText('POINT(-70.612 -33.428)')),
('Instalación basureros', 'Colocar nuevos basureros en calle principal', '2023-12-19 11:30:00', 'PENDING', 10, 2, ST_GeographyFromText('POINT(-70.610 -33.435)')),
('Limpieza grafitis', 'Limpiar grafitis en muro municipal', '2023-12-23 13:00:00', 'PENDING', 10, 2, ST_GeographyFromText('POINT(-70.608 -33.433)')),
-- Sector Ñuñoa
('Reparación riego', 'Arreglar sistema de riego automático', '2023-12-03 09:00:00', 'COMPLETED', 10, 3, ST_GeographyFromText('POINT(-70.572 -33.450)')),
('Poda árboles', 'Podar árboles que obstruyen alumbrado', '2023-12-10 08:00:00', 'PENDING', 10, 3, ST_GeographyFromText('POINT(-70.575 -33.452)')),
('Limpieza canaletas', 'Limpiar canaletas de aguas lluvia', '2023-12-14 10:30:00', 'PENDING', 10, 3, ST_GeographyFromText('POINT(-70.570 -33.455)')),
('Reparación juegos', 'Arreglar juegos infantiles en plaza', '2023-12-18 12:00:00', 'PENDING', 10, 3, ST_GeographyFromText('POINT(-70.568 -33.448)')),
('Control maleza', 'Eliminar maleza en áreas públicas', '2023-12-22 14:30:00', 'PENDING', 10, 10, ST_GeographyFromText('POINT(-70.573 -33.453)')),
-- Sector Las Condes
('Mantenimiento piscina', 'Limpiar y revisar piscina municipal', '2023-12-05 07:30:00', 'COMPLETED', 2, 4, ST_GeographyFromText('POINT(-70.532 -33.415)')),
('Reparación cerco', 'Arreglar cerco perimetral del estadio', '2023-12-12 09:00:00', 'PENDING', 2, 4, ST_GeographyFromText('POINT(-70.535 -33.418)')),
('Limpieza gimnasio', 'Limpiar y desinfectar gimnasio municipal', '2023-12-16 11:30:00', 'PENDING', 2, 4, ST_GeographyFromText('POINT(-70.530 -33.412)')),
('Instalación cartel', 'Colocar cartel informativo nuevo', '2023-12-20 13:00:00', 'PENDING', 2, 4, ST_GeographyFromText('POINT(-70.533 -33.420)')),
('Mantenimiento canchas', 'Marcar líneas y reparar redes de canchas', '2023-12-24 15:30:00', 'PENDING', 2, 4, ST_GeographyFromText('POINT(-70.528 -33.416)'));


-- Tareas para los demás usuarios:

INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
-- Sector La Florida (6)
('Reparación juegos plaza', 'Reparar columpios en plaza principal', '2023-12-03 08:00:00', 'COMPLETED', 3, 6, ST_GeographyFromText('POINT(-70.575 -33.515)')),
('Limpieza canaletas', 'Limpiar canaletas en Av. Vicuña Mackenna', '2023-12-07 09:30:00', 'PENDING', 3, 6, ST_GeographyFromText('POINT(-70.572 -33.520)')),
('Poda de árboles', 'Podar árboles que obstruyen veredas', '2023-12-11 10:00:00', 'PENDING', 3, 6, ST_GeographyFromText('POINT(-70.578 -33.518)')),
('Reparación bache', 'Reparar bache en calle San Francisco', '2023-12-15 13:00:00', 'PENDING', 3, 6, ST_GeographyFromText('POINT(-70.570 -33.522)')),
('Instalación basureros', 'Instalar nuevos basureros en plaza', '2023-12-19 14:30:00', 'PENDING', 3, 6, ST_GeographyFromText('POINT(-70.573 -33.517)')),
('Mantenimiento áreas verdes', 'Regar y fertilizar jardines municipales', '2023-12-23 07:30:00', 'PENDING', 3, 6, ST_GeographyFromText('POINT(-70.576 -33.520)')),
('Limpieza grafitis', 'Limpiar grafitis en muro municipal', '2023-12-27 11:00:00', 'PENDING', 3, 6, ST_GeographyFromText('POINT(-70.571 -33.519)')),
('Reparación luminarias', 'Cambiar luminarias dañadas en calle principal', '2023-12-31 16:00:00', 'PENDING', 3, 6, ST_GeographyFromText('POINT(-70.574 -33.521)')),
('Control de maleza', 'Eliminar maleza en espacios públicos', '2024-01-04 09:00:00', 'PENDING', 3, 6, ST_GeographyFromText('POINT(-70.577 -33.516)')),
('Limpieza post evento', 'Limpiar plaza después de evento cultural', '2024-01-08 15:00:00', 'PENDING', 3, 6, ST_GeographyFromText('POINT(-70.569 -33.518)')),
-- Sector Puente Alto (7)
('Reparación vereda', 'Arreglar vereda dañada en Av. Concha y Toro', '2023-12-05 08:30:00', 'COMPLETED', 3, 7, ST_GeographyFromText('POINT(-70.570 -33.610)')),
('Mantenimiento parque', 'Podar pasto en parque municipal', '2023-12-09 10:00:00', 'PENDING', 3, 7, ST_GeographyFromText('POINT(-70.575 -33.615)')),
('Instalación señalética', 'Colocar nueva señalética vial', '2023-12-13 11:30:00', 'PENDING', 3, 7, ST_GeographyFromText('POINT(-70.572 -33.612)')),
('Limpieza alcantarillas', 'Limpiar alcantarillas en sector céntrico', '2023-12-17 13:00:00', 'PENDING', 3, 7, ST_GeographyFromText('POINT(-70.578 -33.618)')),
('Reparación cerco', 'Arreglar cerco de escuela municipal', '2023-12-21 14:30:00', 'PENDING', 3, 7, ST_GeographyFromText('POINT(-70.573 -33.616)')),
('Control plagas', 'Aplicar tratamiento contra plagas en árboles', '2023-12-25 08:00:00', 'PENDING', 3, 7, ST_GeographyFromText('POINT(-70.576 -33.613)')),
('Pintura mural', 'Pintar mural en centro comunitario', '2023-12-29 10:30:00', 'PENDING', 3, 7, ST_GeographyFromText('POINT(-70.571 -33.614)')),
('Mantenimiento canchas', 'Reparar redes de canchas de fútbol', '2024-01-02 12:00:00', 'PENDING', 3, 7, ST_GeographyFromText('POINT(-70.574 -33.617)')),
('Limpieza quebrada', 'Limpiar basura en quebrada del sector', '2024-01-06 14:00:00', 'PENDING', 3, 7, ST_GeographyFromText('POINT(-70.577 -33.611)')),
('Instalación riego', 'Instalar sistema de riego en plaza', '2024-01-10 16:30:00', 'PENDING', 3, 7, ST_GeographyFromText('POINT(-70.572 -33.615)'));

INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
-- Sector Quilicura (8)
('Reparación bache industrial', 'Reparar bache en calle industrial', '2023-12-04 07:00:00', 'COMPLETED', 4, 8, ST_GeographyFromText('POINT(-70.730 -33.375)')),
('Limpieza zona franca', 'Limpiar área común de zona franca', '2023-12-08 08:30:00', 'PENDING', 4, 8, ST_GeographyFromText('POINT(-70.735 -33.378)')),
('Mantenimiento alumbrado', 'Reparar alumbrado público industrial', '2023-12-12 10:00:00', 'PENDING', 4, 8, ST_GeographyFromText('POINT(-70.728 -33.380)')),
('Poda árboles avenida', 'Podar árboles en avenida principal', '2023-12-16 11:30:00', 'PENDING', 4, 8, ST_GeographyFromText('POINT(-70.732 -33.377)')),
('Reparación vereda escolar', 'Arreglar vereda frente a escuela', '2023-12-20 13:00:00', 'PENDING', 4, 8, ST_GeographyFromText('POINT(-70.726 -33.382)')),
('Instalación basureros', 'Colocar basureros en plaza cívica', '2023-12-24 14:30:00', 'PENDING', 4, 8, ST_GeographyFromText('POINT(-70.734 -33.379)')),
('Limpieza post feria', 'Limpiar después de feria libre', '2023-12-28 07:30:00', 'PENDING', 4, 8, ST_GeographyFromText('POINT(-70.729 -33.376)')),
('Mantenimiento parque', 'Regar y fertilizar parque municipal', '2024-01-01 09:00:00', 'PENDING', 4, 8, ST_GeographyFromText('POINT(-70.731 -33.381)')),
('Reparación semáforo', 'Arreglar semáforo en intersección', '2024-01-05 10:30:00', 'PENDING', 4, 8, ST_GeographyFromText('POINT(-70.733 -33.378)')),
('Control maleza', 'Eliminar maleza en espacios públicos', '2024-01-09 12:00:00', 'PENDING', 4, 8, ST_GeographyFromText('POINT(-70.727 -33.379)')),
-- Sector Maipú (5)
('Reparación plaza', 'Arreglar bancas en plaza de Maipú', '2023-12-06 08:00:00', 'COMPLETED', 4, 5, ST_GeographyFromText('POINT(-70.750 -33.510)')),
('Limpieza canal', 'Limpiar canal de regadío', '2023-12-10 09:30:00', 'PENDING', 4, 5, ST_GeographyFromText('POINT(-70.755 -33.515)')),
('Mantenimiento centro', 'Reparar veredas en centro de Maipú', '2023-12-14 11:00:00', 'PENDING', 4, 5, ST_GeographyFromText('POINT(-70.748 -33.512)')),
('Poda árboles históricos', 'Podar árboles en plaza histórica', '2023-12-18 12:30:00', 'PENDING', 4, 5, ST_GeographyFromText('POINT(-70.752 -33.511)')),
('Instalación luminarias', 'Colocar nuevas luminarias en calle', '2023-12-22 14:00:00', 'PENDING', 4, 5, ST_GeographyFromText('POINT(-70.746 -33.513)')),
('Limpieza mercado', 'Limpiar área exterior del mercado', '2023-12-26 07:00:00', 'PENDING', 4, 5, ST_GeographyFromText('POINT(-70.754 -33.514)')),
('Reparación cerco', 'Arreglar cerco de cementerio', '2023-12-30 08:30:00', 'PENDING', 4, 5, ST_GeographyFromText('POINT(-70.749 -33.516)')),
('Mantenimiento piscina', 'Limpiar piscina municipal', '2024-01-03 10:00:00', 'PENDING', 4, 5, ST_GeographyFromText('POINT(-70.751 -33.512)')),
('Control plagas', 'Fumigar contra mosquitos', '2024-01-07 11:30:00', 'PENDING', 4, 5, ST_GeographyFromText('POINT(-70.747 -33.515)')),
('Limpieza post evento', 'Limpiar después de festival', '2024-01-11 13:00:00', 'PENDING', 4, 5, ST_GeographyFromText('POINT(-70.753 -33.513)'));

INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
-- Sector Ñuñoa (3)
('Mantenimiento plaza', 'Mantenimiento general plaza Ñuñoa', '2023-12-05 07:30:00', 'COMPLETED', 5, 3, ST_GeographyFromText('POINT(-70.570 -33.450)')),
('Reparación vereda', 'Arreglar vereda en Av. Irarrázaval', '2023-12-09 09:00:00', 'PENDING', 5, 3, ST_GeographyFromText('POINT(-70.575 -33.455)')),
('Limpieza post feria', 'Limpiar después de feria libre', '2023-12-13 10:30:00', 'PENDING', 5, 3, ST_GeographyFromText('POINT(-70.572 -33.452)')),
('Poda árboles', 'Podar árboles en calles residenciales', '2023-12-17 12:00:00', 'PENDING', 5, 3, ST_GeographyFromText('POINT(-70.578 -33.448)')),
('Instalación arte', 'Instalar escultura en plaza', '2023-12-21 13:30:00', 'PENDING', 5, 3, ST_GeographyFromText('POINT(-70.573 -33.453)')),
('Mantenimiento gimnasio', 'Reparar equipos gimnasio municipal', '2023-12-25 07:00:00', 'PENDING', 5, 3, ST_GeographyFromText('POINT(-70.576 -33.451)')),
('Limpieza canaletas', 'Limpiar canaletas de aguas lluvia', '2023-12-29 08:30:00', 'PENDING', 5, 3, ST_GeographyFromText('POINT(-70.571 -33.449)')),
('Reparación luminarias', 'Cambiar luminarias en plaza', '2024-01-02 10:00:00', 'PENDING', 5, 3, ST_GeographyFromText('POINT(-70.574 -33.454)')),
('Control plagas', 'Fumigar contra insectos', '2024-01-06 11:30:00', 'PENDING', 5, 3, ST_GeographyFromText('POINT(-70.577 -33.450)')),
('Limpieza grafitis', 'Limpiar grafitis en muros', '2024-01-10 13:00:00', 'PENDING', 5, 3, ST_GeographyFromText('POINT(-70.572 -33.451)')),
-- Sector Las Condes (4)
('Mantenimiento parque', 'Mantenimiento parque Araucano', '2023-12-07 08:00:00', 'COMPLETED', 5, 4, ST_GeographyFromText('POINT(-70.535 -33.415)')),
('Reparación juegos', 'Reparar juegos infantiles', '2023-12-11 09:30:00', 'PENDING', 5, 4, ST_GeographyFromText('POINT(-70.530 -33.418)')),
('Limpieza espejo agua', 'Limpiar espejo de agua en parque', '2023-12-15 11:00:00', 'PENDING', 5, 4, ST_GeographyFromText('POINT(-70.532 -33.416)')),
('Poda árboles', 'Podar árboles en avenida', '2023-12-19 12:30:00', 'PENDING', 5, 4, ST_GeographyFromText('POINT(-70.538 -33.412)')),
('Instalación señalética', 'Colocar señalética nueva', '2023-12-23 14:00:00', 'PENDING', 5, 4, ST_GeographyFromText('POINT(-70.533 -33.420)')),
('Mantenimiento centro', 'Reparar veredas en centro', '2023-12-27 07:30:00', 'PENDING', 5, 4, ST_GeographyFromText('POINT(-70.536 -33.417)')),
('Limpieza post evento', 'Limpiar después de concierto', '2023-12-31 09:00:00', 'PENDING', 5, 4, ST_GeographyFromText('POINT(-70.531 -33.415)')),
('Reparación cerco', 'Arreglar cerco perimetral', '2024-01-04 10:30:00', 'PENDING', 5, 4, ST_GeographyFromText('POINT(-70.534 -33.419)')),
('Control maleza', 'Eliminar maleza en áreas verdes', '2024-01-08 12:00:00', 'PENDING', 5, 4, ST_GeographyFromText('POINT(-70.537 -33.414)')),
('Limpieza piscina', 'Limpiar piscina municipal', '2024-01-12 13:30:00', 'PENDING', 5, 4, ST_GeographyFromText('POINT(-70.532 -33.413)'));



INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
-- Sector Centro (1)
('Reparación bache', 'Reparar bache en calle San Diego', '2023-12-06 08:00:00', 'COMPLETED', 6, 1, ST_GeographyFromText('POINT(-70.655 -33.445)')),
('Limpieza alcantarilla', 'Limpiar alcantarilla en esquina', '2023-12-10 09:30:00', 'PENDING', 6, 1, ST_GeographyFromText('POINT(-70.652 -33.443)')),
('Mantenimiento plaza', 'Mantenimiento plaza San Miguel', '2023-12-14 11:00:00', 'PENDING', 6, 1, ST_GeographyFromText('POINT(-70.658 -33.440)')),
('Poda árboles', 'Podar árboles en calle', '2023-12-18 12:30:00', 'PENDING', 6, 1, ST_GeographyFromText('POINT(-70.654 -33.442)')),
('Instalación basureros', 'Colocar basureros nuevos', '2023-12-22 14:00:00', 'PENDING', 6, 1, ST_GeographyFromText('POINT(-70.656 -33.441)')),
('Limpieza post feria', 'Limpiar después de feria libre', '2023-12-26 07:30:00', 'PENDING', 6, 1, ST_GeographyFromText('POINT(-70.653 -33.444)')),
('Reparación luminarias', 'Cambiar luminarias dañadas', '2023-12-30 09:00:00', 'PENDING', 6, 1, ST_GeographyFromText('POINT(-70.657 -33.443)')),
('Mantenimiento veredas', 'Reparar veredas dañadas', '2024-01-03 10:30:00', 'PENDING', 6, 1, ST_GeographyFromText('POINT(-70.651 -33.440)')),
('Control plagas', 'Fumigar contra insectos', '2024-01-07 12:00:00', 'PENDING', 6, 1, ST_GeographyFromText('POINT(-70.655 -33.441)')),
('Limpieza grafitis', 'Limpiar grafitis en muros', '2024-01-11 13:30:00', 'PENDING', 6, 1, ST_GeographyFromText('POINT(-70.654 -33.444)')),
-- Sector La Florida (6)
('Reparación juegos', 'Reparar juegos infantiles', '2023-12-08 08:30:00', 'COMPLETED', 6, 6, ST_GeographyFromText('POINT(-70.570 -33.520)')),
('Mantenimiento parque', 'Mantenimiento parque La Florida', '2023-12-12 10:00:00', 'PENDING', 6, 6, ST_GeographyFromText('POINT(-70.575 -33.515)')),
('Limpieza canaletas', 'Limpiar canaletas de aguas lluvia', '2023-12-16 11:30:00', 'PENDING', 6, 6, ST_GeographyFromText('POINT(-70.572 -33.518)')),
('Poda árboles', 'Podar árboles en plaza', '2023-12-20 13:00:00', 'PENDING', 6, 6, ST_GeographyFromText('POINT(-70.578 -33.517)')),
('Instalación señalética', 'Colocar señalética nueva', '2023-12-24 14:30:00', 'PENDING', 6, 6, ST_GeographyFromText('POINT(-70.573 -33.519)')),
('Reparación vereda', 'Arreglar vereda dañada', '2023-12-28 07:00:00', 'PENDING', 6, 6, ST_GeographyFromText('POINT(-70.576 -33.520)')),
('Limpieza post evento', 'Limpiar después de evento', '2024-01-01 08:30:00', 'PENDING', 6, 6, ST_GeographyFromText('POINT(-70.571 -33.516)')),
('Mantenimiento canchas', 'Reparar canchas deportivas', '2024-01-05 10:00:00', 'PENDING', 6, 6, ST_GeographyFromText('POINT(-70.574 -33.517)')),
('Control maleza', 'Eliminar maleza en áreas verdes', '2024-01-09 11:30:00', 'PENDING', 6, 6, ST_GeographyFromText('POINT(-70.577 -33.519)')),
('Limpieza piscina', 'Limpiar piscina municipal', '2024-01-13 13:00:00', 'PENDING', 6, 6, ST_GeographyFromText('POINT(-70.572 -33.518)'));


INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
-- Sector Centro (1)
('Reparación vereda', 'Arreglar vereda en calle Recoleta', '2023-12-07 08:00:00', 'COMPLETED', 7, 1, ST_GeographyFromText('POINT(-70.645 -33.420)')),
('Limpieza plaza', 'Limpiar plaza Recoleta', '2023-12-11 09:30:00', 'PENDING', 7, 1, ST_GeographyFromText('POINT(-70.648 -33.418)')),
('Mantenimiento parque', 'Mantenimiento parque urbano', '2023-12-15 11:00:00', 'PENDING', 7, 1, ST_GeographyFromText('POINT(-70.642 -33.422)')),
('Poda árboles', 'Podar árboles en avenida', '2023-12-19 12:30:00', 'PENDING', 7, 1, ST_GeographyFromText('POINT(-70.646 -33.419)')),
('Instalación basureros', 'Colocar basureros nuevos', '2023-12-23 14:00:00', 'PENDING', 7, 1, ST_GeographyFromText('POINT(-70.644 -33.421)')),
('Limpieza post feria', 'Limpiar después de feria libre', '2023-12-27 07:30:00', 'PENDING', 7, 1, ST_GeographyFromText('POINT(-70.647 -33.420)')),
('Reparación luminarias', 'Cambiar luminarias dañadas', '2023-12-31 09:00:00', 'PENDING', 7, 1, ST_GeographyFromText('POINT(-70.643 -33.419)')),
('Mantenimiento veredas', 'Reparar veredas dañadas', '2024-01-04 10:30:00', 'PENDING', 7, 1, ST_GeographyFromText('POINT(-70.649 -33.421)')),
('Control plagas', 'Fumigar contra insectos', '2024-01-08 12:00:00', 'PENDING', 7, 1, ST_GeographyFromText('POINT(-70.645 -33.418)')),
('Limpieza grafitis', 'Limpiar grafitis en muros', '2024-01-12 13:30:00', 'PENDING', 7, 1, ST_GeographyFromText('POINT(-70.644 -33.420)')),
-- Sector Quilicura (8)
('Reparación juegos', 'Reparar juegos infantiles', '2023-12-09 08:30:00', 'COMPLETED', 7, 8, ST_GeographyFromText('POINT(-70.730 -33.380)')),
('Mantenimiento parque', 'Mantenimiento parque industrial', '2023-12-13 10:00:00', 'PENDING', 7, 8, ST_GeographyFromText('POINT(-70.735 -33.378)')),
('Limpieza canaletas', 'Limpiar canaletas de aguas lluvia', '2023-12-17 11:30:00', 'PENDING', 7, 8, ST_GeographyFromText('POINT(-70.728 -33.382)')),
('Poda árboles', 'Podar árboles en plaza', '2023-12-21 13:00:00', 'PENDING', 7, 8, ST_GeographyFromText('POINT(-70.732 -33.379)')),
('Instalación señalética', 'Colocar señalética nueva', '2023-12-25 14:30:00', 'PENDING', 7, 8, ST_GeographyFromText('POINT(-70.726 -33.381)')),
('Reparación vereda', 'Arreglar vereda dañada', '2023-12-29 07:00:00', 'PENDING', 7, 8, ST_GeographyFromText('POINT(-70.734 -33.380)')),
('Limpieza post evento', 'Limpiar después de evento', '2024-01-02 08:30:00', 'PENDING', 7, 8, ST_GeographyFromText('POINT(-70.729 -33.378)')),
('Mantenimiento canchas', 'Reparar canchas deportivas', '2024-01-06 10:00:00', 'PENDING', 7, 8, ST_GeographyFromText('POINT(-70.731 -33.381)')),
('Control maleza', 'Eliminar maleza en áreas verdes', '2024-01-10 11:30:00', 'PENDING', 7, 8, ST_GeographyFromText('POINT(-70.733 -33.379)')),
('Limpieza piscina', 'Limpiar piscina municipal', '2024-01-14 13:00:00', 'PENDING', 7, 8, ST_GeographyFromText('POINT(-70.727 -33.380)'));

INSERT INTO tasks (title, description, due_date, status, user_id, sector_id, location) VALUES
-- Sector Puente Alto (7)
('Reparación bache', 'Reparar bache en Av. Concha y Toro', '2023-12-08 08:00:00', 'COMPLETED', 8, 7, ST_GeographyFromText('POINT(-70.575 -33.615)')),
('Limpieza plaza', 'Limpiar plaza principal', '2023-12-12 09:30:00', 'PENDING', 8, 7, ST_GeographyFromText('POINT(-70.572 -33.618)')),
('Mantenimiento parque', 'Mantenimiento parque municipal', '2023-12-16 11:00:00', 'PENDING', 8, 7, ST_GeographyFromText('POINT(-70.578 -33.616)')),
('Poda árboles', 'Podar árboles en avenida', '2023-12-20 12:30:00', 'PENDING', 8, 7, ST_GeographyFromText('POINT(-70.573 -33.617)')),
('Instalación basureros', 'Colocar basureros nuevos', '2023-12-24 14:00:00', 'PENDING', 8, 7, ST_GeographyFromText('POINT(-70.576 -33.614)')),
('Limpieza post feria', 'Limpiar después de feria libre', '2023-12-28 07:30:00', 'PENDING', 8, 7, ST_GeographyFromText('POINT(-70.571 -33.616)')),
('Reparación luminarias', 'Cambiar luminarias dañadas', '2024-01-01 09:00:00', 'PENDING', 8, 7, ST_GeographyFromText('POINT(-70.574 -33.618)')),
('Mantenimiento veredas', 'Reparar veredas dañadas', '2024-01-05 10:30:00', 'PENDING', 8, 7, ST_GeographyFromText('POINT(-70.577 -33.615)')),
('Control plagas', 'Fumigar contra insectos', '2024-01-09 12:00:00', 'PENDING', 8, 7, ST_GeographyFromText('POINT(-70.572 -33.617)')),
('Limpieza grafitis', 'Limpiar grafitis en muros', '2024-01-13 13:30:00', 'PENDING', 8, 7, ST_GeographyFromText('POINT(-70.575 -33.616)')),
-- Sector La Florida (6)
('Reparación juegos', 'Reparar juegos infantiles', '2023-12-10 08:30:00', 'COMPLETED', 8, 6, ST_GeographyFromText('POINT(-70.570 -33.520)')),
('Mantenimiento parque', 'Mantenimiento parque La Florida', '2023-12-14 10:00:00', 'PENDING', 8, 6, ST_GeographyFromText('POINT(-70.575 -33.515)')),
('Limpieza canaletas', 'Limpiar canaletas de aguas lluvia', '2023-12-18 11:30:00', 'PENDING', 8, 6, ST_GeographyFromText('POINT(-70.572 -33.518)')),
('Poda árboles', 'Podar árboles en plaza', '2023-12-22 13:00:00', 'PENDING', 8, 6, ST_GeographyFromText('POINT(-70.578 -33.517)')),
('Instalación señalética', 'Colocar señalética nueva', '2023-12-26 14:30:00', 'PENDING', 8, 6, ST_GeographyFromText('POINT(-70.573 -33.519)')),
('Reparación vereda', 'Arreglar vereda dañada', '2023-12-30 07:00:00', 'PENDING', 8, 6, ST_GeographyFromText('POINT(-70.576 -33.520)')),
('Limpieza post evento', 'Limpiar después de evento', '2024-01-03 08:30:00', 'PENDING', 8, 6, ST_GeographyFromText('POINT(-70.571 -33.516)')),
('Mantenimiento canchas', 'Reparar canchas deportivas', '2024-01-07 10:00:00', 'PENDING', 8, 6, ST_GeographyFromText('POINT(-70.574 -33.517)')),
('Control maleza', 'Eliminar maleza en áreas verdes', '2024-01-11 11:30:00', 'PENDING', 8, 6, ST_GeographyFromText('POINT(-70.577 -33.519)')),
('Limpieza piscina', 'Limpiar piscina municipal', '2024-01-15 13:00:00', 'PENDING', 8, 6, ST_GeographyFromText('POINT(-70.572 -33.518)'));