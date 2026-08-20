package requestflow.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestDto {

    @Size(min = 3, max = 150)
    private String title;

    @Size(min = 5, max = 2000)
    private String description;

    private Long categoryId;
}
