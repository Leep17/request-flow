package requestflow.statushistory.service;

import requestflow.statushistory.StatusHistory;
import requestflow.statushistory.dto.StatusHistoryDto;

import java.util.Collection;

public interface StatusHistoryService {
    Collection<StatusHistoryDto> getAllByRequestId(Long requestId);

    StatusHistoryDto save(StatusHistory statusHistory);
}
