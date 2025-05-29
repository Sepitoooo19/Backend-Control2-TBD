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

@Repository
public class SectorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Metodo save (insert)
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

        // Obtener ID generado
        if (keyHolder.getKeys() != null && keyHolder.getKeys().containsKey("id")) {
            sector.setId(((Number) keyHolder.getKeys().get("id")).intValue());
        }

        return sector;
    }

    // Metodo findById
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

    public boolean deleteById(int id) {
        String sql = "DELETE FROM sectors WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        return deleted > 0;
    }
}