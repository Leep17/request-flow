package requestflow.comment.mapper;

import requestflow.comment.Comment;
import requestflow.comment.dto.CommentDto;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getRequest().getId());
    }
}
