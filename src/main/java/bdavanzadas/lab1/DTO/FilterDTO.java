package bdavanzadas.lab1.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterDTO {
    public String status;
    public String word;
}
