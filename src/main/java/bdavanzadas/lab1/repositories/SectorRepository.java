package bdavanzadas.lab1.repositories;

import bdavanzadas.lab1.entities.SectorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * La clase SectorRepository representa el repositorio para gestionar sectores geográficos en la base de datos.
 * Esta clase proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre la tabla `sectors`.
 * Utiliza JdbcTemplate de Spring para facilitar la interacción con la base de datos.
 *
 * Las ubicaciones se almacenan como geometrías en formato WKT (Well-Known Text) y se transforman con funciones PostGIS.
 */
@Repository
public class SectorRepository {

    /**
     * JdbcTemplate permite ejecutar sentencias SQL de forma simplificada y segura.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Guarda un nuevo sector en la base de datos.
     * @param sector El objeto SectorEntity que representa el sector a guardar.
     *               El campo `location` debe estar en formato WKT (Well-Known Text).
     *               El ID generado se asigna automáticamente al objeto después de la inserción.
     * @return El objeto SectorEntity actualizado con su ID generado.
     */
    public SectorEntity save(SectorEntity sector) {
        String sql = """
            INSERT INTO sectors (name, location) 
            VALUES (?, ST_GeomFromText(?, 4326))
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sector.getName());
            ps.setString(2, sector.getLocation());
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys() != null && keyHolder.getKeys().containsKey("id")) {
            sector.setId(((Number) keyHolder.getKeys().get("id")).intValue());
        }

        return sector;
    }

    /**
     * Busca un sector por su ID.
     * @param id El ID del sector a buscar.
     * @return Un Optional que contiene el SectorEntity si se encuentra, o vacío si no existe.
     *         La ubicación se devuelve en formato WKT (Well-Known Text).
     */
    public Optional<SectorEntity> findById(int id) {
        String sql = """
            SELECT id, name, ST_AsText(location) as location 
            FROM sectors 
            WHERE id = ?
            """;

        try {
            SectorEntity sector = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new SectorEntity(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("location")
                    ), id);
            return Optional.ofNullable(sector);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Obtiene todos los sectores almacenados en la base de datos.
     * @return Una lista de objetos SectorEntity con toda la información de los sectores.
     *         Las ubicaciones se devuelven en formato WKT (Well-Known Text).
     */
    public List<SectorEntity> findAll() {
        String sql = """
            SELECT id, name, ST_AsText(location) as location 
            FROM sectors
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new SectorEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location")
                ));
    }

    /**
     * Actualiza la información de un sector existente.
     * @param sector El objeto SectorEntity con los datos actualizados (debe incluir el ID).
     * @return true si se realizó la actualización correctamente, false si no se encontró el sector.
     */
    public boolean update(SectorEntity sector) {
        String sql = """
            UPDATE sectors SET 
                name = ?, 
                location = ST_GeomFromText(?, 4326)
            WHERE id = ?
            """;

        int updated = jdbcTemplate.update(sql,
                sector.getName(),
                sector.getLocation(),
                sector.getId()
        );

        return updated > 0;
    }

    /**
     * Elimina un sector de la base de datos por su ID.
     * @param id El ID del sector a eliminar.
     * @return true si el sector fue eliminado exitosamente, false si no se encontró.
     */
    public boolean deleteById(int id) {
        String sql = "DELETE FROM sectors WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        return deleted > 0;
    }
}
