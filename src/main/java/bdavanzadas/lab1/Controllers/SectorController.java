package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.SectorEntity;
import bdavanzadas.lab1.services.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sectors")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @PostMapping
    public ResponseEntity<?> createSector(@RequestBody SectorEntity sector) {
        try {
            SectorEntity createdSector = sectorService.createSector(sector);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSector);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSectorById(@PathVariable int id) {
        try {
            SectorEntity sector = sectorService.getSectorById(id);
            return ResponseEntity.ok(sector);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping
    public List<SectorEntity> getAllSectors() {
        return sectorService.getAllSectors();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSector(@PathVariable int id, @RequestBody SectorEntity sector) {
        try {
            sector.setId(id);
            SectorEntity updatedSector = sectorService.updateSector(sector);
            return ResponseEntity.ok(updatedSector);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSector(@PathVariable int id) {
        try {
            boolean deleted = sectorService.deleteSector(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Sector eliminado correctamente"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}