package requestflow.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import requestflow.comment.dto.CommentDto;
import requestflow.comment.dto.NewCommentDto;
import requestflow.comment.dto.UpdateCommentDto;
import requestflow.comment.mapper.CommentMapper;
import requestflow.comment.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private  final CommentService commentService;

    @GetMapping("/comments/{id}")
    public CommentDto getById(@PathVariable Long id) {
        return CommentMapper.toCommentDto(commentService.getById(id));
    }

    @GetMapping("/requests/{requestId}/comments")
    public List<CommentDto> getAllByRequestId(@PathVariable Long requestId) {
        return commentService.findByRequestId(requestId).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
    }

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto saveNewComment(@RequestBody NewCommentDto newCommentDto) {
        return CommentMapper.toCommentDto(commentService.save(newCommentDto));
    }

    @PatchMapping("/comments/{id}")
    public CommentDto updateComment(@PathVariable Long id, @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        return CommentMapper.toCommentDto(commentService.updateById(id, updateCommentDto));
    }

    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        commentService.deleteId(id);
    }
}
