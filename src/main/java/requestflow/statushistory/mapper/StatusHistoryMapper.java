package requestflow.statushistory.mapper;

import requestflow.statushistory.StatusHistory;
import requestflow.statushistory.dto.StatusHistoryDto;

public class StatusHistoryMapper {
    public static StatusHistoryDto toStatusHistoryDto(StatusHistory statusHistory) {
        return new StatusHistoryDto(statusHistory.getId(),
                statusHistory.getRequest().getId(),
                statusHistory.getOldStatus(),
                statusHistory.getNewStatus(),
                statusHistory.getChangedAt());
    }
}
