package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorEntity {

    /**
     * La clase SectorEntity representa un sector geográfico en el sistema.
     * Contiene información sobre el ID del sector, su nombre y su ubicación en formato de polígono WKT.
     */

    @Schema(
            description = "ID único del sector",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer id;

    @Schema(
            description = "Nombre descriptivo del sector",
            example = "Zona Norte",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @Schema(
            description = "Ubicación geográfica en formato WKT (POLYGON)",
            example = "POLYGON((-70.65 -33.45, -70.65 -33.44, -70.64 -33.44, -70.64 -33.45, -70.65 -33.45))",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String location; // Formato WKT: "POLYGON((long1 lat1, long2 lat2, ...))"
}