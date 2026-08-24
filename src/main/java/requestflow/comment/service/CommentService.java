package requestflow.comment.service;

import requestflow.comment.Comment;
import requestflow.comment.dto.NewCommentDto;
import requestflow.comment.dto.UpdateCommentDto;

import java.util.Collection;

public interface CommentService {
    Comment save(NewCommentDto newCommentDto);

    Comment getById(Long id);

    Comment updateById(Long id, UpdateCommentDto updateCommentDto);

    Collection<Comment> findByRequestId(Long requestId);

    void deleteId(Long id);

}
