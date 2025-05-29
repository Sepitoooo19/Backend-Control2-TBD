package bdavanzadas.lab1.Controllers;

import bdavanzadas.lab1.entities.SectorEntity;
import bdavanzadas.lab1.services.SectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sectors")
@Tag(name = "Gestión de Sectores", description = "API para la administración de sectores geográficos")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @PostMapping
    @Operation(
            summary = "Crear un nuevo sector",
            description = "Registra un nuevo sector geográfico definido por un polígono en formato WKT (longitud latitud)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sector creado exitosamente",
                            content = @Content(schema = @Schema(implementation = SectorEntity.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                            content = @Content(examples = {
                                    @ExampleObject(
                                            name = "Formato inválido",
                                            value = "{\"success\": false, \"message\": \"Formato de ubicación inválido. Use: POLYGON((lon1 lat1, lon2 lat2, ...))\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Nombre vacío",
                                            value = "{\"success\": false, \"message\": \"El nombre del sector no puede estar vacío\"}"
                                    )
                            }))
            }
    )
    public ResponseEntity<?> createSector(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del sector a crear",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SectorEntity.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo Roma",
                                            summary = "Sector en Roma",
                                            value = "{\"name\": \"Roma Centro\", \"location\": \"POLYGON((12.47831 41.89462, 12.48000 41.89462, 12.48000 41.89600, 12.47831 41.89600, 12.47831 41.89462))\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Ejemplo Milán",
                                            summary = "Sector en Milán",
                                            value = "{\"name\": \"Milán Norte\", \"location\": \"POLYGON((9.15 45.45, 9.20 45.45, 9.20 45.50, 9.15 45.50, 9.15 45.45))\"}"
                                    )
                            }
                    )
            )
            @RequestBody SectorEntity sector) {
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
    @Operation(
            summary = "Obtener sector por ID",
            description = "Recupera la información de un sector específico según su identificador",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sector encontrado",
                            content = @Content(schema = @Schema(implementation = SectorEntity.class))),
                    @ApiResponse(responseCode = "404", description = "Sector no encontrado",
                            content = @Content(examples = @ExampleObject(
                                    value = "{\"success\": false, \"message\": \"Sector no encontrado con ID: 1\"}"
                            )))
            }
    )
    public ResponseEntity<?> getSectorById(
            @Parameter(description = "ID del sector", example = "1", required = true)
            @PathVariable int id) {
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
    @Operation(
            summary = "Listar todos los sectores",
            description = "Devuelve un listado completo de todos los sectores registrados en el sistema",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado de sectores",
                            content = @Content(schema = @Schema(implementation = SectorEntity[].class)))
            }
    )
    public List<SectorEntity> getAllSectors() {
        return sectorService.getAllSectors();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar sector",
            description = "Modifica la información de un sector existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sector actualizado",
                            content = @Content(schema = @Schema(implementation = SectorEntity.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "404", description = "Sector no encontrado")
            }
    )
    public ResponseEntity<?> updateSector(
            @Parameter(description = "ID del sector a actualizar", example = "1", required = true)
            @PathVariable int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del sector",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Ejemplo actualización",
                                    value = "{\"name\": \"Roma Centro Actualizado\", \"location\": \"POLYGON((12.45 41.88, 12.50 41.88, 12.50 41.92, 12.45 41.92, 12.45 41.88))\"}"
                            )
                    )
            )
            @RequestBody SectorEntity sector) {
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
    @Operation(
            summary = "Eliminar sector",
            description = "Elimina permanentemente un sector del sistema",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sector eliminado",
                            content = @Content(examples = @ExampleObject(
                                    value = "{\"success\": true, \"message\": \"Sector eliminado correctamente\"}"
                            ))),
                    @ApiResponse(responseCode = "404", description = "Sector no encontrado")
            }
    )
    public ResponseEntity<?> deleteSector(
            @Parameter(description = "ID del sector a eliminar", example = "1", required = true)
            @PathVariable int id) {
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