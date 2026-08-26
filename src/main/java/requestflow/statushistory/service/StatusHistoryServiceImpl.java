package requestflow.statushistory.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import requestflow.exception.NotFoundException;
import requestflow.request.repository.RequestRepository;
import requestflow.statushistory.StatusHistory;
import requestflow.statushistory.dto.StatusHistoryDto;
import requestflow.statushistory.mapper.StatusHistoryMapper;
import requestflow.statushistory.repository.StatusHistoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusHistoryServiceImpl implements StatusHistoryService {
    private final StatusHistoryRepository statusHistoryRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<StatusHistoryDto> getAllByRequestId(Long requestId) {
        requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Заявки с id=" + requestId + " не найдена"));
        return statusHistoryRepository.findAllByRequestIdOrderByChangedAt(requestId).stream()
                .map(StatusHistoryMapper::toStatusHistoryDto)
                .toList();
    }

    @Transactional
    @Override
    public StatusHistoryDto save(StatusHistory statusHistory) {
        return StatusHistoryMapper.toStatusHistoryDto(statusHistoryRepository.save(statusHistory));
    }
}
