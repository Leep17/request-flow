package requestflow.request.service;

import org.springframework.data.domain.Page;
import requestflow.request.Request;
import requestflow.request.RequestStatus;
import requestflow.request.dto.NewRequestDto;
import requestflow.request.dto.UpdateRequestDto;

public interface RequestService {
    Page<Request> getAll(RequestStatus status, Long categoryId, Long authorId, String sort, int page, int size);

    Request save(NewRequestDto newRequestDto);

    Request getById(Long id);

    Request update(Long id, UpdateRequestDto updateRequestDto);

    Request submitRequest(Long id);

    Request reviewRequest(Long id);

    Request approveRequest(Long id);

    Request cancelRequest(Long id);

    Request rejectRequest(Long id);

    void deleteById(Long id);

}
