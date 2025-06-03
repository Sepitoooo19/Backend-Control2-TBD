package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.SectorEntity;
import bdavanzadas.lab1.repositories.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio de sectores que proporciona operaciones para crear, obtener,
 * actualizar y eliminar sectores. También valida el formato del polígono WKT.
 */
@Service
public class SectorService {

    /**
     * Repositorio de sectores.
     * Se utiliza para acceder y manipular datos de sectores en la base de datos.
     */
    @Autowired
    private SectorRepository sectorRepository;

    /**
     * Crea un nuevo sector en la base de datos.
     *
     * @param sector Objeto SectorEntity que contiene el nombre y la ubicación.
     * @return El sector guardado.
     * @throws IllegalArgumentException Si el nombre está vacío o la ubicación no tiene formato válido.
     */
    public SectorEntity createSector(SectorEntity sector) {
        if (sector.getName() == null || sector.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del sector no puede estar vacío");
        }
        if (sector.getLocation() == null || !sector.getLocation().startsWith("POLYGON((")) {
            throw new IllegalArgumentException("Formato de ubicación inválido. Use: POLYGON((long1 lat1, long2 lat2, ...))");
        }

        return sectorRepository.save(sector);
    }

    /**
     * Obtiene un sector por su ID.
     *
     * @param id ID del sector a buscar.
     * @return Entidad del sector correspondiente.
     * @throws RuntimeException Si no se encuentra el sector con el ID proporcionado.
     */
    public SectorEntity getSectorById(int id) {
        return sectorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sector no encontrado con ID: " + id));
    }

    /**
     * Retorna una lista con todos los sectores registrados.
     *
     * @return Lista de entidades de sectores.
     */
    public List<SectorEntity> getAllSectors() {
        return sectorRepository.findAll();
    }

    /**
     * Actualiza la información de un sector existente.
     *
     * @param sector Entidad con los datos actualizados del sector.
     * @return Entidad del sector actualizada.
     * @throws IllegalArgumentException Si el ID del sector es nulo o inválido.
     * @throws RuntimeException Si no se puede actualizar o recuperar el sector.
     */
    public SectorEntity updateSector(SectorEntity sector) {
        if (sector.getId() == null || sector.getId() <= 0) {
            throw new IllegalArgumentException("ID de sector inválido");
        }
        if (!sectorRepository.update(sector)) {
            throw new RuntimeException("No se pudo actualizar el sector con ID: " + sector.getId());
        }
        return sectorRepository.findById(sector.getId())
                .orElseThrow(() -> new RuntimeException("Error al recuperar sector actualizado"));
    }

    /**
     * Elimina un sector por su ID.
     *
     * @param id ID del sector a eliminar.
     * @return true si el sector fue eliminado correctamente.
     * @throws RuntimeException Si no se pudo eliminar el sector.
     */
    public boolean deleteSector(int id) {
        if (!sectorRepository.deleteById(id)) {
            throw new RuntimeException("No se pudo eliminar el sector con ID: " + id);
        }
        return true;
    }
}
