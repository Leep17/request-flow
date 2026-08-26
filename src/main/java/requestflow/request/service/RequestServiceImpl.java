package requestflow.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import requestflow.category.Category;
import requestflow.category.repository.CategoryRepository;
import requestflow.exception.ConflictException;
import requestflow.exception.NotFoundException;
import requestflow.request.Request;
import requestflow.request.RequestStatus;
import requestflow.request.dto.NewRequestDto;
import requestflow.request.dto.UpdateRequestDto;
import requestflow.request.repository.RequestRepository;
import requestflow.statushistory.StatusHistory;
import requestflow.statushistory.service.StatusHistoryService;
import requestflow.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StatusHistoryService statusHistoryService;

    @Override
    public Collection<Request> getAll() {
        return requestRepository.findAll();
    }

    @Transactional
    @Override
    public Request save(NewRequestDto newRequestDto) {
        Request request = new Request();
        request.setTitle(newRequestDto.getTitle());
        request.setDescription(newRequestDto.getDescription());
        request.setStatus(RequestStatus.DRAFT);
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        request.setCategory(categoryRepository.findById(newRequestDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Категория с id=" + newRequestDto.getCategoryId() + " не найдена")));
        request.setAuthor(userRepository.findById(newRequestDto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + newRequestDto.getAuthorId() + " не найден")));

        return requestRepository.save(request);
    }

    @Override
    public Request getById(Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Заявки с id=" + id + " не найдена"));
    }

    @Transactional
    @Override
    public Request update(Long id, UpdateRequestDto updateRequestDto) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Заявки с id=" + id + " не найдена"));
        if (!request.getStatus().equals(RequestStatus.DRAFT)) {
            throw  new ConflictException("Заявка " + id + " уже согласована");
        }

        if (updateRequestDto.getDescription() != null) {
            if (updateRequestDto.getDescription().isBlank()) {
                throw new ConflictException("Описание не может быть пустым");
            }
            request.setDescription(updateRequestDto.getDescription());
        }

        if (updateRequestDto.getTitle() != null) {
            if (updateRequestDto.getTitle().isBlank()) {
                throw new ConflictException("Название не может быть пустым");
            }
            request.setTitle(updateRequestDto.getTitle());
        }

        if (updateRequestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateRequestDto.getCategoryId()).orElseThrow(() -> new NotFoundException("Категория с id=" + updateRequestDto.getCategoryId() + " не найдена"));
            request.setCategory(category);
        }
        request.setUpdatedAt(LocalDateTime.now());
        return request;
    }

    @Transactional
    @Override
    public Request submitRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Заявки с id=" + id + " не найдена"));
          if (!request.getStatus().equals(RequestStatus.DRAFT)) {
              throw  new ConflictException("Заявка " + id + " уже согласована");
          }
        request.setStatus(RequestStatus.SUBMITTED);
        request.setUpdatedAt(LocalDateTime.now());

        newStatusHistory(request, RequestStatus.DRAFT, request.getStatus());

        return request;
    }

    @Transactional
    @Override
    public Request reviewRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Заявки с id=" + id + " не найдена"));
        if (!request.getStatus().equals(RequestStatus.SUBMITTED)) {
            throw  new ConflictException("Заявка " + id + " должна быть согласована");
        }
        request.setStatus(RequestStatus.IN_REVIEW);
        request.setUpdatedAt(LocalDateTime.now());

        newStatusHistory(request, RequestStatus.SUBMITTED, request.getStatus());

        return request;
    }

    @Transactional
    @Override
    public Request approveRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Заявки с id=" + id + " не найдена"));
        if (!request.getStatus().equals(RequestStatus.IN_REVIEW)) {
            throw  new ConflictException("Заявка " + id + " должна быть на рассмотрении");
        }
        request.setStatus(RequestStatus.APPROVED);
        request.setUpdatedAt(LocalDateTime.now());

        newStatusHistory(request, RequestStatus.IN_REVIEW, request.getStatus());

        return request;
    }

    @Transactional
    @Override
    public Request rejectRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Заявки с id=" + id + " не найдена"));

        if (!request.getStatus().equals(RequestStatus.IN_REVIEW)) {
            throw  new ConflictException("Заявка " + id + " должна быть на рассмотрении");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setUpdatedAt(LocalDateTime.now());

        newStatusHistory(request, RequestStatus.IN_REVIEW, request.getStatus());

        return request;
    }

    @Transactional
    @Override
    public Request cancelRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Заявки с id=" + id + " не найдена"));

        if (!request.getStatus().equals(RequestStatus.DRAFT) && !request.getStatus().equals(RequestStatus.SUBMITTED)) {
            throw  new ConflictException("Заявка " + id + " должна иметь статус DRAFT или SUBMITTED!");
        }
        request.setUpdatedAt(LocalDateTime.now());
        newStatusHistory(request, request.getStatus(), RequestStatus.CANCELLED);
        request.setStatus(RequestStatus.CANCELLED);

        return request;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Заявки с id=" + id + " не найдена"));
        if (!request.getStatus().equals(RequestStatus.DRAFT)) {
            throw  new ConflictException("Заявка " + id + " уже согласована");
        }
        requestRepository.deleteById(id);
    }

    @Transactional
    public void newStatusHistory(Request request, RequestStatus oldRequestStatus, RequestStatus newRequestStatus) {
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setRequest(request);
        statusHistory.setOldStatus(oldRequestStatus);
        statusHistory.setNewStatus(newRequestStatus);
        statusHistory.setChangedAt(request.getUpdatedAt());
        statusHistoryService.save(statusHistory);
    }
}
