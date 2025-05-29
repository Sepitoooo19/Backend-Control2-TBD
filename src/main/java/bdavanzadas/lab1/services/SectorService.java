package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.SectorEntity;
import bdavanzadas.lab1.repositories.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SectorService {

    @Autowired
    private SectorRepository sectorRepository;

    public SectorEntity createSector(SectorEntity sector) {
        if (sector.getName() == null || sector.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del sector no puede estar vacío");
        }
        if (sector.getLocation() == null || !sector.getLocation().startsWith("POLYGON((")) {
            throw new IllegalArgumentException("Formato de ubicación inválido. Use: POLYGON((long1 lat1, long2 lat2, ...))");
        }

        return sectorRepository.save(sector);
    }

    public SectorEntity getSectorById(int id) {
        return sectorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sector no encontrado con ID: " + id));
    }

    public List<SectorEntity> getAllSectors() {
        return sectorRepository.findAll();
    }

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

    public boolean deleteSector(int id) {
        if (!sectorRepository.deleteById(id)) {
            throw new RuntimeException("No se pudo eliminar el sector con ID: " + id);
        }
        return true;
    }
}