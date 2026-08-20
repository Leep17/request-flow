package requestflow.request.mapper;

import requestflow.request.Request;
import requestflow.request.dto.RequestDto;

public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {

        return new RequestDto(request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getCreatedAt(),
                request.getUpdatedAt(),
                request.getAuthor().getId(),
                request.getAuthor().getName(),
                request.getCategory().getId(),
                request.getCategory().getName());
    }
}
