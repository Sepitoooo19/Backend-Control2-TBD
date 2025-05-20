-- Sectores
-- Insertar comunas de Santiago como sectores
INSERT INTO sectors (name, location) VALUES
                                         ('Santiago', ST_GeomFromText('POINT(-70.6505 -33.4379)', 4326)),       -- Santiago Centro (ej. Plaza de Armas)
                                         ('Providencia', ST_GeomFromText('POINT(-70.6095 -33.4317)', 4326)),   -- Providencia
                                         ('Las Condes', ST_GeomFromText('POINT(-70.5670 -33.4000)', 4326)),    -- Las Condes
                                         ('Vitacura', ST_GeomFromText('POINT(-70.5800 -33.3833)', 4326)),      -- Vitacura
                                         ('Lo Barnechea', ST_GeomFromText('POINT(-70.5000 -33.3500)', 4326)),  -- Lo Barnechea
                                         ('Ñuñoa', ST_GeomFromText('POINT(-70.6120 -33.4580)', 4326)),         -- Ñuñoa
                                         ('La Reina', ST_GeomFromText('POINT(-70.5400 -33.4500)', 4326)),      -- La Reina
                                         ('Macul', ST_GeomFromText('POINT(-70.6000 -33.4833)', 4326)),         -- Macul
                                         ('Peñalolén', ST_GeomFromText('POINT(-70.5333 -33.4833)', 4326)),    -- Peñalolén
                                         ('La Florida', ST_GeomFromText('POINT(-70.5833 -33.5333)', 4326)),   -- La Florida
                                         ('Maipú', ST_GeomFromText('POINT(-70.7619 -33.5111)', 4326)),         -- Maipú
                                         ('Estación Central', ST_GeomFromText('POINT(-70.6833 -33.4500)', 4326)), -- Estación Central
                                         ('Quinta Normal', ST_GeomFromText('POINT(-70.6986 -33.4356)', 4326)),  -- Quinta Normal
                                         ('Lo Prado', ST_GeomFromText('POINT(-70.7200 -33.4417)', 4326)),      -- Lo Prado
                                         ('Pudahuel', ST_GeomFromText('POINT(-70.7700 -33.4300)', 4326)),      -- Pudahuel (centro urbano aprox.)
                                         ('Cerro Navia', ST_GeomFromText('POINT(-70.7333 -33.4167)', 4326)),   -- Cerro Navia
                                         ('Renca', ST_GeomFromText('POINT(-70.7100 -33.4000)', 4326)),         -- Renca
                                         ('Quilicura', ST_GeomFromText('POINT(-70.7333 -33.3667)', 4326)),    -- Quilicura
                                         ('Huechuraba', ST_GeomFromText('POINT(-70.6500 -33.3667)', 4326)),   -- Huechuraba
                                         ('Conchalí', ST_GeomFromText('POINT(-70.6667 -33.3833)', 4326)),     -- Conchalí
                                         ('Independencia', ST_GeomFromText('POINT(-70.6600 -33.4167)', 4326)), -- Independencia
                                         ('Recoleta', ST_GeomFromText('POINT(-70.6333 -33.4167)', 4326)),     -- Recoleta
                                         ('San Miguel', ST_GeomFromText('POINT(-70.6500 -33.4833)', 4326)),   -- San Miguel
                                         ('San Joaquín', ST_GeomFromText('POINT(-70.6250 -33.4833)', 4326)),  -- San Joaquín
                                         ('Pedro Aguirre Cerda', ST_GeomFromText('POINT(-70.6750 -33.4833)', 4326)), -- Pedro Aguirre Cerda
                                         ('Cerrillos', ST_GeomFromText('POINT(-70.7250 -33.4900)', 4326)),    -- Cerrillos
                                         ('La Cisterna', ST_GeomFromText('POINT(-70.6583 -33.5167)', 4326)),  -- La Cisterna
                                         ('La Granja', ST_GeomFromText('POINT(-70.6250 -33.5333)', 4326)),    -- La Granja
                                         ('San Ramón', ST_GeomFromText('POINT(-70.6417 -33.5333)', 4326)),    -- San Ramón
                                         ('El Bosque', ST_GeomFromText('POINT(-70.6667 -33.5583)', 4326)),    -- El Bosque
                                         ('Lo Espejo', ST_GeomFromText('POINT(-70.6917 -33.5333)', 4326)),    -- Lo Espejo
                                         ('La Pintana', ST_GeomFromText('POINT(-70.6250 -33.5833)', 4326));   -- La Pintana                                   ('Maipú', ST_GeomFromText('POINT(-70.7619 -33.5111)', 4326));