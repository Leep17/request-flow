package requestflow.request.service;

import requestflow.request.Request;
import requestflow.request.dto.NewRequestDto;
import requestflow.request.dto.UpdateRequestDto;

import java.util.Collection;

public interface RequestService {
    Collection<Request> getAll();

    Request save(NewRequestDto newRequestDto);

    Request getById(Long id);

    Request update(Long id, UpdateRequestDto updateRequestDto);

    Request submitRequest(Long id);

    void deleteById(Long id);

}
