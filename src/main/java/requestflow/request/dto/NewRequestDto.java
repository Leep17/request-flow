package requestflow.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewRequestDto {
    @NotBlank
    @Size(min = 3, max = 150)
    private String title;

    @NotBlank
    @Size(min = 5, max = 2000)
    private String description;

    @NotNull
    private Long authorId;

    @NotNull
    private Long categoryId;
}
