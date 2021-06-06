package project.sec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.sec.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
