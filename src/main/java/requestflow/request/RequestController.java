package requestflow.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import requestflow.request.dto.NewRequestDto;
import requestflow.request.dto.RequestDto;
import requestflow.request.dto.UpdateRequestDto;
import requestflow.request.mapper.RequestMapper;
import requestflow.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getAll() {
        return requestService.getAll().stream()
                .map(RequestMapper::toRequestDto)
                .toList();
    }

    @GetMapping("/{id}")
    public RequestDto getById(@PathVariable Long id) {
        return RequestMapper.toRequestDto(requestService.getById(id));
    }

    @PostMapping
    public RequestDto saveNewRequest(@Valid @RequestBody NewRequestDto newRequestDto) {
        return RequestMapper.toRequestDto(requestService.save(newRequestDto));
    }

    @PostMapping("/{id}/submit")
    public RequestDto submitRequest(@PathVariable Long id) {
        return RequestMapper.toRequestDto(requestService.submitRequest(id));
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
