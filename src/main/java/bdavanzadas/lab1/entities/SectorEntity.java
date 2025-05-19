package bdavanzadas.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorEntity {
    private Integer id;
    private String name;
    private String location; // Formato WKT (POINT(long lat))
}