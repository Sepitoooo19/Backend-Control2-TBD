package bdavanzadas.lab1.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    /**
     *
     *  La clase TaskEntity representa una tarea en el sistema.
     *  Esta clase contiene información sobre la tarea, incluyendo su ID, título, descripción,
     *  fecha de vencimiento, estado, usuario asignado, sector asociado y ubicación geográfica.
     *
     */

    @Schema(description = "ID de la tarea", example = "1")
    private int id;

    @Schema(description = "Título de la tarea", example = "Reparar semáforo")
    private String title;

    @Schema(description = "Descripción detallada", example = "El semáforo en la esquina principal no funciona")
    private String description;

    @Schema(description = "Fecha de vencimiento", example = "2025-12-31T23:59:59")
    private LocalDateTime dueDate;

    @Schema(description = "Estado de la tarea", allowableValues = {"PENDING", "COMPLETED"}, example = "PENDING")
    private String status;

    @Schema(description = "ID del usuario asignado", example = "1")
    private int userId;

    @Schema(description = "ID del sector asociado", example = "2")
    private int sectorId;

    @Schema(
            description = "Ubicación geográfica en formato WKT (POINT)",
            example = "POINT(-70.651 -33.456)"
    )
    private String location; // WKT: "POINT(longitud latitud)"

    @Schema(description = "Fecha de creación (automática)", example = "2025-05-20T10:30:00")
    private LocalDateTime createdAt;
}