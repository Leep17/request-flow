package requestflow.statushistory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import requestflow.request.RequestStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistoryDto {
    private Long id;
    private Long requestId;
    private RequestStatus oldStatus;
    private RequestStatus newStatus;
    private LocalDateTime changedAt;
}
