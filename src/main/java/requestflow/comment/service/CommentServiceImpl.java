package requestflow.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import requestflow.comment.Comment;
import requestflow.comment.dto.NewCommentDto;
import requestflow.comment.dto.UpdateCommentDto;
import requestflow.comment.repository.CommentRepository;
import requestflow.exception.ConflictException;
import requestflow.exception.NotFoundException;
import requestflow.request.Request;
import requestflow.request.repository.RequestRepository;
import requestflow.user.User;
import requestflow.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private  final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Transactional
    @Override
    public Comment save(NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        User author = userRepository.findById(newCommentDto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + newCommentDto.getAuthorId() + " не найден"));
        Request request = requestRepository.findById(newCommentDto.getRequestId())
                .orElseThrow(() -> new NotFoundException("Заявка с id=" + newCommentDto.getRequestId() + " не найдена"));

        comment.setText(newCommentDto.getText());
        comment.setAuthor(author);
        comment.setRequest(request);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Комментарий с id=" + id + " не найден"));
    }

    @Transactional
    @Override
    public Comment updateById(Long id, UpdateCommentDto updateCommentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));

        if (updateCommentDto.getText() != null) {
            if (updateCommentDto.getText().isBlank()) {
                throw new ConflictException("Комментарий не может быть пустым");
            }
            comment.setText(updateCommentDto.getText());
            comment.setUpdatedAt(LocalDateTime.now());
        }
        return comment;
    }

    @Override
    public Collection<Comment> findByRequestId(Long requestId) {
        requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Заявки с id=" + requestId + " не найдена"));
        return commentRepository.findAllByRequestIdOrderByCreatedAtDesc(requestId);
    }

    @Override
    public void deleteId(Long id) {
        commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));

        commentRepository.deleteById(id);
    }
}
