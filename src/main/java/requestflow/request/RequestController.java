package requestflow.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import requestflow.request.dto.NewRequestDto;
import requestflow.request.dto.RequestDto;
import requestflow.request.dto.UpdateRequestDto;
import requestflow.request.mapper.RequestMapper;
import requestflow.request.service.RequestService;
import requestflow.statushistory.dto.StatusHistoryDto;
import requestflow.statushistory.service.StatusHistoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestService requestService;
    private final StatusHistoryService statusHistoryService;

    @GetMapping
    public Page<RequestDto> getAll(@PositiveOrZero @RequestParam(defaultValue = "0") int page,
                                   @Max (100) @Min (1) @RequestParam(defaultValue = "20") int size,
                                   @RequestParam(required = false) RequestStatus status,
                                   @RequestParam(required = false) Long categoryId,
                                   @RequestParam(required = false) Long authorId,
                                   @Pattern(
                                           regexp = "^(id|createdAt|updatedAt|title)(,(asc|desc))?$",
                                           message = "Сортировка должна производиться только по этим параметрам: id, createdAt, updatedAt, title"
                                   ) @RequestParam(defaultValue = "createdAt,desc") String sort) {

        return requestService.getAll(status, categoryId, authorId, sort, page, size)
                .map(RequestMapper::toRequestDto);
    }

    @GetMapping("/{id}")
    public RequestDto getById(@PathVariable Long id) {
        return RequestMapper.toRequestDto(requestService.getById(id));
    }

    @GetMapping("/{requestId}/history")
    public List<StatusHistoryDto> getHistoryByRequestId(@PathVariable Long requestId) {
        return statusHistoryService.getAllByRequestId(requestId).stream()
                .toList();
    }

    @PostMapping
    public RequestDto saveNewRequest(@Valid @RequestBody NewRequestDto newRequestDto) {
        return RequestMapper.toRequestDto(requestService.save(newRequestDto));
    }

    @PostMapping("/{id}/submit")
    public RequestDto submitRequest(@PathVariable Long id) {
        return RequestMapper.toRequestDto(requestService.submitRequest(id));
    }

    @PostMapping("/{id}/review")
    public RequestDto reviewRequest(@PathVariable Long id) {
        return  RequestMapper.toRequestDto(requestService.reviewRequest(id));
    }

    @PostMapping("/{id}/approve")
    public RequestDto approveRequest(@PathVariable Long id) {
        return  RequestMapper.toRequestDto(requestService.approveRequest(id));
    }

    @PostMapping("/{id}/reject")
    public RequestDto rejectRequest(@PathVariable Long id) {
        return RequestMapper.toRequestDto(requestService.rejectRequest(id));
    }

    @PostMapping("/{id}/cancel")
    public RequestDto cancelRequest(@PathVariable Long id) {
        return RequestMapper.toRequestDto(requestService.cancelRequest(id));
    }

    @PatchMapping("/{id}")
    public RequestDto updateById(@PathVariable Long id, @Valid @RequestBody UpdateRequestDto updateRequestDto) {
        return RequestMapper.toRequestDto(requestService.update(id, updateRequestDto));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        requestService.deleteById(id);
    }
}
