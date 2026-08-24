package requestflow.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import requestflow.comment.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findAllByRequestIdOrderByCreatedAtDesc(Long requestId);
}
